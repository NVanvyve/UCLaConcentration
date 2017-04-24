package groupe.onze.uclaconcentration;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import groupe.onze.uclaconcentration.dataBaseMan.DatabaseHandler;
import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.eventTime;


public class EventActivity extends BasicActivity {

    private SharedPreferences mPrefs;
    private TextView money;

    private long last_purchase;
    private int procraCoins;
    private int delayLastPurchase;
    private SharedPreferences.Editor mEditor;

    private Random rand;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    EditText datePick;
    EditText datePick2;
    EditText startPick;
    EditText endPick;
    DatabaseHandler db = new DatabaseHandler(EventActivity.this);

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        MainActivity.isInBackground = false;
        datePick = (EditText) findViewById(R.id.datePick);
        startPick = (EditText) findViewById(R.id.startPick);
        endPick = (EditText) findViewById(R.id.endPick);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        startPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startPick.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        endPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endPick.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        datePick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText namePick = (EditText) findViewById(R.id.nameEvent);
                EditText descPick = (EditText) findViewById(R.id.descPick);
                EventPerso newEvent = new EventPerso(0, datePick.getText().toString(), namePick.getText().toString(), eventTime.stringToEventTime(startPick.getText().toString()), eventTime.stringToEventTime(endPick.getText().toString()), descPick.getText().toString());
                db.addEvent(newEvent);
                Log.v("Added",newEvent.getEventDate());
                Toast.makeText(EventActivity.this, "Event added", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datePick2 = (EditText) findViewById(R.id.datePick);
        datePick2.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onPause(){
        MainActivity.isInBackground=true;
        super.onPause();
    }
    @Override
    public void onDestroy(){
        MainActivity.isInBackground=true;
        super.onDestroy();
    }
    @Override
    public void onResume(){
        MainActivity.isInBackground=false;
        super.onResume();
    }
    @Override
    public void onStop(){
        MainActivity.isInBackground=true;
        super.onStop();
    }

    private void update() {
        mEditor.putInt("save_coins",procraCoins).apply();
        mEditor.putLong("last_purchase",last_purchase).apply();
        mEditor.putInt("delayLastPurchase",delayLastPurchase).apply();
    }



    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
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
                Intent s = new Intent(this,MainActivity.class);
                startActivity(s);
                return true;

            case R.id.action_recompense:
                Intent so = new Intent(this,StoreActivity.class);
                startActivity(so);
                return true;

            case R.id.credits:
                Outils.showCredits(this);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
