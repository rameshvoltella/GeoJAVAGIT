package com.example.maps;

import static android.app.PendingIntent.FLAG_MUTABLE;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.widget.Switch;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class Geofencehelper extends ContextWrapper {
    PendingIntent pendingIntent;
    public Geofencehelper(Context base) {
        super(base);
    }
    public Geofence getgeofence(String ID,LatLng latLng,float GEOFENCE_RADIUS_IN_METERS,int Transtationtype)
    {

  return new Geofence.Builder()
                .setRequestId(ID)

                .setCircularRegion(
                        latLng.latitude,
                        latLng.longitude,
                      GEOFENCE_RADIUS_IN_METERS
                )

                .setTransitionTypes(Transtationtype)
          .setLoiteringDelay(5000)
          .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }
    public PendingIntent getpendingIntent()
    {
       if(pendingIntent!=null)
       {
           return pendingIntent;
       }
        Intent intent=new Intent(this,GeofenceBroadcastreciver.class);
       pendingIntent=PendingIntent.getBroadcast(this,230,intent,pendingIntent.FLAG_UPDATE_CURRENT|FLAG_MUTABLE);
       return pendingIntent;
    }
    public GeofencingRequest geofencingRequest(Geofence geofence)
    {
      return new GeofencingRequest.Builder()
              .addGeofence(geofence)
              .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
              .build();
    }
    public String geterror(Exception e)
    {
        if(e instanceof ApiException)
        {
            ApiException apiException=(ApiException) e;
            switch (apiException.getStatusCode())
            {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "Geofence not available";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "to many geofences";
                    case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                        return "to many pending intents";

            }

        }
        return e.getLocalizedMessage();

    }
}
