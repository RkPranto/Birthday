package com.prantology.birthdayhelper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    private String channelId ="BirthdayId", channelName="BirthdayName";
    NotificationManager manager;

    public void setChannelDetails(String id, String name){
         channelId = id;
         channelName = name;
    }

    public NotificationHelper(Context base, String id, String name) {
        super(base);
        channelId = id;
        channelName = name;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            getManager().createNotificationChannel(channel);
        }
    }

    public NotificationManager getManager(){
        if(manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String text){
        return new NotificationCompat.Builder(getApplicationContext(),
                channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.birthday)
                .setContentTitle(title)
                .setContentText(text);
    }
}
