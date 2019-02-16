package be_healthy_license_2014141300.be_healthy.fragment

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import be_healthy_license_2014141300.be_healthy.Advice
import com.be_healthy_license_2014141300.be_healthy.R

class AdviceFragment : Fragment() {

    private var advice1: Advice?=null
    private var advice2: Advice?=null
    private var advice3: Advice?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.n_fragment_advice, container, false)
        val text1=view.findViewById<TextView>(R.id.advice_text1)
        val text2=view.findViewById<TextView>(R.id.advice_text2)
        val text3=view.findViewById<TextView>(R.id.advice_text3)
        val button1=view.findViewById<ImageView>(R.id.update_image1)
        val button2=view.findViewById<ImageView>(R.id.update_image2)
        val button3=view.findViewById<ImageView>(R.id.update_image3)
        advice1=getUniqueData()
        advice2=getUniqueData()
        advice3=getUniqueData()
        text1.text = advice1!!.advice
        text2.text = advice2!!.advice
        text3.text = advice3!!.advice
        button1.setOnClickListener {
            advice1 = getUniqueData()
            text1.text = advice1!!.advice
        }
        button2.setOnClickListener {
            advice2 = getUniqueData()
            text2.text = advice2!!.advice
        }
        button3.setOnClickListener {
            advice3 = getUniqueData()
            text3.text = advice3!!.advice
        }
        return view
    }

    private fun getUniqueData(): Advice {
        var advice= Advice(activity)
        var unique=false
        while(!unique){
            unique=true
            if (advice==advice1 || advice==advice2 || advice==advice3){
                unique=false
                advice= Advice(activity)
            }
        }
        return advice
    }
}
