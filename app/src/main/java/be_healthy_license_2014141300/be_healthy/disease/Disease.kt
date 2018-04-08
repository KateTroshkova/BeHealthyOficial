package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import android.os.Parcelable
import be_healthy_license_2014141300.be_healthy.disease.*
import com.be_healthy_license_2014141300.be_healthy.R

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
            activity.resources.getString(R.string.angina)->{
                return Angina(activity)
            }
            activity.resources.getString(R.string.ard)->{
                return ARD(activity)
            }
            activity.resources.getString(R.string.arrhythmia)->{
                return Arrhythmia(activity)
            }
            activity.resources.getString(R.string.arvi)->{
                return ARVI(activity)
            }
            activity.resources.getString(R.string.asthma)->{
                return Asthma(activity)
            }
            activity.resources.getString(R.string.caries)->{
                return Caries(activity)
            }
            activity.resources.getString(R.string.cataract)->{
                return Cataract(activity)
            }
            activity.resources.getString(R.string.dry_eye_syndrome)->{
                return DryEyeSyndrome(activity)
            }
            activity.resources.getString(R.string.flu)->{
                return Flu(activity)
            }
            activity.resources.getString(R.string.frontite)->{
                return Frontite(activity)
            }
            activity.resources.getString(R.string.hypertension)->{
                return Hypertension(activity)
            }
            activity.resources.getString(R.string.measles)->{
                return Measles(activity)
            }
            activity.resources.getString(R.string.myopia)->{
                return Myopia(activity)
            }
            activity.resources.getString(R.string.myocarditis)->{
                return Myocarditis(activity)
            }
            activity.resources.getString(R.string.otitis)->{
                return Otitis(activity)
            }
            activity.resources.getString(R.string.otosclerosis)->{
                return Otosclerosis(activity)
            }
            activity.resources.getString(R.string.pharyngitis)->{
                return Pharyngitis(activity)
            }
            activity.resources.getString(R.string.tonsillitis)->{
                return Tonsillitis(activity)
            }
            activity.resources.getString(R.string.sprain)->{
                return Sprain(activity)
            }
            activity.resources.getString(R.string.sleep_disorders)->{
                return BadSleep(activity)
            }
            activity.resources.getString(R.string.obesity)->{
                return Obesity(activity)
            }
            activity.resources.getString(R.string.flatfoot)->{
                return Flatfoot(activity)
            }
            activity.resources.getString(R.string.brain_concussion)->{
                return BrainConcussion(activity)
            }
            activity.resources.getString(R.string.intoxication)->{
                return Intoxication(activity)
            }
            activity.resources.getString(R.string.allergy)->{
                return Allergy(activity)
            }
            activity.resources.getString(R.string.stomatitis)->{
                return Stomatitis(activity)
            }
            activity.resources.getString(R.string.gastritis)->{
                return Gastritis(activity)
            }
            activity.resources.getString(R.string.herpes)->{
                return Herpes(activity)
            }
            activity.resources.getString(R.string.cholecystitis)->{
                return Cholecystitis(activity)
            }
            activity.resources.getString(R.string.laryngitis)->{
                return Laryngitis(activity)
            }
            activity.resources.getString(R.string.osteoarthritis)->{
                return Osteoarthritis(activity)
            }
            activity.resources.getString(R.string.atherosclerosis)->{
                return Atherosclerosis(activity)
            }
            activity.resources.getString(R.string.bronchitis)->{
                return Bronchitis(activity)
            }
            activity.resources.getString(R.string.scurvy)->{
                return Scurvy(activity)
            }
            activity.resources.getString(R.string.hives)->{
                return Hives(activity)
            }
            activity.resources.getString(R.string.nail_fungus)->{
                return NailFungus(activity)
            }
            activity.resources.getString(R.string.acne)->{
                return Acne(activity)
            }
            activity.resources.getString(R.string.gumboil)->{
                return Gumboil(activity)
            }
            activity.resources.getString(R.string.osteochondrosis)->{
                return Osteochondrosis(activity)
            }
            activity.resources.getString(R.string.migraine)->{
                return Migraine(activity)
            }
            activity.resources.getString(R.string.stenocardia)->{
                return Stenocardia(activity)
            }
            activity.resources.getString(R.string.conjunctivitis)->{
                return Conjunctivitis(activity)
            }
            activity.resources.getString(R.string.eczema)->{
                return Eczema(activity)
            }
            activity.resources.getString(R.string.lichen)->{
                return Lichen(activity)
            }
            activity.resources.getString(R.string.mononucleosis)->{
                return Mononucleosis(activity)
            }
            activity.resources.getString(R.string.streptococcus)->{
                return Streptococcus(activity)
            }
            activity.resources.getString(R.string.glaucoma)->{
                return Glaucoma(activity)
            }
            activity.resources.getString(R.string.depression)->{
                return Depression(activity)
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