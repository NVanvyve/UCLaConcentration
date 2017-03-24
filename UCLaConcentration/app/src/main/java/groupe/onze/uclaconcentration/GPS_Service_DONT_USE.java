package groupe.onze.uclaconcentration;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Nicolas on  24/03/2017.
 */
public class GPS_Service_DONT_USE extends Service {


    //public boolean onPause = false;
    public GPSTracker gps;
    private double latitude;
    private double longitude;

    public static final String service_name = "GPS";
    public static final String latitude_string = "LAT";
    public static final String longitude_string = "LONG";

    /**
     * Méthode pour broadcast les messages aux activités
     */
    private void announceTimerChanges(double lat, double lon)//this method sends broadcast messages
    {
        Intent intent = new Intent(service_name);
        intent.putExtra(latitude_string,lat);
        intent.putExtra(longitude_string,lon);
        sendBroadcast(intent);// Broadcast la donnée
    }

    /* Initialisation du Service de timer */
    public GPS_Service_DONT_USE(Context context,Activity activity) {
        super();
        gps = new GPSTracker(context,activity);

    }

    public GPS_Service_DONT_USE() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        super.onStartCommand(intent,flags,startId);

        startTimer();

        return START_STICKY;
    }

    /* Relance le timer en cas d'arrêt non-voulu */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT","ondestroy!");
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();// Initialisation du timer
        timer.schedule(timerTask,1000,1000); // Tâche à faire toutes les x
    }

    /**
     * A faire toutes les x secondes
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                double location [] = gps.giveMeLatLong();
                latitude = location[0];
                longitude = location [1];
                Log.i("Mise à jour du timer","Lat : "+latitude+" Longitude : "+longitude);
                announceTimerChanges(latitude,longitude);//Envoie à l'activité
            }
        };
    }

    /**
     * Stoppe le timer
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
/*
    public void pauseTimer() {
        Log.i("Pause","pause 1");
        onPause = true;
        Log.i("Pause","pause 2");
        while (this.onPause) {
            Log.i("Pause","pause 3");
            try {
                Log.i("Pause","pause 4");
                this.wait();
            } catch (InterruptedException e) {
                Log.i("Pause","pause erreur");
                onPause = false;
                return;
            }
        }
        Log.i("Pause","sortie");
    }

    public void resumeTimer() {
        onPause = false;
    }
*/
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
