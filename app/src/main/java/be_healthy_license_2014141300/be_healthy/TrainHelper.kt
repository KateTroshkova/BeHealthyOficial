package be_healthy_license_2014141300.be_healthy

import android.view.View
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R

class TrainHelper(var view:View){
    private val screen0= listOf(R.id.start, R.id.instruction)
    private val screen1= listOf(R.id.start, R.id.instruction)
    private val screen2=listOf(R.id.circle, R.id.timer, R.id.finish)
    private val screen3= listOf(R.id.start, R.id.instruction)
    private val screen4=listOf(R.id.circle, R.id.timer, R.id.finish)
    private val screen5= listOf(R.id.start, R.id.instruction)
    private val screen6=listOf(R.id.circle, R.id.timer, R.id.finish)
    private val screen7= listOf(R.id.start, R.id.instruction)
    private val screen8= listOf(R.id.moveView, R.id.finish)
    private val screen9= listOf(R.id.instruction, R.id.finish)

    private val screens=listOf(screen0, screen1, screen2, screen3, screen4, screen5, screen6, screen7, screen8, screen9)
    private val instructions=listOf("Сядьте в удобное положение,снимите очки и расположите экран немного дальше,чем при чтении. \nНаш комплекс упражнений состоит из пяти небольших заданий, которые займут у вас чуть больше пяти минут. \nНажмите 'Старт', когда будете готовы.",
            "Моргайте как можно чаще", "", "Переводите взгляд с точки на экране на любой отдаленный предмет.(дерево, машина и т.п.) Постарайтесь сфокусировать взгляд. Затем снова посмотрите на точку.",
            "", "Держите телефон в вытянутой руке на уровне глаз. Медленно приближайте телефон, стараясь сфокусировать взгляд на зеленой точке. Затем так же медленно верните телефон в исходное положение.",
            "", "Следите взглядом за точкой", "", "Потрите руки друг о друга,чтобы ощутимо их нагреть. " +
            "Прикройте глаза руками так,чтобы центры ладоней находились на уровне зрачков. " +
            "Не стоит сильно прижимать ладони к лицу, лучше всего,если ресницы будут слегка задевать ладони. " +
            "Пальцы можно перекрестить на лбу или расположить рядом — делайте так,как Вам удобно. " +
            "Важно, чтобы отсутствовали «щелочки», пропускающие свет. Закройте глаза. " +
            "Упражнение считается выполненным, когда пройдет напряжение глаз и Вы полностью расслабитесь.")

    init{
        for(i in 0..9){
            hideScreen(i)
        }
    }

    fun hideScreen(i:Int){
        for(widget in screens[i]){
            view.findViewById<View>(widget).visibility=View.INVISIBLE
        }
    }

    fun showScreen(i:Int){
        for(widget in screens[i]){
            view.findViewById<View>(widget).visibility=View.VISIBLE
        }
    }

    fun updateTimer(time:Long){
        val timer = view.findViewById<TextView>(R.id.timer)
        timer.text=time.toString()
    }

    fun updateInstruction(i:Int){
        val instruction=view.findViewById<TextView>(R.id.instruction)
        instruction.text=instructions[i]
    }

    fun clearProgress(){
        val progress=view.findViewById<TextView>(R.id.progress)
        progress.text=""
    }

    fun updateProgress(i:Int){
        val progress=view.findViewById<TextView>(R.id.progress)
        progress.text=i.toString()+"/7"
    }
}