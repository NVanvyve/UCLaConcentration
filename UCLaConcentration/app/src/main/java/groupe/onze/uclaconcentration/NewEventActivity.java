package groupe.onze.uclaconcentration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import groupe.onze.uclaconcentration.dataBaseMan.DatabaseHandler;
import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.eventTime;

/**
 * Created by X on 20-04-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class NewEventActivity extends BasicActivity {
    Button btn;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    DatabaseHandler db = new DatabaseHandler(NewEventActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add);
        MainActivity.isInBackground=false;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        Button rdv = (Button) findViewById(R.id.deleteEvent);
        rdv.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent3 = new Intent(NewEventActivity.this,ListEventActivity.class);
                startActivity(intent3);
            }
        });
        Button newEvent = (Button) findViewById(R.id.newEvent);
        newEvent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent3 = new Intent(NewEventActivity.this,EventActivity.class);
                startActivity(intent3);
            }
        });


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
