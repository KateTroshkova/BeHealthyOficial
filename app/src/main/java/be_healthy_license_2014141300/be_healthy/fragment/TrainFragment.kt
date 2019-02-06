package be_healthy_license_2014141300.be_healthy.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.view.AnimationPathView
import com.be_healthy_license_2014141300.be_healthy.view.Circle
import java.util.*

class TrainFragment : Fragment() {

    private lateinit var animatedCircle:AnimationPathView
    private lateinit var bigTimer:TextView
    private lateinit var exCount:TextView
    private lateinit var circle:Circle
    private lateinit var smallTimer:TextView
    private lateinit var icon: ImageView
    private var trainNumber=0
    private var time:Long=20000
    private var timeInfo: Timer?=null
    private lateinit var handler: Handler
    private var currentTime=time/1000

    private var train= listOf<List<View>>(listOf(bigTimer, exCount), listOf(circle, smallTimer, exCount), listOf(circle, smallTimer, exCount),
            listOf(animatedCircle, smallTimer, exCount), listOf(icon, smallTimer))

    private var callback= Handler.Callback { p0 ->
        if(currentTime>=0) {
            updateTimer(currentTime)
            currentTime--
        }
        else{
            //TODO:go to next
        }
        true
    }

    private var movingPointReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            //TODO: go to next
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_eye, container, false)
        LocalBroadcastManager.getInstance(activity).registerReceiver(movingPointReceiver, IntentFilter(resources.getString(R.string.action_finish)))
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(movingPointReceiver)
        handler = Handler(callback)
        showTrain()
        return view
    }

    fun hideAll(){
        animatedCircle.visibility=View.INVISIBLE
        bigTimer.visibility=View.INVISIBLE
        exCount.visibility=View.INVISIBLE
        circle.visibility=View.INVISIBLE
        smallTimer.visibility=View.INVISIBLE
        icon.visibility=View.INVISIBLE
    }

    fun showTrain(){
        hideAll()
        for(view in train[trainNumber]){
            view.visibility=View.VISIBLE
        }
        if (trainNumber<3){
            startTimer()
        }
    }

    fun updateTimer(time:Long){
        smallTimer.text=time.toString()
    }

    fun startTimer(){
        if (timeInfo!=null){
            timeInfo?.cancel()
            timeInfo=null
        }
        timeInfo = Timer()
        timeInfo?.schedule(UpdateTimeTask(), 0, 1000)
    }

    private inner class UpdateTimeTask: TimerTask() {
        override fun run() {
            handler.sendEmptyMessage(0)
        }
    }
}
