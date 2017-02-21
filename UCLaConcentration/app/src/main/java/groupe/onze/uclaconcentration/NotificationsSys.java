package groupe.onze.uclaconcentration;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.R;
import android.view.View;

/**
 * Created by alexis on 21-02-17.
 */

public class NotificationsSys extends Activity {
    public static void sendNotif(Context mContext,String title, String Description,Class classToLoad) {
//Récupération du notification Manager
        final NotificationManager mNotification = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        final Intent launchNotifiactionIntent = new Intent(mContext,classToLoad);
        final PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                1, launchNotifiactionIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(mContext)
                .setWhen(System.currentTimeMillis())
                .setTicker(title)
                .setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle(title)
                .setContentText(Description)
                .setContentIntent(pendingIntent);

        mNotification.notify(1, builder.build());

    }
}
