package com.be_healthy_license_2014141300.be_healthy.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.EditText
import be_healthy_license_2014141300.be_healthy.UpdateSender
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import com.be_healthy_license_2014141300.be_healthy.R
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.dialog.ClockDialog
import com.be_healthy_license_2014141300.be_healthy.dialog.RingtoneDialog
import com.be_healthy_license_2014141300.be_healthy.view.DayOfWeekView

class AlarmClockAdapter(var context: Activity, var data:MutableList<AlarmClock>): BaseAdapter(), ClockDialog.OnNewAlarmClockListener{

    private val open= mutableListOf<Int>()

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val inflater= LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.alarm_clock_item, null)

        val time=view.findViewById(R.id.time_text) as TextView
        time.text=formatTime(data[position].hour, data[position].minute)
        val switchView=view.findViewById(R.id.switch1) as Switch
        val checkBox=view.findViewById(R.id.checkBox) as CheckBox
        val deleteImage=view.findViewById(R.id.deleteImage) as ImageView
        val week = arrayListOf(view.findViewById(R.id.sun) as DayOfWeekView,
                view.findViewById(R.id.mon) as DayOfWeekView,
                view.findViewById(R.id.tue) as DayOfWeekView,
                view.findViewById(R.id.wen) as DayOfWeekView,
                view.findViewById(R.id.th) as DayOfWeekView,
                view.findViewById(R.id.fr) as DayOfWeekView,
                view.findViewById(R.id.sat) as DayOfWeekView)
        val layout=view.findViewById(R.id.daysLayout) as LinearLayout
        val sublayout=view.findViewById(R.id.submenu) as LinearLayout
        val button=view.findViewById(R.id.button) as ImageButton
        val ringtone=view.findViewById(R.id.ringtone_text) as TextView
        val description=view.findViewById(R.id.description) as EditText
        val anotherInfo=view.findViewById(R.id.another_info) as TextView
        description.setText(data[position].description)
        ringtone.text=context.resources.getString(R.string.ringtone)+data[position].ringtoneName
        anotherInfo.text=data[position].description
        switchView.isChecked =data[position].alarm==1
        checkBox.isChecked =data[position].repeat==1
        checkBox.isEnabled = switchView.isChecked

        if (data[position].id in open){
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            sublayout.layoutParams = param
            sublayout.visibility = View.VISIBLE
            button.setImageResource(R.mipmap.less)
        }

        for(day in week){
            day.currentid=position
            day.setOnClickListener {
                data[position].cancelAlarm(context)
                if (day.isChecked){
                    data[position].week[week.indexOf(day)]=1
                }
                else{
                    data[position].week[week.indexOf(day)]=0
                }
                dayClick(position, checkBox, layout)
            }
            day.isChecked=data[position].week[week.indexOf(day)]==1
        }

        if (checkBox.isChecked){
            layout.visibility = View.VISIBLE
        }
        else{
            layout.visibility= View.INVISIBLE
        }

        switchView.setOnClickListener {
            if (switchView.isChecked) {
                data[position].alarm=1
                checkBox.isEnabled=true
                data[position].setAlarm(context)
            } else{
                data[position].cancelAlarm(context)
                data[position].alarm=0
                checkBox.isChecked=false
                checkBox.isEnabled=false
                data[position].repeat=0
                for(day in week){
                    day.isChecked=false
                    data[position].week[week.indexOf(day)]=0
                }
                layout.visibility= View.INVISIBLE
            }
            DB_Operation(context).updateAlarm(data[position])
        }
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                data[position].cancelAlarm(context)
                data[position].repeat = 1
                layout.visibility = View.VISIBLE
                for(day in week){
                    day.isChecked=true
                    data[position].week[week.indexOf(day)]=1
                }
                data[position].setRepeatingAlarm(context)
            } else{
                data[position].cancelAlarm(context)
                data[position].repeat=0
                layout.visibility = View.INVISIBLE
                for(day in week){
                    day.isChecked=false
                    data[position].week[week.indexOf(day)]=0
                }
                data[position].setAlarm(context)
            }
            DB_Operation(context).updateAlarm(data[position])
        }
        deleteImage.setOnClickListener {
            data[position].cancelAlarm(context)
            open.remove(data[position].id)
            DB_Operation(context).deleteAlarm(data[position])
            data.removeAt(position)
            val intent= Intent(context.resources.getString(R.string.action_alarm_delete))
            intent.putExtra(context.resources.getString(R.string.param_id), transtaleLists(data))
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        button.setOnClickListener {
            if (sublayout.visibility== View.VISIBLE) {
                val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
                sublayout.layoutParams = param
                sublayout.visibility = View.INVISIBLE
                button.setImageResource(R.mipmap.more)
                open.remove(data[position].id)
            } else{
                val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                sublayout.layoutParams = param
                sublayout.visibility = View.VISIBLE
                button.setImageResource(R.mipmap.less)
                open.add(data[position].id)
            }
        }
        time.setOnClickListener {
            val dialog = ClockDialog()
            dialog.setListener(this@AlarmClockAdapter)
            dialog.setAlarm(position, data[position])
            dialog.show(context.fragmentManager, "dialog")
        }

        ringtone.setOnClickListener {
            val dialog = RingtoneDialog()
            dialog.alarm=data[position]
            dialog.show(context.fragmentManager, "dialog")
        }

        description.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed) {
                    data[position].cancelAlarm(context)
                    data[position].description=description.text.toString().replace("#", "")
                    DB_Operation(context).updateAlarm(data[position])
                    val intent= Intent(context.resources.getString(R.string.action_alarm_update))
                    intent.putExtra(context.resources.getString(R.string.param_alarm), data[position])
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                    hideKeyboard(view)
                    anotherInfo.text=data[position].description
                    if (data[position].alarm==1){
                        if (data[position].repeat==1){
                            data[position].setRepeatingAlarm(context)
                        }
                        else{
                            data[position].setAlarm(context)
                        }
                    }
                    return@OnEditorActionListener true
                }
            }
            false
        })
        return view
    }

    override fun OnNewAlarmClock(hour: Int, minute: Int, position:Int) {
        data[position].cancelAlarm(context)
        data[position].hour=hour
        data[position].minute=minute
        DB_Operation(context).updateAlarm(data[position])
        UpdateSender(context).send(R.string.action_alarm_update, R.string.param_alarm, data[position])
        if (data[position].repeat == 1) {
            data[position].setRepeatingAlarm(context)
        } else {
            data[position].setAlarm(context)
        }
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    private fun hasRepeat(position:Int):Boolean =
            data[position].week.contains(1)

    private fun dayClick(position:Int, checkBox: CheckBox, layout: LinearLayout){
        data[position].setRepeatingAlarm(context)
        if (!hasRepeat(position)){
            data[position].cancelAlarm(context)
            checkBox.isChecked=false
            data[position].repeat=0
            layout.visibility= View.INVISIBLE
            data[position].setAlarm(context)
        }
        DB_Operation(context).updateAlarm(data[position])
    }

    private fun hideKeyboard(view:View){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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

    private fun transtaleLists(list:MutableList<AlarmClock>):ArrayList<AlarmClock>{
        var result= arrayListOf<AlarmClock>()
        for(item in list){
            result.add(item)
        }
        return result
    }
}