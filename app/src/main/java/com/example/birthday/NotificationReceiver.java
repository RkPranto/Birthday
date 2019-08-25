package com.example.birthday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {
    private static Random rand = new Random();
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("Helper","Noti");
        if(DefaultSettings.notificationEnanbled(context)){
            String channelId = intent.getStringExtra("number");
            String channelName = intent.getStringExtra("name");
            //Log.d("Helper: ", channelName + " "+channelId);
            channelName = channelName.substring(0,1).toUpperCase() + channelName.substring(1);

            String title = intent.getStringExtra("name")+"'s birthday today";
            String text = "Remember ? Wish him/her !";

            NotificationHelper notificationHelper = new NotificationHelper(context,channelId, channelName);
            NotificationCompat.Builder builder = notificationHelper.getChannelNotification(title, text);
            notificationHelper.getManager().notify(rand.nextInt(10000),builder.build());
        }
    }
}
