package groupe.onze.uclaconcentration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by nicolasvanvyve on 5/03/17.
 */

public class AdeActivity extends BasicActivity {


    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ade_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences mPrefs = getSharedPreferences("label", 0);
        String codes = mPrefs.getString("codes_cours","");


        if (codes.equals("")) {
            Context context = getApplicationContext();
            String text = getString(R.string.Avertissement_pas_de_decours);
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();

            //Intent s = new Intent(AdeActivity.this, MainActivity.class);
            //startActivity(s);
        } else {
            WebView browser = (WebView) findViewById(R.id.ade_view);

            browser.getSettings().setJavaScriptEnabled(true);
            String url = "http://horairev6.uclouvain.be/direct/index.jsp?displayConfName=webEtudiant&showTree=false&showOptions=false&login=etudiant&password=student&projectId=21&code=" + codes + "&weeks=";
            browser.loadUrl(url);
        }
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
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_home:
                Intent t = new Intent(AdeActivity.this, MainActivity.class);
                startActivity(t);

                return true;

            case R.id.action_recompense:
                Intent s = new Intent(AdeActivity.this, StoreActivity.class);
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