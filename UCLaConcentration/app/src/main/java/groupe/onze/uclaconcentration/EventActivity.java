package groupe.onze.uclaconcentration;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.content.Intent;

import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import groupe.onze.uclaconcentration.objetPerso.DatabaseHandler;
import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.eventTime;


public class EventActivity extends BasicActivity {


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
        datePick = (EditText) findViewById(R.id.datePick);
        startPick = (EditText) findViewById(R.id.startPick);
        endPick = (EditText) findViewById(R.id.endPick);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Selectionne l'heure de début
        startPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
                mTimePicker.setTitle(getResources().getString(R.string.select_time));
                mTimePicker.show();

            }
        });

        //Selectionne l'heure de fin
        endPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endPick.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle(getResources().getString(R.string.select_time));
                mTimePicker.show();

            }
        });

        //Selectionne la date
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        datePick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /*SAUVEGARDE*/
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText namePick = (EditText) findViewById(R.id.nameEvent);
                EditText descPick = (EditText) findViewById(R.id.descPick);
                if(namePick.getText().toString().matches("") || descPick.getText().toString().matches("") || startPick.getText().toString().matches("") || endPick.getText().toString().matches("") || datePick.getText().toString().matches("")){
                    Toast.makeText(EventActivity.this, getResources().getString(R.string.complete_info), Toast.LENGTH_LONG).show();

                }
                else{
                    EventPerso newEvent = new EventPerso(datePick.getText().toString(), namePick.getText().toString(), eventTime.stringToEventTime(startPick.getText().toString()), eventTime.stringToEventTime(endPick.getText().toString()), descPick.getText().toString());
                    db.addEvent(newEvent);
                    Log.v("Added",newEvent.getEventDate());
                    Toast.makeText(EventActivity.this, getResources().getString(R.string.event_add), Toast.LENGTH_LONG).show();
                    finish();

                }
            }
        });
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datePick2 = (EditText) findViewById(R.id.datePick);
        datePick2.setText(sdf.format(myCalendar.getTime()));
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
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onStop(){
        super.onStop();
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
