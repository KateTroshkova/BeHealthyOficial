package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import android.os.Parcelable

open class Disease(): Parcelable {

    var name:String = ""
    var description:String=""
    var symptoms = mutableListOf<String>()
    var treatment = mutableListOf<String>()
    var magic = mutableListOf<String>()

    protected constructor(dest: android.os.Parcel) : this() {
        name=dest.readString()
        description=dest.readString()
        symptoms=decodeList(dest.readString())
        treatment=decodeList(dest.readString())
        magic=decodeList(dest.readString())
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<Disease> {
            override fun createFromParcel(`in`: android.os.Parcel): Disease {
                return Disease(`in`)
            }

            override fun newArray(size: Int): Array<Disease?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(dest: android.os.Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(description)
        dest?.writeString(codeList(symptoms))
        dest?.writeString(codeList(treatment))
        dest?.writeString(codeList(magic))
    }

    override fun describeContents(): Int {
        return 0
    }

    private fun codeList(data:MutableList<String>):String{
        var result=""
        for(element in data){
            result+=element+"#"
        }
        result=result.removeSuffix("#")
        return result
    }

    private fun decodeList(data:String):MutableList<String>{
        val result= mutableListOf<String>()
        val array=data.split("#")
        result += array
        return result
    }

    fun findDiseaseByName(activity: Activity, name:String): Disease {
        when(name){
            "ангина"->{
                return Angina(activity)
            }
            "angina"->{
                return Angina(activity)
            }
            "орз"->{
                return ARD(activity)
            }
            "ard"->{
                return ARD(activity)
            }
            "аритмия"->{
                return Arrhythmia(activity)
            }
            "arrhythmia"->{
                return Arrhythmia(activity)
            }
            "орви"->{
                return ARVI(activity)
            }
            "arvi"->{
                return ARVI(activity)
            }
            "астма"->{
                return Asthma(activity)
            }
            "asthma"->{
                return Asthma(activity)
            }
            "кариес"->{
                return Caries(activity)
            }
            "caries"->{
                return Caries(activity)
            }
            "катаракта"->{
                return Cataract(activity)
            }
            "cataract"->{
                return Cataract(activity)
            }
            "синдром сухого глаза"->{
                return DryEyeSyndrome(activity)
            }
            "dry eye syndrome"->{
                return DryEyeSyndrome(activity)
            }
            "грипп"->{
                return Flu(activity)
            }
            "flu"->{
                return Flu(activity)
            }
            "фронтит"->{
                return Frontite(activity)
            }
            "frontite"->{
                return Frontite(activity)
            }
            "гипертония"->{
                return Hypertension(activity)
            }
            "hypertension"->{
                return Hypertension(activity)
            }
            "корь"->{
                return Measles(activity)
            }
            "measles"->{
                return Measles(activity)
            }
            "миопия"->{
                return Myopia(activity)
            }
            "myopia"->{
                return Myopia(activity)
            }
            "миокардит"->{
                return Myocarditis(activity)
            }
            "myocarditis"->{
                return Myocarditis(activity)
            }
            "отит"->{
                return Otitis(activity)
            }
            "otitis"->{
                return Otitis(activity)
            }
            "отосклероз"->{
                return Otosclerosis(activity)
            }
            "otosclerosis"->{
                return Otosclerosis(activity)
            }
            "фарингит"->{
                return Pharyngitis(activity)
            }
            "pharyngitis"->{
                return Pharyngitis(activity)
            }
            "тонзиллит"->{
                return Tonsillitis(activity)
            }
            "tonsillitis"->{
                return Tonsillitis(activity)
            }
            "растяжение связок"->{
                return Sprain(activity)
            }
            "sprain"->{
                return Sprain(activity)
            }
            "нарушения сна"->{
                return BadSleep(activity)
            }
            "sleep disorders"->{
                return BadSleep(activity)
            }
            "obesity"->{
                return Obesity(activity)
            }
            "ожирение"->{
                return Obesity(activity)
            }
            "flatfoot"->{
                return Flatfoot(activity)
            }
            "плоскостопие"->{
                return Flatfoot(activity)
            }
            "brain concussion"->{
                return BrainConcussion(activity)
            }
            "сотрясение мозга"->{
                return BrainConcussion(activity)
            }
            "отравление"->{
                return Intoxication(activity)
            }
            "intoxication"->{
                return Intoxication(activity)
            }
            "аллергия"->{
                return Allergy(activity)
            }
            "allergy"->{
                return Allergy(activity)
            }
            "стоматит"->{
                return Stomatitis(activity)
            }
            "stomatitis"->{
                return Stomatitis(activity)
            }
            "gastritis"->{
                return Gastritis(activity)
            }
            "гастрит"->{
                return Gastritis(activity)
            }
            "herpes"->{
                return Herpes(activity)
            }
            "герпес"->{
                return Herpes(activity)
            }
            "cholecystitis"->{
                return Cholecystitis(activity)
            }
            "холецистит"->{
                return Cholecystitis(activity)
            }
            "laryngitis"->{
                return Laryngitis(activity)
            }
            "ларингит"->{
                return Laryngitis(activity)
            }
            "osteoarthritis"->{
                return Osteoarthritis(activity)
            }
            "остеоартроз"->{
                return Osteoarthritis(activity)
            }
            "atherosclerosis"->{
                return Atherosclerosis(activity)
            }
            "атеросклероз"->{
                return Atherosclerosis(activity)
            }
            "bronchitis"->{
                return Bronchitis(activity)
            }
            "бронхит"->{
                return Bronchitis(activity)
            }
            "scurvy"->{
                return Scurvy(activity)
            }
            "цинга"->{
                return Scurvy(activity)
            }
            "hives"->{
                return Hives(activity)
            }
            "крапивница"->{
                return Hives(activity)
            }
            "nail fungus"->{
                return NailFungus(activity)
            }
            "ногтевой грибок"->{
                return NailFungus(activity)
            }
        }
        return Disease()
    }

    protected fun fillList(activity: Activity, mutableList: MutableList<String>, resource:Int){
        val array=activity.resources.getStringArray(resource)
        mutableList.clear()
        mutableList += array
    }
}