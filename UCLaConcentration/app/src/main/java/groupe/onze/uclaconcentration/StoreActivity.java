package groupe.onze.uclaconcentration;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class StoreActivity extends Activity {

    TextView money;
    int coins;
    Button more;
    Button serie20;
    Button serie40;
    Button serie60;
    Context context;
    CharSequence text = "You don't have enough money";
    CharSequence text2 = "You must wait 10 seconds between 2 purchases";
    int duration = 2;
    int waiting;
    long last_purchase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        //test alexis notif
        context=getApplicationContext();
        NotificationsSys.sendNotif(context,"test title store","test description store",MainActivity.class);
        //
        coins = 0;
        waiting = 10000;



        money = (TextView) findViewById(R.id.var_coin);
        more = (Button) findViewById(R.id.moremoney);
        serie20 = (Button) findViewById(R.id.button20min);
        serie40 = (Button) findViewById(R.id.button40min);
        serie60 = (Button) findViewById(R.id.button60min);

        money.setText(coins+"");

        more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                coins = coins + 50;
                money.setText(coins+"");
            }
        });

        serie20.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int cost  = 150;
                if(cost<=coins && ((System.currentTimeMillis() - last_purchase)>=waiting)) {
                    last_purchase = System.currentTimeMillis();
                    coins = coins - cost;
                    money.setText(coins + "");
                }
                else if(cost>=coins){
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
                if(cost<=coins && ((System.currentTimeMillis() - last_purchase)>=waiting)) {
                    last_purchase = System.currentTimeMillis();
                    coins = coins - cost;
                    money.setText(coins + "");
                }
                else if(cost>=coins){
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
                if(cost<=coins && ((System.currentTimeMillis() - last_purchase)>=waiting)) {
                    last_purchase = System.currentTimeMillis();
                    coins = coins - cost;
                    money.setText(coins + "");
                }
                else if(cost>=coins){
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
