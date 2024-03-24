package com.example.mehranm4.ui.alarms;

import static android.app.PendingIntent.FLAG_MUTABLE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mehranm4.MainActivity;
import com.example.mehranm4.R;
import com.example.mehranm4.database.AppDatabase;
import com.example.mehranm4.database.entity.AlarmEntity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmEntity alarmEntity = AppDatabase.getAppDatabase(context).alarmDao().getAlarm(intent.getLongExtra("id", 0));

        if (alarmEntity != null) {


            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.putExtra("frag", "alarm");
            PendingIntent notiIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ChannelAlarm")
                    .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                    .setContentTitle(alarmEntity.getTitle())
                    .setContentText(String.format("Time to pay: %s", alarmEntity.getTitle()))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(notiIntent);

            NotificationManagerCompat.from(context).notify((int) alarmEntity.getId(), builder.build());

            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(alarmEntity.getTime());
            calendar.add(Calendar.MONTH, 1);
            alarmEntity.setTime(calendar.getTimeInMillis());
            alarmEntity.setPay(false);
            AppDatabase.getAppDatabase(context).alarmDao().update(alarmEntity);

            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intenti = new Intent(context, AlarmReceiver.class);
            intenti.putExtra("id", alarmEntity.getId());
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intenti, PendingIntent.FLAG_IMMUTABLE);

            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);


        }

    }


}
