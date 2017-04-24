package groupe.onze.uclaconcentration;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import groupe.onze.uclaconcentration.dataBaseMan.DatabaseHandler;
import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.EventPersoAdapter;
import groupe.onze.uclaconcentration.objetPerso.eventTime;


public class ListEventActivity extends BasicActivity implements EventPersoAdapter.DispoAdapterListener {
    DatabaseHandler db = new DatabaseHandler(ListEventActivity.this);
    EventPersoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        MainActivity.isInBackground=false;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //EXAMPLE
        //List<EventPerso> listeD = EventPerso.getAListOfEventPerso();
        //db.addEvent(new EventPerso(789,"1994-12-25" , "Volley",new eventTime(18,30) , new eventTime(20,30), "hihi"));
       // db.addEvent(new EventPerso(478,"1992-12-25" , "Volloy",new eventTime(18,30) , new eventTime(20,30), "haha"));
        List<EventPerso> listeD = db.getAllEvents();

        EventPersoAdapter adapter = new EventPersoAdapter(this, listeD);
        adapter.addListener(this);
        ListView list = (ListView) findViewById(R.id.ListView4);
        list.setAdapter(adapter);

       /* ImageView imageVif = (ImageView) findViewById(R.id.imageVif);
        imageVif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               //TODO DELETE EVENT
            }
        });
*/
       /*
        TextView descr = (TextView) findViewById(R.id.descr);
        descr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Toast.makeText(ListEventActivity.this, , Toast.LENGTH_LONG).show();
            }
        });
        */
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

    public void onClickNom(EventPerso item, int position) {

        //Toast.makeText(getApplicationContext(), "Youpie", Toast.LENGTH_LONG);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                Intent intento = new Intent(this,StoreActivity.class);
                startActivity(intento);
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

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }
}

