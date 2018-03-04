package com.be_healthy_license_2014141300.be_healthy.activity

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.database.DB_Operation
import java.io.IOException
import android.view.WindowManager

class AlarmActivity : AppCompatActivity(), View.OnTouchListener, MediaPlayer.OnPreparedListener {

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
        repeat = findViewById(R.id.sleep_image) as ImageView
        cancel = findViewById(R.id.cancel_image) as ImageView
        image = findViewById(R.id.alarm_image) as ImageView
        image.setOnTouchListener(this)
        alarm=AlarmClock().decodeFromString(intent.action)
        (findViewById(R.id.time_text) as TextView).text=formatTime(alarm.hour, alarm.minute)
        (findViewById(R.id.description) as TextView).text=alarm.description
        alarm(Uri.parse(alarm.ringtone))
        window.setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }

    private fun alarm(uri: Uri){
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

    override fun onPrepared(p0: MediaPlayer?) {
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

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (p1?.action == MotionEvent.ACTION_DOWN) {
            val x = p1.x
            val y = p1.y
            dx = x - image.x
            dy = y - image.y
            oldX=image.x
            oldY=image.y
        }
        if (p1?.action == MotionEvent.ACTION_MOVE) {
            image.x = p1.x -dx
            image.y = p1.y -dy
        }
        if (p1?.action == MotionEvent.ACTION_UP) {
            if (image.x > repeat.x-3*image.width && image.x < repeat.x+3*image.width && image.y > repeat.y-3*image.width && image.y < repeat.y+3*image.width) {
                updateAlarm()
                finish()
            }
            else {
                if (image.x > cancel.x - 3*image.width && image.x < cancel.x + 3*image.width && image.y > cancel.y - 3*image.width && image.y < cancel.y + 3*image.width) {
                    cancelAlarm()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else{
                    image.x=oldX
                    image.y=oldY
                }
            }
        }
        return true
    }

    private fun cancelAlarm(){
        if (alarm.repeat==0) {
            alarm.cancelAlarm(this)
            alarm.alarm=0
            DB_Operation(this).updateAlarm(alarm)
            val intent= Intent(resources.getString(R.string.action_alarm_update))
            intent.putExtra(resources.getString(R.string.param_alarm), alarm)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
        release()
    }

    private fun updateAlarm(){
        alarm.cancelAlarm(this)
        var hour=alarm.hour
        var minute=alarm.minute
        minute += 5
        if (minute>=60){
            minute -= 60
            hour += 1
        }
        if (hour>=24){
            hour -= 24
        }
        alarm.hour=hour
        alarm.minute=minute
        if (alarm.repeat==1){
            alarm.setRepeatingAlarm(this)
        }
        else {
            alarm.setAlarm(this)
        }
        DB_Operation(this).updateAlarm(alarm)
        val intent= Intent(resources.getString(R.string.action_alarm_update))
        intent.putExtra(resources.getString(R.string.param_alarm), alarm)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        release()
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
