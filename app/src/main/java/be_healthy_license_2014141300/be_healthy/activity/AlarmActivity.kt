package com.be_healthy_license_2014141300.be_healthy.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import be_healthy_license_2014141300.be_healthy.UpdateSender
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import com.be_healthy_license_2014141300.be_healthy.R
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
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

       // repeat = findViewById(R.id.sleep_image) as ImageView
       // cancel = findViewById(R.id.cancel_image) as ImageView
       // image = findViewById(R.id.alarm_image) as ImageView
       // image.setOnTouchListener(this)
//        val toolBar=findViewById(R.id.toolbar) as Toolbar
//        toolBar.title="Будильник"
//        setSupportActionBar(toolbar)
        (findViewById(R.id.time_text) as TextView).text=formatTime(alarm.hour, alarm.minute)
        (findViewById(R.id.description) as TextView).text=alarm.description
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

    /**override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            dx = x - image.x
            dy = y - image.y
            oldX=image.x
            oldY=image.y
        }
        if (event?.action == MotionEvent.ACTION_MOVE) {
            image.x = event.x -dx
            image.y = event.y -dy
        }
        if (event?.action == MotionEvent.ACTION_UP) {
            if (closeEnough(image, repeat)) {
                updateAlarm()
                finish()
            }
            else {
                if (closeEnough(image, cancel)) {
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
    }*/

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

    /**private fun closeEnough(image:ImageView, goal:ImageView) =
            image.x > goal.x - 3 * image.width &&
                    image.x < goal.x + 3*image.width &&
                    image.y > goal.y - 3*image.width &&
                    image.y < goal.y + 3*image.width*/
}
