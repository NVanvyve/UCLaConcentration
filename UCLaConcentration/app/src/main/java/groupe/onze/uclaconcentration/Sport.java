package groupe.onze.uclaconcentration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Nicolas in mars 2017.
 */

public class Sport extends BasicActivity {


    private double latitude;
    private double longitude;
    private GPSTracker gps;


    private Context mContext;
    private double[] newLocation;
    private boolean already_define;

    private SharedPreferences mPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private int lvl;
    private MapFragment map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sport); // TODO : BUG :  Unable to start activity ComponentInfo{groupe.onze.uclaconcentration/groupe.onze.uclaconcentration.Sport}: android.view.InflateException: Binary XML file line #57: Error inflating class fragment

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Fragment currentFragment = this.getActivity().getFragmentManager().findFragmentById(R.id.map_frag);
        //Fragment currentFragment = fragmentManager.findFragmentByTag("fragmentTag");
        FragmentManager fm = getSupportFragmentManager();
        List<android.support.v4.app.Fragment> fragments = fm.getFragments();
        map = (MapFragment) fragments.get(fragments.size() - 1);
        Log.i("FRAGMENT MANAGER","Fragments is non null");

        newLocation = new double[2];

        mPrefs = getSharedPreferences("label",0);


        prefListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs,
                                                          String key) {
                        if (key.equals("NL_0") || key.equals("NL_1")) {
                            map.setMarker();
                        }

                    }
                };

        mPrefs.registerOnSharedPreferenceChangeListener(prefListener);


        final SharedPreferences.Editor mEditor = mPrefs.edit();
        mContext = this;

        //GPS
        gps = new GPSTracker(mContext,Sport.this);
        Intent mServiceIntent = new Intent(mContext,gps.getClass());
        //startService(mServiceIntent);


        //"SPORT"
        newLocation = new double[2];

        already_define = mPrefs.getBoolean("already_define",false);
        newLocation[0] = (double) mPrefs.getFloat("NL_0",0);
        newLocation[1] = (double) mPrefs.getFloat("NL_1",0);

        lvl = mPrefs.getInt("sport_level",0);
        TextView level = (TextView) findViewById(R.id.sport_level);
        String tab_level[] = getResources().getStringArray(R.array.sport_level_array);
        level.setText(getString(R.string.your_sport_level) + tab_level[lvl]);

        final int dist_tab [] = new int[]{50,90,120,170,210,500};
        final int recompence_tab[] = {7,12,17,24,30,71}; // dist div 7

        if (lvl > dist_tab.length || dist_tab.length != recompence_tab.length) {
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
                newLocation = gps.newLocation(dist_tab[lvl]);
                mEditor.putFloat("NL_0",(float) newLocation[0]).apply();
                mEditor.putFloat("NL_1",(float) newLocation[1]).commit();

                // Note : Lorsque on change de niveau de sport lors de l'apui sur new location, l'acti quitte

                already_define = true;
                mEditor.putBoolean("already_define",true).commit();

                map.setMarker();

            }
        });


        Button here_i_am = (Button) findViewById(R.id.jy_suis);
        assert here_i_am != null;
        here_i_am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (already_define) {
                    latitude = gps.giveMeLatLong()[0];
                    longitude = gps.giveMeLatLong()[1];
                    double tolerance = 10;
                    double dist = gps.distance(latitude,longitude,newLocation[0],newLocation[1]);
                    if (dist <= tolerance) {
                        String text = "Vous y etes!! Récompence : " + recompence_tab[lvl] + " " + getString(R.string.coin_name);
                        Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
                        map.removeMarker();
                        int coins = mPrefs.getInt("save_coins",0);
                        mEditor.putInt("save_coins",coins + recompence_tab[lvl]).commit();
                        newLocation[0] = 0;
                        newLocation[1] = 0;
                        already_define = false;
                        mEditor.putBoolean("already_define",false).commit();
                    } else {
                        String text = "Vous n'etes pas encore assez proche. Continuez à marcher";
                        Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String avert = "Vous n'avez pas encore defini de cible. Vous ne pouvez donc pas y etre.";
                    Toast.makeText(mContext,avert,Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i("SPORT", "ONCREATE");


    }

    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onStop(){
        super.onStop();
    }

    /**
     * Initialise le récepteur de message broadcast
     */
    @Override
    public void onResume() {
        IntentFilter movementFilter;
        movementFilter = new IntentFilter(GPSTracker.service_name);
        TimerServiceReceiver timerReceiver = new TimerServiceReceiver();
        registerReceiver(timerReceiver,movementFilter);

        lvl = mPrefs.getInt("sport_level",0);
        TextView level = (TextView) findViewById(R.id.sport_level);
        String tab_level[] = getResources().getStringArray(R.array.sport_level_array);
        level.setText(getString(R.string.your_sport_level) + tab_level[lvl]);

        super.onResume();

        Log.i("SPORT", "ONRESUME");
    }


    /**
     * Update le TimeViewer
     */
    private final Runnable myRunnable = new Runnable() {
        public void run() {
            //coord.setText("Your position : \nLat : " + latitude + "\nLong : " + longitude);
        }
    };

    private final Handler myHandler = new Handler();

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
            latitude = intent.getDoubleExtra(GPSTracker.latitude_string,42);
            longitude = intent.getDoubleExtra(GPSTracker.longitude_string,42);
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

        switch (item.getItemId()) {
            case R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_home:
                Intent t = new Intent(this,MainActivity.class);
                finish();
                startActivity(t);
                return true;

            case R.id.action_recompense:
                Intent s = new Intent(this,StoreActivity.class);
                finish();
                startActivity(s);
                return true;

            case R.id.credits:
                Outils.showCredits(this);
                return true;

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
