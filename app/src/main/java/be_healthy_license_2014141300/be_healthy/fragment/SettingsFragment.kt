package com.be_healthy_license_2014141300.be_healthy.fragment

import android.app.Fragment
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import be_healthy_license_2014141300.be_healthy.dialog.UserTermsDialog
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.AppCompatButton
import be_healthy_license_2014141300.be_healthy.dialog.DeleteDialog
import com.be_healthy_license_2014141300.be_healthy.ShareManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView


class SettingsFragment : Fragment(), SeekBar.OnSeekBarChangeListener{

    private var defaultSize=12
    private lateinit var sizeText: TextView
    //private lateinit var ageInfoText:TextView
    //private lateinit var ageText: EditText
    private lateinit var content:View
    //private lateinit var removeHistoryButton:AppCompatButton
    private lateinit var userTermsButton:AppCompatButton
    private lateinit var webButton:AppCompatButton

    companion object {
        private var fragment:SettingsFragment?=null

        fun getInstance(): SettingsFragment {
            if (fragment==null){
                fragment=SettingsFragment()
            }
            return fragment as SettingsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        content = inflater!!.inflate(R.layout.fragment_settings, container, false)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        sizeText=content.findViewById(R.id.size_text)
        webButton=content.findViewById<AppCompatButton>(R.id.web)
        userTermsButton=content.findViewById<AppCompatButton>(R.id.userterms)

        val seekbar=content.findViewById<SeekBar>(R.id.seekBar)
        seekbar.setOnSeekBarChangeListener(this)
        seekbar.max=20

        val preferences=activity.getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val sizeToLoad=(preferences.getFloat(activity.resources.getString(R.string.param_size), 1f)*defaultSize).toInt()
        sizeText.text=activity.resources.getString(R.string.text_size)+" "+sizeToLoad
        sizeText.textSize= sizeToLoad.toFloat()
        seekbar.progress= sizeToLoad-defaultSize
        userTermsButton.setOnClickListener {UserTermsTask().execute()}
        webButton.setOnClickListener {
            val url = "https://dshv12bh3.wixsite.com/behealthy"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        userTermsButton.textSize=sizeToLoad.toFloat()
        webButton.textSize=sizeToLoad.toFloat()
        var mAdView = content.findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        (content.findViewById<Button>(R.id.share).setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                ShareManager(activity).saveUrl()
                Toast.makeText(activity, resources.getString(R.string.shared_info), Toast.LENGTH_SHORT).show()
            }
        }))
        return content
    }

    private fun supportTextSize(size:Int){
        val preferences=activity.getSharedPreferences(activity.resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val editor=preferences.edit()
        editor.putFloat(activity.resources.getString(R.string.param_size), (size/defaultSize.toFloat()))
        editor.apply()
    }

    /**private fun supportAge(age:Int){
        hideKeyboard(content)
        val preferences=activity.getSharedPreferences(activity.resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val editor=preferences.edit()
        editor.putInt(activity.resources.getString(R.string.param_age), age)
        editor.apply()
    }*/

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        val size=p0?.progress!!+defaultSize
        sizeText.text = activity.resources.getString(R.string.text_size) + " " + size
        sizeText.textSize= size.toFloat()
        //ageText.textSize =size.toFloat()
        //ageInfoText.textSize=size.toFloat()
        //removeHistoryButton.textSize=size.toFloat()
        webButton.textSize=size.toFloat()
        userTermsButton.textSize=size.toFloat()
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        supportTextSize(p0?.progress!!+defaultSize)
        (activity.application as CustomApplication).size_coef=(p0.progress+defaultSize)/defaultSize.toFloat()
    }

    /**private fun hideKeyboard(view:View){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }*/

    private inner class UserTermsTask: AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg p0: Void?): String {
            var termsOfUse=""
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
                        reader.close()
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
                val dialog = UserTermsDialog()
                dialog.setData(result.replace("#", "\n\n"))
                dialog.show(activity.fragmentManager, "userterms")
            }
        }
    }
}
