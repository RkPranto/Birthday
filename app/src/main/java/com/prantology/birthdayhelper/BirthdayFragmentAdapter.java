package com.prantology.birthdayhelper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BirthdayFragmentAdapter extends RecyclerView.Adapter<BirthdayFragmentAdapter.BirthdayFragmentViewHolder> {
    Context context;
    ArrayList<ContactModel> arrayList ;
    BirthDatabase birthDatabase ;
    OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onCallItemClicked(String number);
        void onSMSItemClicked(String number, String text);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public BirthdayFragmentAdapter(Context context, ArrayList<ContactModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        birthDatabase = new BirthDatabase(context);
    }


    @NonNull
    @Override
    public BirthdayFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.birthday_row,parent, false);
        return new BirthdayFragmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthdayFragmentViewHolder holder, int position) {

        String firstLetter = arrayList.get(position).getName().substring(0,1);
        holder.profileIcon.setText(firstLetter);

        String x = " " +arrayList.get(position).getWish()+" "+ arrayList.get(position).getHour();
        holder.nameTv.setText(arrayList.get(position).getName());


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

    public class BirthdayFragmentViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv, birthdayTv;
        Button mSMS, mCall;
        Button profileIcon;
        public BirthdayFragmentViewHolder(@NonNull View itemView) {
            super(itemView);

            profileIcon = itemView.findViewById(R.id.profile_pic_icon);
            nameTv = itemView.findViewById(R.id.name_tv);
            birthdayTv = itemView.findViewById(R.id.birthdate_tv);
            mSMS = itemView.findViewById(R.id.sms_btn);
            mCall = itemView.findViewById(R.id.call_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            mSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        String num = arrayList.get(position).getContact();
                        String text = arrayList.get(position).getWish();
                        if(text.isEmpty())text = DefaultSettings.getDefaultMessage(context);
                        mListener.onSMSItemClicked(num, text);
                        Toast.makeText(context, "Call: "+num, Toast.LENGTH_LONG).show();
                    }

                }
            });

            mCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        String num = arrayList.get(position).getContact();
                        mListener.onCallItemClicked(num);
                        Toast.makeText(context, "Call: "+num, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }


    }



}
