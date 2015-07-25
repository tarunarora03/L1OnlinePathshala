package com.l1.op.helper

import java.util.Calendar

import android.content.{ContentValues, Context}
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.l1.op.util.Messages._

/**
 * Created by Tarun on 4/4/2015.
 */

object DataBaseAdapter {
  val TBL_USER: String = "USER"
  val TBL_QUES_CACHE: String = "QUES"
  val DB_CREATE_USER_TBL = "CREATE TABLE " + TBL_USER + " ( " +
    "id INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, FULLNAME TEXT, PASSWORD TEXT, EMAIL TEXT, FIELD TEXT, PHONE  TEXT , STATUS TEXT, INST_DATE TEXT, TOTAL_QUES TEXT, TOTAL_ANS TEXT, LAST_UPD_DATE TEXT)";
  val DB_CREATE_CACHE_QUES = "CREATE TABLE " + TBL_QUES_CACHE + " ( " +
    "id INTEGER PRIMARY KEY AUTOINCREMENT, RECV_DATE TEXT, QUES_DTLS TEXT, ANS_OPT TEXT, CORR_ANS TEXT)";

  val WHERE_USERNAME = "USERNAME = ?"
  val WHERE_RECVDATE = "RECV_DATE = ?"

  def apply(context: Context) = new DataBaseAdapter(context)

}

class DataBaseAdapter(_context: Context) {

  import com.l1.op.helper.DataBaseAdapter._
  import com.l1.op.util.Messages._

  val context: Context = _context
  val dbHelper: DataBaseHelper = new DataBaseHelper(context);
  val db: SQLiteDatabase = dbHelper.getWritableDatabase();

  def getDatabaseInstance(): SQLiteDatabase = db

  //  def open(): LoginDataBaseAdapter = {
  //    try {
  //      db = dbHelper.getWritableDatabase
  //      return this
  //    } catch {
  //      case ex: SQLException => throw new SQLException()
  //    }
  //  }

  def close(): Unit = {
    db.close()
  }

  //Methods to Insert Delete Update and Search
  def insertUser(username: String, fullname: String, password: String, email: String, field: String, phoneNumber: String, successAns: String, totals: String) = {
    val currDate: String = dateForamtter.format(Calendar.getInstance().getTime())
    val values: ContentValues = new ContentValues()
    values.put("USERNAME", username)
    values.put("FULLNAME", fullname)
    values.put("PASSWORD", password)
    values.put("EMAIL", email)
    values.put("FIELD", field)
    values.put("PHONE", phoneNumber)
    values.put("INST_DATE", currDate)
    values.put("TOTAL_QUES", totals)
    values.put("TOTAL_ANS", successAns)
    values.put("LAST_UPD_DATE", currDate)

    db.insert(TBL_USER, null, values)
  }

  def deleteUser(username: String): Int = {
    val nbrEntriesDelet = db.delete(TBL_USER, WHERE_USERNAME, Array(username))
    nbrEntriesDelet
  }

  def updateDetails(username: String, password: String) = {
    val updValues: ContentValues = new ContentValues()
    updValues.put("USERNAME", username)
    updValues.put("PASSWORD", password)

    db.update(TBL_USER, updValues, WHERE_USERNAME, Array.empty[String])
  }

  def updateScore(username: String, totAns: String, totalQues: String) = {
    val updValues: ContentValues = new ContentValues()
    updValues.put("TOTAL_QUES", totalQues)
    updValues.put("TOTAL_ANS", totAns)

    db.update(TBL_USER, updValues, WHERE_USERNAME, Array(username))
  }

  def searchUser(userName: String): String = {
    val cursor: Cursor = db.query(TBL_USER, null, WHERE_USERNAME, Array(userName), null, null, null)
    cursor.getCount match {
      case 1 => cursor.moveToFirst()
        val pwd = cursor.getString(cursor.getColumnIndex("PASSWORD"))
        cursor.close()
        return pwd
      case 0 => cursor.close()
        return "NOT_EXISTS"
    }
  }

  def getUserDetails(userName: String): UserDetails = {
    val cursor: Cursor = db.query(TBL_USER, null, WHERE_USERNAME, Array(userName), null, null, null)
    cursor.getCount match {
      case 1 => cursor.moveToFirst()
        val user = UserDetails(
          cursor.getString(cursor.getColumnIndex("USERNAME")),
          cursor.getString(cursor.getColumnIndex("FULLNAME")),
          ACTIVE,
          CurrentScore(cursor.getString(cursor.getColumnIndex("TOTAL_ANS")), cursor.getString(cursor.getColumnIndex("TOTAL_QUES"))),
          cursor.getString(cursor.getColumnIndex("FIELD")),
          cursor.getString(cursor.getColumnIndex("LAST_UPD_DATE")))
        cursor.close()
        return user
      //TODO validate that below condition will never occur
      //case 0 => cursor.close()
      //  return UserDetails("",)
    }
  }

  def fetchScores(userName: String): Option[CurrentScore] = {
    val cursor: Cursor = db.query(TBL_USER, null, WHERE_USERNAME, Array(userName), null, null, null)
    cursor.getCount match {
      case 1 => cursor.moveToFirst()
        val ans = cursor.getString(cursor.getColumnIndex("TOTAL_ANS"))
        val ques = cursor.getString(cursor.getColumnIndex("TOTAL_QUES"))
        cursor.close()
        Some(CurrentScore(ans, ques))
      case 0 => cursor.close()
        return None
    }
  }

  /*
  A method to cache questions on the daily basis. whenever the webservice is called the question(s) will be cached by calling this
   */

  def insertQuestions(questions: Seq[Questions]) = {
    questions.foreach(
      q => {
        val values: ContentValues = new ContentValues()
        values.put("RECV_DATE", q.date)
        values.put("QUES_DTLS", q.questionText)
        //TODO: check how to insert array in sqllite
        values.put("ANS_OPT", convertArrayToString(q.ansArray))
        values.put("CORR_ANS", q.correctAns)
        db.insert(TBL_QUES_CACHE, null, values)
      }
    )
  }

  /*
  If any questions are available then get all of them and display to the user. This can be either 1 or 7
   */
  //TODO fix empty Array Issue
  def getQuestionsFromCache(): Seq[Questions] = {
    val cursor: Cursor = db.query(TBL_QUES_CACHE, null, null, null, null, null, null)
    cursor.getCount match {
      case 0 => cursor.close()
        Seq.empty[Questions]
      case _ =>
        cursor.moveToFirst()
        val ques: Seq[Questions] = Nil
        while (!cursor.isAfterLast) {
          Log.d("DatabaseAdapter", "Iterating the collection to get list of Questions")
          val q = Questions(cursor.getString(cursor.getColumnIndex("RECV_DATE")),
            cursor.getString(cursor.getColumnIndex("QUES_DTLS")),
            convertStringToArray(cursor.getString(cursor.getColumnIndex("ANS_OPT"))),
            cursor.getString(cursor.getColumnIndex("CORR_ANS")))
          ques :+ q
          cursor.moveToNext()
        }
        cursor.close()
        ques
    }
  }

  /*
  Once the ans has been submitted call this to purge the data
   */
  def deleteQuestionFromCache(): Int = {
    val quesPurged = db.delete(TBL_QUES_CACHE, null, null)
    quesPurged
  }

  def deleteQuestionFromCache(dates: Seq[String]): Int = {
    val quesPurged = db.delete(TBL_QUES_CACHE, WHERE_RECVDATE, dates.toArray)
    quesPurged
  }
}