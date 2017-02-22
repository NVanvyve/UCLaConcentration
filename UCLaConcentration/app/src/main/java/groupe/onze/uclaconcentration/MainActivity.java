package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static int procraCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variable pour stocker les coins
        procraCoins = 0;

        //Acces au store
        Button store = (Button) findViewById(R.id.button_store);
        assert store != null;
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, StoreActivity.class);
                startActivity(s);
            }
        });

        Button b = (Button) findViewById(R.id.testSophie);
        b.setText("Timer");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(k);
            }
        });
    }
}
