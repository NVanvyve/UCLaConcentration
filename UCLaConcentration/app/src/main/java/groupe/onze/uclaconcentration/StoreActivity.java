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
    Button more;
    Button serie20;
    Button serie40;
    Button serie60;
    Button reset;
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
        mEditor.putInt("save_coins",procraCoins).commit();
    }

    private void sell(int cost){
        if(cost<=(procraCoins) && ((System.currentTimeMillis() - last_purchase) >= waiting)) {
            last_purchase = System.currentTimeMillis();
            procraCoins -= cost;
            update();
            money.setText(Integer.toString(procraCoins));;
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
        more = (Button) findViewById(R.id.moremoney);
        serie20 = (Button) findViewById(R.id.button20min);
        serie40 = (Button) findViewById(R.id.button40min);
        serie60 = (Button) findViewById(R.id.button60min);
        reset = (Button) findViewById(R.id.reset_button);


        mPrefs = getSharedPreferences("label", 0);
        procraCoins = mPrefs.getInt("save_coins",0);
        money.setText(procraCoins+"");


        more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // En attendant une conversion du temps en coins
                procraCoins += 20 + rand.nextInt(30);
                update();
                money.setText(Integer.toString(procraCoins));
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                procraCoins = 0;
                update();
                money.setText(Integer.toString(procraCoins));
            }
        });

        serie20.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int cost  = 150;
                sell(cost);
            }
        });

        serie40.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int cost  = 350;
                sell(cost);
            }
        });

        serie60.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int cost  = 600;
                sell(cost);
            }
        });
    }
}
