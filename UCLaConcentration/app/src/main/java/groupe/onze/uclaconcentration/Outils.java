package groupe.onze.uclaconcentration;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.facebook.Profile;

/**
 * Created by nicolasvanvyve in avr. 2017.
 */

public class Outils {

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

    public static boolean isConnectedInternet(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
        {
            NetworkInfo.State networkState = networkInfo.getState();
            return networkState.compareTo(NetworkInfo.State.CONNECTED) == 0;
        }
        else return false;
    }

    public static boolean checkFaceConnexion(){
        Profile profile = Profile.getCurrentProfile();
        if (profile==null){
            Log.i("FACEBOOK","profile = null");
            return false;
        }
        else{
            Log.i("FACEBOOK",profile.toString());
            return true;
        }
    }

}
