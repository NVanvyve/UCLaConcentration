package groupe.onze.uclaconcentration;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sophie on 12/03/2017.
 */
public class SensorService extends Service {
    public int counter=0;// Compteur

    public static final String TIMER_UPDATE = "com.client.gaitlink.AccelerationService.action.MOVEMENT_UPDATE";
    public static final String COUNTER = "com.client.gaitlink.AccelerationService.ACCELERATION_X";

    /** Méthode pour broadcast les messages aux activités */
    private void announceTimerChanges()//this method sends broadcast messages
    {
        Intent intent = new Intent(TIMER_UPDATE);
        intent.putExtra(COUNTER, counter); // Ajout de la donnée compteur actuel
        sendBroadcast(intent);// Broadcast la donnée
    }

    /* Initialisation du Service de timer */
    public SensorService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public SensorService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    /* Relance le timer en cas d'arrêt non-voulu */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("groupe.onze.uclaconcentration.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();

        initializeTimerTask();// Initialisation du timer

        timer.schedule(timerTask, 1000, 1000); // Tâche à faire toutes les x secondes
    }

    /** A faire toutes les x secondes */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ counter);
                announceTimerChanges();//Envoie à l'activité l'état du compteur
                counter++;//Incrémentation du compteur

            }
        };
    }

    /** Stoppe le timer */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
