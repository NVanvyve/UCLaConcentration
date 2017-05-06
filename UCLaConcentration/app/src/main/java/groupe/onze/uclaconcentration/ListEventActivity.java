package groupe.onze.uclaconcentration;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.List;
import groupe.onze.uclaconcentration.objetPerso.DatabaseHandler;
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
        //db.addEvent(new EventPerso(789,"1994-12-25" , "Volley",new eventTime(18,30) , new eventTime(20,30), "hihi"));
       // db.addEvent(new EventPerso(478,"1992-12-25" , "Volloy",new eventTime(18,30) , new eventTime(20,30), "haha"));
        List<EventPerso> listeD = db.getAllEvents();

        adapter = new EventPersoAdapter(this, listeD);
        adapter.addListener(this);
        ListView list = (ListView) findViewById(R.id.ListView4);
        list.setAdapter(adapter);

    }

    //TODO Lorsque l'utilisateur clique sur le nom de l'event, il le supprime
    public void onClickNom(EventPerso item, int position) {
        boolean bool=db.deleteEvent(position);
        if (bool) {Log.v("Deleted?","YES");}
        Log.v("ONCLICKNOM","delete");
        finish();
        /*
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/

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