package groupe.onze.uclaconcentration;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by nicolasvanvyve.
 */

public class UConfessions extends BasicActivity {

    private String paging_token;
    private String message;
    private String until;
    private final String TAG = "CONF";
    private SharedPreferences mPrefs;
    private long conf_begin;
    private int time_limit;
    private boolean dialogOnScreen;
    private SharedPreferences.Editor mEditor;
    private int count_null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_conf);
        MainActivity.isInBackground=false;

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Outils.checkFaceConnexion()){
            Toast.makeText(getApplicationContext(),"Vous n'etes pas connecter à Facebook.\n" +
                    "Normalement vous n'auriez jamais du acceder au UConfessions.\nTRICHEUR VA!! :)",Toast.LENGTH_LONG).show();
            //startActivity(new Intent(this,MainActivity.class));
        }

        mPrefs = getSharedPreferences("label",0);
        mEditor = mPrefs.edit();

        dialogOnScreen = false;
        count_null = 0;

        int time_limit_init = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            time_limit_init = bundle.getInt("time_limit",10);  // Limte de temps en minute
        } else {
            Log.i(TAG,"bundle == null");
        }
        time_limit = time_limit_init * 60 * 1000; //Milisec

        conf_begin = mPrefs.getLong("conf_begin",System.currentTimeMillis());


        paging_token = null;
        until = null;
        final TextView conf_tv = (TextView) findViewById(R.id.textView);

        Log.i(TAG,"-------------------------------------------------------------------------");
        Log.i(TAG,"message = " + message);
        //Log.i(TAG,"token = " + paging_token);
        //Log.i(TAG,"until = " + until);
        confRequest();
        conf_tv.setText(message);


        Button new_conf = (Button) findViewById(R.id.next_conf);
        assert new_conf != null;
        final int finalTime_limit_init = time_limit_init;
        new_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"-------------------------------------------------------------------------");
                Log.i(TAG,"message = " + message);
                Log.i(TAG,"token = " + paging_token);
                Log.i(TAG,"until = " + until);
                confRequest();

                //Reload activity if request fail
                if (message==null){
                    count_null++;
                    if (count_null==3){
                        Log.i(TAG,"3 times message=null");
                        Intent s = new Intent(UConfessions.this,UConfessions.class);
                        s.putExtra("time_limit",finalTime_limit_init);
                        startActivity(s);
                    }
                }else {
                    count_null = 0;
                }

                conf_tv.setText(message);
                checkTime();
            }
        });

    }

    private void checkTime() {
        long now = System.currentTimeMillis();
        long duration = now - conf_begin;
        Log.i(TAG,"Time = " + Outils.timeFormat((int) (duration / 1000)));
        if (duration >= time_limit) {
            Log.i(TAG,"Time exceeded");
            if (!dialogOnScreen) {
                showDialogBox();
            }
        }
    }

    private void showDialogBox() {
        dialogOnScreen = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        int limit = time_limit / 1000; // En sec
        builder.setMessage("Vous regardez des confessions depuis plus de " + Outils.timeFormat(limit) + ". C'est bien assez, il est temps de retourner bosser");
        builder.setTitle("Time exceeded");
        builder.setCancelable(false);

        builder.setPositiveButton("Back to work",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialogOnScreen = false;
                Log.i(TAG,"Back to work");
                startActivity(new Intent(UConfessions.this,MainActivity.class));
            }
        });
/*
        builder.setNegativeButton(R.string.dialog_sport_cancel,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {

            }
        });
*/
        AlertDialog dialog = builder.create();
        dialog.show();
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

    private void confRequest() {
        if (!Outils.isConnectedInternet(UConfessions.this)) {
            message = "No internet connection";
        } else {
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
                Intent s = new Intent(UConfessions.this,StoreActivity.class);
                finish();
                startActivity(s);

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

    @Override
    public void finish() {
        Intent s = new Intent(UConfessions.this,MainActivity.class);
        startActivity(s);
    }


}
