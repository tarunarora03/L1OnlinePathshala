package com.l1.op.activity

import java.util.Calendar

import android.app.{Activity, AlarmManager, PendingIntent}
import android.content.{Context, Intent}
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.{ImageView, Toast}
import com.l1.op.R
import com.l1.op.helper.DataBaseAdapter
import com.l1.op.services.BackgroundNotificationReceiver
import com.l1.op.util.{TR, TypedActivity}

/**
 * Created by Tarun on 4/7/2015.
 */
class SignInSignUpActivity extends Activity with TypedActivity {

  lazy val databaseAdapter = new DataBaseAdapter(this)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_signin_signup)

    //Clickable Header Code
    findViewById(R.id.header).asInstanceOf[ImageView].onClick {
      view: View => startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.l1coaching.co.in")))
    }

    //StartBackgroung AlarmSerice
    startNotificationService()
    //Add button events
    findView(TR.signUpButton).onClick {
      view: View => startActivity(new Intent(getApplicationContext, classOf[RegistrationActivity]))
    }

    findView(TR.signInButton).onClick {
      view: View =>
        val usernameF = findView(TR.usernameEditText)
        val passwordF = findView(TR.passwordEditText)

        val username = usernameF.getText.toString
        val pwd = passwordF.getText.toString

        if (username == "" || pwd == "") {
          Toast.makeText(getApplicationContext, "Field Vaccant", Toast.LENGTH_LONG).show()
          return
        } else {
          databaseAdapter.searchUser(username) match {
            case value if value == pwd => Toast.makeText(SignInSignUpActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
            case _ =>
              Toast.makeText(SignInSignUpActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show()
              return
          }
          val signInIntent: Intent = new Intent(getApplicationContext, classOf[MainActivity])
          signInIntent.putExtra("username", username)
          startActivity(signInIntent)
        }
    }
  }

  def startNotificationService() = {
    val alarmManager = getSystemService(Context.ALARM_SERVICE).asInstanceOf[AlarmManager]
    val alarmIntent = new Intent(SignInSignUpActivity.this, classOf[BackgroundNotificationReceiver])
    val pendingIntent = PendingIntent.getBroadcast(SignInSignUpActivity.this, 0, alarmIntent, 0)

    //Set time of Day to send a Notification
    val alarmStartTime: Calendar = Calendar.getInstance
    alarmStartTime.set(Calendar.HOUR_OF_DAY, 10)
    alarmStartTime.set(Calendar.MINUTE, 2)
    alarmStartTime.set(Calendar.SECOND, 0)
    alarmManager.setRepeating(AlarmManager.RTC, alarmStartTime.getTimeInMillis, getInterval, pendingIntent)
  }

  def getInterval(): Int = {
    val days = 1
    val hours = 24
    val minutes = 60
    val seconds = 60
    val ms = 1000
    days * hours * minutes * seconds * ms
  }

  override def onDestroy(): Unit = {
    super.onDestroy()
    databaseAdapter.close()
  }
}
