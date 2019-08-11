package com.example.birthday;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddFragment extends Fragment {
    EditText mName, mContact;
    Button mSave, mBirthday;
    BirthDatabase birthDatabase;
    String name, contact;
    int d, m , y;
    Toolbar mToolbar;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Context context;

    public AddFragment() {
        // Required empty public constructor
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
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                d = dayOfMonth;
                m = month;
                y = year;
                String bd = d + " / "+m+ " / "+y;
                mBirthday.setText(bd);
            }
        };





        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mName.getText().toString();
                contact = mContact.getText().toString();

                ContactModel contactModel = new ContactModel(name, contact,d, m, y);
                boolean fine = birthDatabase.insertContact(contactModel);
                if(fine){
                    Toast.makeText(context,"Saved Successfully !", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"Saving Error !", Toast.LENGTH_SHORT).show();
                }

                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_area,new HomeFragment()).commit();

            }
        });


        return  v;
    }

    private void init(View v) {
        mName = v.findViewById(R.id.name_et);
        mContact = v.findViewById(R.id.contact_et);
        mBirthday = v.findViewById(R.id.birthday);
        mSave = v.findViewById(R.id.save_btn);

        birthDatabase = new BirthDatabase(context);
    }

}
