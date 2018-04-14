package com.be_healthy_license_2014141300.be_healthy.fragment

import android.app.Activity
import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.view.AnimationPathView
import com.be_healthy_license_2014141300.be_healthy.view.Circle
import com.be_healthy_license_2014141300.be_healthy.view.CustomSizeTextView
import java.util.*

class EyeFragment : Fragment(), View.OnClickListener {

    private lateinit var circle: Circle
    private lateinit var path: AnimationPathView
    private lateinit var startButton: FloatingActionButton
    private lateinit var finishButton: FloatingActionButton
    private lateinit var info: TextView
    private lateinit var timerText:TextView

    private var time:Long=20000
    private var timeInfo:Timer?=null
    private var timer:Timer?=null
    private lateinit var handler: Handler

    private val ACTION_INSTRUCTION_2=0
    private val ACTION_INSTRUCTION_3=1
    private val ACTION_INSTRUCTION_4=2

    private val ACTION_UPDATE_TIME=3

    private var currentTime=time/1000

    private lateinit var existActivity: Activity

    companion object {
        private var fragment:EyeFragment?=null

        fun getInstance(): EyeFragment {
            if (fragment==null){
                fragment=EyeFragment()
            }
            return fragment as EyeFragment
        }
    }

    private var receiver=object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            reset()
            info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[5]
            finishButton.visibility = View.VISIBLE
            startButton.visibility=View.INVISIBLE
        }
    }

    private var callback= Handler.Callback { p0 ->
        when(p0?.what){
            ACTION_INSTRUCTION_2->{
                reset()
                info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[2]
                circle.visibility = View.VISIBLE
                timerText.visibility=View.VISIBLE
                finishButton.visibility=View.VISIBLE
                startButton.visibility=View.INVISIBLE
                timeInfo = Timer()
                timeInfo?.schedule(UpdateTimeTask(), 0, 1000)
            }
            ACTION_INSTRUCTION_3->{
                reset()
                info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[3]
                circle.visibility = View.VISIBLE
                timerText.visibility=View.VISIBLE
                finishButton.visibility=View.VISIBLE
                startButton.visibility=View.INVISIBLE
                timeInfo = Timer()
                timeInfo?.schedule(UpdateTimeTask(), 0, 1000)
            }
            ACTION_INSTRUCTION_4->{
                reset()
                info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[4]
                path.visibility = View.VISIBLE
                finishButton.visibility=View.VISIBLE
                startButton.visibility=View.INVISIBLE
            }
            ACTION_UPDATE_TIME->{
                if(currentTime>=0) {
                    timerText.text = currentTime.toString()
                    currentTime--
                }
                else{
                    info.text=""
                    timerText.visibility=View.INVISIBLE
                }
            }
            else->{
                Toast.makeText(existActivity, existActivity.resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
        true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_eye, container, false)
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(activity.resources.getString(R.string.action_finish)))
        existActivity=activity
        startButton=view.findViewById(R.id.start) as FloatingActionButton
        startButton.setOnClickListener(this)
        finishButton=view.findViewById(R.id.finish) as FloatingActionButton
        finishButton.setOnClickListener(this)
        circle=view.findViewById(R.id.circle) as Circle
        path=view.findViewById(R.id.moveView) as AnimationPathView
        handler = Handler(callback)
        timerText=view.findViewById(R.id.timer) as CustomSizeTextView
        info=view.findViewById(R.id.instruction) as TextView
        reset()
        return view
    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.start->{
                start()
                startButton.visibility = View.INVISIBLE
                timerText.visibility=View.VISIBLE
                finishButton.visibility=View.VISIBLE
            }
            R.id.finish->{
                reset()
            }
        }
    }

    private fun reset(){
        try {
            if (timeInfo != null) {
                timeInfo!!.cancel()
            }
            timerText.text = ""
            currentTime = time/1000
            timerText.visibility = View.INVISIBLE
            finishButton.visibility = View.INVISIBLE
            circle.visibility = View.INVISIBLE
            path.visibility = View.INVISIBLE
            startButton.visibility = View.VISIBLE
            try {
                info.text = activity.resources.getStringArray(R.array.eye_training_instruction)[0]
            }
            catch(e:NullPointerException){
                //activity died
            }
        }
        catch(e:UninitializedPropertyAccessException){
            //it's ok
        }
    }

    fun stop(){
        if (timer!=null){
            timer!!.cancel()
        }
        reset()
    }

    private fun start(){
        info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[1]
        timer = Timer()
        timer!!.schedule(LookAtWindowTask(), time)
        timer!!.schedule(FarCloseTask(), 2*time)
        timer!!.schedule(MoveTask(), 3*time)
        timeInfo = Timer()
        timeInfo?.schedule(UpdateTimeTask(), 0, 1000)
    }

    private inner class LookAtWindowTask: TimerTask(){
        override fun run() {
            handler.sendEmptyMessage(ACTION_INSTRUCTION_2)
        }
    }

    private inner class FarCloseTask: TimerTask(){
        override fun run() {
            handler.sendEmptyMessage(ACTION_INSTRUCTION_3)
        }
    }

    private inner class MoveTask: TimerTask(){
        override fun run() {
            handler.sendEmptyMessage(ACTION_INSTRUCTION_4)
        }
    }

    private inner class UpdateTimeTask:TimerTask(){
        override fun run() {
            handler.sendEmptyMessage(ACTION_UPDATE_TIME)
        }

    }
}

