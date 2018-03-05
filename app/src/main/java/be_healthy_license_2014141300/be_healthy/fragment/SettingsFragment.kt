package com.be_healthy_license_2014141300.be_healthy.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import be_healthy_license_2014141300.be_healthy.dialog.UserTermsDialog
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.database.DB_Operation
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SettingsFragment : Fragment(), View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    //private var defaultLanguage:Int=0
    private var defaultSize=12
    private lateinit var sizeText: TextView
    private lateinit var ageInfoText:TextView
    private lateinit var ageText: EditText
    private lateinit var languageText: TextView
    private lateinit var rusButton: RadioButton
    private lateinit var engButton: RadioButton
    private lateinit var content:View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        content = inflater!!.inflate(R.layout.fragment_settings, container, false)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        rusButton=content.findViewById(R.id.russian_button) as RadioButton
        engButton=content.findViewById(R.id.english_button) as RadioButton
        rusButton.setOnClickListener(this)
        engButton.setOnClickListener(this)
        sizeText=content.findViewById(R.id.size_text) as TextView
        languageText=content.findViewById(R.id.language_text) as TextView
        ageText=content.findViewById(R.id.age_edit_text) as EditText
        ageInfoText=content.findViewById(R.id.textView) as TextView

        val seekbar=content.findViewById(R.id.seekBar) as SeekBar
        seekbar.setOnSeekBarChangeListener(this)
        seekbar.max=30

        val preferences=activity.getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val languageToLoad = preferences.getString(resources.getString(R.string.param_language), "ru")
        val sizeToLoad=(preferences.getFloat(activity.resources.getString(R.string.param_size), 1f)*defaultSize).toInt()
        val age=preferences.getInt(activity.resources.getString(R.string.param_age), 0)
        sizeText.text=activity.resources.getString(R.string.text_size)+" "+sizeToLoad
        sizeText.textSize= sizeToLoad.toFloat()
        languageText.textSize= sizeToLoad.toFloat()
        ageText.textSize=sizeToLoad.toFloat()
        ageText.setText(age.toString())
        rusButton.textSize=sizeToLoad.toFloat()
        engButton.textSize=sizeToLoad.toFloat()
        seekbar.progress= sizeToLoad-defaultSize
        val defaultLanguage=activity.resources.getStringArray(R.array.keys).indexOf(languageToLoad)
        when (defaultLanguage){
            0->{
                rusButton.isChecked=true
            }
            1->{
                engButton.isChecked=true
            }
        }
        ageText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val age=ageText.text.toString().toInt()
                if (age>0) {
                    supportAge(age)
                }
                else{
                    Toast.makeText(activity, activity.resources.getString(R.string.error_negative_age), Toast.LENGTH_SHORT).show()
                }
                return@OnEditorActionListener true
            }
            false
        })
        (content.findViewById(R.id.fab)).setOnClickListener { DB_Operation(activity).clearHistory() }
        (content.findViewById(R.id.userterms)).setOnClickListener {UserTermsTask(activity).execute()}
        return content
    }

    private fun supportLanguage(language:String){
        val preferences=activity.getSharedPreferences(activity.resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val editor=preferences.edit()
        editor.putString(activity.resources.getString(R.string.param_language), language)
        editor.apply()
        activity.recreate()
    }

    private fun supportTextSize(size:Int){
        val preferences=activity.getSharedPreferences(activity.resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val editor=preferences.edit()
        editor.putFloat(activity.resources.getString(R.string.param_size), (size/defaultSize.toFloat()))
        editor.apply()
    }

    private fun supportAge(age:Int){
        hideKeyboard(content)
        val preferences=activity.getSharedPreferences(activity.resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val editor=preferences.edit()
        editor.putInt(activity.resources.getString(R.string.param_age), age)
        editor.apply()
    }

    override fun onClick(p0: View?) {
        val position = activity.resources.getStringArray(R.array.languages).indexOf((p0 as RadioButton).text)
        supportLanguage(activity.resources.getStringArray(R.array.keys)[position])
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        val size=p0?.progress!!+defaultSize
        sizeText.text = activity.resources.getString(R.string.text_size) + " " + size
        sizeText.textSize= size.toFloat()
        ageText.textSize =size.toFloat()
        ageInfoText.textSize=size.toFloat()
        languageText.textSize=size.toFloat()
        rusButton.textSize=size.toFloat()
        engButton.textSize=size.toFloat()
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        supportTextSize(p0?.progress!!+defaultSize)
        (activity.application as CustomApplication).size_coef=(p0.progress+defaultSize)/defaultSize.toFloat()
    }

    private fun hideKeyboard(view:View){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private class UserTermsTask(var activity: Activity): AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg p0: Void?): String {
            var termsOfUse="";
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(InputStreamReader(activity.assets.open("data.txt"), "UTF-8"))
                var line= reader.readLine()
                while (line!= null) {
                    termsOfUse+=line
                    line= reader.readLine()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return termsOfUse
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result!=null) {
                val dialog = UserTermsDialog(result.replace("#", "\n\n"))
                dialog.show(activity.fragmentManager, "userterms")
            }
        }
    }
}
