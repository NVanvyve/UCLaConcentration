package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nicolasvanvyve on 12/03/17.
 */

public class Conf extends BasicActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_conf);
        Log.v("CONF","Lancement de l'activité");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final JSONObject[] jo = new JSONObject[1];

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/967219966645066/feed",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        jo[0] = response.getJSONObject();
                        JSONArray retJa;
                        try {
                            retJa = jo[0].getJSONArray("data");
                            JSONObject retJaElem1 = retJa.getJSONObject(1);
                            String str = String.valueOf(retJaElem1.get("message"));
                            TextView tv = (TextView) findViewById(R.id.textView);
                            tv.setText(str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        //Bundle parameters = new Bundle();
        //parameters.putString("limit","2");
        //request.setParameters(parameters);
        request.executeAsync();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
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
                finish();
                startActivity(intent);
                return true;

            case R.id.action_home:
                finish();
                return true;

            case R.id.action_recompense:
                Intent s = new Intent(Conf.this,StoreActivity.class);
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
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void finish() {
        Intent s = new Intent(Conf.this,MainActivity.class);
        startActivity(s);
    }


}
