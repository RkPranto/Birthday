package com.example.birthday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

public class MessageAndNotificationHelper {
    private Context context ;
    private AlarmManager alarmManager;
    Random random = new Random();

    public MessageAndNotificationHelper(Context context){
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void sendMessage(ContactModel contactModel){
        //Log.d("Helper", "SendMSG");
        long triggerTime = setUpCalender(contactModel);
        String contact = contactModel.getContact();
        String wishText = contactModel.getWish();

        Intent msgIntent = new Intent(context,MessageReceiver.class);
        msgIntent.putExtra("number", contact);
        if(wishText.isEmpty()){
            msgIntent.putExtra("message", "NULL");
        }
        else{
            msgIntent.putExtra("message", wishText);
        }

        PendingIntent msgPending = PendingIntent.getBroadcast(context,
                random.nextInt(20000),
                msgIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //Log.d("Helper", "Msg pending end");
        if(triggerTime == 0){
            //cancel pending intent
            alarmManager.cancel(msgPending);
        }else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, msgPending);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerTime, msgPending);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP,triggerTime, msgPending);
            }

    }

    public void sendNotification(ContactModel c) {
        //Log.d("Helper", "SendNotification");
        String name = c.getName();
        String contact = c.getContact();
        long triggerTime = setUpCalender(c);
        //Log.d("Helper", "noti cal time end");
        if(contact.equals("") || contact == null){
            contact = String.valueOf(random.nextInt());
        }
        Intent notificationIntent = new Intent(context,NotificationReceiver.class);
        notificationIntent.putExtra("name", name);
        notificationIntent.putExtra("number", contact);

        PendingIntent notificationPending = PendingIntent.getBroadcast(context,
                random.nextInt(20000),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if(triggerTime == 0){
            //cancel pending intent
            alarmManager.cancel(notificationPending);
        }else

            //Log.d("Helper", "noti pending end");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, notificationPending);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerTime, notificationPending);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP,triggerTime, notificationPending);
            }
    }


    private long setUpCalender(ContactModel contactModel){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,contactModel.getMonth());
        cal.set(Calendar.DAY_OF_MONTH,contactModel.getDay());
        cal.set(Calendar.HOUR_OF_DAY,contactModel.getHour());
        cal.set(Calendar.MINUTE,contactModel.getMinute());
        cal.set(Calendar.SECOND,0);

        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        if(cal.before(now)){
            return 0;
        }
        return cal.getTimeInMillis();
    }
}
