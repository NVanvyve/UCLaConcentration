package groupe.onze.uclaconcentration;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by alexis on 21-02-17.
 */

public class NotificationsSys extends Activity {
    /*
    @Pre: Prend en argument le contexte dans lequel la méthode est appelé, Le titre de la notification a envoyer, la description de la notification et la classe a charger dans le cas ou l'utilisateur clique sur la notification
    @Post Crée une notifcation qui contiendra les informations données en paramètre
     */
    public static void sendNotif(Context mContext,String title,String Description,Class classToLoad) {
//Récupération du notification Manager
        final NotificationManager mNotification = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        final Intent launchNotifiactionIntent = new Intent(mContext,classToLoad);
        launchNotifiactionIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        final PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                1,launchNotifiactionIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(mContext)
                .setWhen(System.currentTimeMillis())
                .setTicker(title)
                .setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle(title)
                .setContentText(Description)
                .setContentIntent(pendingIntent);

        mNotification.notify(1,builder.build());

    }
}
