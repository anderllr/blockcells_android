package br.com.blockcells.blockcells.funcs;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by anderson on 22/12/16.
 */

public class BlockService extends Service {

    private static final String TAG = "BlockService";
    private static double latAnt = 0;
    private static double lonAnt = 0;
    static final Double EARTH_RADIUS = 6371.00;
    private static Location mLastLocation = null;

    private boolean isRunning = false;

    @Override
    public void onCreate() {
        //  Toast.makeText(this, "Criando serviço...", Toast.LENGTH_SHORT).show();

        isRunning = true;
    }


    public void Localizacao() {
        final GlobalSpeed globalSpeed = (GlobalSpeed) getApplicationContext();


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
              /*  Double lat = location.getLatitude();
                Double lon = location.getLongitude();
                double Radius = EARTH_RADIUS;
                Float spTest = location.getSpeed();

                String speed = "0";
                String speedTest = String.valueOf((int) (spTest * 3.6));
                Toast.makeText(getBaseContext(), "Lat.: " + lat + " Long.: " + lon, Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), "Lat. Ant: " + latAnt + " Long. Ant: " + lonAnt, Toast.LENGTH_SHORT).show();
                //speed = String.valueOf((int) ((location.getSpeed()*3600)/1000));
                if (latAnt != 0) {

                    double dLat = Math.toRadians(lat - latAnt);
                    double dLon = Math.toRadians(lon - lonAnt);
                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                            Math.cos(Math.toRadians(latAnt)) * Math.cos(Math.toRadians(lat)) *
                                    Math.sin(dLon / 2) * Math.sin(dLon / 2);
                    double c = 2 * Math.asin(Math.sqrt(a));
                    speed = String.valueOf((int) (((Radius * c) * 3.6)));
                }
                //   Toast.makeText(ActPrincipal.this, "Current speed:" + speed, Toast.LENGTH_SHORT).show();
                //   Toast.makeText(ActPrincipal.this, "Mudou... Lat.: " + String.valueOf(lat) + " - Ant.: " + String.valueOf(latAnt), Toast.LENGTH_SHORT).show();
                //    speed = btnSpeed.getText().toString();

                //  speed = String.valueOf(Integer.parseInt(speed) + 1);
                if (Integer.parseInt(speed) < 200) {
                    //  btnSpeed.setText(speed);

                    if (Integer.parseInt(speed) > 3) {
                        globalSpeed.setSpeed(Integer.parseInt(speed));
                    }
                }

             //   Toast.makeText(getBaseContext(), "Speed: " + speed, Toast.LENGTH_SHORT).show();
               // Toast.makeText(getBaseContext(), "Speed Test: " + speedTest + " | " + spTest, Toast.LENGTH_SHORT).show();
                if ((lonAnt == lon) && (latAnt == lat)) {
                    Toast.makeText(getBaseContext(), "Tá igual...", Toast.LENGTH_SHORT).show();
                }

                latAnt = lat;
                lonAnt = lon; */

                //calcul manually speed
                double speed = 0;
                if (mLastLocation != null) {
                    speed = Math.sqrt(
                            Math.pow(location.getLongitude() - mLastLocation.getLongitude(), 2)
                                    + Math.pow(location.getLatitude() - mLastLocation.getLatitude(), 2)
                    ) / (location.getTime() - mLastLocation.getTime());
                }
                //if there is speed from location
                if (location.hasSpeed()) {
                    //get location speed
                    speed = location.getSpeed();
                }
                mLastLocation = location;
                ////////////
                //DO WHAT YOU WANT WITH speed VARIABLE
                ////////////
                //Change to km/h
                speed = speed * 3.6;
                Integer speed1 = (int) speed;

                globalSpeed.setSpeed(speed1);
                globalSpeed.setLatitude(location.getLatitude());
                globalSpeed.setLongitude(location.getLongitude());

 //               Toast.makeText(getBaseContext(), "Velocidade final: " + speed1, Toast.LENGTH_SHORT).show();

            }


            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Toast.makeText(ActPrincipal.this, "Mudou o status...", Toast.LENGTH_SHORT).show();

            }

            public void onProviderEnabled(String provider) {
                Log.i(TAG, "Provider Enabled...");
                Toast.makeText(getBaseContext(), "Provider Enabled...", Toast.LENGTH_SHORT).show();

            }

            public void onProviderDisabled(String provider) {
                Log.i(TAG, "Provider Disabled...");
                Toast.makeText(getBaseContext(), "Provider Disabled...", Toast.LENGTH_SHORT).show();
            }

        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {


                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    if (isRunning) {
                        Log.i(TAG, "Service running");
                    }
                }

                //Stop service once it finishes its task
                stopSelf();
            }
        }).start();

        Localizacao();
        return Service.START_STICKY;


    }


    @Override
    public IBinder onBind(Intent arg0) {
        //  Toast.makeText(this, "Serviço onBind", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        //  Toast.makeText(this, "Serviço finalizado", Toast.LENGTH_SHORT).show();
    }
}