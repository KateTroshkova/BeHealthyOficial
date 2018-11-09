package be_healthy_license_2014141300.be_healthy.fragment


import android.graphics.Color
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.be_healthy_license_2014141300.be_healthy.R
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.util.TypedValue
import android.widget.*
import be_healthy_license_2014141300.be_healthy.dialog.InfoDialog
import be_healthy_license_2014141300.be_healthy.view.IMBView
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.activity.MainActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class IMBFragment:Fragment() {

     private var age=0
     private var weight=0
     private var height=0

    private var minArray=arrayOf<String>()
    private var maxArray=arrayOf<String>()

    private lateinit var ageText:EditText
    private lateinit var weightText:EditText
    private lateinit var heightText:EditText
    private lateinit var infoText: TextView
    private lateinit var imb:IMBView

     override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
         var view=inflater!!.inflate(R.layout.fragment_imb, container, false)
         ageText=view.findViewById(R.id.editText3)
         weightText=view.findViewById<EditText>(R.id.editText4)
         heightText=view.findViewById<EditText>(R.id.editText5)
         infoText=view.findViewById<EditText>(R.id.textView7)
         imb=view.findViewById<IMBView>(R.id.imageView)
         var button=view.findViewById<AppCompatButton>(R.id.start)
         button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.textSize*(activity.application as CustomApplication).size_coef*0.6f)
         button.setOnClickListener {
             if (!ageText.text.isEmpty() && !weightText.text.isEmpty() && !heightText.text.isEmpty()){
                 age=ageText.text.toString().toInt()
                 weight=weightText.text.toString().toInt()
                 height=heightText.text.toString().toInt()
                 var result=weight.toFloat()/(height.toFloat()*height.toFloat())*100*100
                 if (age<=25){
                     minArray=resources.getStringArray(R.array.young_min_range)
                     maxArray=resources.getStringArray(R.array.young_max_range)
                 }
                 else{
                     minArray=resources.getStringArray(R.array.old_min_range)
                     maxArray=resources.getStringArray(R.array.old_max_range)
                 }
                 var index=0
                 for(i in 0.. minArray.size-1){
                     if (result>minArray[i].toFloat() && result<=maxArray[i].toFloat()){
                         index=i
                     }
                 }
                 result= (Math.round(result * 10.0) / 10.0).toFloat()
                 var dialog=InfoDialog()
                 dialog.setData((resources.getStringArray(R.array.fatty_diagnosis)[index]), "Индекс массы тела: "+result+"\n"+(resources.getStringArray(R.array.fatty_diagnosis)[index])+"\n"+
                         /**"Риск для здоровья: "+resources.getStringArray(R.array.risk_info)[index]+"\n"+*/
                         resources.getStringArray(R.array.fat_advice)[index])
                 dialog.show(fragmentManager, "")
                 imb.setSteps(result.toInt())
                 //if ((activity as MainActivity).mInterstitialAd.isLoaded()) {
                 //    (activity as MainActivity).mInterstitialAd.show()
                // }
             }
             else{
                 Toast.makeText(activity, resources.getString(R.string.empty_fields_error), Toast.LENGTH_SHORT).show()
             }
         }
         var mAdView = view.findViewById<AdView>(R.id.adView)
         val adRequest = AdRequest.Builder().build()
         mAdView.loadAd(adRequest)
         return view
     }

 }