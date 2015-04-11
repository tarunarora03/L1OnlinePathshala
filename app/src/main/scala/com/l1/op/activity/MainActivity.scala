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
 * Created by Tarun on 4/5/2015.
 */
class MainActivity extends Activity with TypedActivity{

  lazy val databaseAdapter = new DataBaseAdapter(this)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val extras: Bundle = getIntent.getExtras
    val userName = extras.getString("username")

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

    val scores: Array[String] = databaseAdapter.fetchScores(userName)
    val userTextView = findView(TR.usernameR)
    val scoreView = findViewById(R.id.ScoreResults).asInstanceOf[TextView]
    userTextView.setText(userName)
    scoreView.setText(scores(0) + "/" + scores(1))

    addListenerOnSubmit(userName,scores)
  }

  def addListenerOnSubmit(username: String, scores: Array[String]) = {

    val ansGroup = findView(TR.selectedAnsRadioGrp)
    val btnSubmit = findView(TR.submitButton)

    btnSubmit.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = {
        val selectedOpt = ansGroup.getCheckedRadioButtonId
        val selAns = findViewById(selectedOpt).asInstanceOf[RadioButton]
        val idx: Int = ansGroup.indexOfChild(selAns)
        idx match {
          case 1 => databaseAdapter.updateScore(username, (scores(0).toInt+1).toString, (scores(1).toInt+1).toString)
          case _ => databaseAdapter.updateScore(username, scores(0).toInt.toString, (scores(1).toInt+1).toString)
        }
        val intent: Intent = getIntent
        intent.putExtra("username", username)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
      }
    })
  }

  override def onDestroy(): Unit ={
    super.onDestroy()
    databaseAdapter.close()
  }
}
