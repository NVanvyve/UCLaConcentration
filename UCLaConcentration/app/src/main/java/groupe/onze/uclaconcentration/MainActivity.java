package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Button timer = (Button) findViewById(R.id.testSophie);
        timer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(k);
            }
        });

        Button ade = (Button) findViewById(R.id.ade_button);
        assert ade != null;
        ade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, AdeActivity.class);
                startActivity(s);
            }
        });

        Button prefs = (Button) findViewById(R.id.prefs_button);
        assert prefs != null;
        prefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, PrefsActivity.class);
                startActivity(s);
            }
        });
    }
}