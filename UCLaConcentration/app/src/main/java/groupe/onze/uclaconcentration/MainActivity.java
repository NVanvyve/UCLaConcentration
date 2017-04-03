package groupe.onze.uclaconcentration;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

public class MainActivity extends BasicActivity {
    TimerServiceReceiver timerReceiver;
    int counter;
    TextView tv;
    public static boolean onPause = false;
    Context mContext;

    public final int PERIOD = 1;
    public final int RATE = 1000;

    private SharedPreferences mPrefs;

    private TextView typeTimer;


    private ImageAdapter mAdapter;
    private static boolean onPlay = false;

    private String typeTimerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        mAdapter = new ImageAdapter(this, onPlay, onPause);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                launch(position);
            }
        });

        mPrefs = getSharedPreferences("label",0);



        /*  Ca marche pas pour l'instant
        Button calendar=(Button)findViewById(R.id.button_calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, CalendarGoogle.class);
                startActivity(s);
            }
        }); */
        mContext = this;
    }


    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        tv = (TextView) findViewById(R.id.tv1);
        typeTimer = (TextView) findViewById(R.id.type_of_timer);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        mAdapter = new ImageAdapter(this, onPlay, onPause);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                launch(position);
            }
        });

        if (onPlay)
            typeTimer.setText(typeTimerString);
        else
            typeTimer.setText(R.string.no_timer_active);

    }

    public void launch(int pos) {
        Context context= mContext;
        Intent s;
        switch (pos) {
            case 0:
                onPlay = !onPlay;
                if (isMyServiceRunning(SensorService.class)) {

                    Toast.makeText(context, getResources().getString(R.string.stopping_chrono), Toast.LENGTH_LONG).show();
                    Intent serviceIntent = new Intent(mContext, SensorService.class);
                    context.stopService(serviceIntent);
                    mPrefs = getSharedPreferences("label",0);
                    int time = mPrefs.getInt("counterSeconds",0);
                    addCoins(time);
                    onPause = false;
                }
                else {
                    mSensorService = new SensorService(context);
                    mServiceIntent = new Intent(mContext, mSensorService.getClass());
                    if (!isMyServiceRunning(mSensorService.getClass())) {
                        startService(mServiceIntent);
                    }
                    typeTimerString = getResources().getString(R.string.timer_timer);
                    tv.setText("0");
                    SharedPreferences.Editor editor = mPrefs.edit();
                }
                onStart();
                break;
            case 1:
                if (isMyServiceRunning(SensorService.class)) {
                    if (!onPause) {
                        Toast.makeText(context, getResources().getString(R.string.pausing_chrono), Toast.LENGTH_LONG).show();
                        onPause=true;
                        typeTimerString = getResources().getString(R.string.pause_timer);
                        tv.setText(" ");
                    }
                    else{
                        Toast.makeText(context, getResources().getString(R.string.resuming_chrono), Toast.LENGTH_LONG).show();
                        onPause=false;
                        tv.setText(" ");
                        typeTimerString = getResources().getString(R.string.timer_timer);
                    }
                }
                else {
                    Toast.makeText(context, getResources().getString(R.string.no_timer_active), Toast.LENGTH_LONG).show();
                }
                onStart();
                break;
            case 2:
                s = new Intent(MainActivity.this, AdeActivity.class);
                startActivity(s);
                break;
            case 3:
                s = new Intent(MainActivity.this, NewEventActivity.class);
                startActivity(s);
                break;
            case 4:
                s = new Intent(MainActivity.this, StoreActivity.class);
                startActivity(s);
                break;
            case 5:
                s = new Intent(MainActivity.this,Sport.class);
                startActivity(s);
                break;
            case 6:
                s = new Intent(MainActivity.this, Connexion.class);
                startActivity(s);
                break;
            case 7:
                Toast.makeText(this, "Coucou", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }


    /**
     * Service de timer
     */
    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;

    public Context getCtx() {
        return ctx;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.home :
                finish(); // close this activity and return to preview activity (if there is any)

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_home:
                return true;

            case R.id.action_recompense:
                Intent s = new Intent(MainActivity.this, StoreActivity.class);
                startActivity(s);
                return true;

            case R.id.help:
                String help[] = getResources().getStringArray(R.array.help_main);
                for (int i = 0; i < help.length; i++) {
                    Toast.makeText(this,help[i],Toast.LENGTH_LONG).show();
                    //Snackbar.make(findViewById(android.R.id.content), help[i],
                      //      Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    /**
     * Initialise le récepteur de message broadcast
     */
    @Override
    public void onResume() {
        IntentFilter movementFilter;
        movementFilter = new IntentFilter(SensorService.TIMER_UPDATE);
        timerReceiver = new TimerServiceReceiver();
        registerReceiver(timerReceiver, movementFilter);

        super.onResume();
    }
    /**
     * Update le TimeViewer
     */
    final Runnable myRunnable = new Runnable() {
        public void run() {
            tv.setText(String.valueOf(counter));
        }
    };

    final Handler myHandler = new Handler();

    /**
     * Update le TimeViewer du compteur grâce au Runnable
     */
    private void UpdateGUI() {
        myHandler.post(myRunnable);
    }

    //----------------
    public class TimerServiceReceiver extends BroadcastReceiver {
        /**
         * Reçoit les messages broadcast par les services
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            counter = intent.getIntExtra(SensorService.COUNTER, 0);
            if(counter==10) {//Envoie une notif après 10 secondes d'écoulées
                context=getApplicationContext();
                if (!onPause) {
                    NotificationsSys.sendNotif(context, getResources().getString(R.string.TIME_OUT)
                            , getResources().getString(R.string.more_10), MainActivity.class);
                }else {
                    NotificationsSys.sendNotif(context, getResources().getString(R.string.end_pause)
                            , getResources().getString(R.string.more_10), MainActivity.class);

                }
            }
            UpdateGUI();
        }
    }


    private void addCoins(int time)
    {
        int procraCoins = (time / PERIOD) * RATE + mPrefs.getInt("save_coins",0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putInt("save_coins",procraCoins).commit();
    }

    }
