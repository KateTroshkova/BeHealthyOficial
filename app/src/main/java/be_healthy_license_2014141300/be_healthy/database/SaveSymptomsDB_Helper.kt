package com.be_healthy_license_2014141300.be_healthy.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class SaveSymptomsDB_Helper(context: Context):SQLiteOpenHelper(context, "com.be_healthy_license_2014141300.be_healthy.database.symptoms", null, 1), BaseColumns {

    val TABLE_NAME="nottable"
    val COLUMN_NAME="name"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table "+TABLE_NAME+" ( " +android.provider.BaseColumns._ID + " integer primary key autoincrement, "+ COLUMN_NAME+" text);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}