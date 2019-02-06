package be_healthy_license_2014141300.be_healthy.database

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import be_healthy_license_2014141300.be_healthy.UpdateSender
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.database.DiseaseDB_Helper
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class DB_Operation(var context: Context) {

    fun readDisease(){
        ReadDiseaseTask(false).execute()
    }

    fun saveDisease(disease: Disease){
        ReadDiseaseTask(true, disease.name).execute()
    }

    fun deleteDisease(name:String){
        DeleteDiseaseTask(name).execute()
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
}