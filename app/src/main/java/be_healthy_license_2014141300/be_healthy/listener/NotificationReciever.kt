package be_healthy_license_2014141300.be_healthy.listener

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.be_healthy_license_2014141300.be_healthy.R
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.support.v7.app.NotificationCompat
import org.apache.xmlbeans.impl.schema.SchemaTypeLoaderImpl.build



class NotificationReciever : BroadcastReceiver() {

    private val TIME:Long=777600000

    override fun onReceive(context: Context, intent: Intent) {
        var preferences=context.getSharedPreferences(context.resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        if (preferences.contains(context.resources.getString(R.string.preferences))){
            var time=preferences.getInt(context.resources.getString(R.string.preferences), 0)
            var text=""
            when(time%9){
                8->{
                    text="Мы будем рады получить от вас комментарий по улучшению сервиса"
                }
                7->{
                    text="Теперь можно заняться самолечением без тревоги о последствиях"
                }
                6->{
                    text="Достоверная информация из сотни справочников специально для вашего эффективного выздоровления"
                }
                5->{
                    text="Проверьте свой ИМТ в нашем приложении. В здоровом теле, здоровый дух"
                }
                4->{
                    text="Мини советы на главной странице специально для вас"
                }
                3->{
                    text="Спасибо что цените своё время и выбрали наше приложение"
                }
                2->{
                    text="Не пренебрегайте рекомендацией обращения к врачу"
                }
                1->{
                    text="Выздоравливайте вместе с нами и будьте здоровы"
                }
                0->{
                    text="Рекомендуем провести тренировку для глаз после рабочего дня"
                }
            }
            var editor=preferences.edit()
            editor.putInt(context.resources.getString(R.string.preferences), time+1)
            editor.apply()
            var intent = Intent(context, NotificationReciever::class.java)
            var pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            var am = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            am.set(AlarmManager.RTC, System.currentTimeMillis()+TIME, pIntent)
            val builder = NotificationCompat.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.resources.getString(R.string.app_name))
                    .setContentText(text)
                    .setStyle(android.support.v4.app.NotificationCompat.BigTextStyle().bigText(text))
            val notification = builder.build()
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
        }
    }
}
