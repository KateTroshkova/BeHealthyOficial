package be_healthy_license_2014141300.be_healthy.database

import android.content.ContentValues
import android.content.Context
import be_healthy_license_2014141300.be_healthy.disease.Disease
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class DBOperation(var context: Context) {

    fun readDisease():Observable<ArrayList<String>>{
        return Observable.create<ArrayList<String>> {o->
            val result = ArrayList<String>()
            val helper = DiseaseDBHelper(context)
            val db=helper.readableDatabase
            val cursor=db.query(helper.tableName, null, null, null, null, null, null)
            while(cursor.moveToNext()){
                result.add(cursor.getString(cursor.getColumnIndex(helper.columnName)))
            }
            cursor.close()
            o.onNext(result)
            o.onComplete()
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveDisease(disease: Disease):Observable<Int>{
        return Observable.create<Int> { o->
            val helper = DiseaseDBHelper(context)
            val db=helper.writableDatabase
            val value = ContentValues()
            value.put(helper.columnName, disease.name)
            db.insert(helper.tableName, null, value)
            o.onComplete()
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteDisease(name:String):Observable<Int>{
        return Observable.create<Int>{ o->
            val helper = DiseaseDBHelper(context)
            val db=helper.writableDatabase
            db.delete(helper.tableName, helper.columnName+" = '"+name+"';", null)
            o.onComplete()
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}