package be_healthy_license_2014141300.be_healthy.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DiseaseDBHelper(var context: Context):
        SQLiteOpenHelper(context,
                "com.be_healthy_license_2014141300.be_healthy.database.disease",
                null,
                1),
        android.provider.BaseColumns{

    val tableName="saved_data"
    val columnName="name"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table "+tableName+" ( "
                +android.provider.BaseColumns._ID + " integer primary key autoincrement, "+
                columnName+" text not null);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
}