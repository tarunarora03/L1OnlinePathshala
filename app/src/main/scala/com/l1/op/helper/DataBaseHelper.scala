package com.l1.op.helper

import android.content.Context;
import android.database.sqlite.{SQLiteOpenHelper, SQLiteDatabase};
import android.util.Log;
import DataBaseHelper._

/**
* Created by Tarun on 4/4/2015.
*/

object DataBaseHelper {
  val DB_NAME: String = "L1OP.DB"
  val DB_VERSION: Int = 1
}

class DataBaseHelper(context: Context) extends SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

  override def onCreate(db: SQLiteDatabase) {
    db.execSQL(LoginDataBaseAdapter.DB_CREATE_QUERY)
  }

  // Called when there is a database version mismatch meaning that the version of the database on disk needs to be upgraded to the current version.
  override def onUpgrade(db: SQLiteDatabase, _oldVersion: Int, _newVersion: Int) {
    // Log the version upgrade.
    Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion + " to " + _newVersion + ", which will destroy all old data");
    // The simplest case is to drop the old table and create a new one.
    db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
    // Create a new one.
    onCreate(db);
  }
}
