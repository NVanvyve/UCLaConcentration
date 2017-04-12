package groupe.onze.uclaconcentration;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sophie on 12/03/2017.
 */
public class SensorService extends Service {
    public int counter = 0;// Compteur
    public int pauseCounter = 0;// compteur pour la pause
    String serviceName;

    /** Quid de ces Strings?? :) */

    public static final String TIMER_UPDATE = "com.client.gaitlink.AccelerationService.action.MOVEMENT_UPDATE";
    public static final String COUNTER = "com.client.gaitlink.AccelerationService.ACCELERATION_X";
    public boolean onPause = false;

    private SharedPreferences mPrefs;

    /**
     * Méthode pour broadcast les messages aux activités
     */
    private void announceTimerChanges(int time)//this method sends broadcast messages
    {
        Intent intent = new Intent(TIMER_UPDATE);
        intent.putExtra(COUNTER,time); // Ajout de la donnée compteur actuel
        sendBroadcast(intent);// Broadcast la donnée

        if (!onPause)
        {
            mPrefs = getSharedPreferences("label",0);
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putInt("counterSeconds",time).apply();
        }
    }

    /* Initialisation du Service de timer */
    public SensorService(Context context) {
        super();
        Log.i("HERE","here I am!");
    }

    public SensorService() {
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

        timer.schedule(timerTask,0,1000); // Tâche à faire toutes les x
    }

    /**
     * A faire toutes les x secondes
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                if (!MainActivity.onPause) {
                    Log.i("in timer","in timer ++++  " + counter);
                    announceTimerChanges(counter);//Envoie à l'activité l'état du compteur
                    counter++;//Incrémentation du compteur
                    pauseCounter = 0;
                } else {
                    Log.i("in pause","in pause ++++  " + pauseCounter);
                    announceTimerChanges(pauseCounter);//Envoie à l'activité l'état du compteur
                    pauseCounter++;//Incrémentation du compteur

                }

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
