package groupe.onze.uclaconcentration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private GPSTracker gps;
    private double latitude;
    private double longitude;
    private SharedPreferences mPrefs;
    private boolean already_define;

    private Marker marker;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map,container,false);

        double[] save_location = {0,0};

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        gps = new GPSTracker(getActivity().getApplicationContext(),getActivity());

        mPrefs = this.getActivity().getSharedPreferences("label",0);
        already_define = mPrefs.getBoolean("already_define",false);

        double loc[] = gps.giveMeLatLong();
        latitude = loc[0];
        longitude = loc[1];

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // NEVER DELETE !! :)
                try {
                    googleMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    Log.e("ERROR","No permission to check the location");
                    e.printStackTrace();
                }

                //googleMap.setMapStyle(3);//Terrain

                if (already_define) {
                    setMarker();
                }

                // For zooming automatically to the location of the marker
                LatLng myPosition = new LatLng(latitude,longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(myPosition)
                        //TODO Ajuster le zoom en fonction de la distance https://developers.google.com/maps/documentation/android-api/views?hl=fr
                        .zoom(15)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.isInBackground=false;
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.isInBackground=true;
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.isInBackground=true;
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void setMarker() {
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        float lat = mPrefs.getFloat("NL_0",0);
        float lon = mPrefs.getFloat("NL_1",0);
        removeMarker();
        if (lat != 0 && lon != 0) {
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat,lon))
                    .title(getResources().getString(R.string.goal)));

            Log.i("UPDATE","Destination updated");
        }
    }

    public void removeMarker() {
        try {
            if (marker != null)
                marker.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
