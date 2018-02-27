package com.be_healthy_license_2014141300.be_healthy.view

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.support.v4.content.LocalBroadcastManager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.be_healthy_license_2014141300.be_healthy.R

class AnimationPathView : View {

    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var pathMeasure: PathMeasure
    private lateinit var mMatrix: Matrix

    private var length: Float = 0.toFloat()
    private var step = 10f
    private var distance: Float = 0.toFloat()
    private var position = FloatArray(2)
    private var tan= FloatArray(2)
    private var currentX: Float = 0.toFloat()
    private var currentY: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.parseColor("#388e3c")
        paint.style = Paint.Style.FILL

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)

        path = Path()
        path.moveTo(50f, (metrics.heightPixels/2).toFloat())
        for(i in 0..3) {
            drawRightCircle(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
        }
        for(i in 0..3){
            drawLeftCircle(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
        }
        for(i in 0..3){
            drawHorizontalLine(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
            drawHorizontalLine(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
            toCenter(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
            drawVerticalLine(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
            drawVerticalLine(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
            toCenter(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
        }
        for(i in 0..2) {
            horizontalInfinity(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
        }
        for(i in 0..2) {
            verticalInfinity(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
        }
        val mCornerPathEffect = CornerPathEffect(200f)
        paint.pathEffect = mCornerPathEffect
        pathMeasure = PathMeasure(path, false)
        length = pathMeasure.length
        mMatrix = Matrix()
    }

    private fun drawRightCircle(width:Float, height:Float){
        path.lineTo(50f, 50f)
        path.lineTo(width - 50f, 50f)
        path.lineTo(width - 50f, height-50)
        path.lineTo(50f, height-50)
        path.lineTo(50f, height/2)
    }

    private fun drawLeftCircle(width:Float, height:Float){
        path.lineTo(50f, height-50)
        path.lineTo(width - 50f, height-50)
        path.lineTo(width - 50f, 50f)
        path.lineTo(50f, 50f)
        path.lineTo(50f, height/2)
    }

    private fun drawHorizontalLine(width:Float, height:Float){
        path.lineTo(width - 50f, height/2)
        path.lineTo(50f, height/2)
    }

    private fun drawVerticalLine(width:Float, height:Float){
        path.lineTo(width/2, height-50)
        path.lineTo(width/2, 50f)
    }

    private fun toCenter(width:Float, height:Float){
        path.lineTo(width/2, height/2)
    }

    private fun horizontalInfinity(width:Float, height:Float){
        path.lineTo(width/2, height/2)
        path.lineTo(3*width/4, height/4)
        path.lineTo(width, height/2)
        path.lineTo(3*width/4, 3*height/4)
        path.lineTo(width/2, height/2)
        path.lineTo(width/4, height/4)
        path.lineTo(0f, height/2)
        path.lineTo(width/4, 3*height/4)
    }

    private fun verticalInfinity(width:Float, height:Float){
        path.lineTo(width/2, height/2)
        path.lineTo(3*width/4, height/4)
        path.lineTo(width/2, 0f)
        path.lineTo(width/4, height/4)
        path.lineTo(width/2, height/2)
        path.lineTo(3*width/4, 3*height/4)
        path.lineTo(width/2, height)
        path.lineTo(width/4, 3*height/4)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mMatrix.reset()
        if (distance < length) {
            pathMeasure.getPosTan(distance, position, tan)
            currentX = position[0]
            currentY = position[1]
            mMatrix.postTranslate(currentX, currentY)
            canvas.drawCircle(currentX, currentY, 42f, paint)
            distance += step
        } else {
            distance=0f
            val intent= Intent(context.resources.getString(R.string.action_finish))
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
        invalidate()
    }
}