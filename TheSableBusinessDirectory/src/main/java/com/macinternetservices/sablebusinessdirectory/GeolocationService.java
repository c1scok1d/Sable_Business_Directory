package com.macinternetservices.sablebusinessdirectory;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.ContentValues.TAG;
import static com.macinternetservices.sablebusinessdirectory.GeofenceNotification.CHANNEL_ID;
import static com.macinternetservices.sablebusinessdirectory.MainActivity.currentMarker;
import static com.macinternetservices.sablebusinessdirectory.MainActivity.mMap;

public class GeolocationService extends Service implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener, ResultCallback<Status> {
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 5;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    int counter=0;
    String NOTIFICATION_CHANNEL_ID = "example.permanence";
    String channelName = "Background Service";
    private PendingIntent mPendingIntent;

    @Override
    public void onCreate() {
        buildGoogleApiClient();
        mGoogleApiClient.connect();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(),
                    R.layout.custom_notification);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, TAG,
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).setSmallIcon(R.mipmap.launcher_round).setContent(remoteViews).build();
            startForeground(1, notification);
        }

    }
     /*  if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());*/



    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {

        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
              /*  .setAutoCancel(true)
                .setOngoing(false)
                .setContentTitle("App is running in background")

                .setCategory(Notification.CATEGORY_SERVICE)*/
                .build();
/*        notification.flags |= Notification.FLAG_AUTO_CANCEL;*/
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_REDELIVER_INTENT;
    }

    private void restartService() {
        Log.e("OyApp", "Restart");

            Intent intent = new Intent(this, GeolocationService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 99, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 500, pendingIntent);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        restartService();
     /*   if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();

        }*/
        //stoptimertask();
    }
    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Log.i("Count", "=========  "+ (counter++));
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS
            * DateUtils.HOUR_IN_MILLIS;
    //private int loiteringDelay = 60000;
    public static final long GEOFENCE_RADIUS_IN_METERS = 100;
    @SuppressLint("MissingPermission")
    protected void registerGeofences() {
        if (MainActivity.geofencesAlreadyRegistered) {
            return;
        }

        HashMap<String, SimpleGeofence> geofences = MainActivity.geofences;
        GeofencingRequest.Builder geofencingRequestBuilder = new GeofencingRequest.Builder();
        for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
            SimpleGeofence sg = item.getValue();
            geofencingRequestBuilder.addGeofence(sg.toGeofence());
        }

        GeofencingRequest geofencingRequest = geofencingRequestBuilder.build();
        mPendingIntent = requestPendingIntent();
        LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
                geofencingRequest, mPendingIntent).setResultCallback(this);

        MainActivity.geofencesAlreadyRegistered = true;
    }


    private PendingIntent requestPendingIntent() {

        if (null != mPendingIntent) {

            return mPendingIntent;
        } else {

            Intent intent = new Intent(this, GeofenceReceiver.class);
            return PendingIntent.getService(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

        }
    }

    public void broadcastLocationFound(Location location) {
        Intent intent = new Intent("me.hoen.geofence_21.geolocation.service");
        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        intent.putExtra("done", 1);

        sendBroadcast(intent);
    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //Log.i("Geofence", "Connected to GoogleApiClient");
        startLocationUpdates();
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("Geofence",
                "new location : " + location.getLatitude() + ", "
                        + location.getLongitude() + ". "
                        + location.getAccuracy());
        broadcastLocationFound(location);
        if(!MainActivity.isLoggedIn) {
            if (currentMarker!=null)
                currentMarker.remove();
            currentMarker=mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("You are here!").snippet("Double tap\nanywhere on\nthe map to zoom")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        } else {
            if (currentMarker!=null)
                currentMarker.remove();
           currentMarker= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("Welcome "+ MainActivity.firstName).snippet("Double tap\nanywhere on\nthe map to zoom")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

        if (!MainActivity.geofencesAlreadyRegistered) {
          /*  if (MainActivity.geofences.size() == 0 || MainActivity.mapLocations.size() == 0) {
                return;
            } else { */
                registerGeofences();
            //}
        }
    }
    @Override
    public void onConnectionSuspended(int cause) {
        //Log.i("Geofence", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Log.i("Geofence",
               // "Connection failed: ConnectionResult.getErrorCode() = "
                 //       + result.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        //Log.i("Geofence", "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(400);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {

        } else {
            MainActivity.geofencesAlreadyRegistered = false;
            String errorMessage = getErrorString(this, status.getStatusCode());
            Toast.makeText(getApplicationContext(), errorMessage,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static String getErrorString(Context context, int errorCode) {
        Resources mResources = context.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return mResources.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return mResources.getString(R.string.geofence_too_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return mResources
                        .getString(R.string.geofence_too_many_pending_intents);
            default:
                ////Log.e("Geofence Error: ", String.valueOf(+errorCode));
                return mResources.getString(R.string.unknown_geofence_error) +errorCode;
        }
    }
}