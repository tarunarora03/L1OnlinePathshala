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
  val COL_NAME: Int = 1
  val TBL_NAME: String = "USER"
  val DB_CREATE_QUERY = "CREATE TABLE "+TBL_NAME+" ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, EMAIL TEXT, FIELD TEXT, INST_DATE TEXT )";

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
  def insertUser(username: String, password: String, email: String, field: String, phoneNumber: String, successAns: Int, totals: Int) = {
    val format = new SimpleDateFormat("d-M-y")
    val currDate:String = format.format(Calendar.getInstance().getTime())
    val values: ContentValues = new ContentValues()
    values.put("USERNAME", username)
    values.put("PASSWORD", password)
    values.put("EMAIL", email)
    values.put("FIELD", phoneNumber)
    values.put("INST_DATE",currDate)
   // values.put("TOT_QUES", totals)
    //values.put("TOT_ANS_SUC", successAns)

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

    val where = "USERNAME = "
    db.update(TBL_NAME, updValues, where, Array.empty[String])
  }

  def searchUser(userName: String): String = {
    val cursor: Cursor = db.query(TBL_NAME, null, " USERNAME = ?", Array(userName), null, null, null)
    cursor.getCount match {
      case 1 => cursor.moveToFirst()
        val pwd = cursor.getString(cursor.getColumnIndex("PASSWORD"))
        cursor.close()
        return pwd
      case 0 => cursor.close()
        return "NOT_EXISTS"
    }
  }
}