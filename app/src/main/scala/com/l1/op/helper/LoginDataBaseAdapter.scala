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

object LoginDataBaseAdapter {
  val TBL_NAME: String = "USER"
  val DB_CREATE_QUERY = "CREATE TABLE "+TBL_NAME+" ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, EMAIL TEXT, FIELD TEXT, PHONE  TEXT , INST_DATE TEXT, TOTAL_QUES TEXT, TOTAL_ANS TEXT)";
  val where = "USERNAME = ?"
  def apply(context: Context) = new LoginDataBaseAdapter(context)

}

class LoginDataBaseAdapter(_context: Context) {
  import LoginDataBaseAdapter._

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

    db.insert(TBL_NAME, null, values)
  }

  def deleteUser(username: String): Int = {
    val where = " USERNAME = ";
    val nbrEntriesDelet = db.delete(TBL_NAME, where, Array(username))
    nbrEntriesDelet
  }

  def updateDetails(username: String, password: String) = {
    val updValues: ContentValues = new ContentValues()
    updValues.put("USERNAME", username)
    updValues.put("PASSWORD", password)

    db.update(TBL_NAME, updValues, where, Array.empty[String])
  }

  def updateScore(username: String,totAns : String, totalQues: String) = {
    val updValues: ContentValues = new ContentValues()
    updValues.put("TOTAL_QUES", totalQues)
    updValues.put("TOTAL_ANS", totAns)

    db.update(TBL_NAME, updValues, where, Array(username))
  }

  def searchUser(userName: String): String = {
    val cursor: Cursor = db.query(TBL_NAME, null, where, Array(userName), null, null, null)
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
    val cursor: Cursor = db.query(TBL_NAME, null, " USERNAME = ?", Array(userName), null, null, null)
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
}