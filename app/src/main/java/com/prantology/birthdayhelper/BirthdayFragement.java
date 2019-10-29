package com.prantology.birthdayhelper;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BirthdayFragement extends Fragment {
    RecyclerView recyclerView;
    BirthdayFragmentAdapter  birthdayFragmentAdapter;
    BirthDatabase  birthDatabase;
    Context context;
    ArrayList<ContactModel> arrayList= new ArrayList<>();
    TextView birthText, noData;
    String listType;

    public BirthdayFragement() {
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
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_birthday_fragement, container, false);

        init(v);

        listType = getArguments().getString("ListType");

        if(listType != null){
            if(listType.equals("today")){
                birthText.setText("Today");
                arrayList = birthDatabase.getTodayBirth();
            }
            else if(listType.equals("tomorrow")){
                birthText.setText("Tomorrow");
                arrayList = birthDatabase.getTommorowBirth();
            }
            else if(listType.equals("month")){
                birthText.setText("This Month");
                arrayList = birthDatabase.getMonthBirth();
            }
        }

        Collections.sort(arrayList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel o1, ContactModel o2) {
                int x = o1.getDay()-o2.getDay();
                return x;
            }
        });

        if(!arrayList.isEmpty())
            loadRecyclerContent(arrayList);
        else {
            noData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        return v;
    }



    private void init(View v) {
        recyclerView = v.findViewById(R.id.birthday_list);
        birthDatabase = new BirthDatabase(context);
        birthText = v.findViewById(R.id.day_tv);
        noData = v.findViewById(R.id.no_data);

    }


    private void loadRecyclerContent(ArrayList<ContactModel> array) {
        if(array != null){
            noData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            birthdayFragmentAdapter = new BirthdayFragmentAdapter(context,array);
            birthdayFragmentAdapter.setOnItemClickListener(new BirthdayFragmentAdapter.OnItemClickListener() {
                @Override
                public void onCallItemClicked(String number) {
                    Uri uri = Uri.parse("tel:"+number);
                    Intent i = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(i);
                }

                @Override
                public void onSMSItemClicked(String number, String text) {
                    Uri uri = Uri.parse("smsto:"+number);
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.putExtra("sms_body", text);
                    startActivity(i);
                }
            });

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(birthdayFragmentAdapter);
        }
        else{
            noData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }



}
