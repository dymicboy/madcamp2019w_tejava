package com.example.tab.ui.main;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.tab.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class tab1 extends Fragment {

    private Hashtable<String, String> id_to_name;
    private Hashtable<String, String> id_to_phone;

    public tab1(Hashtable<String, String> tmp_id_to_name, Hashtable<String, String> tmp_id_to_phone){
        id_to_name = tmp_id_to_name;
        id_to_phone = tmp_id_to_phone;
    }

    public static tab1 newInstance(Hashtable<String, String> tmp_id_to_name, Hashtable<String, String> tmp_id_to_phone) {
        return new tab1(tmp_id_to_name,tmp_id_to_phone);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.tab1_layout, container, false);
        ListView listview= vi.findViewById(R.id.listView1);


        ArrayList nameList = new ArrayList();
        List keys = new ArrayList(id_to_name.keySet());

        for(int i =0; i<keys.size();i++){
            String key = keys.get(i).toString();
            Log.i("name",id_to_name.get(key));
            Log.i("phone",id_to_phone.get(key));
            nameList.add(id_to_name.get(key)+" : "+id_to_phone.get(key));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_list_item_1, nameList);
        listview.setAdapter(adapter);

        return vi;
    }

}
