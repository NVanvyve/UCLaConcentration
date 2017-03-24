package groupe.onze.uclaconcentration;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by nicolasvanvyve on 19/03/17.
 */

public class Sport extends BasicActivity {


    private double latitude;
    private double longitude;
    private GPS_Service gps_service;
    private GPSTracker gps;
    private Context mContext;
    private TextView coord;
    private TextView tv_dist;
    private TimerServiceReceiver timerReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sport);

        mContext = this;
        coord = (TextView) findViewById(R.id.coord_sport);

        //GPS

        gps_service = new GPS_Service(mContext,Sport.this);
        gps = new GPSTracker(mContext,Sport.this);

        // "SPORT"

        tv_dist = (TextView) findViewById(R.id.dist);
        Button newPosition = (Button) findViewById(R.id.new_position);
        assert newPosition != null;
        newPosition.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                latitude = gps.giveMeLatLong()[0];
                longitude = gps.giveMeLatLong()[1];
                Random rand = new Random();
                int d = rand.nextInt(2000);
                double nl[] = gps.newLocation(d);
                int dist = (int) gps.distance(latitude,longitude,nl[0],nl[1]);
                tv_dist.setText("Distance demandée = " + d + " m\nDistance réelle= " + dist + " m");

            }
        });

    }

    //SERVICE

    Intent mService;

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?",true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?",false + "");
        return false;
    }

    /**
     * Initialise le récepteur de message broadcast
     */
    @Override
    public void onResume() {
        IntentFilter movementFilter;
        movementFilter = new IntentFilter(GPS_Service.service_name);
        timerReceiver = new Sport.TimerServiceReceiver();
        registerReceiver(timerReceiver,movementFilter);

        super.onResume();
    }


    /**
     * Update le TimeViewer
     */
    final Runnable myRunnable = new Runnable() {
        public void run() {
            coord.setText("Your position : \nLat : "+latitude+"\nLong : "+longitude);
        }
    };

    final Handler myHandler = new Handler();

    /**
     * Update TimeView grâce au Runnable
     */
    private void UpdateGUI() {
        myHandler.post(myRunnable);
    }


    public class TimerServiceReceiver extends BroadcastReceiver {
        /**
         * Reçoit les messages broadcast par les services
         */
        @Override
        public void onReceive(Context context,Intent intent) {
            latitude = intent.getIntExtra(GPS_Service.latitude_string,0);
            longitude = intent.getIntExtra(GPS_Service.longitude_string,0);
            UpdateGUI();
        }
    }



    // TOOLBAR
    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)

            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                finish();
                startActivity(intent);

            case R.id.action_home:
                Intent t = new Intent(Sport.this,MainActivity.class);
                finish();
                startActivity(t);

            case R.id.action_recompense:
                Intent s = new Intent(Sport.this,StoreActivity.class);
                finish();
                startActivity(s);

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }




}