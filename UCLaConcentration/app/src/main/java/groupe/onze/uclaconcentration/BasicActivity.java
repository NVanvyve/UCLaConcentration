package groupe.onze.uclaconcentration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public abstract class BasicActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        configureToolbar();
    }

    protected abstract int getLayoutResource();

    private void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    public static String timeFormat(int time) {
        int min = 60;
        int heure = 60 * min;
        String format;

        String h_string = " h ";
        String m_string = " min ";
        String s_string = " sec ";

        if (time % heure == 0) {
            if (time == 0) {
                format = time + s_string;
            } else {
                format = time / heure + h_string;
            }
        } else if (time % min == 0) {
            int h = time / heure;
            int m = (time % heure) / min;
            if (h != 0) {
                format = h + h_string + m + m_string;
            } else {
                format = m + m_string;
            }
        } else {
            int h = time / heure;
            int m = (time % heure) / min;
            int s = (time % min);
            if (h == 0 && m == 0) {
                format = s + s_string;
            } else if (h == 0) {
                format = m + m_string + s + s_string;
            } else {
                format = h + h_string + m + m_string + s + s_string;
            }
        }
        return format;
    }


}