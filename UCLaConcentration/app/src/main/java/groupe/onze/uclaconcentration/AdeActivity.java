package groupe.onze.uclaconcentration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by nicolasvanvyve on 5/03/17.
 */

public class AdeActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ade_layout);

        SharedPreferences mPrefs = getSharedPreferences("label", 0);
        String codes = mPrefs.getString("codes_cours","");

        WebView browser = (WebView) findViewById(R.id.ade_view);

        browser.getSettings().setJavaScriptEnabled(true);
        String url = "http://horairev6.uclouvain.be/direct/index.jsp?displayConfName=webEtudiant&showTree=false&showOptions=false&login=etudiant&password=student&projectId=21&code="+codes+"&weeks=";
        browser.loadUrl(url);
    }
}