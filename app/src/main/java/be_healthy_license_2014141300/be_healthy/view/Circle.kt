package be_healthy_license_2014141300.be_healthy.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Circle: View {

    private val paint=Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = Color.parseColor("#388e3c")
        canvas?.drawCircle(width / 2f, height / 2f, 42f, paint)
    }
}