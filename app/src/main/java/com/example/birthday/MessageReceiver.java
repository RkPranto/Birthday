package com.example.birthday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Log.d("Helper", "MsgReceivver");
        String number = intent.getStringExtra("number");
        String text = intent.getStringExtra("message");

        if(text.equals("NULL")){
            text = DefaultSettings.getDefaultMessage(context);
        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number,
                null,
                text,
                null,
                null);
    }
}
