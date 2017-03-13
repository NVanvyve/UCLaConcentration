package groupe.onze.uclaconcentration;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.content.ContentValues.TAG;
import static android.util.Log.VERBOSE;
import static groupe.onze.uclaconcentration.R.id.text;
import static groupe.onze.uclaconcentration.R.id.title;

/** ACTUELLEMENT CETTE CLASSE EST USELESS **/


/* LE TEMPS ECOULE DEPUIS QUE LE TIMER A ETE LANCE EST RECUPERABLE PAR:
*  mPrefs = getSharedPreferences("label", 0);
   int seconds = mPrefs.getInt("counterSeconds", "0");
   int minutes = mPrefs.getInt("counterMinutes", "0");
* */
public class TimerActivity extends AppCompatActivity {
    MenuItem countdownTimer;
    private BroadcastReceiver uiUpdated;
    TextView timerTextView;
    TextView textView2;
    int seconds;
    int minutes;
    long startTime = 0;
    SharedPreferences mPrefs;
    Context context;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    int procraCoins = 0;
    final int RATE = 100;  // Coins per seconds
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            if(seconds>10) {//Envoie une notif après 10 secondes d'écoulées
                context=getApplicationContext();
                NotificationsSys.sendNotif(context,"TIME OUT","plus de 10sec",TimerActivity.class);
            }
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putInt("CounterSeconds",seconds);
            mEditor.putInt("CounterMinutes",minutes);
            mEditor.commit();
            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        countdownTimer = menu.findItem(R.id.action_favorite);
        return true;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);



/////////////////////

        registerReceiver(uiUpdated, new IntentFilter("COUNTDOWN_UPDATED"));
//Log.d("SERVICE", "STARTED!");


        uiUpdated = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //This is the part where I get the timer value from the service and I update it every second, because I send the data from the service every second. The coundtdownTimer is a MenuItem
                countdownTimer.setTitle(intent.getExtras().getString("countdown"));

            }
        };

        /////////////////////////////////////////


        timerTextView = (TextView) findViewById(R.id.textViewtime);
        /* Affiche le temps actuel écoulé*/
        mPrefs = getSharedPreferences("label", 0);
        int seconds = mPrefs.getInt("seconds", 0);//0=def value
        int minutes = mPrefs.getInt("minutes", 0);//0=def value
        String count = String.format("%d:%02d", minutes, seconds);
        //textView2 = (TextView) findViewById(R.id.timetotal);
        //textView2.setText(count);
        procraCoins = mPrefs.getInt("save_coins",0);

        Button b = (Button) findViewById(R.id.buttonTimer);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;

                if (b.getText().equals("stop")) {
                    addCoins();
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }
            }
        });

        Button c = (Button) findViewById(R.id.testSophieBack);
        c.setText(R.string.back);
        c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) findViewById(R.id.buttonTimer);
                if (b.getText().equals("stop"))
                    addCoins();
                finish();
            }
        });
    }

    public void addCoins(){
        mPrefs = getSharedPreferences("label", 0);
        int seconds = mPrefs.getInt("counterSeconds", 0) + 60 * mPrefs.getInt("counterMinutes", 0);
        seconds ++;
        TextView c = (TextView) findViewById(R.id.test1);
        c.setText("Procrastinacoins récupérés : " + seconds * RATE);
        procraCoins += seconds * RATE;
        update();
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "++ ON START ++");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "+ ON RESUME +");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "- ON DESTROY -");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "- ON PAUSE -");
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.buttonTimer);
        b.setText("start");
    }

    private void update(){
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putInt("save_coins", procraCoins).commit();
    }

}