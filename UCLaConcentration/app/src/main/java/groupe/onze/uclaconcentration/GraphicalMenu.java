package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class GraphicalMenu extends BasicActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.content_graphical_menu);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                launch(position);
            }
        });
    }

    public void launch(int pos) {
        switch (pos) {
            case 0:
                Toast.makeText(this, "Coucou", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(this, "Coucou", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(this, "Coucou", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(this, "Coucou", Toast.LENGTH_LONG).show();
                break;
            case 4:
                Toast.makeText(this, "Coucou", Toast.LENGTH_LONG).show();
                break;
            case 5:
                Toast.makeText(this, "Coucou", Toast.LENGTH_LONG).show();
                break;
            case 6:
                Toast.makeText(this, "Coucou", Toast.LENGTH_LONG).show();
                break;
            case 7:
                Toast.makeText(this, "Coucou", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.home :
                finish(); // close this activity and return to preview activity (if there is any)

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_home:
                Intent i2 = new Intent(this, MainActivity.class);
                finish();
                startActivity(i2);
                return true;

            case R.id.action_recompense:
                Intent s = new Intent(this, StoreActivity.class);
                startActivity(s);
                return true;

            case R.id.help:
                String help[] = getResources().getStringArray(R.array.help_main);
                for (int i = 0; i < help.length; i++) {
                    Toast.makeText(this,help[i],Toast.LENGTH_LONG).show();
                    //Snackbar.make(findViewById(android.R.id.content), help[i],
                    //      Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

}
