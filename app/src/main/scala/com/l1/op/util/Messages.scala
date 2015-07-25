package com.l1.op.util

import java.text.SimpleDateFormat

object Messages {

  case class CurrentScore(totalAns: String, totalQuestions: String)

  case class Questions(date: String, questionText: String, ansArray: Array[String], correctAns: String)

  case class UserDetails(username: String, fullname: String, status: String, currentScore: CurrentScore, field: String, lastUpdDate: String)

  //TODO: Tarun implemnt the below two fields. So once user registers his status will be will br egistered not Active
  // meaning SMS authentication is pending Once verfication is done status will be active.
  //In SplashScreen this will be checked always and user will be redericted all the time to correct page
  val REGISTERED = "REGISTERED"
  val ACTIVE = "ACTIVE"

  val TAGS_QuestionViewPagerAdapter = "QuestionViewPagerAdapter"
  val TAGS_DataBaseAdapter = "DatabaseAdapter"
  val TAGS_MainActivity = "MainActivity"
  val TAGS_QuestionViewFragment = "QuestionViewFragment"


  val dateFormat: String = "d-M-y"
  val dateForamtter = new SimpleDateFormat(dateFormat)

  def convertArrayToString(s: Array[String]): String = {
    s.mkString(",")
  }

  def convertStringToArray(s: String): Array[String] = {
    s.split(",")
  }
}
