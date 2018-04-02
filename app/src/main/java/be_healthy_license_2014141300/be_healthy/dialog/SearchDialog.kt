package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import be_healthy_license_2014141300.be_healthy.disease.Acne
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.adapter.SymptomsAdapter
import com.be_healthy_license_2014141300.be_healthy.disease.*
import java.util.*

class SearchDialog: DialogFragment(), AdapterView.OnItemClickListener{

    companion object {
        @JvmField var listener:SearchDialog.OnChooseDiseaseListener?=null
    }

    private var diseases = mutableListOf<Disease>()
    private var allSymptoms = mutableListOf<String>()
    private var currentSymptoms = mutableListOf<String>()
    private lateinit var adapter: SymptomsAdapter
    private lateinit var userRequestText: EditText

    interface OnChooseDiseaseListener{
        fun onChooseDisease(value:String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.search_item, null)
        initData()
        initOptionsList(view)
        initHintEditText(view)
        dialog.setNegativeButton(activity.resources.getString(R.string.cancel), null)
        dialog.setView(view)
        return dialog.create()
    }

    fun setListener(l:OnChooseDiseaseListener){
        listener=l
    }

    private fun initHintEditText(view:View){
        userRequestText=view.findViewById(R.id.symptom_text) as EditText
        userRequestText.addTextChangedListener(watcher)
    }

    private fun initOptionsList(view:View){
        val list=view.findViewById(R.id.all_symptoms_list) as ListView
        list.onItemClickListener=this
        adapter=SymptomsAdapter(activity, currentSymptoms)
        list.adapter=adapter
    }

    private fun initData(){
        diseases = mutableListOf(Angina(activity), ARD(activity), Arrhythmia(activity), ARVI(activity),
                Asthma(activity), Caries(activity), Cataract(activity), DryEyeSyndrome(activity), Flu(activity),
                Frontite(activity), Hypertension(activity), Measles(activity), Myocarditis(activity), Myopia(activity),
                Otitis(activity), Otosclerosis(activity), Pharyngitis(activity), Tonsillitis(activity), Sprain(activity),
                BadSleep(activity), Obesity(activity), Flatfoot(activity), BrainConcussion(activity), Intoxication(activity),
                Allergy(activity), Stomatitis(activity), Gastritis(activity), Herpes(activity), Cholecystitis(activity),
                Laryngitis(activity), Osteoarthritis(activity), Atherosclerosis(activity), Bronchitis(activity), Scurvy(activity),
                Hives(activity), NailFungus(activity), Acne(activity))
        allSymptoms.clear()
        for(disease in diseases){
            disease.symptoms
                    .filter { it !in allSymptoms }
                    .forEach {
                        allSymptoms.add(it)
                        currentSymptoms.add(it)
                    }
        }
        Collections.sort(allSymptoms)
        Collections.sort(currentSymptoms)
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        listener?.onChooseDisease(currentSymptoms[position])
        dismiss()
    }

    private var watcher = object: TextWatcher {

        override fun afterTextChanged(editable: Editable?) {}

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            currentSymptoms.clear()
            for(symptom in allSymptoms){
                currentSymptoms.add(symptom)
            }
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            currentSymptoms.clear()
            val subtext=userRequestText.text.toString()
            allSymptoms
                    .filter { subtext in it }
                    .forEach { currentSymptoms.add(it) }
            adapter.notifyDataSetChanged()
        }
    }
}