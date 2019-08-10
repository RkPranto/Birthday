package com.example.birthday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    Toolbar mToolbar;
    RecyclerView recyclerView;
    ContactAdapter  contactAdapter;
    FloatingActionButton addBtn, addConBtn;
    BirthDatabase  birthDatabase;
    TextView textViewQueryResult;
    EditText editText ;
    ArrayList<ContactModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //TOOLBAR SECTION
        setSupportActionBar(mToolbar);

        //RECYCLER SECTION
        arrayList = birthDatabase.getAllContact();
        Collections.sort(arrayList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel o1, ContactModel o2) {
                int x = o2.getName().compareTo(o1.getName());
                return x;
            }
        });
       loadRecyclerContent(arrayList);


        addConBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,Add.class));
                importFromContact();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this,Add.class));
            }
        });


        searchFromRecycler(birthDatabase.getAllContact());


    }

    private void searchFromRecycler(final ArrayList<ContactModel> allContact) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString(), allContact);
            }
        });

    }

    private void filter(String str, ArrayList<ContactModel> allContact) {
        ArrayList<ContactModel> contacts = new ArrayList<>();
        for(ContactModel contactModel: allContact){
            if(contactModel.getName().toLowerCase().contains(str.toLowerCase())){
                contacts.add(contactModel);
            }
        }
        contactAdapter.filterList(contacts);
    }


    private void loadRecyclerContent(ArrayList<ContactModel> array) {
        contactAdapter = new ContactAdapter(MainActivity.this,array);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactAdapter);
    }

    private void importFromContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if( getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS )
                    != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},
                        0);
            }
        }


        ContentResolver contentResolver = getContentResolver();


        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection= new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = null;
        String[] selectionArg = null;
        String orderBy = "";

        Cursor cursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                null,
                null,
                "");

        if(cursor!=null && cursor.getCount()>0){
            String name, phone;
            ContactModel contactModel;
            while (cursor.moveToNext()){
                name = cursor.getString(0);
                phone = cursor.getString(1);
                contactModel = new ContactModel(name, phone, 01, 01 ,2000);
                birthDatabase.insertContact(contactModel);
            }
            loadRecyclerContent(birthDatabase.getAllContact());
        }


    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.birthday_rv);
        addConBtn = findViewById(R.id.addcon_btn);
        addBtn = findViewById(R.id.add_btn);
        textViewQueryResult = findViewById(R.id.text);
        editText = findViewById(R.id.search_name);

        birthDatabase = new BirthDatabase(MainActivity.this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==0){

        }
    }
}
