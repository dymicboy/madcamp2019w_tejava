package com.example.tab.ui.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tab.R;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Hashtable<String, String> id_to_name;
    private Hashtable<String, String> id_to_phone;
    private Hashtable<String, String> phone_to_id;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Hashtable<String, String> tmp_id_to_name, Hashtable<String, String> tmp_id_to_phone, Hashtable<String, String> tmp_phone_to_id) {
        super(fm);
        id_to_name = tmp_id_to_name;
        id_to_phone = tmp_id_to_phone;
        phone_to_id = tmp_phone_to_id;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position == 0){
            return tab1.newInstance(id_to_name, id_to_phone);
        }
        else if(position == 1){
            //return tab2.newInstance();
            return tab2.newInstance();
        }
        else if(position == 2){
            return tab3.newInstance(id_to_name, phone_to_id);
        }
        else {
            return null;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }

}