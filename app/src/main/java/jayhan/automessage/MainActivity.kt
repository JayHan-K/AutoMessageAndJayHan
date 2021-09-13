package jayhan.automessage

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences:SharedPreferences


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){

        }else{
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE),1001)
        }
        if(checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){

        }else{
            requestPermissions(arrayOf(Manifest.permission.READ_CALL_LOG),1001)
        }

        if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){

        }else{
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS),1001)
        }

        sharedPreferences = getSharedPreferences("Message", Context.MODE_PRIVATE)

        var message : String? = sharedPreferences.getString("Message", "")
        val messageET = findViewById<EditText>(R.id.messageET)
        messageET.setText(message)
        var lenText = messageET.text.toString().toByteArray(Charset.forName("EUC-KR")).size

        val saveBT = findViewById<Button>(R.id.saveBT)
        saveBT.setOnClickListener{
            with(sharedPreferences.edit()){
                putString("Message", messageET.text.toString())
                commit()
            }
            Toast.makeText(this, "메시지가 저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
        val messageLengthTV = findViewById<TextView>(R.id.messageLengthTV)
        messageLengthTV.text = lenText.toString() + "/140"

        messageET.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                var lenText = messageET.text.toString().toByteArray(Charset.forName("EUC-KR")).size
                messageLengthTV.text = lenText.toString() + "/140"
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val charset = Charsets.UTF_8
                var lenText = messageET.text.toString().toByteArray(Charset.forName("EUC-KR")).size
                messageLengthTV.text = lenText.toString() + "/140"
            }
        })




    }
}