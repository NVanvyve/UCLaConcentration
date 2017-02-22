package groupe.onze.uclaconcentration;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class StoreActivity extends AppCompatActivity {

    TextView money;
    Button more;
    Button serie20;
    Button serie40;
    Button serie60;
    Context context;
    CharSequence text = "You don't have enough money";
    CharSequence text2 = "You must wait 10 seconds between 2 purchases";
    int duration = 1;
    int waiting;
    long last_purchase;

    Random rand;


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

        money.setText(MainActivity.procraCoins+"");

        more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // En attendant une conversion du temps en coins
                MainActivity.procraCoins += 20 + rand.nextInt(30);
                money.setText(MainActivity.procraCoins+"");;
            }
        });

        serie20.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int cost  = 150;
                if(cost<=(MainActivity.procraCoins) && ((System.currentTimeMillis() - last_purchase)>=waiting)) {
                    last_purchase = System.currentTimeMillis();
                    MainActivity.procraCoins -= cost;
                    money.setText(MainActivity.procraCoins+"");;
                }
                else if(cost>= MainActivity.procraCoins){
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
        });

        serie40.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int cost  = 350;
                if(cost<=(MainActivity.procraCoins) && ((System.currentTimeMillis() - last_purchase)>=waiting)) {
                    last_purchase = System.currentTimeMillis();
                    MainActivity.procraCoins -= cost;
                    money.setText(MainActivity.procraCoins+"");;
                }
                else if(cost>=(MainActivity.procraCoins)){
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
        });

        serie60.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int cost  = 600;
                if(cost<=(MainActivity.procraCoins) && ((System.currentTimeMillis() - last_purchase)>=waiting)) {
                    last_purchase = System.currentTimeMillis();
                    MainActivity.procraCoins -=cost;
                    money.setText(MainActivity.procraCoins+"");;
                }
                else if(cost>=(MainActivity.procraCoins)){
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
        });
    }
}
