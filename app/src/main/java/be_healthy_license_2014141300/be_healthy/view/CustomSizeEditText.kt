package com.be_healthy_license_2014141300.be_healthy.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.EditText
import com.be_healthy_license_2014141300.be_healthy.CustomApplication

class CustomSizeEditText(context: Context?, attrs: AttributeSet?) : EditText(context, attrs) {

    init{
        var size_coef: Float
        try {
            size_coef = ((context as Activity).application as CustomApplication).size_coef
        }
        catch(e:Exception){
            size_coef=0f
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * size_coef)
    }
}