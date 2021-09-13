package jayhan.automessage;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


//전화 중이면 전화를 받고 아니면 메세지
public class CallReceiver extends BroadcastReceiver {//현재 전화가 오는지 받는지 끊는지 기본인지
    private SharedPreferences sharedPreferences;
    String phonestate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Test", "TESTTESTTEST");
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {

            TelecomManager telephonyManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

            Bundle extras = intent.getExtras();
            if (extras != null) {

                String state = extras.getString(TelephonyManager.EXTRA_STATE); // 현재 폰 상태 가져옴
                if (state.equals(phonestate)) {
                    return;
                } else {
                    phonestate = state;
                }
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    String phoneNo = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    sharedPreferences = context.getSharedPreferences("Message", Context.MODE_PRIVATE);
                    String message = sharedPreferences.getString("Message", "");
                    if(message.equals("")){
                        Toast.makeText(context, phoneNo + " 번호로 보낼 메시지가 없습니다. 메시지를 입력 후 저장해주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        boolean isNotSended = sharedPreferences.getBoolean(phoneNo, true);
                        if(isNotSended) {
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                                Toast.makeText(context, phoneNo + " 번호로 :: " + message + " :: 전송합니다.", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(phoneNo, false);
                                editor.commit();
                            }catch (Exception e){
                                Toast.makeText(context, "SMS Failed, " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(context, phoneNo + " 번호로 이미 메시지가 전송된적이 있습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }

                }


            }

        }



    }
}