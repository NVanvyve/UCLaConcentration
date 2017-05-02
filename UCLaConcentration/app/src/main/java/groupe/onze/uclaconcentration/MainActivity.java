package groupe.onze.uclaconcentration;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BasicActivity {
    private ImageAdapter mAdapter;
    private static boolean onPlay = false;

    private String typeTimerString;

    private TimerServiceReceiver timerReceiver;
    private int counter;
    private TextView tv;
    public static boolean onPause = false;
    public static boolean isInBackground=false;
    private Context mContext;
    private TextView typeTimer;
    private boolean dialogOnScreen;

    private final int PERIOD = 60;
    private final int RATE = 1;

    private int lastSportTime;
    private int sportDelay;
    private int sportSnooze;
    private int counterMemo;

    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(!BuildConfig.DEBUG)
        {
            Fabric.with(this, new Crashlytics());
            Log.i("CRASH","Crashlytics ENABLE  && BuildConfig.DEBUG = "+BuildConfig.DEBUG);
        }else{
            Log.i("CRASH","Crashlytics NOT ENABLE  && BuildConfig.DEBUG = "+BuildConfig.DEBUG);
        }
        setContentView(R.layout.activity_main);
        isInBackground=false;

        mContext = this;

        mPrefs = getSharedPreferences("label",0);
        mEditor = mPrefs.edit();



        if(!mPrefs.getBoolean("tuto", false)){
            Log.i("TUTO","Start");
            startActivity(new Intent(this,SwipeTuto.class));
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        DisplayMetrics screen = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screen);
        int width = screen.widthPixels;
        mAdapter = new ImageAdapter(this, onPlay, onPause, width);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,View v,int position,long id) {
                launch(position);
            }
        });

        lastSportTime = mPrefs.getInt("lastSportTime",0);
        dialogOnScreen = false;
        sportDelay = mPrefs.getInt("sportDelay",60); // Par defaut : 1h
        sportSnooze = mPrefs.getInt("sportSnooze",60); // Par defaut : 1 min

    }


    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
        isInBackground=false;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        tv = (TextView) findViewById(R.id.tv1);
        typeTimer = (TextView) findViewById(R.id.type_of_timer);

        DisplayMetrics screen = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screen);
        int width = screen.widthPixels;

        GridView gridview = (GridView) findViewById(R.id.gridview);
        mAdapter = new ImageAdapter(this, onPlay, onPause, width);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,View v,int position,long id) {
                launch(position);
            }
        });

        if (onPlay) {
            typeTimer.setText(typeTimerString);
            int time = mPrefs.getInt("counterSeconds",0);
            tv.setText(" " + time);
        } else
            typeTimer.setText(R.string.no_timer_active);

    }

    private void launch(int pos) {
        Context context = mContext;
        Intent s;
        isInBackground=false;
        switch (pos) {
            case 0:
                onPlay = !onPlay;
                if (isMyServiceRunning(SensorService.class)) {

                    Toast.makeText(context,getResources().getString(R.string.stopping_chrono),Toast.LENGTH_LONG).show();
                    Intent serviceIntent = new Intent(mContext,SensorService.class);
                    context.stopService(serviceIntent);
                    int time = mPrefs.getInt("counterSeconds",0);
                    mEditor.putInt("lastSportTime",0).apply();
                    addCoins(time);
                    onPause = false;
                } else {
                    SensorService mSensorService = new SensorService(context);
                    mServiceIntent = new Intent(mContext,mSensorService.getClass());
                    if (!isMyServiceRunning(mSensorService.getClass())) {
                        startService(mServiceIntent);
                    }
                    typeTimerString = getResources().getString(R.string.timer_timer);
                    tv.setText("0");
                }
                onStart();
                break;
            case 1:
                if (isMyServiceRunning(SensorService.class)) {
                    if (!onPause) {
                        Toast.makeText(context,getResources().getString(R.string.pausing_chrono),Toast.LENGTH_LONG).show();
                        onPause = true;
                        typeTimerString = getResources().getString(R.string.pause_timer);
                        tv.setText(" ");
                    } else {
                        Toast.makeText(context,getResources().getString(R.string.resuming_chrono),Toast.LENGTH_LONG).show();
                        onPause = false;
                        tv.setText(" ");
                        typeTimerString = getResources().getString(R.string.timer_timer);
                        lastSportTime = mPrefs.getInt("lastSportTime",0);
                    }
                } else {
                    Toast.makeText(context,getResources().getString(R.string.no_timer_active),Toast.LENGTH_LONG).show();
                }
                onStart();
                break;
            case 2:
                s = new Intent(MainActivity.this,ADE.class);
                startActivity(s);
                break;
            case 3:
                s = new Intent(MainActivity.this,NewEventActivity.class);
                startActivity(s);
                break;
            case 4:
                s = new Intent(MainActivity.this,StoreActivity.class);
                //miseEnPause();
                startActivity(s);
                break;
            case 5:
                //miseEnPause();
                s = new Intent(MainActivity.this,Sport.class);
                //miseEnPause();
                startActivity(s);
                break;
            default:
                break;
        }
    }


    /**
     * Service de timer
     */
    private Intent mServiceIntent;

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
                startActivity(intent);
                return true;

            case R.id.action_home:
                return true;

            case R.id.action_recompense:
                Intent s = new Intent(MainActivity.this,StoreActivity.class);
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


    @Override
    protected void onDestroy() {
        try {
            stopService(mServiceIntent);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        Log.i("MAINACT","onDestroy!");
        isInBackground=true;
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
        isInBackground=false;
        IntentFilter movementFilter;
        movementFilter = new IntentFilter(SensorService.TIMER_UPDATE);
        timerReceiver = new TimerServiceReceiver();
        registerReceiver(timerReceiver,movementFilter);

        super.onResume();
        isInBackground=false;
    }
    @Override
    public void onPause(){
        //MainActivity.isInBackground=true;
        super.onPause();
    }
    @Override
    public void onStop(){
        //MainActivity.isInBackground=true;
        super.onStop();
    }
    /**
     * Update le TimeViewer
     */
    private final Runnable myRunnable = new Runnable() {
        public void run() {
            tv.setText(Outils.timeFormat(counter));
        }
    };

    private final Handler myHandler = new Handler();

    /**
     * Update le TimeViewer du compteur grâce au Runnable
     */
    private void UpdateGUI() {
        myHandler.post(myRunnable);
        isInBackground=false;
    }

    //----------------
    public class TimerServiceReceiver extends BroadcastReceiver {
        /**
         * Reçoit les messages broadcast par les services
         */
        @Override
        public void onReceive(Context context,Intent intent) {
            counter = intent.getIntExtra(SensorService.COUNTER,0);

            if (!dialogOnScreen && !onPause && (counter - lastSportTime >= sportDelay)) {
                counterMemo = counter;
                showDialogBox();
            }

            UpdateGUI();
        }
    }


    private void addCoins(int time) {
        // TODO : Ajouter des coins à chaque pause --> Memoriser le dernier timer
        int procraCoins = (int) (Math.floor((time / PERIOD) * RATE) + mPrefs.getInt("save_coins",0));
        mEditor.putInt("save_coins",procraCoins).commit();
    }

    private void showDialogBox() {
        dialogOnScreen = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_sport_1) + Outils.timeFormat(counter) + getString(R.string.dialog_sport_2));
        builder.setTitle(R.string.alerte_sport);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.dialog_sport_ok,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {

                // Mise en mémoire du timer pour
                mEditor.putInt("lastSportTime",counter).commit();

                miseEnPause();

                //Lancer l'activité Sport
                dialogOnScreen = false;
                startActivity(new Intent(MainActivity.this,Sport.class));

            }
        });

        builder.setNegativeButton(R.string.dialog_sport_cancel,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                lastSportTime = lastSportTime + counter - counterMemo + sportSnooze;
                dialogOnScreen = false;
                Toast.makeText(getApplicationContext(),"Prochain rappel dans "
                        + Outils.timeFormat(sportSnooze) + ".",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void miseEnPause() {
        if (isMyServiceRunning(SensorService.class)) {
            if (!onPause) {
                onPause = true;
                typeTimer.setText(R.string.pause_timer);
                tv.setText(" ");
                onStart();
            }
            Toast.makeText(mContext,"Le timer de pause vient de s'activer.",Toast.LENGTH_LONG).show();
        }

    }

}
