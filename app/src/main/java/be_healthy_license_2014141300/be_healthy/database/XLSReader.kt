package be_healthy_license_2014141300.be_healthy.database

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import be_healthy_license_2014141300.be_healthy.disease.IncurableDisease
import be_healthy_license_2014141300.be_healthy.disease.StaticDiseaseData
import com.be_healthy_license_2014141300.be_healthy.disease.Disease
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow

class XLSReader private constructor(){

    private var observer:Observer<Int>? =null

    private var observable = Observable.create(ObservableOnSubscribe<Int> { e ->
        e.onComplete()
    }
    )

    companion object {
        private var INSTANCE:XLSReader?=null

        fun getInstance(): XLSReader {
            if (INSTANCE == null) {
                INSTANCE = XLSReader()
            }
            return INSTANCE as XLSReader
        }
    }

    fun register(observer:Observer<Int>){
        this.observer=observer
    }

    private inner class ReadTask(var context:Context): AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            var data= arrayListOf<Disease>()
            try {
                val myInput: InputStream
                // initialize asset manager
                val assetManager = context.getAssets()
                //  open excel sheet
                myInput = assetManager.open("desease_data.xlsx")
                // Create a POI File System object
                val myFileSystem = OPCPackage.open(myInput)
                // Create a workbook using the File System
                val myWorkBook = XSSFWorkbook(myFileSystem)
                // Get the first sheet from workbook
                val mySheet = myWorkBook.getSheetAt(0)
                // We now need something to iterate through the cells.
                val rowIter = mySheet.rowIterator()
                var rowno = 0
                while (rowIter.hasNext()) {
                    val myRow = rowIter.next() as XSSFRow
                    val cellIter = myRow.cellIterator()
                    var colno = 0
                    var id=0;
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
                        if (status.toFloat()<1){
                            var disease=IncurableDisease()
                            disease.name=name
                            disease.description=description
                            disease.warning=warning
                            for(symptom in symptoms){
                                disease.symptoms.add(symptom.trim())
                            }
                            data.add(disease)
                        }
                        else{
                            var disease=IncurableDisease()
                            disease.name=name
                            disease.description=description
                            disease.warning=warning
                            for(symptom in symptoms){
                                disease.symptoms.add(symptom.trim())
                            }
                            for(treat in treatment){
                                disease.treatment.add(treat.trim())
                            }
                            for(mag in magic){
                                disease.magic.add(mag.trim())
                            }
                            data.add(disease)
                        }
                    }
                    rowno++
                }
                StaticDiseaseData.diseases=data
            } catch (e: Exception) {
                Log.e("XLS_READER", "error " + e.toString())
            }
            observable.subscribe(observer)
            return null
        }
    }
    fun read(context: Context) {
        ReadTask(context).execute()
    }
}