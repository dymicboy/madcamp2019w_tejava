package com.example.tab.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.tab.R;

public class tab2 extends Fragment {

    static tab2 newInstance() {
        return new tab2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.tab2_layout, container, false);

        View view = inflater.inflate(R.layout.tab2_layout, container, false);


        return view;
    }


}