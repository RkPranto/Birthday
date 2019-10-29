package com.prantology.birthdayhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationSetter extends BroadcastReceiver {
    BirthDatabase birthDatabase ;
    ArrayList<ContactModel> arrayList = new ArrayList<>();
    MessageAndNotificationHelper notificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        birthDatabase = new BirthDatabase(context);
        arrayList = birthDatabase.getAllContact();
        notificationHelper = new MessageAndNotificationHelper(context);
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);

        if(DefaultSettings.notificationEnanbled(context)){
            //Log.d("Helper","Setter NOTI");
            for(ContactModel c: arrayList){
                if(c.getNotificationState().equals("on")  && day == c.getDay() && month == c.getMonth()){
                    notificationHelper.sendNotification(c);
                }
            }
        }
    }
}
