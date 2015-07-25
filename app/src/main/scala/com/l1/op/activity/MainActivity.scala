package com.l1.op.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarActivity
import android.util.Log
import android.view.View.OnTouchListener
import android.view._
import android.widget._
import com.l1.op.R
import com.l1.op.adapters.QuestionViewPagerAdapter
import com.l1.op.fragments.DataPass
import com.l1.op.helper.DataBaseAdapter
import com.l1.op.util.Messages._
import com.l1.op.util.{TR, TypedActivity}

/**
 * Created by Tarun on 4/5/2015.
 */
class MainActivity extends ActionBarActivity with TypedActivity with DataPass {

  lazy val databaseAdapter = new DataBaseAdapter(this)
  var pager: ViewPager = null
  var currentScore: CurrentScore = _
  var userName: String = _
  var fullName: String = _
  var scoreDisplay: TextView = _
  var displayView: FrameLayout = _
  var noQuestions: TextView = _

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val extras: Bundle = getIntent.getExtras
    userName = extras.getString("username")
    fullName = extras.getString("fullname")
    //TODO: Local code below remove after implementation. remove the True condition
    //getSupportActionBar.hide()

    val questionList = extras.getBoolean("isRegistered") match {
      case true => val tempQues = Questions("2015-03-21", getResources.getString(R.string.sample_ques) + "HOOOOOOOOOOOOOOOOOOOO", Array("0", "1", "2"), "0")
        val tempQues1 = Questions("2015-03-22", getResources.getString(R.string.sample_ques) + "HOOOOOOOOOOOOOOOOOOOO", Array("3", "4", "5"), "5")
        val tempQues2 = Questions("2015-03-23", getResources.getString(R.string.sample_ques), Array("6", "7", "8"), "8")
        val tempQues3 = Questions("2015-03-24", getResources.getString(R.string.sample_ques), Array("9", "10", "11"), "9")
        Array(tempQues, tempQues1, tempQues2, tempQues3)
      case false => extras.getSerializable("questionList").asInstanceOf[Array[Questions]]
    }

    //Fetch Scores from Database
    currentScore = databaseAdapter.fetchScores(userName) match {
      case Some(a) => a
      case _ => CurrentScore("0", "0")
    }
    noQuestions = findView(TR.noQuestions)
    findView(TR.usernameR).setText(fullName)
    scoreDisplay = findView(TR.scoreResultsR)
    scoreDisplay.setText(currentScore.totalAns + "/" + currentScore.totalQuestions)
    //Create view to display questions.

    displayView = findViewById(R.id.viewmanager).asInstanceOf[FrameLayout]
    displayView.setVisibility(View.VISIBLE)

    questionList.size match {
      case 0 =>
        noQuestions.setVisibility(View.VISIBLE)
        pager.setVisibility(View.GONE)
      case _ =>
        pager = findViewById(R.id.questionviewpager).asInstanceOf[ViewPager]
        val myAdapter = new QuestionViewPagerAdapter(getSupportFragmentManager, questionList)
        pager.setOnTouchListener(new OnTouchListener {
          override def onTouch(v: View, event: MotionEvent): Boolean = true
        })
        pager.setAdapter(myAdapter)
        //make the other one invisible
        noQuestions.setVisibility(View.GONE)
        pager.setVisibility(View.VISIBLE)
    }
  }

  override def onDestroy(): Unit = {
    super.onDestroy()
    databaseAdapter.close()
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.common_menu, menu)
    //super.onCreateOptionsMenu(menu)
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

  override def onDataPass(score: CurrentScore, isLastQuestion: Boolean, smoothScroll: Boolean): Unit = {
    Log.d(TAGS_MainActivity, s"Received event back from fragment with scores ${score}. updating totals....")
    currentScore = CurrentScore((currentScore.totalAns.toInt + score.totalAns.toInt).toString, (currentScore.totalQuestions.toInt + score.totalQuestions.toInt).toString)
    databaseAdapter.updateScore(userName, currentScore.totalAns, currentScore.totalQuestions)
    scoreDisplay.setText(currentScore.totalAns + "/" + currentScore.totalQuestions)
    isLastQuestion match {
      case false => pager.setCurrentItem(pager.getCurrentItem + 1, smoothScroll);
      case true =>
        pager.setVisibility(View.GONE)
        noQuestions.setVisibility(View.VISIBLE)
    }
  }
}
