package groupe.onze.uclaconcentration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        setContentView(R.layout.layout_ade);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences mPrefs = getSharedPreferences("label", 0);

        String codes = "";
        String intitules [] = {"programme","majeure","mineure"};

        for (int i = 0;i<intitules.length;i++){
            String ref = mPrefs.getString(intitules[i],"");
            if (i==0 && ref.equals("")){
                Context context = getApplicationContext();
                String text = getString(R.string.avertissement_pas_de_cours);
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
            if (!ref.equals("")) {
                if (codes.equals("")) {
                    codes = ref;
                } else {
                    codes = codes + "," + ref;
                }
            }
        }

        String cours_supp = mPrefs.getString("cours_supp","");
        if (!cours_supp.equals("")){
            codes = codes+","+cours_supp;
        }

        if(!codes.equals("")) {
            WebView browser = (WebView) findViewById(R.id.ade_view);
            browser.getSettings().setJavaScriptEnabled(true);
            String url = "http://horairev6.uclouvain.be/direct/index.jsp?displayConfName=webEtudiant&showTree=false&showOptions=false&login=etudiant&password=student&projectId=21&code="
                    + codes + "&weeks=";
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
                finish();
                startActivity(intent);

            case R.id.action_home:
                Intent t = new Intent(this, MainActivity.class);
                finish();
                startActivity(t);

            case R.id.action_recompense:
                Intent s = new Intent(this, StoreActivity.class);
                finish();
                startActivity(s);

            default:
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