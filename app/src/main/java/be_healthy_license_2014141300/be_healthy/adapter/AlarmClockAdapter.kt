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
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.database.DB_Operation
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
        val image=view.findViewById(R.id.imageView2) as ImageView
        val sunday=view.findViewById(R.id.sun) as DayOfWeekView
        val monday=view.findViewById(R.id.mon) as DayOfWeekView
        val tuesday=view.findViewById(R.id.tue) as DayOfWeekView
        val wednesday=view.findViewById(R.id.wen) as DayOfWeekView
        val thursday=view.findViewById(R.id.th) as DayOfWeekView
        val friday=view.findViewById(R.id.fr) as DayOfWeekView
        val saturday=view.findViewById(R.id.sat) as DayOfWeekView
        val layout=view.findViewById(R.id.daysLayout) as LinearLayout
        val sublayout=view.findViewById(R.id.submenu) as LinearLayout
        val button=view.findViewById(R.id.button) as ImageButton
        val ringtone=view.findViewById(R.id.ringtone_text) as TextView
        val description=view.findViewById(R.id.description) as EditText
        description.setText(data[position].description)
        ringtone.text=data[position].ringtoneName

        if (data[position].id in open){
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            sublayout.layoutParams = param
            sublayout.visibility = View.VISIBLE
            button.setImageResource(R.mipmap.less)
        }

        sunday.setOnClickListener {
            data[position].cancelAlarm(context)
            if (sunday.isChecked){
                data[position].sun=1
            } else{
                data[position].sun=0
            }
            dayClick(position, checkBox, layout)
        }
        monday.currentid =position
        monday.setOnClickListener {
            data[position].cancelAlarm(context)
            if (monday.isChecked){
                data[position].mon=1
            } else{
                data[position].mon=0
            }
            dayClick(position, checkBox, layout)
        }
        tuesday.currentid =position
        tuesday.setOnClickListener {
            data[position].cancelAlarm(context)
            if (tuesday.isChecked){
                data[position].tue=1
            } else{
                data[position].tue=0
            }
            dayClick(position, checkBox, layout)
        }
        wednesday.currentid=position
        wednesday.setOnClickListener {
            data[position].cancelAlarm(context)
            if (wednesday.isChecked){
                data[position].wen=1
            } else{
                data[position].wen=0
            }
            dayClick(position, checkBox, layout)
        }
        thursday.currentid=position
        thursday.setOnClickListener {
            data[position].cancelAlarm(context)
            if (thursday.isChecked){
                data[position].th=1
            } else{
                data[position].th=0
            }
            dayClick(position, checkBox, layout)
        }
        friday.currentid=position
        friday.setOnClickListener {
            data[position].cancelAlarm(context)
            if (friday.isChecked){
                data[position].fr=1
            } else{
                data[position].fr=0
            }
            dayClick(position, checkBox, layout)
        }
        saturday.currentid=position
        saturday.setOnClickListener {
            data[position].cancelAlarm(context)
            if (saturday.isChecked){
                data[position].sat=1
            } else{
                data[position].sat=0
            }
            dayClick(position, checkBox, layout)
        }

        switchView.isChecked =data[position].alarm==1
        checkBox.isChecked =data[position].repeat==1
        sunday.isChecked =data[position].sun==1
        monday.isChecked =data[position].mon==1
        tuesday.isChecked =data[position].tue==1
        wednesday.isChecked =data[position].wen==1
        thursday.isChecked =data[position].th==1
        friday.isChecked =data[position].fr==1
        saturday.isChecked =data[position].sat==1
        checkBox.isEnabled = switchView.isChecked
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
                sunday.isChecked=false
                data[position].sun=0
                monday.isChecked=false
                data[position].mon=0
                tuesday.isChecked=false
                data[position].tue=0
                wednesday.isChecked=false
                data[position].wen=0
                thursday.isChecked=false
                data[position].th=0
                friday.isChecked=false
                data[position].fr=0
                saturday.isChecked=false
                data[position].sat=0
                layout.visibility= View.INVISIBLE
            }
            DB_Operation(context).updateAlarm(data[position])
        }
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                data[position].cancelAlarm(context)
                data[position].repeat = 1
                layout.visibility = View.VISIBLE
                sunday.isChecked =true
                data[position].sun=1
                monday.isChecked =true
                data[position].mon=1
                tuesday.isChecked =true
                data[position].tue=1
                wednesday.isChecked =true
                data[position].wen=1
                thursday.isChecked =true
                data[position].th=1
                friday.isChecked =true
                data[position].fr=1
                saturday.isChecked =true
                data[position].sat=1
                data[position].setRepeatingAlarm(context)
            } else{
                data[position].cancelAlarm(context)
                data[position].repeat=0
                layout.visibility = View.INVISIBLE
                sunday.isChecked =false
                data[position].sun=0
                monday.isChecked =false
                data[position].mon=0
                tuesday.isChecked =false
                data[position].tue=0
                wednesday.isChecked =false
                data[position].wen=0
                thursday.isChecked =false
                data[position].th=0
                friday.isChecked =false
                data[position].fr=0
                saturday.isChecked =false
                data[position].sat=0
                data[position].setAlarm(context)
            }
            DB_Operation(context).updateAlarm(data[position])
        }
        image.setOnClickListener {
            data[position].cancelAlarm(context)
            open.remove(data[position].id)
            DB_Operation(context).deleteAlarm(data[position])
            val intent= Intent(context.resources.getString(R.string.action_alarm_delete))
            intent.putExtra(context.resources.getString(R.string.param_id), position)
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
        time.setOnClickListener { ClockDialog(context, this@AlarmClockAdapter, position, data[position]).show(context.fragmentManager, "dialog") }

        ringtone.setOnClickListener { RingtoneDialog(data[position]).show(context.fragmentManager, "dialog") }

        description.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null && event.action === KeyEvent.ACTION_DOWN && event.keyCode === KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed) {
                    data[position].cancelAlarm(context)
                    data[position].description=description.text.toString().replace("#", "")
                    DB_Operation(context).updateAlarm(data[position])
                    val intent= Intent(context.resources.getString(R.string.action_alarm_update))
                    intent.putExtra(context.resources.getString(R.string.param_alarm), data[position])
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                    hideKeyboard(view)
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
        val intent= Intent(context.resources.getString(R.string.action_alarm_update))
        intent.putExtra(context.resources.getString(R.string.param_alarm), data[position])
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        if (data[position].alarm==1) {
            if (data[position].repeat == 1) {
                data[position].setRepeatingAlarm(context)
            } else {
                data[position].setAlarm(context)
            }
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

    private fun hasRepeat(position:Int):Boolean{
        return data[position].sun==1 ||
                data[position].mon==1 ||
                data[position].tue==1 ||
                data[position].wen==1 ||
                data[position].th==1 ||
                data[position].fr==1 ||
                data[position].sat==1
    }

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
}