package be_healthy_license_2014141300.be_healthy.database

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.AsyncTask
import android.provider.BaseColumns
import android.support.v4.content.LocalBroadcastManager
import be_healthy_license_2014141300.be_healthy.UpdateSender
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import com.be_healthy_license_2014141300.be_healthy.HeartBeat
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.database.AlarmClockDB_Helper
import com.be_healthy_license_2014141300.be_healthy.database.DiseaseDB_Helper
import com.be_healthy_license_2014141300.be_healthy.database.Heartrate_DB_Helper
import com.be_healthy_license_2014141300.be_healthy.database.SaveSymptomsDB_Helper
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class DB_Operation(var context: Context) {

    fun readSymptoms(){
        ReadSymptomsTask().execute()
    }

    fun readDisease(){
        ReadDiseaseTask(false).execute()
    }

    fun readHearBeat(){
        ReadHeartBeatTask().execute()
    }

    fun saveSymptom(name:String){
        SaveSymptomTask(name).execute()
    }

    fun saveDisease(disease: Disease){
        ReadDiseaseTask(true, disease.name).execute()
    }

    fun saveResult(result:HeartBeat){
        SaveHeartBeatTask(result.date, result.line, result.result).execute()
    }

    fun cleanSymptoms(){
        ClearSymptomsTask().execute()
    }

    fun deleteDisease(name:String){
        DeleteDiseaseTask(name).execute()
    }

    fun saveAlarm(alarm:AlarmClock){
        SaveAlarmTask(alarm).execute()
    }

    fun readAlarm(){
        ReadAlarmClockTask().execute()
    }

    fun updateAlarm(alarm: AlarmClock){
        UpdateAlarm(alarm).execute()
    }

    fun deleteAlarm(alarm:AlarmClock){
        DeleteAlarmTask(alarm).execute()
    }

    fun clearHistory(){
        ClearHistoryTask().execute()
    }

    private inner class ReadSymptomsTask:AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {
            val result = arrayListOf<String>()
            val helper= SaveSymptomsDB_Helper(context)
            val db=helper.readableDatabase
            val cursor=db.query(helper.TABLE_NAME, null, null, null, null, null, null)
            while(cursor.moveToNext()){
                result.add(cursor.getString(cursor.getColumnIndex(helper.COLUMN_NAME)))
            }
            cursor.close()
            UpdateSender(context).send(R.string.action_read_ready, R.string.param_symptoms_for_search, result)
            return null
        }
    }

    private inner class SaveSymptomTask(var name:String):AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {
            val helper= SaveSymptomsDB_Helper(context)
            val db=helper.writableDatabase
            val value=ContentValues()
            value.put(helper.COLUMN_NAME, name)
            db.insert(helper.TABLE_NAME, null, value)
            return null
        }
    }

    private inner class ClearSymptomsTask:AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {
            val helper= SaveSymptomsDB_Helper(context)
            val db=helper.writableDatabase
            db.delete(helper.TABLE_NAME, null, null)
            return null
        }
    }

    private inner class SaveDiseaseTask(var name:String): AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            val helper = DiseaseDB_Helper(context)
            val db=helper.writableDatabase
            val value = ContentValues()
            value.put(helper.COLUMN_NAME, name)
            db.insert(helper.TABLE_NAME, null, value)
            return null
        }
    }

    private inner class ReadDiseaseTask(var needSave:Boolean, var name:String?=null): AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            val result = ArrayList<String>()
            val helper = DiseaseDB_Helper(context)
            val db=helper.readableDatabase
            val cursor=db.query(helper.TABLE_NAME, null, null, null, null, null, null)
            while(cursor.moveToNext()){
                result.add(cursor.getString(cursor.getColumnIndex(helper.COLUMN_NAME)))
            }
            cursor.close()
            if (needSave){
                if (name !=null && name!! !in result){
                    SaveDiseaseTask(name!!).execute()
                }
            }
            else {
                UpdateSender(context).send(R.string.action_save, R.string.param_saved_list, result)
            }
            return null
        }
    }

    private inner class DeleteDiseaseTask(var name:String): AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            val helper = DiseaseDB_Helper(context)
            val db=helper.writableDatabase
            db.delete(helper.TABLE_NAME, helper.COLUMN_NAME+" = '"+name+"';", null)
            return null
        }
    }

    private inner class SaveHeartBeatTask(var date:String, var line:String, var result:Int): AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            val helper = Heartrate_DB_Helper(context)
            val db=helper.writableDatabase
            val value = ContentValues()
            value.put(helper.COLUMN_DATE, date)
            value.put(helper.COLUMN_LINE, line)
            value.put(helper.COLUMN_RESULT, result)
            db.insert(helper.TABLE_NAME, null, value)
            return null
        }
    }

    private inner class ReadHeartBeatTask: AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            val result = ArrayList<HeartBeat>()
            val helper = Heartrate_DB_Helper(context)
            val db=helper.readableDatabase
            val cursor=db.query(helper.TABLE_NAME, null, null, null, null, null, null)
            while(cursor.moveToNext()){
                val date=cursor.getString(cursor.getColumnIndex(helper.COLUMN_DATE))
                val line=cursor.getString(cursor.getColumnIndex(helper.COLUMN_LINE))
                val heartrate=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_RESULT))
                result.add(HeartBeat(date, line, heartrate))
            }
            val intent= Intent(context.resources.getString(R.string.action_read_ready))
            intent.putExtra(context.resources.getString(R.string.param_heartbeat), result)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            cursor.close()
            return null
        }
    }

    private inner class SaveAlarmTask(var data: AlarmClock): AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            val helper = AlarmClockDB_Helper(context)
            val db=helper.writableDatabase
            val value = ContentValues()
            value.put(helper.COLUMN_HOUR, data.hour)
            value.put(helper.COLUMN_MINUTE, data.minute)
            value.put(helper.COLUMN_ON, data.alarm)
            value.put(helper.COLUMN_REPEAT, data.repeat)
            value.put(helper.COLUMN_SUN, data.week[0])
            value.put(helper.COLUMN_MON, data.week[1])
            value.put(helper.COLUMN_TUE, data.week[2])
            value.put(helper.COLUMN_WEN, data.week[3])
            value.put(helper.COLUMN_TH, data.week[4])
            value.put(helper.COLUMN_FR, data.week[5])
            value.put(helper.COLUMN_SAT, data.week[6])
            if (data.ringtone?.isEmpty()!! || data.ringtoneName?.isEmpty()!!){
                val ringtoneData=getDefaultRingtone().split("#")
                data.ringtone=ringtoneData[0]
                data.ringtoneName=ringtoneData[1]
            }
            value.put(helper.COLUMN_RINGTONE, data.ringtone)
            value.put(helper.COLUMN_RINGTONE_NAME, data.ringtoneName)
            value.put(helper.COLUMN_DESCRIPTION, data.description)
            val id=db.insert(helper.TABLE_NAME, null, value)
            data.id=id.toInt()
            UpdateSender(context).send(R.string.action_id_back, R.string.param_id, data)
            return null
        }
    }

    private inner class ReadAlarmClockTask: AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            val result = ArrayList<AlarmClock>()
            val helper = AlarmClockDB_Helper(context)
            val db=helper.readableDatabase
            val cursor=db.query(helper.TABLE_NAME, null, null, null, null, null, null)
            while(cursor.moveToNext()){
                val id=cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val hour=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_HOUR))
                val minute=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_MINUTE))
                val alarm=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_ON))
                val repeat=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_REPEAT))
                val sun=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_SUN))
                val mon=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_MON))
                val tue=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_TUE))
                val wen=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_WEN))
                val th=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_TH))
                val fr=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_FR))
                val sat=cursor.getInt(cursor.getColumnIndex(helper.COLUMN_SAT))
                val ringtone=cursor.getString(cursor.getColumnIndex(helper.COLUMN_RINGTONE))
                val ringtoneName=cursor.getString(cursor.getColumnIndex(helper.COLUMN_RINGTONE_NAME))
                val description=cursor.getString(cursor.getColumnIndex(helper.COLUMN_DESCRIPTION))
                result.add(AlarmClock(hour, minute, alarm, repeat, sun, mon, tue, wen, th, fr, sat, id, ringtone, ringtoneName, description))
            }
            val intent= Intent(context.resources.getString(R.string.action_alarm_ready))
            intent.putExtra(context.resources.getString(R.string.param_alarm), result)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            cursor.close()
            return null
        }
    }

    private inner class DeleteAlarmTask(var alarm:AlarmClock): AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            val helper = AlarmClockDB_Helper(context)
            val db=helper.writableDatabase
            db.delete(helper.TABLE_NAME, BaseColumns._ID+" = "+alarm.id+";", null)
            return null
        }
    }

    private inner class UpdateAlarm(var data:AlarmClock):AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {
            val helper= AlarmClockDB_Helper(context)
            val db=helper.readableDatabase
            val value = ContentValues()
            value.put(helper.COLUMN_HOUR, data.hour)
            value.put(helper.COLUMN_MINUTE, data.minute)
            value.put(helper.COLUMN_ON, data.alarm)
            value.put(helper.COLUMN_REPEAT, data.repeat)
            value.put(helper.COLUMN_SUN, data.week[0])
            value.put(helper.COLUMN_MON, data.week[1])
            value.put(helper.COLUMN_TUE, data.week[2])
            value.put(helper.COLUMN_WEN, data.week[3])
            value.put(helper.COLUMN_TH, data.week[4])
            value.put(helper.COLUMN_FR, data.week[5])
            value.put(helper.COLUMN_SAT, data.week[6])
            value.put(helper.COLUMN_RINGTONE, data.ringtone)
            value.put(helper.COLUMN_RINGTONE_NAME, data.ringtoneName)
            value.put(helper.COLUMN_DESCRIPTION, data.description)
            db.update(helper.TABLE_NAME, value, BaseColumns._ID+" = "+data.id+";", null)
            return null
        }
    }

    private inner class ClearHistoryTask:AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {
            val helper= Heartrate_DB_Helper(context)
            val db=helper.writableDatabase
            db.delete(helper.TABLE_NAME, null, null)
            return null
        }
    }

    private fun getDefaultRingtone():String{
        val ringtoneMgr = RingtoneManager(context)
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM)
        val alarmsCursor = ringtoneMgr.cursor
        val result= ringtoneMgr.getRingtoneUri(0).toString()+"#"+ringtoneMgr.getRingtone(0).getTitle(context)
        alarmsCursor.close()
        return result
    }
}