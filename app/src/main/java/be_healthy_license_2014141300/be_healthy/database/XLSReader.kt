package be_healthy_license_2014141300.be_healthy.database

import android.content.Context
import android.util.Log
import be_healthy_license_2014141300.be_healthy.disease.StaticDiseaseData
import be_healthy_license_2014141300.be_healthy.disease.Disease
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

class XLSReader private constructor(){

    companion object {
        private var INSTANCE:XLSReader?=null

        fun getInstance(): XLSReader {
            if (INSTANCE == null) {
                INSTANCE = XLSReader()
            }
            return INSTANCE as XLSReader
        }
    }

    fun read(context: Context):Observable<Int>{
        return Observable.create<Int>{o->
            val data= arrayListOf<Disease>()
            try {
                val assetManager = context.assets
                val myInput:InputStream = assetManager.open("desease_data.xlsx")
                val myFileSystem = OPCPackage.open(myInput)
                val myWorkBook = XSSFWorkbook(myFileSystem)
                val mySheet = myWorkBook.getSheetAt(0)
                val iterator = mySheet.rowIterator()
                var rowno = 0
                while (iterator.hasNext()) {
                    val myRow = iterator.next() as XSSFRow
                    val cellIter = myRow.cellIterator()
                    var colno = 0
                    var name = ""
                    var description = ""
                    var symptoms= listOf<String>()
                    var treatment= listOf<String>()
                    if (rowno != 0) {
                        while (cellIter.hasNext()) {
                            val myCell = cellIter.next() as XSSFCell
                            when(colno){
                                1->{
                                    name=myCell.toString()
                                }
                                2->{
                                    description=myCell.toString()
                                }
                                3->{
                                    symptoms=myCell.toString().split(',')
                                }
                                4->{
                                    treatment=myCell.toString().split('&')
                                }
                            }
                            colno++
                        }
                        val disease= Disease()
                        disease.name=name
                        disease.description=description
                        for(symptom in symptoms){
                            disease.symptoms.add(symptom.trim())
                        }
                        for(treat in treatment){
                            disease.treatment.add(treat.trim())
                        }
                        data.add(disease)
                    }
                    rowno++
                }
                StaticDiseaseData.diseases=data
            } catch (e: Exception) {
                Log.e("XLS_READER", "error " + e.toString())
            }
            o.onComplete()
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}