package com.example.tab.ui.main;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class tab3 extends Fragment implements
        OnMapReadyCallback{
    static tab3 newInstance() {
        return new tab3();
    }

//    private static final LatLng SEOUL = new LatLng(37.566535, 126.977969);
//    private Marker mSeoul;

    private GoogleMap mMap;

    MyTimer myTimer;
    LocationManager locationManager;
    LocationListener locationListner = new LocationListener() {
        public void onLocationChanged(Location location) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onProviderDisabled(String provider) {
        }
    };

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.tab3_layout, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        return vi;
    }

    class MyTimer extends CountDownTimer
    {
        public MyTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
            this.start();
        }
        @Override
        public void onTick(long millisUntilFinished) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng yourplace = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(yourplace).title("your place"));
            }
        }
        @Override
        public void onFinish() {
            this.start();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mSeoul=mMap.addMarker(new MarkerOptions()
//                .position(SEOUL)
//                .title("SEOUL"));
//        mSeoul.setTag(0);

        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListner);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng yourplace = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourplace,16));
            
        }
        myTimer = new MyTimer(6000, 1000);
    }

}
