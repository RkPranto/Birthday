package com.prantology.birthdayhelper;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.setting);
    }


}