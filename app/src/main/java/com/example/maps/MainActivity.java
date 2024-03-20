package com.example.maps;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends ComponentActivity {
private EditText radiusdata;
private Button submit;
//    LatLng latLng = new LatLng(9.767926616447395, 76.49069944635423);
//    LatLng latLng = new LatLng(9.755716232144868, 76.48774164327573);

public static final String RADIUS_DATA="dfffejfe";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setview();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String radius=radiusdata.getText().toString();

                if(!radius.equals("") )
                {
                    Intent intent=new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra(RADIUS_DATA,radius);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "please set radius to trigger", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void setview()
    {
        radiusdata=findViewById(R.id.radius);
        submit=findViewById(R.id.submit);
    }

  }