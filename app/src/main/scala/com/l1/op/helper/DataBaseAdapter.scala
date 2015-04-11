package com.l1.op.helper

import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase;
import android.content.{ContentValues, Context}

/**
* Created by Tarun on 4/4/2015.
*/

object DataBaseAdapter {
  val TBL_USER: String = "USER"
  val TBL_QUES_CACHE: String =  "QUES"
  val DB_CREATE_USER_TBL = "CREATE TABLE "+TBL_USER+" ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, EMAIL TEXT, FIELD TEXT, PHONE  TEXT , INST_DATE TEXT, TOTAL_QUES TEXT, TOTAL_ANS TEXT, LAST_UPD_DATE TEXT)";
  val DB_CREATE_CACHE_QUES = "CREATE TABLE "+TBL_QUES_CACHE+" ( " +
    "id INTEGER PRIMARY KEY AUTOINCREMENT, RECV_DATE TEXT, QUES_DTLS TEXT, ANS_OPT TEXT, CORR_ANS TEXT)";

  val WHERE_USERNAME = "USERNAME = ?"
  val WHERE_RECVDATE = "RECV_DATE = ?"
  def apply(context: Context) = new DataBaseAdapter(context)

}

class DataBaseAdapter(_context: Context) {
  import DataBaseAdapter._

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
  def insertUser(username: String, password: String, email: String, field: String, phoneNumber: String, successAns: String, totals: String) = {
    val format = new SimpleDateFormat("d-M-y")
    val currDate:String = format.format(Calendar.getInstance().getTime())
    val values: ContentValues = new ContentValues()
    values.put("USERNAME", username)
    values.put("PASSWORD", password)
    values.put("EMAIL", email)
    values.put("FIELD", field)
    values.put("PHONE", phoneNumber)
    values.put("INST_DATE",currDate)
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

  def updateScore(username: String,totAns : String, totalQues: String) = {
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

  def fetchScores(userName: String): Array[String] = {
    val cursor: Cursor = db.query(TBL_USER, null, WHERE_USERNAME, Array(userName), null, null, null)
    cursor.getCount match {
      case 1 => cursor.moveToFirst()
        val ans = cursor.getString(cursor.getColumnIndex("TOTAL_ANS"))
        val ques = cursor.getString(cursor.getColumnIndex("TOTAL_QUES"))
        cursor.close()
        return Array(ans,ques)
      case 0 => cursor.close()
        return Array.empty[String]
    }
  }

  def getTodayQuestion(todayDate: String) = ???
}