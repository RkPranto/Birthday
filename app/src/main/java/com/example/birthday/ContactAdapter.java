package com.example.birthday;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.logging.Handler;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    Context context;
    ArrayList<ContactModel> arrayList = new ArrayList<>();
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
        holder.nameTv.setText(arrayList.get(position).getName());


        String d = arrayList.get(position).getYear()+" "+
                arrayList.get(position).getMonth()+" "+
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
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.name_tv);
            contactTv = itemView.findViewById(R.id.contact_tv);
            birthdayTv = itemView.findViewById(R.id.birthdate_tv);

            itemView.setOnClickListener(new View.OnClickListener() {

                TextView upName, upContact;
                Button mDelete, mUpdate, upBirthdate;
                ContactModel contactModel;
                String name, contact;
                int day, month, year;
                String bd;
                AlertDialog dialog;
                DatePickerDialog.OnDateSetListener onDateSetListener;
                int id;
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,String.valueOf(arrayList.get(getAdapterPosition()).getId()),Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alertBuild = new AlertDialog.Builder(context);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View view = layoutInflater.inflate(R.layout.update,null);
                    upName = view.findViewById(R.id.name_et);
                    upContact = view.findViewById(R.id.contact_et);
                    upBirthdate = view.findViewById(R.id.birthday);
                    mDelete = view.findViewById(R.id.delete_btn);
                    mUpdate = view.findViewById(R.id.update_btn);

                    name = arrayList.get(getAdapterPosition()).getName();
                    contact = arrayList.get(getAdapterPosition()).getContact();
                    day = arrayList.get(getAdapterPosition()).getDay();
                    month = arrayList.get(getAdapterPosition()).getMonth();
                    year = arrayList.get(getAdapterPosition()).getYear();

                    bd = "Change: "+day + " / "+month+ " /"+year;

                    upName.setText(name);
                    upContact.setText(contact);
                    upBirthdate.setText(bd);
                    id = arrayList.get(getAdapterPosition()).getId();

                    contactModel = new ContactModel(id, name, contact, day, month, year);

                    dialog = alertBuild.create();
                    dialog.setView(view);
                    dialog.show();

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
                            month = y;
                            day = d;
                            String bd = d + " / "+m+ " / "+y;
                            upBirthdate.setText(bd);
                        }
                    };

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

                    mUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context,"Updated: "+id+" "+ name+" "+contact,Toast.LENGTH_SHORT).show();
                            ContactModel contact = new ContactModel(id,
                                    upName.getText().toString(),
                                    upContact.getText().toString(),
                                    day, month, year);
                            boolean x = birthDatabase.updateItem(contact);
                            if(x){
                                Toast.makeText(context,"Updated Successfully !",Toast.LENGTH_SHORT).show();
                                goToHomeFragment(v);
                            }
                            else{
                                Toast.makeText(context,"Updated Failed !",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
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
