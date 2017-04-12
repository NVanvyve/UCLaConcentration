package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by nicolasvanvyve.
 */

public class Conf extends BasicActivity {

    String paging_token;
    String message;
    String until;
    final String TAG = "CONF";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_conf);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Log.i(TAG,"init");

        paging_token = null;
        until = null;
        final TextView conf_tv = (TextView) findViewById(R.id.textView);

        Log.i(TAG,"-------------------------------------------------------------------------");
        Log.i(TAG,"message = " + message);
        Log.i(TAG,"token = " + paging_token);
        Log.i(TAG,"until = " + until);
        confRequest();
        Log.i(TAG,"message = " + message);
        conf_tv.setText(message);


        Button new_conf = (Button) findViewById(R.id.next_conf);
        assert new_conf != null;
        new_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"-------------------------------------------------------------------------");
                Log.i(TAG,"message = " + message);
                Log.i(TAG,"token = " + paging_token);
                Log.i(TAG,"until = " + until);
                confRequest();
                conf_tv.setText(message);
            }
        });

    }

    private void confRequest() {
        if (!Outils.isConnectedInternet(Conf.this)) {
            message = "No internet connection";
        }
        else{
            final String[] ret = new String[2];

            Bundle parameters = new Bundle();

            parameters.putString("limit","1");

            if (paging_token != null) {
                parameters.putString("__paging_token",paging_token);
            } else {
                Log.i(TAG,"paging_token is null");
            }

            GraphRequest request = GraphRequest.newGraphPathRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/967219966645066/feed",
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            JSONObject jo = response.getJSONObject();
                            JSONArray retJa;
                            try {
                                retJa = jo.getJSONArray("data");
                                JSONObject retJaElem1 = retJa.getJSONObject(0);
                                try {
                                    ret[0] = String.valueOf(retJaElem1.get("message"));
                                    message = ret[0];
                                } catch (JSONException ex) {
                                    Log.i(TAG,"JSONException : " + ex.getMessage());
                                    message = "Erreur lors du chargement de la confession :@";
                                }
                                JSONObject pa = jo.getJSONObject("paging");
                                ret[1] = String.valueOf(pa.get("next"));
                                if (ret[1] != null) {
                                    String match = "__paging_token=";
                                    String match_2 = "until=";
                                    int index = ret[1].lastIndexOf(match);
                                    int index_2 = ret[1].lastIndexOf(match_2);
                                    paging_token = ret[1].substring(index + 15);
                                    int index_3 = paging_token.indexOf("&");
                                    if (index_3 > 0) {
                                        paging_token = paging_token.substring(0,index_3);
                                    }
                                    until = ret[1].substring(index_2 + 6,index_2 + 6 + 10);
                                }
                            } catch (JSONException e) {
                                Log.i(TAG,e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
            if (paging_token != null) {
                parameters.putString("__paging_token",paging_token);
                parameters.putString("until",until);
            }
            request.setParameters(parameters);
            request.executeAsync();
        }
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
