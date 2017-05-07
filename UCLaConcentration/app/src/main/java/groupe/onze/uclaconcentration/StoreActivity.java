package groupe.onze.uclaconcentration;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class StoreActivity extends BasicActivity {

    private SharedPreferences mPrefs;
    private TextView money;

    private long last_purchase;
    private int procraCoins;
    private int delayLastPurchase;
    private SharedPreferences.Editor mEditor;

    private Random rand;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        money = (TextView) findViewById(R.id.var_coin);


        // Récupération de la mémoire
        mPrefs = getSharedPreferences("label",0);
        procraCoins = mPrefs.getInt("save_coins",0);
        last_purchase = mPrefs.getLong("last_purchase",0);
        delayLastPurchase = mPrefs.getInt("delayLastPurchase",0);
        money.setText(": " + procraCoins + " P");
        mEditor = mPrefs.edit();

/*
        //////////////////////////////////////////////////////////////////////////////
        //DEBUG

        Button more = (Button) findViewById(R.id.moremoney);
        Button reset = (Button) findViewById(R.id.reset_button);

        rand = new Random(System.currentTimeMillis());
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procraCoins += 50 + rand.nextInt(50);
                update();
                money.setText(": " + procraCoins + " P");
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procraCoins = 0;
                last_purchase = 0;
                update();
                money.setText(": " + procraCoins + " P");
            }
        });
        //////////////////////////////////////////////////////////////////////////////
*/

        // Récompenses 
        // Boutons et Prix associés
        Button[] ButtonList = {
                (Button) findViewById(R.id.button20min),
                (Button) findViewById(R.id.button40min),
                (Button) findViewById(R.id.button60min),
                (Button) findViewById(R.id.buttonConf)};

        // Durée de la pause confession
        final int confDuration = 5;
        TextView conf_tv = (TextView) findViewById(R.id.store_conf_tv);
        conf_tv.setText("UConfession : " + confDuration + " min");

        final int[] CostList = {
                60,
                130,
                200,
                6 * confDuration};

        // Initialisation des prix sur les boutons
        for (int i = 0; i < ButtonList.length; i++) {
            ButtonList[i].setText(CostList[i] + " P");
        }

        // Méthode onClick() pour tous les boutons
        ButtonList[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell(CostList[0],20,false);
            }
        });
        ButtonList[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell(CostList[1],40,false);
            }
        });
        ButtonList[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell(CostList[2],60,false);
            }
        });
        ButtonList[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell(CostList[3],confDuration,true);
                //Toast.makeText(getApplicationContext(),"Indisponible pour le moment, Facebook vient de faire une mise à jour",Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onStop(){
        super.onStop();
    }

    private void update() {
        mEditor.putInt("save_coins",procraCoins).apply();
        mEditor.putLong("last_purchase",last_purchase).apply();
        mEditor.putInt("delayLastPurchase",delayLastPurchase).apply();
    }

    private void sell(int cost,int minute,boolean confession) {

        String notEnoughMoney = getResources().getString(R.string.lack_money);
        String waitPurchase = getResources().getString(R.string.wait_purchase);

        double waitFactor = 1.5;
        int waiting = (int) Math.floor(minute * 60 * 1000 * waitFactor);

        if (cost <= (procraCoins) && ((System.currentTimeMillis() - last_purchase) >= waiting)) {
            showDialogBoxSell(cost,minute,confession);

        } else if (cost >= procraCoins) {
            Toast.makeText(this,notEnoughMoney,Toast.LENGTH_SHORT).show();
        } else {
            int time = (int) (delayLastPurchase * waitFactor - (System.currentTimeMillis() - last_purchase) / 1000);
            String wait = waitPurchase + Outils.timeFormat(time) + getString(R.string.wait_purchase2);
            Toast.makeText(this,wait,Toast.LENGTH_SHORT).show();
        }

    }

    private void showDialogBoxSell(final int prix,final int minute,final boolean confession) {
        if (mPrefs.getBoolean("SellAvert",true)) {

            String positiveText = getString(R.string.yes);
            String negativeText = getString(R.string.no);
            String neutralText = getString(R.string.sell_and_mask);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.achat);
            builder.setMessage(getString(R.string.dialog_sell_mess_1)
                    + prix + "P.\n" +
                    getString(R.string.dialog_sell_mess_2) + positiveText + getString(R.string.dialog_sell_mess_3)
                    + negativeText
                    + getString(R.string.dialog_sell_mess_4));
            builder.setCancelable(false);
            builder.setPositiveButton(positiveText,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    microSell(prix,minute,confession);
                }
            });

            builder.setNegativeButton(negativeText,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                }
            });

            builder.setNeutralButton(neutralText,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    mEditor.putBoolean("SellAvert",false);
                    microSell(prix,minute,confession);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            microSell(prix,minute,confession);
        }

    }

    private void microSell(int prix,int minute,boolean confession) {

        if (confession && Outils.checkFaceConnexion()) {
            showDialogBoxConf();
        } else {

            last_purchase = System.currentTimeMillis();
            procraCoins -= prix;
            delayLastPurchase = minute * 60;
            update();
            money.setText(": " + procraCoins + " P");

            if (confession) {
                mEditor.putLong("conf_begin",last_purchase).apply();
                Intent s = new Intent(this,UConfessions.class);
                s.putExtra("time_limit",minute);
                startActivity(s);
            }
        }
    }

    private void showDialogBoxConf() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_conf_title);
        builder.setMessage(R.string.dialog_conf_message);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.dialog_conf_positive,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                startActivity(new Intent(StoreActivity.this,SettingsActivity.class));
            }
        });

        builder.setNegativeButton(R.string.dialog_conf_negative,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
                Intent s = new Intent(this,MainActivity.class);
                startActivity(s);
                return true;

            case R.id.action_recompense:
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


}
