package com.be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.media.RingtoneManager
import android.net.Uri
import android.media.AudioManager
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.ListView
import com.be_healthy_license_2014141300.be_healthy.R
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import java.io.IOException

class RingtoneDialog: DialogFragment(), MediaPlayer.OnPreparedListener {

    private var uris=mutableListOf<Uri>()
    private var names=mutableListOf<String>()
    private var player: MediaPlayer?=null
    var alarm:AlarmClock?=null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        readRingtone()
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.ringtone_dialog_item, null)
        val dialog= AlertDialog.Builder(activity)
        dialog.setView(view)
        val listView=view.findViewById<ListView>(R.id.listView)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        val adapter= ArrayAdapter(activity, android.R.layout.simple_list_item_single_choice, names)
        listView.adapter=adapter
        listView.setItemChecked(names.indexOf(alarm?.ringtoneName), true)
        dialog.setPositiveButton(activity.resources.getString(R.string.ok)) { _, _ ->
            alarm?.cancelAlarm(activity)
            alarm?.ringtone= uris[listView.checkedItemPosition].toString()
            alarm?.ringtoneName=names[listView.checkedItemPosition]
            val intent= Intent(activity.resources.getString(R.string.action_alarm_update))
            intent.putExtra(activity.resources.getString(R.string.param_alarm), alarm)
            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent)
            DB_Operation(activity).updateAlarm(alarm!!)
            stopPreview()
            if (alarm?.alarm==1){
                if (alarm?.repeat==1){
                    alarm?.setRepeatingAlarm(activity)
                }
                else{
                    alarm?.setAlarm(activity)
                }
            }
        }
        dialog.setNegativeButton(activity.resources.getString(R.string.cancel), null)
        listView.setOnItemClickListener { _, _, position, _ ->
            stopPreview()
            preview(uris[position])
        }
        return dialog.create()
    }

    private fun readRingtone(){
        val ringtoneMgr = RingtoneManager(activity)
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM)
        val alarmsCursor = ringtoneMgr.cursor
        while (!alarmsCursor.isAfterLast && alarmsCursor.moveToNext()) {
            val currentPosition = alarmsCursor.position
            uris.add(ringtoneMgr.getRingtoneUri(currentPosition))
            names.add(ringtoneMgr.getRingtone(currentPosition).getTitle(activity))
        }
        alarmsCursor.close()
    }

    private fun stopPreview(){
        if (player!=null){
            if (player!!.isPlaying){
                player!!.stop()
            }
            player!!.release()
            player=null
        }
    }

    private fun preview(uri: Uri){
        player= MediaPlayer()
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            player!!.setDataSource(activity, uri)
            player!!.prepareAsync()
            player!!.setOnPreparedListener(this)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onPrepared(p0: MediaPlayer?) {
        p0?.start()
    }
}