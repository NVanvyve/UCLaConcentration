package groupe.onze.uclaconcentration;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nicolasvanvyve on 19/03/17.
 */

public class Sport extends BasicActivity {


    double latitude;
    double longitude;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sport);

        //GPS

        gps = new GPSTracker(this,Sport.this);
        final double latlong [] = gps.giveMeLatLong();
        latitude = latlong[0];
        longitude = latlong[1];

        TextView coord = (TextView) findViewById(R.id.textView1);
        coord.setText("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);

        // "SPORT"

        Button newPosition = (Button) findViewById(R.id.new_position);
        assert newPosition != null;
        newPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double np[] = gps.newLocation(100);
                int dist = (int) gps.distance(latitude, longitude, np[0],np[1]);
                Toast.makeText(getApplicationContext(),"Distance = "+dist+" m" ,Toast.LENGTH_LONG).show();

            }
        });

    }




    // TOOLBAR
    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                finish();
                startActivity(intent);

            case R.id.action_home:
                Intent t = new Intent(Sport.this, MainActivity.class);
                finish();
                startActivity(t);

            case R.id.action_recompense:
                Intent s = new Intent(Sport.this, StoreActivity.class);
                finish();
                startActivity(s);

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





}