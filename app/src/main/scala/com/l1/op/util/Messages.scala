package com.l1.op.util

import java.text.SimpleDateFormat

object Messages {

  case class CurrentScore(totalAns: String, totalQuestions: String)

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
