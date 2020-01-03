package com.example.tab.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class tab3 extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Hashtable<String, String> id_to_name;
    private Hashtable<String, String> phone_to_id;

    public tab3(Hashtable<String, String> tmp_id_to_name, Hashtable<String, String> tmp_phone_to_id){
        id_to_name = tmp_id_to_name;
        phone_to_id = tmp_phone_to_id;
    }

    public static tab3 newInstance(Hashtable<String, String> tmp_id_to_name, Hashtable<String, String> tmp_id_to_phone) {
        return new tab3(tmp_id_to_name,tmp_id_to_phone);
    }

    private GoogleMap mMap;
    List<Marker> markerList = new ArrayList<>();
    private int a=0;
    private String number;
    private Activity myActivity;
    private JSONArray json_array = null;
    private View markericon;
    private View markericon2;


    MyTimer myTimer;

    Location location;
    Location target_location;
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
        markericon = inflater.inflate(R.layout.markericon, container, false);
        markericon2 = inflater.inflate(R.layout.markericon2, container, false);

        TelephonyManager tMgr = (TelephonyManager) this.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        number = tMgr.getLine1Number();
        if(number == null){
            number = "dummy";
        }

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
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
        private void setLocation(){
            if(a>=1){
                Log.i("clear","clear");
                mMap.clear();
                markerList.clear();
            }
            if(json_array!=null) {
                for (int i = 0; i < json_array.length(); i++) {
                    JSONObject tmp = null;
                    try {
                        tmp = json_array.getJSONObject(i);
                        String tmp_number;
                        if(tmp.has("number")) tmp_number = tmp.getString("number");
                        else tmp_number = "null";

                        Double tmp_lati = Double.parseDouble(tmp.getString("lati"));
                        Double tmp_longi = Double.parseDouble(tmp.getString("longi"));
                        LatLng tmp_place = new LatLng( tmp_lati, tmp_longi);


                        if(tmp_number.equals(number)){
                            TextView tmp_text = markericon.findViewById(R.id.tv_marker);
                            tmp_text.setText("현재 위치");
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(tmp_place);
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(myActivity,markericon)));
                            markerList.add(mMap.addMarker(markerOptions));
                            a=1;

                        }
                        else if(phone_to_id.containsKey(tmp_number)) {
                            String tmp_name = id_to_name.get(phone_to_id.get(tmp_number));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(tmp_place);
                            TextView tmp_text = markericon2.findViewById(R.id.tv_marker);
                            tmp_text.setText(tmp_name);
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(myActivity,markericon2)));
                            markerList.add(mMap.addMarker(markerOptions));
                            a=1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        @Override
        public void onTick(long millisUntilFinished) {
            locationManager = (LocationManager) myActivity.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListner);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListner);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            request(number, Double.toString(latitude), Double.toString(longitude));

            setLocation();
        }
        @Override
        public void onFinish() {
            this.start();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
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

    @Override
    public boolean onMarkerClick(final Marker marker){
        Log.i("click_log","clicked");
        Double marker_latitude = marker.getPosition().latitude;
        Double marker_longitude = marker.getPosition().longitude;

        marker_latitude = 36.37382494311092;
        marker_longitude = 127.06578209697677;

        Log.i("marker location",Double.toString(marker_latitude)+" "+Double.toString(marker_longitude));
        Log.i("marker location",location.getLatitude()+" "+location.getLongitude());
        String request_url = makeURL (location.getLatitude(), location.getLongitude(), marker_latitude, marker_longitude);
        path_request(request_url);
        return true;
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

    public void path_request(String request_url){
        //url 요청주소 넣는 editText를 받아 url만들기
        String url = "http://be78008c.ngrok.io/";

        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
            JSONObject data= new JSONObject();
            Date now = new Date();

            data.accumulate("number", "path_url_request");
            data.accumulate("url", request_url);
            Log.i("path_request","path request sent to my server");

            //이제 전송해볼까요?
            final RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,data, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //받은 json형식의 응답을 받아
                        JSONObject path_url_json = new JSONObject(response.toString());
                        Log.i("path_request","json raw" + path_url_json.toString());
                        String routes = path_url_json.get("routes").toString();
                        Log.i("path_request","path_url from my server" + routes);

//                        if(result_json!=null){
//                            drawPath(result_json);
//                        }



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

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=YourAPIKey");
        return urlString.toString();
    }

    public void drawPath(String  result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.BLUE).geodesic(true));
            }

        }
        catch (JSONException e) {

        }
    }
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
