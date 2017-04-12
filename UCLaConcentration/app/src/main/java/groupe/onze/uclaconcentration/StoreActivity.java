package groupe.onze.uclaconcentration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class StoreActivity extends BasicActivity {

    SharedPreferences mPrefs;

    TextView money;
    Context context;

    CharSequence text;
    CharSequence text2;
    int duration = Toast.LENGTH_SHORT;
    int waiting;
    long last_purchase;
    int procraCoins;

    Random rand;

    private void update() {
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putInt("save_coins",procraCoins).apply();
        mEditor.putLong("last_purchase",last_purchase).commit();
    }

    private void sell(int cost) {
        if (cost <= (procraCoins) && ((System.currentTimeMillis() - last_purchase) >= waiting)) {
            last_purchase = System.currentTimeMillis();
            procraCoins -= cost;
            update();
            money.setText(": " + procraCoins + " P");
        } else if (cost >= procraCoins) {
            context = getApplicationContext();
            Toast toast = Toast.makeText(context,text,duration);
            toast.show();
        } else {
            context = getApplicationContext();
            Toast toast = Toast.makeText(context,text2,duration);
            toast.show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        text = getResources().getString(R.string.lack_money);
        text2 = getResources().getString(R.string.wait_purchase);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        waiting = 10000;
        rand = new Random(System.currentTimeMillis());
        money = (TextView) findViewById(R.id.var_coin);
        Button more = (Button) findViewById(R.id.moremoney);
        Button reset = (Button) findViewById(R.id.reset_button);

        // Récupération de la mémoire
        mPrefs = getSharedPreferences("label",0);
        procraCoins = mPrefs.getInt("save_coins",0);
        last_purchase = mPrefs.getLong("last_purchase",0);
        money.setText(": "+procraCoins + " P");

        // Récompenses 
        // Boutons et Prix associés
        Button[] ButtonList = {
                (Button) findViewById(R.id.button20min),
                (Button) findViewById(R.id.button40min),
                (Button) findViewById(R.id.button60min)};

        final int[] CostList = {
                150,
                350,
                600};


        // Initialisation des prix sur les boutons
        for (int i = 0; i < ButtonList.length; i++) {
            ButtonList[i].setText(CostList[i] + " P");
        }

        // "More" et "Reset" sont des boutons temporaire qui permettent de tester le fonctionemment du Store
        // A supprimer une fois qu'il existe un systeme pour générer des points
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procraCoins += 20 + rand.nextInt(30);
                update();
                money.setText(": "+procraCoins + " P");
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procraCoins = 0;
                update();
                money.setText(": "+procraCoins + " P");
            }
        });


        // Méthode onClick() pour tous les boutons... Une solution plus compacte est la bien venue
        ButtonList[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell(CostList[0]);
            }
        });
        ButtonList[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell(CostList[1]);
            }
        });
        ButtonList[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell(CostList[2]);
            }
        });
        
        /*
        // Ajout d'un nouveau bouton?
        ButtonList[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell(CostList[i]);
            }
        });
        */

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)

            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_home:
                Intent s = new Intent(StoreActivity.this,MainActivity.class);
                startActivity(s);
                return true;

            case R.id.action_recompense:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
