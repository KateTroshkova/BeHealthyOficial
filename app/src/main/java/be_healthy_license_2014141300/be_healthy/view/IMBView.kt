package be_healthy_license_2014141300.be_healthy.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.graphics.LinearGradient

class IMBView: View {

    private var stepSize=0
    private var steps=0

    constructor(context: Context) : super(context){
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
    }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle){
    }

    fun setSteps(steps:Int){
        if(steps>38){
            this.steps=38
        }
        else {
            if (steps < 2) {
                this.steps = 2
            } else {
                this.steps = steps
            }
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint= Paint(Paint.ANTI_ALIAS_FLAG)
        var shader = createShader()
        paint.setShader(shader)
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        stepSize=width/40
        var linePaint=Paint()
        linePaint.strokeWidth=10f
        canvas?.drawLine(stepSize*steps.toFloat(), 0f, stepSize*steps.toFloat(), height.toFloat(), linePaint)
    }

    private fun createShader(): Shader {
        return LinearGradient(0.toFloat(), 0f, width.toFloat(), 0f,
                intArrayOf(Color.parseColor("#aaaaaa"), Color.parseColor("#388e3c"), Color.parseColor("#ffca28")),
                floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.REPEAT)
    }
}