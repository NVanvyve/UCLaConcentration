package groupe.onze.uclaconcentration;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nicolas on 19/03/17.
 */

public class Sport extends BasicActivity {


    private double latitude;
    private double longitude;
    private GPSTracker gps;
    private Context mContext;
    private TextView coord;
    private TextView tv_dist;
    private TimerServiceReceiver timerReceiver;
    private Intent mServiceIntent;

    SharedPreferences mPrefs;
    int lvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sport);

        mPrefs = getSharedPreferences("label",0);
        mContext = this;
        coord = (TextView) findViewById(R.id.coord_sport);

        //GPS
        gps = new GPSTracker(mContext,Sport.this);
        mServiceIntent = new Intent(mContext,gps.getClass());
        //startService(mServiceIntent);


        //"SPORT"
        tv_dist = (TextView) findViewById(R.id.dist);
        lvl = mPrefs.getInt("sport_level",0);


        TextView level = (TextView) findViewById(R.id.sport_level);
        String tab_level[] = getResources().getStringArray(R.array.sport_level_array);
        level.setText(getString(R.string.your_sport_level) + tab_level[lvl]);

        final int dist_tab[] = {30,70,100,150,200,500};
        final int recompence_tab[] = {30,70,100,150,200,500};

        if (lvl>dist_tab.length || dist_tab.length != recompence_tab.length){
            lvl = 0;
            Toast.makeText(getApplicationContext(),"PROBLEME D'IMPLEMENTATION!!!",Toast.LENGTH_LONG).show();
        }

        Button newPosition = (Button) findViewById(R.id.new_position);
        assert newPosition != null;
        newPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = gps.giveMeLatLong()[0];
                longitude = gps.giveMeLatLong()[1];
                double nl[] = gps.newLocation(dist_tab[lvl]);
                int dist = (int) gps.distance(latitude,longitude,nl[0],nl[1]);
                tv_dist.setText("Destination a atteindre pour gagner " + recompence_tab[lvl]
                        + " " + getString(R.string.coin_name)
                        + " :\nLat : " + nl[0] + "\nLong : " + nl[1]
                        + "\nDistance demandée = " + dist_tab[lvl]
                        + " m\nDistance réelle= " + dist + " m");
            }
        });


    }

    //SERVICE
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
        movementFilter = new IntentFilter(GPSTracker.service_name);
        timerReceiver = new Sport.TimerServiceReceiver();
        registerReceiver(timerReceiver,movementFilter);

        super.onResume();
    }


    /**
     * Update le TimeViewer
     */
    final Runnable myRunnable = new Runnable() {
        public void run() {
            coord.setText("Your position : \nLat : " + latitude + "\nLong : " + longitude);
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
            latitude = intent.getIntExtra(GPSTracker.latitude_string,0);
            longitude = intent.getIntExtra(GPSTracker.longitude_string,0);
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
                Intent t = new Intent(this,MainActivity.class);
                finish();
                startActivity(t);

            case R.id.action_recompense:
                Intent s = new Intent(this,StoreActivity.class);
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