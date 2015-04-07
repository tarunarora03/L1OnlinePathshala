package com.l1.op.activity

import android.app.Activity
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.{Toast, Button, EditText, TextView}
import com.l1.op.R
import com.l1.op.helper.{DataBaseHelper, LoginDataBaseAdapter}
import android.view.View
import android.content.{Intent, Context}

/**
 * Created by Tarun on 4/4/2015.
 */
class LoginActivity extends Activity {
  lazy val loginDatabaseAdapter = new LoginDataBaseAdapter(this)
  //loginDatabaseAdapter.open()

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    val usernameF = findViewById(R.id.username).asInstanceOf[EditText]
    val emailF = findViewById(R.id.email).asInstanceOf[EditText]
    val passwordF = findViewById(R.id.password).asInstanceOf[EditText]
    val phoneF = findViewById(R.id.phoneNumber).asInstanceOf[EditText]

    val registerButton = findViewById(R.id.register_button).asInstanceOf[Button]
    registerButton.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = {
        val username = usernameF.getText.toString
        val email = emailF.getText.toString
        val password = passwordF.getText.toString
        val phone = phoneF.getText.toString
        println(s"username : ${username}, ${password}, ${email}, ${phone} ")
        if (username.equals("") || password.equals("") || email.equals("") || phone.equals("")) {
          Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show()
          return
        } else {
          loginDatabaseAdapter.insertUser(username, password, email, "IIT", phone, "0", "0")
          Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
          val intentWelcome :Intent =new Intent(getApplicationContext(),classOf[MainActivity])
          intentWelcome.putExtra("username",username)
          startActivity(intentWelcome);
        }
      }

    })
  }
  override def onDestroy(): Unit ={
    super.onDestroy()
    //loginDatabaseAdapter.close()
  }
}
