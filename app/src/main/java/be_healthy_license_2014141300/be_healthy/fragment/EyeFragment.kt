package com.be_healthy_license_2014141300.be_healthy.fragment

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be_healthy_license_2014141300.be_healthy.TrainHelper
import com.be_healthy_license_2014141300.be_healthy.R
import java.util.*

class EyeFragment : Fragment(){

    private lateinit var trainHelper:TrainHelper
    private var screenNo=0
    private var exNo=0

    private var time:Long=20000
    private var timeInfo:Timer?=null
    private lateinit var handler: Handler
    private var currentTime=time/1000

    private var progress=0

    companion object {
        private var fragment=EyeFragment()

        fun getInstance():EyeFragment{
            return fragment
        }
    }

    private var callback= Handler.Callback { p0 ->
        if(currentTime>=0) {
            trainHelper.updateTimer(currentTime)
            currentTime--
        }
        else{
            reset()
            trainHelper.hideScreen(screenNo)
            screenNo++
            trainHelper.showScreen(screenNo)
            trainHelper.updateInstruction(screenNo)
            currentTime=20
        }
    true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_eye, container, false)
        trainHelper= TrainHelper(view)
        trainHelper.showScreen(screenNo)
        trainHelper.updateInstruction(screenNo)
        handler = Handler(callback)
        var startButton=view.findViewById<FloatingActionButton>(R.id.start)
        var finishButton=view.findViewById<FloatingActionButton>(R.id.finish)
        startButton.setOnClickListener {
            screenNo++
            exNo++
            trainHelper.hideScreen(screenNo-1)
            if (screenNo<10){
                trainHelper.showScreen(screenNo)
            }
            trainHelper.updateInstruction(screenNo)
            reset()
            if (screenNo>1) {
                timeInfo = Timer()
                timeInfo?.schedule(UpdateTimeTask(), 0, 1000)
                progress++
                trainHelper.updateProgress(progress)
            }
        }
        finishButton.setOnClickListener {
            stop()
        }
        return view
    }

    private fun reset(){
        if (timeInfo != null) {
            timeInfo!!.cancel()
        }
    }

    fun stop(){
        try {
            reset()
            trainHelper.hideScreen(screenNo)
            screenNo = 0
            currentTime=20
            progress=0
            trainHelper.clearProgress()
            trainHelper.updateTimer(currentTime)
            trainHelper.showScreen(screenNo)
            trainHelper.updateInstruction(screenNo)
        }
        catch(e:UninitializedPropertyAccessException){
         //it's ok
        }
    }

    private inner class UpdateTimeTask: TimerTask() {
        override fun run() {
            handler.sendEmptyMessage(0)
        }
    }
}

