package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Menu de choix entre création d'événement et affichage de ceux-ci
 */
@SuppressWarnings("DefaultFileTemplate")
public class NewEventActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Bouton qui mène vers l'affichage d'événements
        Button rdv = (Button) findViewById(R.id.deleteEvent);
        rdv.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent3 = new Intent(NewEventActivity.this,ListEventActivity.class);
                startActivity(intent3);
            }
        });

        //Bouton qui mène vers l'ajout d'un événement
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
