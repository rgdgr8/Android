package com.example.hikerwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView lattextView;
    TextView lngtextView;
    TextView acctextView;
    TextView alttextView;
    TextView addresstextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lattextView=(TextView)findViewById(R.id.lattextView);
        lngtextView=(TextView)findViewById(R.id.lngtextView);
        acctextView=(TextView)findViewById(R.id.acctextView);
        alttextView=(TextView)findViewById(R.id.alttextView);
        addresstextView=(TextView)findViewById(R.id.addresstextView);

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updatelocation(location);
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

        if (Build.VERSION.SDK_INT<23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastLocation!=null){
                    updatelocation(lastLocation);
                }
            }
        }
    }
    public void updatelocation(Location location){
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        DecimalFormat decimalFormat=new DecimalFormat("#.000");
        String address="";
        String lat="";
        String lng="";
        String accuracy="Accuracy: "+decimalFormat.format(location.getAccuracy());
        String alt="Altitude: "+decimalFormat.format(location.getAltitude());
        try {
            List<Address> list=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            lat="Latitude: "+decimalFormat.format(list.get(0).getLatitude());
            lng="Longitude: "+decimalFormat.format(list.get(0).getLongitude());

            if (list!=null && list.size()>0){

                address="Address: ";
                if (list.get(0).getThoroughfare()!=null)
                    address+=list.get(0).getThoroughfare()+ ", ";
                if (list.get(0).getLocality()!=null)
                    address+=list.get(0).getLocality()+ ", ";
                if (list.get(0).getAdminArea()!=null)
                    address+=list.get(0).getAdminArea()+ ", ";
                if (list.get(0).getCountryName()!=null)
                    address+=list.get(0).getCountryName();
                else
                    address="Couldn't find Address";
            }
            lattextView.setText(lat);
            lngtextView.setText(lng);
            acctextView.setText(accuracy);
            alttextView.setText(alt);
            addresstextView.setText(address);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
        }
    }
}
