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

    private var time:Long=30000
    private lateinit var timeInfo:Timer
    private lateinit var handler: Handler

    private val ACTION_SHOW_CIRCLE=0
    private val ACTION_HIDE_CIRCLE=1
    private val ACTION_SHOW_PATH=4
    private val ACTION_HIDE_PATH=5
    private val ACTION_INSTRUCTION_2=6
    private val ACTION_INSTRUCTION_3=7
    private val ACTION_INSTRUCTION_4=8

    private val ACTION_UPDATE_TIME=9

    private var currentTime=30

    private lateinit var existActivity: Activity

    private var receiver=object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            handler.sendEmptyMessage(ACTION_HIDE_PATH)
            info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[5]
            finishButton.visibility = View.VISIBLE
        }
    }

    private var callback= Handler.Callback { p0 ->
        when(p0?.what){
            ACTION_SHOW_CIRCLE->{
                circle.visibility = View.VISIBLE
            }
            ACTION_HIDE_CIRCLE->{
                circle.visibility = View.INVISIBLE
            }
            ACTION_SHOW_PATH->{
                timeInfo.cancel()
                timerText.text=""
                path.visibility = View.VISIBLE
            }
            ACTION_HIDE_PATH->{
                path.visibility = View.INVISIBLE
            }
            ACTION_INSTRUCTION_2->{
                info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[2]
                currentTime=30
            }
            ACTION_INSTRUCTION_3->{
                info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[3]
                currentTime=30
            }
            ACTION_INSTRUCTION_4->{
                info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[4]
                currentTime=30
            }
            ACTION_UPDATE_TIME->{
                if(currentTime>=0) {
                    timerText.text = currentTime.toString()
                    //Log.e("LOG", currentTime.toString())
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
        finishButton.visibility = View.INVISIBLE
        circle=view.findViewById(R.id.circle) as Circle
        circle.visibility = View.INVISIBLE
        path=view.findViewById(R.id.moveView) as AnimationPathView
        path.visibility = View.INVISIBLE
        handler = Handler(callback)
        timerText=view.findViewById(R.id.timer) as CustomSizeTextView
        timerText.visibility=View.INVISIBLE
        info=view.findViewById(R.id.instruction) as TextView
        info.text=activity.resources.getStringArray(R.array.eye_training_instruction)[0]
        return view
    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.start->{
                start()
                startButton.visibility = View.INVISIBLE
                timerText.visibility=View.VISIBLE
            }
            R.id.finish->{
                info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[0]
                startButton.visibility = View.VISIBLE
                finishButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun start(){
        info.text =existActivity.resources.getStringArray(R.array.eye_training_instruction)[1]
        var timer = Timer()
        timer.schedule(LookAtWindowTask(), time)
        timer.schedule(FarCloseTask(), 2*time)
        timer.schedule(MoveTask(), 3*time)
        timeInfo = Timer()
        timeInfo.schedule(UpdateTimeTask(), 0, 1000)
    }

    private inner class LookAtWindowTask: TimerTask(){
        override fun run() {
            handler.sendEmptyMessage(ACTION_SHOW_CIRCLE)
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
            handler.sendEmptyMessage(ACTION_HIDE_CIRCLE)
            handler.sendEmptyMessage(ACTION_SHOW_PATH)
            handler.sendEmptyMessage(ACTION_INSTRUCTION_4)
        }
    }

    private inner class UpdateTimeTask:TimerTask(){
        override fun run() {
            handler.sendEmptyMessage(ACTION_UPDATE_TIME)
        }

    }
}

