package com.example.birthday;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Add extends AppCompatActivity {

    EditText mName, mContact;
    Button mSave, mBirthday;
    BirthDatabase birthDatabase;
    String name, contact;
    int d, m , y;
    Toolbar mToolbar;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mToolbar = findViewById( R.id.toolbar);
        setSupportActionBar(mToolbar);
        mName = findViewById(R.id.name_et);
        mContact = findViewById(R.id.contact_et);
        mBirthday = findViewById(R.id.birthday);
        mSave = findViewById(R.id.save_btn);

        birthDatabase = new BirthDatabase(this);

        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(Add.this,
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
                    Toast.makeText(Add.this,"Saved Successfully !", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Add.this,"Saving Error !", Toast.LENGTH_SHORT).show();
                }
                finish();
                Intent intent = new Intent(Add.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }

}
