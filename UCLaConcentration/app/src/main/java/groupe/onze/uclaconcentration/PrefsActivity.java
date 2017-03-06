package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by nicolasvanvyve on 5/03/17.
 */

public class PrefsActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        final SharedPreferences mPrefs = getSharedPreferences("label", 0);
        final EditText ed_codes = (EditText) findViewById(R.id.ed_codes);

        ed_codes.setText(mPrefs.getString("codes_cours", null));

        Button save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("codes_cours",ed_codes.getText().toString()).commit();
                Intent s = new Intent(PrefsActivity.this, MainActivity.class);
                startActivity(s);
            }
        });
    }
}