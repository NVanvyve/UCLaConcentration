package groupe.onze.uclaconcentration;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PrefsActivity extends BasicActivity {


    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final SharedPreferences mPrefs = getSharedPreferences("label", 0);
        final EditText ed_codes = (EditText) findViewById(R.id.ed_codes);

        ed_codes.setText(mPrefs.getString("codes_cours", null));

        Button save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("codes_cours", ed_codes.getText().toString()).commit();
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {

            case R.id.action_settings:
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.already_param), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;

            case R.id.action_home:
                Intent t = new Intent(PrefsActivity.this, MainActivity.class);
                finish();
                startActivity(t);
                return true;

            case R.id.action_recompense:
                Intent s = new Intent(PrefsActivity.this, StoreActivity.class);
                finish();
                startActivity(s);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}