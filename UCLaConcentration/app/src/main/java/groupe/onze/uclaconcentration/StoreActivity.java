package groupe.onze.uclaconcentration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class StoreActivity extends AppCompatActivity {

    SharedPreferences mPrefs;

    TextView money;
    Context context;

    CharSequence text = "You don't have enough money";
    CharSequence text2 = "You must wait 10 seconds between 2 purchases";
    int duration = 1;
    int waiting;
    long last_purchase;
    int procraCoins;

    Random rand;

    private void update(){
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putInt("save_coins", procraCoins).commit();
    }

    private void sell(int cost){
        if(cost<=(procraCoins) && ((System.currentTimeMillis() - last_purchase) >= waiting)) {
            last_purchase = System.currentTimeMillis();
            procraCoins -= cost;
            update();
            money.setText(procraCoins+" P");;
        }
        else if(cost>= procraCoins){
            context = getApplicationContext();
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            context = getApplicationContext();
            Toast toast = Toast.makeText(context, text2, duration);
            toast.show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //test alexis notif
        //context=getApplicationContext();
        //NotificationsSys.sendNotif(context,"test title store","test description store",MainActivity.class);

        waiting = 10000;
        rand = new Random(System.currentTimeMillis());
        money = (TextView) findViewById(R.id.var_coin);
        Button more = (Button) findViewById(R.id.moremoney);
        Button reset = (Button) findViewById(R.id.reset_button);

        // Récupération de la mémoire
        mPrefs = getSharedPreferences("label", 0);
        procraCoins = mPrefs.getInt("save_coins",0);
        money.setText(procraCoins+" P");

        // Récompenses 
        // Boutons et Prix associés
        Button [] ButtonList = {
                (Button) findViewById(R.id.button20min),
                (Button) findViewById(R.id.button40min),
                (Button) findViewById(R.id.button60min)};

        final int [] CostList = {
                150,
                350,
                600};


        // Initialisation des prix sur les boutons
        for (int i = 0; i<ButtonList.length; i++){
            ButtonList[i].setText(CostList[i]+ " P");
        }
        
        // "More" et "Reset" sont des boutons temporaire qui permettent de tester le fonctionemment du Store
        // A supprimer une fois qu'il existe un systeme pour générer des points
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procraCoins += 20 + rand.nextInt(30);
                update();
                money.setText(procraCoins+" P");
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procraCoins = 0;
                update();
                money.setText(procraCoins+" P");
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
}
