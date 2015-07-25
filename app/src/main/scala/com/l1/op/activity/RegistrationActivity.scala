package com.l1.op.activity

import java.util.Calendar

import android.app.{Activity, AlarmManager, PendingIntent}
import android.content.{Context, Intent, SharedPreferences}
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import android.view.View.OnClickListener
import android.view._
import android.widget._
import com.l1.op.R
import com.l1.op.helper.DataBaseAdapter
import com.l1.op.services.BackgroundNotificationReceiver
import com.l1.op.util.{TR, TypedActivity}

/**
 * Created by Tarun on 4/4/2015.
 */
class RegistrationActivity extends ActionBarActivity /*with TypedActivity*/ {
  lazy val databaseAdapter = new DataBaseAdapter(this)
  //loginDatabaseAdapter.open()

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_registration)

    //    val usernameF = findView(TR.usernameEditText)
    //    val passwordF = findView(TR.passwordEditText)
    //    val emailF = findView(TR.emailEditText)
    //    val phoneF = findView(TR.phoneEditText)
    //    val fullNameF = findView(TR.fullnameEditText)

    val usernameF = findViewById(R.id.username).asInstanceOf[EditText]
    val passwordF = findViewById(R.id.password).asInstanceOf[EditText]
    val emailF = findViewById(R.id.email).asInstanceOf[EditText]
    val phoneF = findViewById(R.id.phoneNumber).asInstanceOf[EditText]
    val fullNameF = findViewById(R.id.fullname).asInstanceOf[EditText]

    //    //Set the contactUS page if Clicked
    //    val contactUs = findViewById(R.id.contact).asInstanceOf[TextView]
    //    contactUs.setOnClickListener(new View.OnClickListener {
    //      override def onClick(v: View): Unit = {
    //        val inflater: LayoutInflater = getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    //        val popupView = inflater.inflate(R.layout.contact_popup, null, false)
    //        val popUpWindow: PopupWindow = new PopupWindow(popupView)
    //        popUpWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    //        popUpWindow.setContentView(popupView)
    //        popUpWindow.setFocusable(true)
    //        val close = popupView.findViewById(R.id.close).asInstanceOf[Button]
    //        close.setOnClickListener(new OnClickListener {
    //          override def onClick(v: View): Unit = {
    //            popUpWindow.dismiss()
    //          }
    //        })
    //      }
    //    })

    //StartBackgroung AlarmSerice
    startNotificationService()

    //    findView(TR.registerButton).onClick {
    findViewById(R.id.register_button).setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = {
        val username = usernameF.getText.toString
        val email = emailF.getText.toString
        val password = passwordF.getText.toString
        val phone = phoneF.getText.toString
        val fullname = fullNameF.getText.toString
        if (username.equals("") || password.equals("") || email.equals("") || phone.equals("") || fullname.equals("")) {
          Toast.makeText(getApplicationContext, "Field Vaccant", Toast.LENGTH_LONG).show()
          return
        } else {
          databaseAdapter.insertUser(username, fullname, password, email, "IIT", phone, "0", "0")
          Toast.makeText(getApplicationContext, "Account Successfully Created ", Toast.LENGTH_LONG).show()
          val intentWelcome: Intent = new Intent(getApplicationContext, classOf[MainActivity])
          intentWelcome.putExtra("username", username)
          intentWelcome.putExtra("fullname", fullname)
          intentWelcome.putExtra("isRegistered", true)
          //Set the shared prefrences to store the username details.
          val sharedPref: SharedPreferences.Editor = getSharedPreferences(getResources.getString(R.string.app_code), Context.MODE_PRIVATE).edit()
          sharedPref.putString("username", username)
          sharedPref.commit()
          //Code to commit shared pref
          startActivity(intentWelcome)
          finish()
        }
      }
    })
    def startNotificationService() = {
      val alarmManager = getSystemService(Context.ALARM_SERVICE).asInstanceOf[AlarmManager]
      val alarmIntent = new Intent(RegistrationActivity.this, classOf[BackgroundNotificationReceiver])
      val pendingIntent = PendingIntent.getBroadcast(RegistrationActivity.this, 0, alarmIntent, 0)

      //Set time of Day to send a Notification
      val alarmStartTime: Calendar = Calendar.getInstance
      alarmStartTime.set(Calendar.HOUR_OF_DAY, 10)
      alarmStartTime.set(Calendar.MINUTE, 0)
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
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.common_menu, menu)
    return true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.contactinfo => true
      case R.id.aboutus => true
      case R.id.visitus =>
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.l1coaching.co.in")))
        true
      case _ => super.onOptionsItemSelected(item)
    }
  }

  override def onDestroy(): Unit = {
    super.onDestroy()
    databaseAdapter.close()
  }
}
