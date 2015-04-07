package com.l1.op.activity

import android.app.Activity
import android.content.{Intent, DialogInterface}
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.{RadioButton, Button, RadioGroup, TextView}
import com.l1.op.R
import com.l1.op.helper.LoginDataBaseAdapter

/**
 * Created by Tarun on 4/5/2015.
 */
class MainActivity extends Activity {

  lazy val loginDatabaseAdapter = new LoginDataBaseAdapter(this)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val extras: Bundle = getIntent().getExtras()
    val userName = extras.getString("username")
    val scores: Array[String] = loginDatabaseAdapter.fetchScores(userName)
    val textView = findViewById(R.id.username).asInstanceOf[TextView]
    val scoreView = findViewById(R.id.ScoreResults).asInstanceOf[TextView]
    textView.setText(userName)
    scoreView.setText(scores(0) + "/" + scores(1))

    addListenerOnSubmit(userName,scores)
  }

  def addListenerOnSubmit(username: String, scores: Array[String]) = {
    val ansGroup = findViewById(R.id.answers).asInstanceOf[RadioGroup]
    val btnSubmit = findViewById(R.id.btnDisplay).asInstanceOf[Button]

    btnSubmit.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = {
        val selectedOpt = ansGroup.getCheckedRadioButtonId
        val selAns = findViewById(selectedOpt).asInstanceOf[RadioButton]
        val idx: Int = ansGroup.indexOfChild(selAns);
        idx match {
          case 1 => loginDatabaseAdapter.updateScore(username, (scores(0).toInt+1).toString, (scores(1).toInt+1).toString)
          case _ => loginDatabaseAdapter.updateScore(username, (scores(0).toInt).toString, (scores(1).toInt+1).toString)
        }
        val intent: Intent = getIntent();
        intent.putExtra("username", username)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent)
      }
    })
  }
}
