package com.be_healthy_license_2014141300.be_healthy.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Heartrate_DB_Helper(var context: Context): SQLiteOpenHelper(context, "com.be_healthy_license_2014141300.be_healthy.database.heartrate", null, 1),
        android.provider.BaseColumns{

    val TABLE_NAME="saved_data"
    val COLUMN_DATE="date"
    val COLUMN_LINE="line"
    val COLUMN_RESULT="result"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table "+TABLE_NAME+" ( "
                +android.provider.BaseColumns._ID + " integer primary key autoincrement, "+
                COLUMN_DATE+" text not null, "+
                COLUMN_LINE+" text not null, "+
                COLUMN_RESULT+" integer);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
}