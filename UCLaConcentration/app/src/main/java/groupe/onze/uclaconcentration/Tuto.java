package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by nicolasvanvyve in avr. 2017.
 */

public class Tuto extends AppCompatActivity {

    SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;
    int i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tuto);

        final ImageView screenImage = (ImageView) findViewById(R.id.screen);
        final Button prev = (Button) findViewById(R.id.prev_button);
        final Button next = (Button) findViewById(R.id.next_button);
        final Button skip = (Button) findViewById(R.id.skip_button);
        assert prev != null;
        assert next != null;
        assert skip != null;

        mPrefs = getSharedPreferences("label",0);
        mEditor = mPrefs.edit();

        final int[] image_tuto = {R.drawable.tuto_firstview,
                                    R.drawable.tuto_menu,
                                    R.drawable.tuto_settings,
                                    R.drawable.tuto_store};

        i = 0;
        screenImage.setImageResource(image_tuto[i]);


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i!=0){
                    i--;
                    screenImage.setImageResource(image_tuto[i]);
                }
                else{
                    Log.i("TUTO","C'est la 1ere image!!");
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i==image_tuto.length){
                    mEditor.putBoolean("tuto",true).commit();
                    startActivity(new Intent(Tuto.this,MainActivity.class));
                }
                else{
                    screenImage.setImageResource(image_tuto[i]);
                }

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = image_tuto.length;
                mEditor.putBoolean("tuto",true).commit();
                startActivity(new Intent(Tuto.this,MainActivity.class));
            }
        });

    }
}