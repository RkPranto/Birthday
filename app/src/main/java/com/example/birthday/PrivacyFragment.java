package com.example.birthday;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class PrivacyFragment extends Fragment {
    WebView privacyWeb;

    public PrivacyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_privacy, container, false);
        privacyWeb = v.findViewById(R.id.privacy_text);

        privacyWeb.loadUrl("file:///android_asset/privacy_policy.html");
        privacyWeb.getSettings().setJavaScriptEnabled(true);

        return v;
    }

}
