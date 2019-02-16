package be_healthy_license_2014141300.be_healthy.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import android.view.View
import com.be_healthy_license_2014141300.be_healthy.R
import android.widget.*

class TrainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val instructions=listOf("Сейчас, пока будет идти таймер, вам нужно моргать как можно чаще. Нажмите 'начать', когда будете готовы.",
            "Пока будет идти таймер, переводите взгляд с точки на экране на любой отдаленный предмет.(дерево, машина и т.п.) Постарайтесь сфокусировать взгляд. Затем снова посмотрите на точку. Нажмите 'начать', когда будете готовы.",
            "Пока будет идти таймер, держите телефон в вытянутой руке на уровне глаз. Медленно приближайте телефон, стараясь сфокусировать взгляд на зеленой точке. Затем так же медленно верните телефон в исходное положение. Нажмите 'начать', когда будете готовы.",
            "Следующие несколько упражнений вам нужно внимательно следить взглядом за точкой. Нажмите 'начать', когда будете готовы.", "Потрите руки друг о друга,чтобы ощутимо их нагреть. " +
            "Потрите ладони друг об друга и нагретые ладони приложите к глазам таким образом, чтобы в глазах наступила абсолютная темнота, без каких либо просветов. Упражнение считается выполненным, когда ладони на глазах остыли. Повторите упражнение 2-3 раза.")
    private var trainNumber=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.n_activity_train)
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.menu.setGroupCheckable(0, false, true)
        if (intent.hasExtra(resources.getString(R.string.param_train_number))){
            trainNumber=intent.getIntExtra(resources.getString(R.string.param_train_number), -1)
        }
        (findViewById<View>(R.id.back_button)).setOnClickListener { this.onBackPressed() }
        nextInstruction()
    }

    fun start(view: View){
        val intent= Intent(this, Train2Activity::class.java)
        intent.putExtra(resources.getString(R.string.param_train_number), trainNumber)
        startActivity(intent)
    }

    private fun nextInstruction(){
        trainNumber++
        if (trainNumber<instructions.size) {
            (findViewById<TextView>(R.id.textView6)).text = instructions[trainNumber]
            if (trainNumber>0) {
                (findViewById<TextView>(R.id.textView19)).text = instructions[trainNumber]
            }
        }
        if (trainNumber>0){
            (findViewById<TextView>(R.id.textView2)).visibility=View.INVISIBLE
            (findViewById<ImageView>(R.id.imageView9)).visibility=View.INVISIBLE
            (findViewById<TextView>(R.id.textView6)).visibility=View.INVISIBLE
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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
}
