package com.example.thilinidineshika.mapdirection;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
//import android.net.Uri;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PathGoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    public double clat=6.7969;
    public double clng=79.9018;
    private GoogleMap mMap;
    private ArrayList<LocationDistance> locationDistances = new ArrayList<>();
    ArrayList<Integer> waypointOrderList;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    // getting ready the map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MarkerOptions options = new MarkerOptions();


        Intent intent = getIntent();
        addMarkers(intent.getStringExtra("response"));
//get distances
        for (int i = 0; i < locationDistances.size(); i++)
            getDistance(i);
       /*
        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);
        */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
//add points
    private void addMarkers(String obj) {
        if (mMap != null) {
            try {
                JSONArray response = new JSONArray(obj);

                JSONObject jsonObj = null;
                for (int i = 0; i < response.length(); i++) {
                    jsonObj = (JSONObject) response.get(i);/*
                mMap.addMarker(new MarkerOptions().position(new LatLng(jsonObj.getDouble("lat"),jsonObj.getDouble("lng")))
                        .title("DB Point"));/*/
                    locationDistances.add(new LocationDistance(0, new LatLng(jsonObj.getDouble("lat"), jsonObj.getDouble("lng"))));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(jsonObj.getDouble("lat"), jsonObj.getDouble("lng")),
                        13));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
//getting currentlocation
    private void getDistance(final int index) {
        //TODO: Get current location as LatLng

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        locationManager.getBestProvider(criteria,false);
        String provider = locationManager.getBestProvider(criteria, false);
        boolean isGpsProvided = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling


            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null) {
            clat = location.getLatitude();
            clng = location.getLongitude();
        }
        //get distances
        String url = getDirectionsUrl(locationDistances.get(index).getLocation(), new LatLng(clat,clng));
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray rows = response.getJSONArray("rows");
                    JSONObject obj = rows.getJSONObject(0);
                    JSONArray elements = obj.getJSONArray("elements");
                    JSONObject elementObj = elements.getJSONObject(0);
                    JSONObject distance = elementObj.getJSONObject("distance");
                    int value = distance.getInt("value");
                    locationDistances.get(index).setDistance(value);

                    boolean allFilled = true;
                    for(LocationDistance ld : locationDistances){
                        if(ld.getDistance() == 0)allFilled = false;
                    }
                    if(allFilled) {
  //sort distances
                        Collections.sort(locationDistances);
//set path
                        String url = getUrlWithWaypoints(new LatLng(clat,clng),locationDistances.get(0).getLocation());
                        ReadTask downloadTask = new ReadTask();
                        downloadTask.execute(url);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(objectRequest);
    }

    private String getDirectionsUrl(LatLng destination, LatLng current){

        // Origin of route
        String str_origin = "origins="+current.latitude+","+current.longitude;

        String str_dest="destinations="+destination.latitude+","+destination.longitude;



        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest;

        // Output format
        String output = "json";

        // driving mode
        String strmode = "&mode=bicycling";
        //TODO : Put the right key here

        String key = "";

        //sample : https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC&mode=bicycling&language=fr-FR&key="AIzaSyCQbERTa82fIlwBhxsnjVliocmSAueKFk4"
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?"+parameters+"&"+"key=AIzaSyCQbERTa82fIlwBhxsnjVliocmSAueKFk4";

        return url;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */





    public String getMapsApiDirectionsUrl(LatLng originL, LatLng destinationL) {

        String origin ="origin=" + originL.latitude + "," + originL.longitude;
        String destination = "destination=" + destinationL.latitude + "," + destinationL.longitude;
        String params = origin + "&" + destination + "&";
        String output = "json";
        String waypoints = "";

       //String url="https://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|via:Lexington,MA&key=AIzaSyC8LZKLKnWBDAbBIU1Tq72hgVY2xB-yUNs";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params+"key=AIzaSyC8LZKLKnWBDAbBIU1Tq72hgVY2xB-yUNs";
        return url;
    }

    public String getUrlWithWaypoints(LatLng originL, LatLng destinationL) {

        String origin ="origin=" + originL.latitude + "," + originL.longitude;
        String destination = "destination=" + destinationL.latitude + "," + destinationL.longitude;
        String params = origin + "&" + destination + "&";
        String output = "json";
        String waypoints = "waypoints=optimize:true|";
        for(LocationDistance ld : locationDistances){
            waypoints += ld.getLocation().latitude + "," +ld.getLocation().longitude+"|";
        }
        //remove the last &
        waypoints = waypoints.substring(0,waypoints.length()-1);
        params += waypoints;

       //String url="https://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|via:Lexington,MA&key=AIzaSyC8LZKLKnWBDAbBIU1Tq72hgVY2xB-yUNs";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params+"&key=AIzaSyC8LZKLKnWBDAbBIU1Tq72hgVY2xB-yUNs";
        return url;
    }



    private class ReadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>{


        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
                JSONArray routesArray = jObject.getJSONArray("routes");
                JSONObject routesFirstObject = routesArray.getJSONObject(0);
                JSONArray waypointOrder = routesFirstObject.getJSONArray("waypoint_order");
                waypointOrderList = new ArrayList<>();
                for(int i=0; i < waypointOrder.length(); i++)
                    waypointOrderList.add(waypointOrder.getInt(i));
                /*
                mMap.addMarker(new MarkerOptions().position(new LatLng(jsonObj.getDouble("lat"),jsonObj.getDouble("lng")))
                        .title("DB Point"));
                */

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLUE);

            }

            int counter = 1;
            for(int i: waypointOrderList ) {
                LatLng markerPoint = locationDistances.get(i).getLocation();
                mMap.addMarker(new MarkerOptions().position(new LatLng(markerPoint.latitude,markerPoint.longitude))
                        .title("Destination :" + counter++)).showInfoWindow();

            }
            mMap.addPolyline(polyLineOptions);
        }
    }
}
