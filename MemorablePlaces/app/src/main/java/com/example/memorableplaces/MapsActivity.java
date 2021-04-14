package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Intent intent = getIntent();
        int placeNumber = intent.getIntExtra("placeNumber", 0);
        mMap = googleMap;
        if (placeNumber==0) {
            mMap.setOnMapLongClickListener(this);
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    updateinfo(location, "YOU ARE HERE");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateinfo(lastLocation, "YOU ARE HERE");
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "WTF", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Location lastSavedLocation = new Location(LocationManager.GPS_PROVIDER);
            lastSavedLocation.setLatitude(MainActivity.latLngArrayList.get(placeNumber).latitude);
            lastSavedLocation.setLongitude(MainActivity.latLngArrayList.get(placeNumber).longitude);
            updateinfo(lastSavedLocation,MainActivity.placeArrayList.get(placeNumber));
        }
    }


    public void updateinfo(Location lastLocation, String place){

        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(place));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        String place="";
        try {
            List<Address> addresses=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if (addresses!=null && addresses.size()>0){
                if (addresses.get(0).getThoroughfare()!=null ) {
                    place = addresses.get(0).getThoroughfare()+", ";
                }
                if (addresses.get(0).getAdminArea()!=null){
                    place+=addresses.get(0).getAdminArea();
                }
            }
            if (place.equals("") || addresses.get(0).getThoroughfare().equals("Unnamed Road")){
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm  dd/MM/yyyy");
                place=simpleDateFormat.format(new Date());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
                mMap.addMarker(new MarkerOptions().position(latLng).title(place).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                MainActivity.placeArrayList.add(place);
                MainActivity.latLngArrayList.add(latLng);
                MainActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);
        ArrayList<String> latitude=new ArrayList<String>();
        ArrayList<String> longtitude=new ArrayList<String>();

        try {
            sharedPreferences.edit().putString("Places",ObjectSerializer.serialize(MainActivity.placeArrayList)).apply();
            for (LatLng coord : MainActivity.latLngArrayList){
                latitude.add(Double.toString(coord.latitude));
                longtitude.add(Double.toString(coord.longitude));
            }
            sharedPreferences.edit().putString("Latitude",ObjectSerializer.serialize(latitude)).apply();
            sharedPreferences.edit().putString("Longitude",ObjectSerializer.serialize(longtitude)).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show();
    }
}
