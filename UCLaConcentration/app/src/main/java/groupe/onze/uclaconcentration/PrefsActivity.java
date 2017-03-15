package groupe.onze.uclaconcentration;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.LinkedList;


public class PrefsActivity extends BasicActivity {

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        final LinearLayout lc = (LinearLayout) findViewById(R.id.layout_cours);

        final LinkedList<EditText> memory = new LinkedList<>();
        final SharedPreferences mPrefs = getSharedPreferences("label", 0);

        // Les 3 de base que on a toujours
        final EditText programme = (EditText) findViewById(R.id.ed_programme);
        final EditText majeure = (EditText) findViewById(R.id.ed_maj);
        final EditText mineure = (EditText) findViewById(R.id.ed_min);

        // On charge ce qui était en mémoire
        programme.setText(mPrefs.getString("programme", null));
        majeure.setText(mPrefs.getString("majeure", null));
        mineure.setText(mPrefs.getString("mineure", null));

        //Chargement des cours supplémentaires précedement enregistrés
        String save_string = mPrefs.getString("cours_supp",null);
        if (save_string!=null) {
            String delim = ",";
            String[] codes = save_string.split(delim);
            for (int i = 0; i < codes.length; i++) {
                EditText ed = new EditText(getApplicationContext());
                ed.setText(codes[i]);
                lc.addView(ed);
                memory.add(ed);
            }
        }

        //Rajout de cours
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText ed = new EditText(getApplicationContext());
                ed.setHint(R.string.autre_cours);
                lc.addView(ed);
                memory.add(ed);
            }
        });

        //Bouton Save&Back sauvegarde la liste des cours supplémentaire qui ont été utilisé
        Button save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("programme", programme.getText().toString()).commit();
                mEditor.putString("majeure", majeure.getText().toString()).commit();
                mEditor.putString("mineure", mineure.getText().toString()).commit();
                String cours_supp = "";
                while (!memory.isEmpty()) {
                    EditText temp = memory.pop();
                    if (!temp.getText().toString().equals("")) {
                        cours_supp += temp.getText().toString() + ",";
                    }
                }
                cours_supp = cours_supp.substring(0,cours_supp.length()-1);
                mEditor.putString("cours_supp",cours_supp).commit();

                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {

            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_home:
                Intent t = new Intent(PrefsActivity.this, MainActivity.class);
                startActivity(t);
                return true;

            case R.id.action_recompense:
                Intent s = new Intent(PrefsActivity.this, StoreActivity.class);
                startActivity(s);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}