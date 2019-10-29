package com.prantology.birthdayhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import java.util.Calendar;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{
    Toolbar mToolbar;
    BirthDatabase  birthDatabase;

    //Fragement
    FragmentManager fm;

    //Navigation Drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;


    SharedPreferences preferences ;
    SharedPreferences.Editor editor;

    //Update alarm time
    TextView ins;
    Button upAlarmTime, doneBtn;
    boolean isAlarmSet = false;
    TimePickerDialog.OnTimeSetListener alarmTimeListener;
    int alarmHour, alarmMin;

    Random rand = new Random();

    Boolean isNeedToHome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //TOOLBAR SECTION
        setSupportActionBar(mToolbar);
        //searchFromRecycler(birthDatabase.getAllContact());

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

       if(preferences.getBoolean("ALARMS", false) == false){
           setDailyAlarm();
           //Log.d("Alarm", "Pref");
           editor.putBoolean("ALARMS", true);
           editor.commit();
       }

        drawerLayout.addDrawerListener(toggle);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();



        final HomeFragment homeFragment = new HomeFragment();
        fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_area,homeFragment).commit();
            navigationView.setCheckedItem(R.id.home);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                switch (menuItem.getItemId()){
                    case R.id.home:
                        fragmentTransaction.replace(R.id.content_area,new HomeFragment()).commit();
                        break;
                    case R.id.setting:
                        fragmentTransaction.replace(R.id.content_area, new SettingsActivity()).commit();
                        break;

                    case R.id.add_new:
                        //fragmentTransaction.remove(homeFragment).commit();
                        fragmentTransaction.replace(R.id.content_area, new AddFragment()).commit();
                        break;

                    /*case R.id.update_alarm:
                        updateAlarmDialog();
                        break;*/
                    case R.id.todaybd:
                        isNeedToHome = true;
                        showBirthdays("today");
                        //Toast.makeText( MainActivity.this, " Today Birthday",Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.tomorrowbd:
                        isNeedToHome = true;
                        showBirthdays("tomorrow");
                       // Toast.makeText( MainActivity.this, " Tommorow Birthday",Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.thismonthbd:
                        isNeedToHome = true;
                       // Toast.makeText( MainActivity.this, " Month Birthday",Toast.LENGTH_SHORT).show();
                        showBirthdays("month");
                        break;

                    case R.id.privacy:
                        isNeedToHome = true;
                        fragmentTransaction.replace(R.id.content_area, new PrivacyFragment()).commit();

                        break;
                    case R.id.help:
                        isNeedToHome = true;
                        fragmentTransaction.replace(R.id.content_area, new HelpFragment()).commit();
                        break;
                    case R.id.about_devloper:
                        aboutDeveloper();

                        break;
                    default:
                        Toast.makeText(MainActivity.this,"Wrong Clicked somehow !",Toast.LENGTH_SHORT).show();
                        break;
                }
                int id = menuItem.getItemId();

                navigationView.setCheckedItem(id);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    /*private void updateAlarmDialog() {

        Calendar c = Calendar.getInstance();
        alarmHour = c.get(Calendar.HOUR_OF_DAY);
        alarmMin = c.get(Calendar.MINUTE);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View v = layoutInflater.inflate(R.layout.set_daily_alarm_update,null);
        ins = v.findViewById(R.id.instruction);
        upAlarmTime = v.findViewById(R.id.update_time);
        doneBtn = v.findViewById(R.id.done_btn);

        int hh = 0, mm =0;
        if(preferences.getInt("hour",99) != 99){
            hh = preferences.getInt("hour",99);
        }
        if(preferences.getInt("minute",88) != 88){
            mm = preferences.getInt("minute",88);
        }
        Log.d("Alarm", "Update: "+ hh+ ": "+mm);
        String bdtime = ((hh%12==0)?"12": ((hh%12)>9)?(hh%12):"0"+(hh%12)) +" : "+
                ((mm>9)? mm: "0"+mm)+" "+
                ((hh>=12)?"PM":"AM");
        upAlarmTime.setText(bdtime);

        ins.setText("***Please set time for daily alarm. We will schedule(not send) all notifications and messages on that time daily once.");

        final AlertDialog dialog = builder.create();

        upAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        alarmTimeListener,
                        alarmHour,
                        alarmMin,
                        DateFormat.is24HourFormat(MainActivity.this)
                        );


                isAlarmSet = true;
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        });

        alarmTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                alarmHour = hourOfDay;
                alarmMin = minute;
                String bdtime = "Update Time: ";
                bdtime += ((alarmHour%12==0)?"12": ((alarmHour%12)>9)?(alarmHour%12):"0"+(alarmHour%12)) +" : "+
                        ((alarmMin>9)? alarmMin: "0"+alarmMin)+" "+
                        ((alarmHour>=12)?"PM":"AM");
                upAlarmTime.setText(bdtime);
            }
        };

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAlarmSet){
                    editor.putInt("hour", alarmHour);
                    editor.putInt("minute", alarmMin);
                    editor.commit();
                    setDailyAlarm();
                    Toast.makeText(MainActivity.this,"Daily update time has been set !", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"First set alarm update time!", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
                navigationView.setCheckedItem(R.id.home);
            }
        });

        dialog.setView(v);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
*/

    private void showBirthdays(String day) {
        BirthdayFragement birthdayFragement = new BirthdayFragement();
        Bundle bundle = new Bundle();
        bundle.putString("ListType",day);
        birthdayFragement.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_area,birthdayFragement ).commit();
    }

    private void aboutDeveloper() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View v = layoutInflater.inflate(R.layout.about_developer,null);
        AlertDialog dialog = builder.create();
        CircleImageView git, web,fb;
        fb = v.findViewById(R.id.fb_link);
        git = v.findViewById(R.id.git_link);
        web = v.findViewById(R.id.web_link);


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/rezwan.pranto.5"));
                startActivity(i);
            }
        });

        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RkPranto?tab=repositories"));
                startActivity(i);
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hipranto.com"));
                startActivity(i);
            }
        });
        dialog.setView(v);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        birthDatabase = new BirthDatabase(MainActivity.this);
        toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout,
                mToolbar,
                R.string.navigation_open,
                R.string.navigation_close);

        preferences = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        int navSelected = navigationView.getCheckedItem().getItemId();
        if(R.id.setting == navSelected
                || R.id.add_new == navSelected
            ) {
            //Toast.makeText(MainActivity.this,"Nav ID", Toast.LENGTH_SHORT).show();
            navigationView.setCheckedItem(R.id.home);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.content_area, new HomeFragment()).commit();
        }
        else if(isNeedToHome){
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.content_area, new HomeFragment()).commit();
            isNeedToHome = false;
        }
        else{
            //Toast.makeText(MainActivity.this,"Nav Super", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        //Toast.makeText(MainActivity.this,navigationView.getCheckedItem().toString(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==0){

        }
    }

    public void setDailyAlarm(){

        Calendar calendar = Calendar.getInstance();

        int hh = 0, mm = 0;
        /*if(preferences.getInt("hour",99) != 99){
            hh = preferences.getInt("hour",99);
        }
        if(preferences.getInt("minute",88) != 88){
            mm = preferences.getInt("minute",88);
        }*/
        //Log.d("Alarm", "Set alarm called: "+hh+ " : "+mm);
        //Log.d("ALARM",hh+": "+mm);

        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm );
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(MainActivity.this, NotificationSetter.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                1,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
                );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //The action bar home/up action will open or close
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
