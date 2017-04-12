package groupe.onze.uclaconcentration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import groupe.onze.uclaconcentration.dataBaseMan.DatabaseHandler;
import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.eventTime;

/**
 * Created by X on 20-04-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class NewEventActivity extends Activity {
    Button btn;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    DatabaseHandler db = new DatabaseHandler(NewEventActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add);
        showDialogOnButtonClick();



        Button rdv = (Button) findViewById(R.id.deleteEvent);
        rdv.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent3 = new Intent(NewEventActivity.this,ListEventActivity.class);
                startActivity(intent3);
            }
        });


    }

    public void showDialogOnButtonClick() {

        btn = (Button) findViewById(R.id.buttoncalendar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);

            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
        }
        return null;


    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x = i;
            month_x = i1;
            day_x = i2;
            StringBuffer sb = new StringBuffer();
            sb.append(i);
            sb.append("-");
            sb.append(i1+1);
            sb.append("-");
            sb.append(i2);
            String str1= sb.toString();
            Log.v("ICIIIII",str1);
            String str2= "1994-06-22";
            EventPerso newEvent = new EventPerso(1, str1, "bonjour", new eventTime(8,30), new eventTime(9,30), "hehe");
            db.addEvent(newEvent);
            Log.v("Added",newEvent.getEventDate());
            Log.v("Added",newEvent.getEventDescr());
            Toast.makeText(NewEventActivity.this, year_x + " /" + month_x+1 + "/" + day_x, Toast.LENGTH_LONG).show();
        }
    };


}
