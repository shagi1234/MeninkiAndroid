package tm.store.meninki.firebase;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;

import tm.store.meninki.R;
import tm.store.meninki.activity.ActivityMain;

public class FirebasePushMessage extends FirebaseMessagingService {
    private FirebaseAuthPreferences account;
    private static final String TAG = "FirebasePushMessage";
    private Notification notification;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        account = FirebaseAuthPreferences.newInstance(this);

    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Log.e(TAG, "handleIntent: " + " title " + intent.getExtras().getString("title") + "\n" + " body " + intent.getExtras().getString("body") + "\n" + " tag " + intent.getExtras().getString("Tag") + "\n" + " image " + intent.getExtras().getString("image") + "\n" + " ticker " + intent.getExtras().getString("Ticker") + "\n" + " ChannelID " + intent.getExtras().getString("ChannelID"));

        if (intent.getExtras().getString("ChannelID") != null)
            getFirebaseMessage(intent.getExtras().getString("title"), intent.getExtras().getString("body"), intent.getExtras().getString("Tag"), intent.getExtras().getString("image"), intent.getExtras().getString("Ticker"));
    }

    @Override
    protected Intent getStartCommandIntent(Intent originalIntent) {
        return super.getStartCommandIntent(originalIntent);

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        account.saveFirebaseToken(s);
    }

    private void getFirebaseMessage(String title, String msg, String type, String imagePath, String ticker) {

        Intent activityIntent = new Intent(getApplicationContext(), ActivityMain.class);

        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        activityIntent.putExtra("notification", type);
        activityIntent.putExtra("json", ticker);

        handleClick();

        PendingIntent contentIntent;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, activityIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.coming_message_sounds);

        GetBitmapToUrl getBitmapToUrl = new GetBitmapToUrl(getApplicationContext()) {
            @Override
            public void onSuccess(Bitmap bm) {

                notification = new NotificationCompat.Builder(getApplicationContext(), "salam")
                        .setSmallIcon(R.drawable.logo_meninki)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo_meninki))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bm))
                        .setContentText(msg).setPriority(NotificationCompat.DEFAULT_SOUND)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setVibrate(new long[]{1500, 1000, 1500, 1000}).setSound(sound)
                        .setColor(getResources().getColor(R.color.contrast))
                        .setAutoCancel(true).setContentIntent(contentIntent).build();

                notificationManagerCompat.notify(1, notification);
            }

            @Override
            public void error() {

                notification = new NotificationCompat.Builder(getApplicationContext(), "meninki")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo_meninki))
                        .setContentTitle(title).setContentText(msg)
                        .setSound(sound)
                        .setPriority(NotificationCompat.DEFAULT_SOUND)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setVibrate(new long[]{1500, 1000, 1500, 1000})
                        .setColor(getResources().getColor(R.color.contrast))
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .build();

                notificationManagerCompat.notify(1, notification);
            }
        };

        if (imagePath != null) {
            getBitmapToUrl.execute(imagePath);
        } else {
            getBitmapToUrl.execute("");
        }
    }

    private void handleClick() {

    }
}

