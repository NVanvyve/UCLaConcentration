package groupe.onze.uclaconcentration;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

public class DummyMenu extends BasicActivity {
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
    int sportDelay ;
    int sportSnooze ;
    int counterMemo;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_menu);

        mPrefs = getSharedPreferences("label",0);
        mEditor = mPrefs.edit();

        if (mPrefs.getBoolean("graphical", true))
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        tv = (TextView) findViewById(R.id.tv1);
        typeTimer = (TextView) findViewById(R.id.type_of_timer);

        mContext = this;

        lastSportTime = mPrefs.getInt("lastSportTime",0);
        dialogOnScreen = false;
        sportDelay = mPrefs.getInt("sportDelay", 3600) ; // Par defaut : 1h
        sportSnooze = mPrefs.getInt("sportSnooze",60); // Par defaut : 1 min

        final Button play = (Button) findViewById(R.id.button_play);
        assert play != null;
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMyServiceRunning(SensorService.class)) {
                    Toast.makeText(mContext,getResources().getString(R.string.stopping_chrono),Toast.LENGTH_LONG).show();
                    Intent serviceIntent = new Intent(mContext,SensorService.class);
                    mContext.stopService(serviceIntent);
                    play.setText(R.string.play);
                    mPrefs = getSharedPreferences("label",0);
                    int time = mPrefs.getInt("counterSeconds",0);
                    mEditor.putInt("lastSportTime",0).commit();
                    addCoins(time);
                } else {
                    mSensorService = new SensorService(mContext);
                    mServiceIntent = new Intent(mContext,mSensorService.getClass());
                    if (!isMyServiceRunning(mSensorService.getClass())) {
                        startService(mServiceIntent);
                    }
                    typeTimer.setText(R.string.timer_timer);
                    tv.setText("0");
                    play.setText(R.string.stop);
                    lastSportTime = mPrefs.getInt("lastSportTime",0);
                }

            }
        });

        pause = (Button) findViewById(R.id.button_pause);
        assert pause != null;
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SensorService.class)) {
                    if (!onPause) {
                        Toast.makeText(mContext,getResources().getString(R.string.pausing_chrono),Toast.LENGTH_LONG).show();
                        onPause = true;
                        typeTimer.setText(R.string.pause_timer);
                        tv.setText(" ");
                        pause.setText(R.string.resume);
                    } else {
                        Toast.makeText(mContext,getResources().getString(R.string.resuming_chrono),Toast.LENGTH_LONG).show();
                        onPause = false;
                        tv.setText(" ");
                        typeTimer.setText(R.string.timer_timer);
                        pause.setText(R.string.pause);
                        lastSportTime = mPrefs.getInt("lastSportTime",0);
                    }
                } else {
                    Toast.makeText(mContext,getResources().getString(R.string.no_timer_active),Toast.LENGTH_LONG).show();
                }

            }
        });

        //Acces au store
        Button store = (Button) findViewById(R.id.button_store);
        assert store != null;
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(DummyMenu.this,StoreActivity.class);
                startActivity(s);
            }
        });

        // Acces à ADE
        Button ade = (Button) findViewById(R.id.ade_button);
        assert ade != null;
        ade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(DummyMenu.this,AdeActivity.class);
                startActivity(s);
            }
        });


        Button event = (Button) findViewById(R.id.event_button);
        assert event != null;
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(DummyMenu.this,NewEventActivity.class);
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

        // Ne sert à rien
        /*
        Button face = (Button) findViewById(R.id.connexion_button);
        assert face != null;
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, Connexion.class);
                startActivity(s);
            }
        });*/


        Button sport = (Button) findViewById(R.id.sport_button);
        assert sport != null;
        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miseEnPause();
                Intent s = new Intent(DummyMenu.this,Sport.class);
                startActivity(s);
            }
        });


    }

    /**
     * Service de timer
     */
    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;

    //public Context getCtx() {return ctx;}

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
                Intent s = new Intent(DummyMenu.this,StoreActivity.class);
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
        try {
            stopService(mServiceIntent);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
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

        if (mPrefs.getBoolean("graphical", true))
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

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
            /*
            if (counter == 10) {//Envoie une notif après 10 secondes d'écoulées
                context = getApplicationContext();
                if (!onPause) {
                    NotificationsSys.sendNotif(context,getResources().getString(R.string.TIME_OUT)
                            ,getResources().getString(R.string.more_10),MainActivity.class);
                } else {
                    NotificationsSys.sendNotif(context,getResources().getString(R.string.end_pause)
                            ,getResources().getString(R.string.more_10),MainActivity.class);
                }
            }
            */
            if (!dialogOnScreen && !onPause && (counter - lastSportTime >= sportDelay)) {
                counterMemo = counter;
                showDialogBox();
            }
            UpdateGUI();
        }
    }

    private void showDialogBox() {
        dialogOnScreen = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("il est temps de se bouger un peu le cul")
                .setTitle("Alerte Sport");

        builder.setPositiveButton("OK. Je me bouge",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {

                // Mise en mémoire du timer pour
                final SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putInt("lastSportTime",counter).commit();

                miseEnPause();

                //Lancer l'activité Sport
                dialogOnScreen = false;
                startActivity(new Intent(DummyMenu.this,Sport.class));

            }
        });

        builder.setNegativeButton("Fuck it!! Je snooze",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                lastSportTime = lastSportTime + counter - counterMemo + sportSnooze;
                dialogOnScreen = false;
                Toast.makeText(getApplicationContext(),"Prochain rappel dans " + timeFormat(sportSnooze) + ".",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addCoins(int time) {
        int procraCoins = (time / PERIOD) * RATE + mPrefs.getInt("save_coins",0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putInt("save_coins",procraCoins).commit();
    }


    private void miseEnPause(){
        if (isMyServiceRunning(SensorService.class)) {
            if (!onPause) {
                onPause = true;
                typeTimer.setText(R.string.pause_timer);
                tv.setText(" ");
                pause.setText(R.string.resume);
            }
            Toast.makeText(mContext,"Le timer de pause vient de s'activer.",Toast.LENGTH_LONG).show();
        }

    }

}
