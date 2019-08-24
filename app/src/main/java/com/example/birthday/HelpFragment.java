package com.example.birthday;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class HelpFragment extends Fragment {
    WebView text ;
    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_help, container, false);
        text = v.findViewById(R.id.help_text);
        text.loadUrl("file:///android_asset/help.html");

        return v;
    }

}
