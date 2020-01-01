package com.example.tab;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.example.tab.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {
    private int flag = 0;
    private int total_flag = 5;

    private Hashtable<String, String> id_to_name = new Hashtable<>();
    private Hashtable<String, String> id_to_phone = new Hashtable<>();
    private Hashtable<String, String> phone_to_id = new Hashtable<>();

    private void init() {
        setAllHashtable();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),id_to_name, id_to_phone, phone_to_id);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 주소록,이미지 권한 확인하고 요청하는 부분
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 400);
        }
        else{
            flag+=1;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        else{
            flag+=1;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }
        else{
            flag+=1;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
        }
        else{
            flag+=1;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 500);
        }
        else{
            flag+=1;
        }
        if(flag == total_flag) init();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 400: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(flag == total_flag-1) init();
                    else flag+=1;
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
            case 500: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(flag == total_flag-1) init();
                    else flag+=1;
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(flag == total_flag-1) init();
                    else flag+=1;
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
            case 200:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(flag == total_flag-1) init();
                    else flag+=1;
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                return;
            }
            case 300:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(flag == total_flag-1) init();
                    else flag+=1;
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @SuppressWarnings("ConstantConditions")
    private ArrayList setAllHashtable() {
        ArrayList<String> nameList = new ArrayList<>();
        ContentResolver cr = this.getContentResolver();

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    id_to_name.put(id,name);
                }
            }
        }
        //핸드폰 번호 hashtable 설정부분
        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null);
        String phoneNo;
        String ID;
        while (pCur != null && pCur.moveToNext()) {
            ID = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phone_to_id.put(phoneNo,ID);
            id_to_phone.put(ID,phoneNo);
        }

        if (cur != null) {
            cur.close();
        }
        return nameList;
    }
}