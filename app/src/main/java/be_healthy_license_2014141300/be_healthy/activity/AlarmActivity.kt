package com.be_healthy_license_2014141300.be_healthy.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import be_healthy_license_2014141300.be_healthy.UpdateSender
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import com.be_healthy_license_2014141300.be_healthy.R
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.IOException

class AlarmActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener {

    private lateinit var image: ImageView
    private lateinit var repeat: ImageView
    private lateinit var cancel: ImageView

    private var dx = 0.0f
    private var dy = 0.0f
    private var oldX=0.0f
    private var oldY=0.0f

    private lateinit var alarm: AlarmClock

    private var player: MediaPlayer?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        alarm=AlarmClock().decodeFromString(intent.action)
        (findViewById<TextView>(R.id.time_text) as TextView).text=formatTime(alarm.hour, alarm.minute)
        (findViewById<TextView>(R.id.description) as TextView).text=alarm.description
        var sleepButton=findViewById<AppCompatButton>(R.id.sleepButton)
        sleepButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, sleepButton.textSize*(application as CustomApplication).size_coef*0.6f)
        var cancelButton=findViewById<AppCompatButton>(R.id.cancelButton) as AppCompatButton
        cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, cancelButton.textSize*(application as CustomApplication).size_coef*0.6f)
        startAlarm(Uri.parse(alarm.ringtone))
        wakeUp()
    }

    private fun wakeUp(){
        window.setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }

    private fun startAlarm(uri: Uri){
        player= MediaPlayer()
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            player!!.setDataSource(this, uri)
            player!!.isLooping=true
            player!!.prepareAsync()
            player!!.setOnPreparedListener(this)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onPrepared(player: MediaPlayer?) {
        player?.start()
    }

    private fun release(){
        if (player!=null){
            if (player!!.isPlaying){
                player!!.stop()
            }
            player!!.release()
            player=null
        }
    }

    fun cancelAlarm(view:View){
        if (alarm.stopAlarm(this)){
            DB_Operation(this).updateAlarm(alarm)
            UpdateSender(this).send(R.string.action_alarm_update, R.string.param_alarm, alarm)
        }
        release()
        finish()
    }

    fun updateAlarm(view:View){
        alarm.updateAlarm(this)
        DB_Operation(this).updateAlarm(alarm)
        UpdateSender(this).send(R.string.action_alarm_update, R.string.param_alarm, alarm)
        release()
        finish()
    }

    private fun formatTime(hour:Int, minute:Int):String{
        var res=""
        if (hour<10){
            res+="0"
        }
        res+=hour.toString()+":"
        if (minute<10){
            res+="0"
        }
        res+=minute.toString()
        return res
    }
}
