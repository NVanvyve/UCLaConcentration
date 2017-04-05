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
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BasicActivity {
    private ImageAdapter mAdapter;
    private static boolean onPlay = false;

    private String typeTimerString;

    TimerServiceReceiver timerReceiver;
    int counter;
    TextView tv;
    public static boolean onPause = false;
    Context mContext;
    TextView typeTimer;
    Button pause;
    boolean dialogOnScreen;

    public final int PERIOD = 1;
    public final int RATE = 10;

    int lastSportTime;
    int sportDelay;
    int sportSnooze;
    int counterMemo;

    SharedPreferences.Editor mEditor;

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mPrefs = getSharedPreferences("label",0);
        mEditor = mPrefs.edit();

        lastSportTime = mPrefs.getInt("lastSportTime",0);
        dialogOnScreen = false;
        sportDelay = mPrefs.getInt("sportDelay",60); // Par defaut : 1h
        sportSnooze = mPrefs.getInt("sportSnooze",60); // Par defaut : 1 min

        //sportDelay = 15;

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

    public void launch(int pos) {
        Context context = mContext;
        Intent s;
        switch (pos) {
            case 0:
                onPlay = !onPlay;
                if (isMyServiceRunning(SensorService.class)) {

                    Toast.makeText(context,getResources().getString(R.string.stopping_chrono),Toast.LENGTH_LONG).show();
                    Intent serviceIntent = new Intent(mContext,SensorService.class);
                    context.stopService(serviceIntent);
                    mPrefs = getSharedPreferences("label",0);
                    int time = mPrefs.getInt("counterSeconds",0);
                    mEditor.putInt("lastSportTime",0).commit();
                    addCoins(time);
                    onPause = false;
                } else {
                    mSensorService = new SensorService(context);
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
                s = new Intent(MainActivity.this,AdeActivity.class);
                startActivity(s);
                break;
            case 3:
                s = new Intent(MainActivity.this,NewEventActivity.class);
                startActivity(s);
                break;
            case 4:
                s = new Intent(MainActivity.this,StoreActivity.class);
                startActivity(s);
                break;
            case 5:
                miseEnPause();
                s = new Intent(MainActivity.this,Sport.class);
                startActivity(s);
                break;
            case 7:
                Toast.makeText(this,"Coucou",Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT","onDestroy!");
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
        registerReceiver(timerReceiver,movementFilter);

        super.onResume();
    }

    /**
     * Update le TimeViewer
     */
    final Runnable myRunnable = new Runnable() {
        public void run() {
            tv.setText(timeFormat(counter));
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
        int procraCoins = (time / PERIOD) * RATE + mPrefs.getInt("save_coins",0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putInt("save_coins",procraCoins).commit();
    }

    private void showDialogBox() {
        dialogOnScreen = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_sport_1) + timeFormat(counter) + getString(R.string.dialog_sport_2));
        builder.setTitle(R.string.alerte_sport);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.dialog_sport_ok,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {

                // Mise en mémoire du timer pour
                final SharedPreferences.Editor mEditor = mPrefs.edit();
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
                Toast.makeText(getApplicationContext(),"Prochain rappel dans " + timeFormat(sportSnooze) + ".",Toast.LENGTH_SHORT).show();
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
