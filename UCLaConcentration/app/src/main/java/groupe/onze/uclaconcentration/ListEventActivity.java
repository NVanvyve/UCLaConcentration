package groupe.onze.uclaconcentration;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import groupe.onze.uclaconcentration.dataBaseMan.DAOB;
import groupe.onze.uclaconcentration.dataBaseMan.DatabaseHandler;
import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.EventPersoAdapter;
import groupe.onze.uclaconcentration.objetPerso.eventTime;



public class ListEventActivity extends Activity implements EventPersoAdapter.DispoAdapterListener {
    DatabaseHandler db = new DatabaseHandler(ListEventActivity.this);
    EventPersoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        //EXAMPLE
        //List<EventPerso> listeD = EventPerso.getAListOfEventPerso();
        db.addEvent(new EventPerso(789,"1994-12-25" , "Volley",new eventTime(18,30) , new eventTime(20,30), "hihi"));
        db.addEvent(new EventPerso(478,"1992-12-25" , "Volloy",new eventTime(18,30) , new eventTime(20,30), "haha"));
        List<EventPerso> listeD = db.getAllEvents();

        EventPersoAdapter adapter = new EventPersoAdapter(this, listeD);
        adapter.addListener(this);
        ListView list = (ListView) findViewById(R.id.ListView4);
        list.setAdapter(adapter);
    }

    public void onClickNom(EventPerso item, int position) {

        Toast.makeText(getApplicationContext(), "Youpie", Toast.LENGTH_LONG);
        finish();
    }
}

