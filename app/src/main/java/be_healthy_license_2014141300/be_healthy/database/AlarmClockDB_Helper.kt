package com.be_healthy_license_2014141300.be_healthy.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AlarmClockDB_Helper(var context: Context): SQLiteOpenHelper(context, "com.be_healthy_license_2014141300.be_healthy.database.alarms", null, 1),
        android.provider.BaseColumns{

    val TABLE_NAME="saved_data"
    val COLUMN_HOUR="hour"
    val COLUMN_MINUTE="minute"
    val COLUMN_ON="onoff"
    val COLUMN_REPEAT="repeat"
    val COLUMN_SUN="sunday"
    val COLUMN_MON="monday"
    val COLUMN_TUE="tuesday"
    val COLUMN_WEN="wednesday"
    val COLUMN_TH="thursday"
    val COLUMN_FR="friday"
    val COLUMN_SAT="saturday"
    val COLUMN_RINGTONE="ringtone"
    val COLUMN_RINGTONE_NAME="ringtone_name"
    val COLUMN_DESCRIPTION="description"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table "+TABLE_NAME+" ( "
                +android.provider.BaseColumns._ID + " integer primary key autoincrement, "+
                COLUMN_HOUR+" integer, "+
                COLUMN_MINUTE+" integer, "+
                COLUMN_ON+" integer, "+
                COLUMN_REPEAT+" integer, "+
                COLUMN_SUN+" integer, "+
                COLUMN_MON+" integer, "+
                COLUMN_TUE+" integer, "+
                COLUMN_WEN+" integer, "+
                COLUMN_TH+" integer, "+
                COLUMN_FR+" integer, "+
                COLUMN_SAT+" integer, "+
                COLUMN_RINGTONE_NAME+" text, "+
                COLUMN_RINGTONE+" text, "+
                COLUMN_DESCRIPTION+" text);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
}