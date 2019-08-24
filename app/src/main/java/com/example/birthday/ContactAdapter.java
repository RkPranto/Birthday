package com.example.birthday;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Handler;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    Context context;
    ArrayList<ContactModel> arrayList ;
    BirthDatabase birthDatabase ;


    public ContactAdapter(Context context, ArrayList<ContactModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        birthDatabase = new BirthDatabase(context);
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.contact_row,parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.contactTv.setText(arrayList.get(position).getContact());
        String x = " " +arrayList.get(position).getWish()+" "+ arrayList.get(position).getHour();
        holder.nameTv.setText(arrayList.get(position).getName());

        String nn = arrayList.get(position).getNotificationState();
        String mm = arrayList.get(position).getMsgState();
        if(nn.equals("on")) {
            holder.notificationIv.setImageResource(R.drawable.notifications_active);
        }
        else{
            holder.notificationIv.setImageResource(R.drawable.notifications_off);
        }

        if(mm.equals("on")){
            holder.messageIv.setImageResource(R.drawable.message);
        }
        else{
            holder.messageIv.setImageResource(R.drawable.message_off);
        }


        String d = arrayList.get(position).getYear()+" "+
                (arrayList.get(position).getMonth()+1)+" "+
                arrayList.get(position).getDay();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd");
        try {
            Date date = simpleDateFormat.parse(d);
            simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            String birthday = simpleDateFormat.format(date);
            holder.birthdayTv.setText(birthday);

        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv, contactTv, birthdayTv;
        ImageView notificationIv, messageIv;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.name_tv);
            contactTv = itemView.findViewById(R.id.contact_tv);
            birthdayTv = itemView.findViewById(R.id.birthdate_tv);
            notificationIv = itemView.findViewById(R.id.notification_state);
            messageIv = itemView.findViewById(R.id.msg_state);

            itemView.setOnClickListener(new View.OnClickListener() {

                EditText upName, upContact, upWish;
                Button mDelete, mUpdate, upBirthdate, upMsgTime, cancelBtn;
                CheckBox autoMsg, autoNotification;
                LinearLayout autoMsgSection;
                ContactModel contactModel;
                String name, contact, wish;
                int day, month, year, hour, minu;
                String msgState = "on", notificationState = "on";
                String bd;
                AlertDialog dialog;
                DatePickerDialog.OnDateSetListener onDateSetListener;
                TimePickerDialog.OnTimeSetListener timeSetListener;
                int id;
                @Override
                public void onClick(View v) {

                    //Initialization
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View view = layoutInflater.inflate(R.layout.update,null);
                    upName = view.findViewById(R.id.name_et);
                    upContact = view.findViewById(R.id.contact_et);
                    upBirthdate = view.findViewById(R.id.birthday);
                    upWish = view.findViewById(R.id.wish_et);
                    autoMsg = view.findViewById(R.id.check_auto);
                    autoNotification = view.findViewById(R.id.check_noti);
                    autoMsgSection = view.findViewById(R.id.auto_or_not);
                    upMsgTime = view.findViewById(R.id.message_time);
                    mDelete = view.findViewById(R.id.delete_btn);
                    mUpdate = view.findViewById(R.id.update_btn);
                    cancelBtn = view.findViewById(R.id.cancel_btn);


                    //AlertDialog Work starts from here
                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(context);

                    //Start Retriving the ContactModel object
                    name = arrayList.get(getAdapterPosition()).getName();
                    contact = arrayList.get(getAdapterPosition()).getContact();
                    wish = arrayList.get(getAdapterPosition()).getWish();
                    day = arrayList.get(getAdapterPosition()).getDay();
                    month = arrayList.get(getAdapterPosition()).getMonth();
                    year = arrayList.get(getAdapterPosition()).getYear();
                    hour = arrayList.get(getAdapterPosition()).getHour();
                    minu = arrayList.get(getAdapterPosition()).getMinute();
                    msgState = arrayList.get(getAdapterPosition()).getMsgState();
                    notificationState = arrayList.get(getAdapterPosition()).getNotificationState();

                    bd = "Change >>  "+day + " - "+month+ "- "+year;
                    upName.setText(name);
                    upContact.setText(contact);
                    upBirthdate.setText(bd);
                    String bdtime;
                    bdtime = ((hour%12==0)?"12": ((hour%12)>9)?(hour%12):"0"+(hour%12)) +" : "+
                        ((minu>9)? minu: "0"+minu)+" "+
                            ((hour>=12)?"PM":"AM");

                    upMsgTime.setText("Change >>  "+bdtime);
                    upWish.setText(wish);
                    id = arrayList.get(getAdapterPosition()).getId();
                    //End retriving model class

                    //Making a model class ready for delete function
                    contactModel = new ContactModel(id, name, contact, day, month, year, hour, minu, msgState, notificationState);

                    dialog = alertBuild.create();
                    dialog.setView(view);
                    dialog.show();

                    //Cancel button
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    //Birthday set
                    upBirthdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DatePickerDialog dialog = new DatePickerDialog(context,
                                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                                    onDateSetListener,
                                    year,month,day);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                        }
                    });

                    onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int y, int m, int d) {
                            year = y;
                            month = m;
                            day = d;
                            String bd = (d>9?d:"0"+d) + " - "+((m+1)>9?(m+1):"0"+(m+1))+ " - "+y;
                            upBirthdate.setText(bd);
                        }
                    };

                    //Checkbox check state retrieve
                    if(msgState.equals("on")){
                        autoMsg.setChecked(true);
                    }else{
                        autoMsg.setChecked(false);
                    }
                    if(notificationState.equals("on")){
                        autoNotification.setChecked(true);
                    }else{
                        autoNotification.setChecked(false);
                    }

                    //Auto Message Checker
                    autoMsg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(autoMsg.isChecked()){
                                msgState = "on";
                            }
                            else{
                                msgState = "off";
                            }
                            checkSectionVisibility();
                        }
                    });
                    //Auto Notification Checker
                    autoNotification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(autoNotification.isChecked()){
                                notificationState = "on";
                            }
                            else{
                                notificationState = "off";
                            }
                            checkSectionVisibility();
                        }
                    });




                    //Auto Message Time
                    upMsgTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimePickerDialog dialog =new TimePickerDialog(context,
                                    android.R.style.Theme_DeviceDefault_Light_Dialog,
                                    timeSetListener,
                                    hour,
                                    minu,
                                    DateFormat.is24HourFormat(context)
                            );
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                            dialog.show();
                        }
                    });

                    timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            hour = hourOfDay;
                            minu = minute;
                            String tt = "Change >> "+hour+" : "+minu+" ";
                            String bdtime;
                            bdtime = ((hour%12==0)?"12": ((hour%12)>9)?(hour%12):"0"+(hour%12)) +" : "+
                                    ((minu>9)? minu: "0"+minu)+" "+
                                    ((hour>=12)?"PM":"AM");
                            upMsgTime.setText(bdtime);
                        }
                    };



                    //Delete button
                    mDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*Toast.makeText(context,"Delete: "+id+" "+ name+" "+contact,Toast.LENGTH_SHORT).show();*/
                            boolean x = birthDatabase.deleteItem(contactModel);

                            if(x){
                                Toast.makeText(context,"Deleted Successfully !",Toast.LENGTH_SHORT).show();
                                goToHomeFragment(v);
                            }
                            else{
                                Toast.makeText(context,"Deleted Failed !",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();

                        }
                    });

                    //Update button
                    mUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context,"Updated: "+id+" "+ name+" "+contact,Toast.LENGTH_SHORT).show();

                            String updatedName = upName.getText().toString().trim();
                            String updatedContact = upContact.getText().toString().trim();
                            String updatedWish = upWish.getText().toString().trim();
                            if(updatedName.isEmpty()){
                                Toast.makeText(context,"Name field can't be empty !",Toast.LENGTH_SHORT).show();
                            }
                            else if(updatedContact.isEmpty()){
                                Toast.makeText(context,"Name field can't be empty !",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                ContactModel contact = new ContactModel(id,
                                        updatedName,
                                        updatedContact,
                                        day, month, year,
                                        hour,minu,
                                        updatedWish,
                                        msgState,
                                        notificationState
                                );
                                boolean x = birthDatabase.updateItem(contact);
                                if(x){
                                    Toast.makeText(context,"Updated Successfully !",Toast.LENGTH_SHORT).show();
                                    goToHomeFragment(v);
                                }
                                else{
                                    Toast.makeText(context,"Updated Failed !",Toast.LENGTH_SHORT).show();
                                }
                            }

                            dialog.dismiss();
                        }
                    });
                }

                private void checkSectionVisibility() {
                    if(autoMsg.isChecked() || autoNotification.isChecked()){
                        autoMsgSection.setVisibility(View.VISIBLE);
                    }
                    else{
                        autoMsgSection.setVisibility(View.GONE);
                    }
                }
            });
        }


    }


    public void filterList(ArrayList<ContactModel> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }

    public void goToHomeFragment(View v){
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_area,homeFragment)
                .commit();
    }


}
