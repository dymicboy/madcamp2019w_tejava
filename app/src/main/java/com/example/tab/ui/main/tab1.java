package com.example.tab.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tab.R;
import com.example.tab.ui.main.PageViewModel;

public class tab1 extends Fragment {

    static tab1 newInstance() {
        return new tab1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab1_layout, container, false);
    }
}