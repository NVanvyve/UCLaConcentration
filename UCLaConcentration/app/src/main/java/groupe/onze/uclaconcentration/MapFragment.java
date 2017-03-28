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

    MapView mMapView;
    private GoogleMap googleMap;
    GPSTracker gps;
    double latitude;
    double longitude;
    SharedPreferences mPrefs;

    Marker marker;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map,container,false);

        double [] save_location = {0,0};

        //Bundle bundle = this.getArguments();
        //if (bundle != null){
        //    save_location = bundle.getDoubleArray("Position");
        //}

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        gps = new GPSTracker(getActivity().getApplicationContext(),getActivity());

        // Pour les tests
        //save_location = gps.newLocation(20);

        double loc[] = gps.giveMeLatLong();
        latitude = loc[0];
        longitude = loc[1];

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final double[] finalSave_location = save_location;
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // NEVER DELETE !! :)
                try {
                    googleMap.setMyLocationEnabled(true);
                }
                catch (SecurityException e) {
                    Log.e("ERROR", "No permission to check the location");
                    e.printStackTrace();
                }

                //googleMap.setMapStyle(3);//Terrain

                if(finalSave_location[0]!=0 && finalSave_location[1]!=0) {
                    // For dropping a marker at a point on the Map
                    LatLng destination = new LatLng(finalSave_location[0],finalSave_location[1]);
                    googleMap.addMarker(new MarkerOptions().position(destination).title("Destination"));
                }

                // For zooming automatically to the location of the marker
                LatLng myPosition = new LatLng(latitude,longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(myPosition)
                        .zoom(17)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        mPrefs = this.getActivity().getSharedPreferences("label",0);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void setMarker(){
        float lat = mPrefs.getFloat("NL_0", 10);
        float lon = mPrefs.getFloat("NL_1", 10);
        if (marker != null)
            marker.remove();
        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(getResources().getString(R.string.goal)));

        Log.i("UPDATE", "Destination updated");
    }

}
