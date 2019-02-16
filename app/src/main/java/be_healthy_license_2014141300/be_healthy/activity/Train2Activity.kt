package be_healthy_license_2014141300.be_healthy.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.LocalBroadcastManager
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.be_healthy_license_2014141300.be_healthy.R
import be_healthy_license_2014141300.be_healthy.view.AnimationPathView
import be_healthy_license_2014141300.be_healthy.view.Circle
import java.util.*

class Train2Activity : AppCompatActivity(),
        BottomNavigationView.OnNavigationItemSelectedListener,
        AnimationPathView.OnStepListener {

    private lateinit var animatedCircle: AnimationPathView
    private lateinit var bigTimer: TextView
    private lateinit var exCount: TextView
    private lateinit var circle: Circle
    private lateinit var smallTimer: TextView
    private lateinit var icon: ImageView
    private lateinit var progress:ProgressBar
    private var trainNumber=0
    private var time:Long=20000
    private var timeInfo: Timer?=null
    private lateinit var handler: Handler
    private var currentTime=time/1000

    private var train=listOf<List<View>>()

    override fun onStep(step: Float, max: Float) {
        progress.max=max.toInt()
        progress.progress=step.toInt()
    }

    private var callback = Handler.Callback {
        if(currentTime>=0) {
            updateTimer(currentTime)
            currentTime--
        }
        else{
            goToNextInstruction()
        }
        true
    }

    private var movingPointReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            goToNextInstruction()
        }
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(movingPointReceiver)
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(movingPointReceiver, IntentFilter(resources.getString(R.string.action_finish)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.n_activity_train2)
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.menu.setGroupCheckable(0, false, true)

        handler = Handler(callback)
        animatedCircle=findViewById(R.id.path)
        bigTimer=findViewById(R.id.textView8)
        exCount=findViewById(R.id.textView9)
        circle=findViewById(R.id.circle)
        smallTimer=findViewById(R.id.textView10)
        icon=findViewById(R.id.imageView8)
        progress=findViewById(R.id.progressBar2)
        animatedCircle.setListener(this)
        progress.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))

        train= listOf(listOf(bigTimer, exCount), listOf(circle, smallTimer, exCount), listOf(circle, smallTimer, exCount),
                listOf(animatedCircle, exCount, progress), listOf(icon, exCount))

        if (intent.hasExtra(resources.getString(R.string.param_train_number))){
            trainNumber=intent.getIntExtra(resources.getString(R.string.param_train_number), 0)
        }

        (findViewById<View>(R.id.back_button)).setOnClickListener { this.onBackPressed() }

        showTrain()
    }

    private fun hideAll(){
        animatedCircle.visibility= View.INVISIBLE
        bigTimer.visibility= View.INVISIBLE
        exCount.visibility= View.INVISIBLE
        circle.visibility= View.INVISIBLE
        smallTimer.visibility= View.INVISIBLE
        icon.visibility= View.INVISIBLE
        progress.visibility=View.INVISIBLE
    }

    private fun showTrain(){
        hideAll()
        if (trainNumber==4){
            val handler = Handler()
            handler.postDelayed({
                for(view in train[trainNumber]){
                    view.visibility= View.VISIBLE
                }
                exCount.text=(trainNumber+1).toString()+'/'+'5'
            }, 10000)
        }
        else {
            for (view in train[trainNumber]) {
                view.visibility = View.VISIBLE
            }
            if (trainNumber < 3) {
                startTimer()
            }
            exCount.text = (trainNumber + 1).toString() + '/' + '5'
        }
    }

    private fun updateTimer(time:Long){
        smallTimer.text=time.toString()
        bigTimer.text=time.toString()
    }

    private fun startTimer(){
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        reset()
        when (item.itemId) {
            R.id.main -> {
                val intent= Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
            R.id.search -> {
                val intent= Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
            else->{
                Toast.makeText(this, resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun goToNextInstruction(){
        reset()
        val intent=Intent(this@Train2Activity, TrainActivity::class.java)
        intent.putExtra(resources.getString(R.string.param_train_number), trainNumber)
        startActivity(intent)
        finish()
    }

    private fun reset(){
        timeInfo?.cancel()
        time=20000
        currentTime=time/1000
        handler.removeCallbacksAndMessages(null)
    }
}
