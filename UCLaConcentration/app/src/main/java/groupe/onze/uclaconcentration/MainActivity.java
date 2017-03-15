package groupe.onze.uclaconcentration;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

public class MainActivity extends BasicActivity {
    TimerServiceReceiver timerReceiver;
    int counter;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv1);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button play = (Button) findViewById(R.id.button_play);
        assert play!= null;
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Context context= getCtx();
                    if (isMyServiceRunning(SensorService.class)) {

                        Toast.makeText(context,getResources().getString(R.string.stopping_chrono),Toast.LENGTH_LONG).show();
                        Intent serviceIntent = new Intent(getCtx(),SensorService.class);
                        context.stopService(serviceIntent);
                    }
                    else {
                        mSensorService = new SensorService(context);
                        mServiceIntent = new Intent(context, mSensorService.getClass());
                        if (!isMyServiceRunning(mSensorService.getClass())) {
                            startService(mServiceIntent);
                        }
                    }

            }
        });

        //Acces au store
        Button store = (Button) findViewById(R.id.button_store);
        assert store != null;
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, StoreActivity.class);
                startActivity(s);
            }
        });

        Button ade = (Button) findViewById(R.id.ade_button);
        assert ade != null;
        ade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, AdeActivity.class);
                startActivity(s);
            }
        });

        /*  Ca marche pas pour l'instant
        Button calendar=(Button)findViewById(R.id.button_calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, CalendarGoogle.class);
                startActivity(s);
            }
        }); */

        Button face = (Button) findViewById(R.id.connexion_button);
        assert face != null;
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, Connexion.class);
                startActivity(s);
            }
        });


        /** Lance le service de timer */
        ctx = this;

        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
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
                for (int i = 0; i < help.length; i ++)
                {
                    Toast.makeText(this, help[i], Toast.LENGTH_LONG).show();
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
        startTimerService();

        super.onResume();
    }

    /**
     * Démarre le timer
     */
    private void startTimerService() {
        startService(new Intent(this, SensorService.class));
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
                NotificationsSys.sendNotif(context,getResources().getString(R.string.TIME_OUT)
                        ,getResources().getString(R.string.more_10),MainActivity.class);
            }
            UpdateGUI();
        }
    }

    }
