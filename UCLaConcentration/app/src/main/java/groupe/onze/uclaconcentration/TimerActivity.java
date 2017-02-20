package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static groupe.onze.uclaconcentration.R.id.text;
import static groupe.onze.uclaconcentration.R.id.title;

public class TimerActivity extends Activity {

    TextView timerTextView;
    TextView textView2;
    int seconds;
    int minutes;
    long startTime = 0;
    SharedPreferences mPrefs;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerTextView = (TextView) findViewById(R.id.textViewtime);
        mPrefs = getSharedPreferences("label", 0);
        String mString = mPrefs.getString("timeTotal", "0");
        textView2 = (TextView) findViewById(R.id.timetotal);
        textView2.setText(mString);

        Button b = (Button) findViewById(R.id.buttonTimer);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putString("timeTotal", String.format("%d:%02d", minutes, seconds)).commit();
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
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.buttonTimer);
        b.setText("start");
    }

}