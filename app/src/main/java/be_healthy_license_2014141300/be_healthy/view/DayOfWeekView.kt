package com.be_healthy_license_2014141300.be_healthy.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.CheckBox

class DayOfWeekView(context: Context?, attrs: AttributeSet?) : CheckBox(context, attrs) {

    private var color= Color.WHITE
    var currentid=-1

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        if (isChecked){
            color= Color.LTGRAY
        }
        else{
            color= Color.WHITE
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        val paint= Paint()
        paint.color = color
        canvas?.drawCircle(width/2f, width/2f, (width/2f)-2, paint)
        paint.textSize=(width/2f)-2
        paint.color= Color.BLACK
        canvas?.drawText(text as String?, width/8f, 2*width/3f, paint)
    }
}