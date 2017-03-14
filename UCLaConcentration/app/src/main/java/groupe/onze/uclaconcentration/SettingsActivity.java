package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends BasicActivity {

    SharedPreferences mPrefs;
    int sportLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.welcome_param), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/


        mPrefs = getSharedPreferences("label", 0);
        sportLevel = mPrefs.getInt("sport_level",0);

        Spinner spinner = (Spinner) findViewById(R.id.sport_spin);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sport_level_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(sportLevel);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        Button prefs = (Button) findViewById(R.id.cursus_button);
        assert prefs != null;
        prefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(SettingsActivity.this, PrefsActivity.class);
                startActivity(s);
            }
        });
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                   long id) {


            SharedPreferences.Editor mEditor = mPrefs.edit();
            sportLevel = pos;
            mEditor.putInt("sport_level", pos).commit();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
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
            case R.id.home :
                finish(); // close this activity and return to preview activity (if there is any)

            case R.id.action_settings:
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.already_param), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;

            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.action_recompense:
                startActivity(new Intent(this, StoreActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
