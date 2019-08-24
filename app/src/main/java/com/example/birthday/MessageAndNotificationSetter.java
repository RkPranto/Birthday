package com.example.birthday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class MessageAndNotificationSetter extends BroadcastReceiver {
    BirthDatabase birthDatabase ;
    ArrayList<ContactModel> arrayList = new ArrayList<>();
    MessageAndNotificationHelper messageAndNotificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        birthDatabase = new BirthDatabase(context);
        arrayList = birthDatabase.getAllContact();
        messageAndNotificationHelper = new MessageAndNotificationHelper(context);
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);

        if(DefaultSettings.autoMessageEnanbled(context)){
            //Log.d("Msg","triggered inner");

            for(ContactModel c: arrayList){
                if(c.getMsgState().equals("on") && day == c.getDay() && month == c.getMonth()){
                    messageAndNotificationHelper.sendMessage(c);
                }
            }
        }

        if(DefaultSettings.notificationEnanbled(context)){

            for(ContactModel c: arrayList){
                if(c.getNotificationState().equals("on")  && day == c.getDay() && month == c.getMonth()){
                    messageAndNotificationHelper.sendNotification(c);
                }
            }
        }
    }
}
