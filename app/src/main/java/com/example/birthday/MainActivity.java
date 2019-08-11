package com.example.birthday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
                        implements HomeFragment.OnClickDrawerOpen {
    Toolbar mToolbar;
    ContactAdapter  contactAdapter;
    BirthDatabase  birthDatabase;
    TextView textViewQueryResult;
    EditText editText ;
    ArrayList<ContactModel> arrayList = new ArrayList<>();
    //Fragement
    FragmentManager fm;
    SearchView searchView;

    //Navigation Drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    //Contact Inport
    SharedPreferences preferences ;
    SharedPreferences.Editor editor;

    //Search
    boolean imported ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //TOOLBAR SECTION
        setSupportActionBar(mToolbar);
        //searchFromRecycler(birthDatabase.getAllContact());

       /* toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
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
*/

        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();



        HomeFragment homeFragment = new HomeFragment();
        fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_area,homeFragment).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();

                switch (menuItem.getItemId()){
                    case R.id.home:
                        fragmentTransaction.replace(R.id.content_area,new HomeFragment()).commit();

                        break;
                    case R.id.setting:
                        fragmentTransaction.replace(R.id.content_area, new Setting()).commit();
                        break;

                    case R.id.add_new:

                        fragmentTransaction.replace(R.id.content_area, new AddFragment()).commit();
                        break;
                    case R.id.todaybd:
                        showBirthdays("today");
                        Toast.makeText( MainActivity.this, " Today Birthday",Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.tomorrowbd:
                        showBirthdays("tomorrow");
                        Toast.makeText( MainActivity.this, " Tommorow Birthday",Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.thismonthbd:
                        Toast.makeText( MainActivity.this, " Month Birthday",Toast.LENGTH_SHORT).show();

                        showBirthdays("month");
                        break;
                    case R.id.contact_import:
                        if(!alreadyImported()){
                            importFromContact();
                        }
                        else{
                            Toast.makeText( MainActivity.this, " Already Imported from Contact",Toast.LENGTH_LONG).show();
                        }



                        break;
                    case R.id.fb_import:


                        break;
                    case R.id.share:


                        break;
                    case R.id.privacy:


                        break;
                    case R.id.help:


                        break;
                    case R.id.about_devloper:
                        aboutDeveloper();

                        break;
                    default:
                        Toast.makeText(MainActivity.this,"Wrong Clicked somehow !",Toast.LENGTH_SHORT).show();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });


    }

    private boolean alreadyImported() {

        if(preferences.getBoolean("imported", false) == true){
            return true;
        }
        else{
            editor.putBoolean("imported", true);
            editor.commit();
            return false;
        }
    }

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
            //loadRecyclerContent(birthDatabase.getAllContact());
        }


    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout,
                mToolbar,
                R.string.navigation_open,
                R.string.navigation_close);

        preferences = getApplicationContext().getSharedPreferences("ContactInfoPref", MODE_PRIVATE);
        editor = preferences.edit();


    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==0){

        }
    }


    @Override
    public void openNavDrawer(boolean x) {
        if(x == true){
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }

    }
}
