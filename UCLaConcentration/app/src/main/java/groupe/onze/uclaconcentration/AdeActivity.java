package groupe.onze.uclaconcentration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by nicolasvanvyve on 5/03/17.
 */

public class AdeActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ade_layout);

        SharedPreferences mPrefs = getSharedPreferences("label", 0);
        String codes = mPrefs.getString("codes_cours","vide");

        if (codes=="vide"){
            Context context = getApplicationContext();
            String text = getString(R.string.Avertissement_pas_de_decours);
            Toast toast = Toast.makeText(context, text, 1);
            toast.show();
            
            //Intent s = new Intent(AdeActivity.this, MainActivity.class);
            //startActivity(s);
        }
        else {
            WebView browser = (WebView) findViewById(R.id.ade_view);

            browser.getSettings().setJavaScriptEnabled(true);
            String url = "http://horairev6.uclouvain.be/direct/index.jsp?displayConfName=webEtudiant&showTree=false&showOptions=false&login=etudiant&password=student&projectId=21&code=" + codes + "&weeks=";
            browser.loadUrl(url);
        }
    }
}