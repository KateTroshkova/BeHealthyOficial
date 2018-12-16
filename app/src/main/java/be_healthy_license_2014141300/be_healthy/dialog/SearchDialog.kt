package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import be_healthy_license_2014141300.be_healthy.adapter.StaticSymptomsAdapter
import be_healthy_license_2014141300.be_healthy.disease.*
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
    private lateinit var adapter: StaticSymptomsAdapter
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
        userRequestText=view.findViewById<EditText>(R.id.symptom_text)
        userRequestText.addTextChangedListener(watcher)
    }

    private fun initOptionsList(view:View){
        val list=view.findViewById<ListView>(R.id.all_symptoms_list)
        list.onItemClickListener=this
        adapter= StaticSymptomsAdapter(activity, currentSymptoms)
        list.adapter=adapter
    }

    private fun initData(){
        diseases = StaticDiseaseData.diseases
        allSymptoms.clear()
        for(disease in diseases){
            disease.symptoms
                    .filter { it !in allSymptoms }
                    .forEach {
                        allSymptoms.add(it)
                        currentSymptoms.add(it)
                    }
        }
        allSymptoms.remove("")
        currentSymptoms.remove("")
        Collections.sort(allSymptoms)
        Collections.sort(currentSymptoms)
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        listener?.onChooseDisease(currentSymptoms[position])
        if (p1 != null) {
            hideKeyboard(p1)
        }
        dismiss()
    }

    private fun hideKeyboard(view:View){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
            val subtext=userRequestText.text.toString().toLowerCase()
            allSymptoms
                    .filter { subtext in it }
                    .forEach { currentSymptoms.add(it) }
            adapter.notifyDataSetChanged()
        }
    }
}