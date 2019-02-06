package be_healthy_license_2014141300.be_healthy.fragment

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.Advice
import com.be_healthy_license_2014141300.be_healthy.R
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers

class AdviceFragment : Fragment() {

    private var advice1:Advice?=null
    private var advice2:Advice?=null
    private var advice3:Advice?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.n_fragment_advice, container, false)
        val text1=view.findViewById<TextView>(R.id.advice_text1)
        val text2=view.findViewById<TextView>(R.id.advice_text2)
        val text3=view.findViewById<TextView>(R.id.advice_text3)
        val button1=view.findViewById<ImageView>(R.id.update_image1)
        val button2=view.findViewById<ImageView>(R.id.update_image2)
        val button3=view.findViewById<ImageView>(R.id.update_image3)
        text1.text=getUniqueData().advice
        text2.text=getUniqueData().advice
        text3.text=getUniqueData().advice
        RxView.clicks(button1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    text1.text=getUniqueData().advice
                }
        RxView.clicks(button2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    text2.text=getUniqueData().advice
                }
        RxView.clicks(button3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    text3.text=getUniqueData().advice
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
