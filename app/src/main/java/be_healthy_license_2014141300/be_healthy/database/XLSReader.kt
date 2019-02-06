package be_healthy_license_2014141300.be_healthy.database

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import be_healthy_license_2014141300.be_healthy.disease.StaticDiseaseData
import com.be_healthy_license_2014141300.be_healthy.disease.Disease
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
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
                    var status = ""
                    var description = ""
                    var symptoms= listOf<String>()
                    var warning=""
                    var treatment= listOf<String>()
                    var magic= listOf<String>()
                    if (rowno != 0) {
                        while (cellIter.hasNext()) {
                            val myCell = cellIter.next() as XSSFCell
                            when(colno){
                                1->{
                                    name=myCell.toString()
                                }
                                2->{
                                    status=myCell.toString()
                                }
                                3->{
                                    description=myCell.toString()
                                }
                                4->{
                                    symptoms=myCell.toString().split(',')
                                }
                                5->{
                                    warning=myCell.toString()
                                    if (warning.contains("%")){
                                        warning=warning.replace("%", "\n")
                                    }
                                }
                                6->{
                                    treatment=myCell.toString().split('&')
                                }
                                7->{
                                    magic=myCell.toString().split('&')
                                }
                            }
                            colno++
                        }
                        val disease=Disease()
                        disease.name=name
                        disease.description=description
                        disease.warning=warning
                        for(symptom in symptoms){
                            disease.symptoms.add(symptom.trim())
                        }
                        if (status.toFloat()>0){
                            for(treat in treatment){
                                disease.treatment.add(treat.trim())
                            }
                            for(mag in magic){
                                disease.magic.add(mag.trim())
                            }
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