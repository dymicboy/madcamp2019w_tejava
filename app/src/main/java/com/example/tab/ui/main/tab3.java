package com.example.tab.ui.main;

import android.Manifest;
import android.app.Activity;
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
import android.telephony.TelephonyManager;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tab.MainActivity;
import com.example.tab.R;
import com.example.tab.ui.main.PageViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class tab3 extends Fragment implements
        OnMapReadyCallback{
    static tab3 newInstance() {
        return new tab3();
    }

//    private static final LatLng SEOUL = new LatLng(37.566535, 126.977969);
//    private Marker mSeoul;

    private GoogleMap mMap;
    private Marker marker;
    private int a=0;
    private String number;
    private Activity myActivity;
    private JSONArray json_array = null;


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
        myActivity = this.getActivity();
        View vi = inflater.inflate(R.layout.tab3_layout, container, false);

        TelephonyManager tMgr = (TelephonyManager) this.getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        number = tMgr.getLine1Number();

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
        private void setLocation(Location location){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng yourplace = new LatLng(latitude, longitude);

            if (a >= 1) {
                marker.remove();
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(yourplace);
            markerOptions.title("yourplace");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            marker = mMap.addMarker(markerOptions);
            a++;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            locationManager = (LocationManager) myActivity.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListner);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                setLocation(location);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListner);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    setLocation(location);
                }
            }
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            request(number, Double.toString(latitude), Double.toString(longitude));
            if(json_array!=null) {
                for (int i = 0; i < json_array.length(); i++) {
                    JSONObject tmp = null;
                    try {
                        tmp = json_array.getJSONObject(i);
                        tmp.getString("number");
                        tmp.getString("lati");
                        tmp.getString("longi");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
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


    public void request(String user_number,String user_lati,String user_longi){
        //url 요청주소 넣는 editText를 받아 url만들기
        String url = "http://be78008c.ngrok.io/";

        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
            JSONObject data= new JSONObject();
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            data.accumulate("number", user_number);
            data.accumulate("lati", user_lati);
            data.accumulate("longi", user_longi);
            data.accumulate("time", format.format(now));

            Log.i("info",data.toString());

            //이제 전송해볼까요?
            final RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,data, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        //받은 json형식의 응답을 받아
                        json_array = new JSONObject(response.toString()).getJSONArray("info");

                        Log.i("json_parse_info",json_array.toString());
                        //key값에 따라 value값을 쪼개 받아옵니다.



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
            //
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
