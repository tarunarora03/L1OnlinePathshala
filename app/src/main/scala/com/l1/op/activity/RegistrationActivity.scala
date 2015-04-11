package com.l1.op.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget._
import com.l1.op.R
import com.l1.op.helper.DataBaseAdapter
import com.l1.op.util.{TR, TypedActivity}

/**
 * Created by Tarun on 4/4/2015.
 */
class RegistrationActivity extends Activity with TypedActivity{
  lazy val databaseAdapter = new DataBaseAdapter(this)
  //loginDatabaseAdapter.open()

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_registration)

    val clickHeader = findViewById(R.id.header).asInstanceOf[ImageView]
    clickHeader.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = {
        val intentHeader: Intent = new Intent()
        intentHeader.setAction(Intent.ACTION_VIEW)
        intentHeader.addCategory(Intent.CATEGORY_BROWSABLE)
        intentHeader.setData(Uri.parse("http://www.l1coaching.co.in"))
        startActivity(intentHeader)
      }
    })

    val usernameF = findView(TR.usernameEditText)
    val passwordF = findView(TR.passwordEditText)
    val emailF = findView(TR.emailEditText)
    val phoneF = findView(TR.phoneEditText)

    val registerButton = findView(TR.registerButton)
    registerButton.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = {
        val username = usernameF.getText.toString
        val email = emailF.getText.toString
        val password = passwordF.getText.toString
        val phone = phoneF.getText.toString
        if (username.equals("") || password.equals("") || email.equals("") || phone.equals("")) {
          Toast.makeText(getApplicationContext, "Field Vaccant", Toast.LENGTH_LONG).show()
          return
        } else {
          databaseAdapter.insertUser(username, password, email, "IIT", phone, "0", "0")
          Toast.makeText(getApplicationContext, "Account Successfully Created ", Toast.LENGTH_LONG).show()
          val intentWelcome :Intent =new Intent(getApplicationContext,classOf[MainActivity])
          intentWelcome.putExtra("username",username)
          startActivity(intentWelcome)
        }
      }

    })
  }
  override def onDestroy(): Unit ={
    super.onDestroy()
    databaseAdapter.close()
  }
}
