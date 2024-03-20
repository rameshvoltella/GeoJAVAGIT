package com.example.maps;

import static com.example.maps.MainActivity.RADIUS_DATA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maps.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener{
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private final int REQUEST_LOCATION_PERMISSIONS = 1001;
    private final int SETTING_INTENT_CODE = 433;
private GeofencingClient geofencingClient;
private  float georadius;
private Geofencehelper geofencehelper;
private String id="mmam";
private final int BACKGROUBD=182;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofencehelper=new Geofencehelper(this);
        Intent intent=getIntent();
        Intent serviceIntent = new Intent(this, GeofenceForegroundService.class);
        startService(serviceIntent);
        if(intent!=null)
        {
            String radius=intent.getStringExtra(RADIUS_DATA);
            if(radius!=null)
            {
                float radi=Float.parseFloat(radius);
                georadius=radi;
                //LatLng latLng = new LatLng(9.755716241185086, 76.48773637337722);
                LatLng latLng = new LatLng(9.755716241185086, 76.48773637337722);

                addgeofence(latLng,georadius);

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng effil = new LatLng(47.3, 2.44);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(effil, 16));
        permissions();
        mMap.setOnMapLongClickListener((GoogleMap.OnMapLongClickListener) MapsActivity.this);
    }

    private void permissions() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            Toast.makeText(this, "Fine-grained and Coarse-grained location access granted.", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, REQUEST_LOCATION_PERMISSIONS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, REQUEST_LOCATION_PERMISSIONS);
            }
        }
    }
    private void showsnackbar()
    {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView,"this application need location permissions",Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant permissions", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:"+ getPackageName()));
                        startActivityForResult(intent,SETTING_INTENT_CODE);
                    }
                }).show();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        if(Build.VERSION.SDK_INT>=29)
        {
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                mMap.clear();
                addmarker(latLng);
                addCircle(latLng,georadius);
                addgeofence(latLng,georadius);
                Toast.makeText(geofencehelper, "anil", Toast.LENGTH_SHORT).show();
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    // Show a dialog explaining the need for the background location permission

                            // Request the permission after the user acknowledges the explanation
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUBD);


                } else {
                    // Request the permission directly as no explanation is needed
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUBD);
                }

            }
        }
        else
        {
        //    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUBD);
            mMap.clear();
            addmarker(latLng);
            addCircle(latLng,georadius);
            addgeofence(latLng,georadius);
        }

    }
    @SuppressLint("MissingPermission")
    private void addgeofence(LatLng latLng, float  radius)
    {
        Geofence geofence=geofencehelper.getgeofence(id,latLng,radius,Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest=geofencehelper.geofencingRequest(geofence);
        PendingIntent peendingIntent=geofencehelper.getpendingIntent();
       geofencingClient.addGeofences(geofencingRequest,peendingIntent)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                       Toast.makeText(geofencehelper, "success", Toast.LENGTH_SHORT).show();
                       Log.d(TAG, "onSuccess: geofences added");
                   }
               })

               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       String error=geofencehelper.geterror(e);
                       Log.d(TAG, "onFailure: "+error);
                       Toast.makeText(geofencehelper, "success"+error, Toast.LENGTH_SHORT).show();

                   }
               });
    }
    private void addmarker(LatLng latLng)
    {
        MarkerOptions markerOptions=new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }
    private void addCircle(LatLng latLng,float radius)
    {
        CircleOptions circleOptions=new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.fillColor(Color.argb(255,255,0,0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);

                Toast.makeText(this, "Fine-grained and Coarse-grained location access granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permissions denied.", Toast.LENGTH_SHORT).show();
            }}
        if (requestCode == BACKGROUBD) {

            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(this, "Bakcground permission accesed", Toast.LENGTH_SHORT).show();
            } else {
                // Location permissions denied, handle this case (e.g., show a message or disable location-related functionality).
                Toast.makeText(this, "background permissions denied.", Toast.LENGTH_SHORT).show();
            }
        }}
}

