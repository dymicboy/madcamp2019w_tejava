package com.example.tab.ui.main;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.tab.R;
import com.example.tab.ui.main.PageViewModel;

import java.util.ArrayList;

public class tab3 extends Fragment {

    static tab3 newInstance() {
        return new tab3();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.tab1_layout, container, false);
        ListView listview= vi.findViewById(R.id.listView1);


        ArrayList nameList;
        nameList = getAllContacts();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_list_item_1, nameList);
        listview.setAdapter(adapter);

        return vi;
    }

    @SuppressWarnings("ConstantConditions")
    private ArrayList getAllContacts() {
        ArrayList<String> nameList = new ArrayList<>();
        ContentResolver cr = this.getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    String phoneNo = "0";
                    while (pCur != null && pCur.moveToNext()) {
                        phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    if( !phoneNo.equals("0")){
                        name = name + " : " + phoneNo;
                    }
                    pCur.close();
                }
                nameList.add(name);
            }
        }
        if (cur != null) {
            cur.close();
        }
        return nameList;
    }
}
