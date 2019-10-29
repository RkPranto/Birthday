package com.prantology.birthdayhelper;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class AddFragment extends Fragment {
    private static final int REQUEST_CODE = 0;
    private static final int CONTACT_REQUEST_CODE = 1;
    EditText mName, mContact, mWish;
    Button mSave, mBirthday, mMessageTime;
    BirthDatabase birthDatabase;
    String name, contact;
    int d, m , y, hour=0 , min=0;
    String wishText, msgState = "off" , notificationState = "off";
    Boolean isBirthSet = false, isTimeSet = false;
    CheckBox mNotification;
    LinearLayout autoMsgSection;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    ImageView contactImport;
    TimePickerDialog.OnTimeSetListener timeListener;
    Context context;
    Random rand = new Random();

    public AddFragment() {

    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
        init(v);


        setBirthday();
        setMessageTime();
        mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNotificationCheckBox();
            }
        });

        saveToDatabase();

        importFromContact();

        return  v;
    }

    private void importFromContact() {
        contactImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkContactPermission();
            }
        });
    }

    private void checkContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if( context.checkSelfPermission(Manifest.permission.READ_CONTACTS )
                    != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
                        0);
            }
            else{
                makeContactPicker();
            }
        }else{
            makeContactPicker();
        }
    }

    private void makeContactPicker(){
        Intent i = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, CONTACT_REQUEST_CODE);
    }

    private void saveToDatabase() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mName.getText().toString().trim();
                contact = mContact.getText().toString().trim();
                wishText = mWish.getText().toString().trim();

                if(name.isEmpty()){
                    Toast.makeText(context,"Name field can't be empty !", Toast.LENGTH_LONG).show();
                }
                else if(!isBirthSet){
                    Toast.makeText(context,"You must set birthday ! If you don't remember birth year, just select any year :)", Toast.LENGTH_LONG).show();
                }
                else if (notificationState.equals("on")){
                    if(!isTimeSet){
                        //time not set
                        Toast.makeText(context,"Notifications require time !", Toast.LENGTH_LONG).show();
                    }
                    else{
                        insertSingleData();
                    }
                }
                else{
                    //validation complete ,now insert data
                    insertSingleData();
                }

            }
        });
    }

    private void insertSingleData() {
        ContactModel contactModel = new ContactModel(name, contact,d, m, y, hour,min, wishText, msgState, notificationState);
        // Toast.makeText(context,hour+" : "+ min+ "  "+wishText, Toast.LENGTH_SHORT).show();

        boolean fine = birthDatabase.insertContact(contactModel);
        if(fine){
            setAlarmCurrent(contactModel);
            Toast.makeText(context,"Saved Successfully !", Toast.LENGTH_SHORT).show();
            checkSMSPermission();
        }
        else{
            Toast.makeText(context,"Saving Error !", Toast.LENGTH_SHORT).show();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(this).commit();
        fm.popBackStack();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.content_area,new HomeFragment()).commit();
    }

    private void setAlarmCurrent(ContactModel c) {
        MessageAndNotificationHelper mm = new MessageAndNotificationHelper(context);
        Calendar calendar = Calendar.getInstance();
        int d  = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        if(d == c.getDay() && m == c.getMonth()){
            if(c.getNotificationState().equals("on")){
                mm.sendNotification(c);
            }
        }

    }

    private void setBirthday() {
        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(context,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        onDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                isBirthSet = true;
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                d = dayOfMonth;
                m = month;
                y = year;
                String bd = (d>9?d:"0"+d) + " - "+((m+1)>9?(m+1):"0"+(m+1)) + " - "+ y;
                mBirthday.setText(bd);
            }
        };
    }

    private void setMessageTime() {
        mMessageTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog =new TimePickerDialog(context,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        timeListener,
                        hour,
                        min,
                        DateFormat.is24HourFormat(context)
                );

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                dialog.show();
                isTimeSet = true;
            }
        });

        timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;

                String bdtime;
                bdtime = ((hour%12==0)?"12": ((hour%12)>9)?(hour%12):"0"+(hour%12)) +" : "+
                        ((min>9)? min: "0"+min)+" "+
                        ((hour>=12)?"PM":"AM");
                mMessageTime.setText(bdtime);
                //Toast.makeText(context,hour+":"+min+" "+am_pm,Toast.LENGTH_LONG).show();
            }
        };

    }


    private void checkSectionVisibility(){
        if(mNotification.isChecked()){
            autoMsgSection.setVisibility(View.VISIBLE);
        }
        else{
            autoMsgSection.setVisibility(View.GONE);
        }
    }

    private void checkNotificationCheckBox() {
        if(mNotification.isChecked()){
            notificationState = "on";
        }
        else{
            notificationState = "off";
        }
        checkSectionVisibility();
    }

    private void checkSMSPermission() {
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_CODE);
        }
        else{
            //setalarm service
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(REQUEST_CODE == requestCode){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context,"Thanks for giving 'SEND SMS' permission !", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(context,"Permission Denied ! Automatic message failed !", Toast.LENGTH_LONG).show();
        }

        if(requestCode == CONTACT_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context,"Thanks for giving 'READ CONTACT' permission !", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(context,"OPPS! Contact permission Denied !", Toast.LENGTH_LONG).show();
        }
    }

    private void init(View v) {
        mName = v.findViewById(R.id.name_et);
        mContact = v.findViewById(R.id.contact_et);
        mBirthday = v.findViewById(R.id.birthday);
        mSave = v.findViewById(R.id.save_btn);
        mMessageTime = v.findViewById(R.id.message_time);
        mWish = v.findViewById(R.id.wish_et);
        mNotification = v.findViewById(R.id.check_noti);
        autoMsgSection = v.findViewById(R.id.auto_or_not);
        birthDatabase = new BirthDatabase(context);
        contactImport = v.findViewById(R.id.contact_import);
        checkSectionVisibility();
        checkNotificationCheckBox();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CONTACT_REQUEST_CODE){
            Uri uri = data.getData();
            String[] projection = new String[]{
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };

            Cursor cursor = context.getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null,
                    null
            );

            if(cursor != null && cursor.moveToFirst()){
                do{
                    int number_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(number_idx);
                    mContact.setText(number);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
    }
}
