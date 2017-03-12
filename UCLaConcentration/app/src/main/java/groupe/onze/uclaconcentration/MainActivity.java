package groupe.onze.uclaconcentration;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends BasicActivity {
    TimerServiceReceiver timerReceiver;
    int counter;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView) findViewById(R.id.tv1);

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

        Button timer = (Button) findViewById(R.id.testSophie);
        timer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, TestActivity.class);
                startActivity(k);
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

        Button prefs = (Button) findViewById(R.id.prefs_button);
        assert prefs != null;
        prefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, PrefsActivity.class);
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

    /** Service de timer*/
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
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    /** Initialise le récepteur de message broadcast */
    @Override
    public void onResume()
    {
        IntentFilter movementFilter;
        movementFilter = new IntentFilter(SensorService.TIMER_UPDATE);
        timerReceiver = new TimerServiceReceiver();
        registerReceiver(timerReceiver, movementFilter);
        startTimerService();

        super.onResume();
    }

    /** Démarre le timer */
    private void startTimerService()
    {
        startService(new Intent(this, SensorService.class));
    }

    /** Update le TimeViewer */
    final Runnable myRunnable = new Runnable() {
        public void run() {
            tv.setText(String.valueOf(counter));
        }
    };

    final Handler myHandler = new Handler();
    /** Update le TimeViewer du compteur grâce au Runnable */
    private void UpdateGUI() {
        myHandler.post(myRunnable);
    }

    //----------------
    public class TimerServiceReceiver extends BroadcastReceiver
    {
        /** Reçoit les messages broadcast par les services */
        @Override
        public void onReceive(Context context, Intent intent)
        {
            counter = intent.getIntExtra(SensorService.COUNTER, 0);
            UpdateGUI();
        }
    }
}
