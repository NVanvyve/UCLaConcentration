package groupe.onze.uclaconcentration;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Random;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class GPSTracker extends Service {

    private Context mContext;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    Activity activity;

    public GPSTracker(Context context,Activity activity) {
        this.mContext = context;
        this.activity = activity;
        getLocation();
    }

    public Location getLocation() {
        try {


            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    int requestPermissionsCode = 50;

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,mLocationListener);
                    Log.d("Network","Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
            // If GPS enabled, get latitude/longitude using GPS Services
            if (isGPSEnabled) {
                if (location == null) {
                    if (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},50);

                    } else {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,mLocationListener);
                        Log.d("GPS Enabled","GPS Enabled");
                        if (locationManager != null) {

                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     */
    public void stopUsingGPS() {

    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

        @Override
        public void onStatusChanged(String provider,int status,Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }


    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    public double distance(double LatA,double LongA,double LatB,double LongB) {
        double R = 6371000; // Rayon de la Terre en mètres
        double a = Math.toRadians(LatA); //latitude du point A (en radians)
        double b = Math.toRadians(LatB); //latitude du point B (en radians)
        double c = Math.toRadians(LongA); //longitude du point A (en radians)
        double d = Math.toRadians(LongB); //longitude du point B (en radians)

        double Dist = R * acos(cos(a) * cos(b) * cos(c - d) + sin(a) * sin(b)); //Distance en metre
        return Dist;
    }

    /*
        @pre  : distance est une distance en mettre
        @post : retourne un tableau contenant lune latitude et une longitude d'une nouvelle position se situant une distance x de la position actuelle.
                x etant un nombre compris entre [0.9 x ; 1.1 x] && [x-100m ; x+100m]
     */
    public double[] newLocation(int distance) {

        Random rand = new Random();

        if (distance < 1000) {
            double m = rand.nextDouble() * 0.2;
            distance = (int) Math.floor(distance * (0.9 + m));
        } else {
            int m = rand.nextInt(200);
            distance = distance - 100 + m;
        }

        getLocation();

        double lat1 = Math.toRadians(latitude);
        double lon1 = Math.toRadians(longitude);

        double sigma = distance / 6371000.0;
        double theta = rand.nextDouble() * 2 * Math.PI;
        theta = theta % (2 * Math.PI);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(sigma) + Math.cos(lat1) * Math.sin(sigma) * Math.cos(theta));
        double lon2 = lon1 + Math.atan2(Math.sin(theta) * Math.sin(sigma) * Math.cos(lat1),Math.cos(sigma) - Math.sin(lat1) * Math.sin(lat2));

        lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);

        double tab[] = {lat2,lon2};
        return tab;

    }

    public double[] giveMeLatLong() {
        double la = 0;
        double lo = 0;

        getLocation();

        if (ContextCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            // TODO : Recharger la page

        } else {
            // Check if GPS enabled
            if (canGetLocation()) {
                la = latitude;
                lo = longitude;
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                showSettingsAlert();
            }
        }
        double tab[] = {la,lo};
        return tab;
    }


}
