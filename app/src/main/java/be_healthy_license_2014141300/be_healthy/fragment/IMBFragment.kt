package be_healthy_license_2014141300.be_healthy.fragment


import android.graphics.Color
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.be_healthy_license_2014141300.be_healthy.R
import android.graphics.drawable.GradientDrawable
import android.widget.*
import be_healthy_license_2014141300.be_healthy.view.IMBView


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
         ageText=view.findViewById(R.id.editText3) as EditText
         weightText=view.findViewById(R.id.editText4) as EditText
         heightText=view.findViewById(R.id.editText5) as EditText
         infoText=view.findViewById(R.id.textView7) as TextView
         imb=view.findViewById(R.id.imageView) as IMBView
         (view.findViewById(R.id.start)).setOnClickListener {
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
                 infoText.text="Индекс массы тела: "+result+"\n"+(resources.getStringArray(R.array.fatty_diagnosis)[index])+"\n"+
                         "Риск для здоровья: "+resources.getStringArray(R.array.risk_info)[index]+"\n"+
                         resources.getStringArray(R.array.fat_advice)[index]
                 imb.setSteps(result.toInt())
             }
             else{
                 Toast.makeText(activity, resources.getString(R.string.empty_fields_error), Toast.LENGTH_SHORT).show()
             }
         }
         return view
     }

 }