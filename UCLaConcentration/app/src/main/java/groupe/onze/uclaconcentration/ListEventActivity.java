package groupe.onze.uclaconcentration;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import groupe.onze.uclaconcentration.dataBaseMan.DatabaseHandler;
import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.EventPersoAdapter;


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
       // db.addEvent(new EventPerso(789,"1994-12-25" , "Volley",new eventTime(18,30) , new eventTime(20,30), "hihi"));
       // db.addEvent(new EventPerso(478,"1992-12-25" , "Volloy",new eventTime(18,30) , new eventTime(20,30), "haha"));
        List<EventPerso> listeD = db.getAllEvents();

        EventPersoAdapter adapter = new EventPersoAdapter(this, listeD);
        adapter.addListener(this);
        ListView list = (ListView) findViewById(R.id.ListView4);
        list.setAdapter(adapter);
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
    public int getLayoutResource() {
        return R.layout.activity_main;
    }
}

