package com.be_healthy_license_2014141300.be_healthy.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.util.DisplayMetrics
import com.be_healthy_license_2014141300.be_healthy.R

class HeartView(context: Context, attribute: AttributeSet): View(context, attribute) {

    private var length=0

    init{
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        length=metrics.widthPixels/160
    }

    private var map=""

    fun update(map:String){
        this.map=map
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint= Paint()
        var x=0
        val y=100
        paint.color = Color.parseColor("#388e3c")
        for(point in map){
            if (point=='0'){
                canvas?.drawLine(x.toFloat(), y.toFloat(), (x+length).toFloat(), y.toFloat(), paint)
            }
            else{
                canvas?.drawLine(x.toFloat(), y.toFloat(), (x+length/3).toFloat(), (y-4*length).toFloat(), paint)
                canvas?.drawLine((x+length/3).toFloat(), (y-4*length).toFloat(), (x+2*length/3).toFloat(), (y+4*length).toFloat(), paint)
                canvas?.drawLine((x+2*length/3).toFloat(), (y+4*length).toFloat(), (x+length).toFloat(), y.toFloat(), paint)
            }
            x+=length
        }
    }
}