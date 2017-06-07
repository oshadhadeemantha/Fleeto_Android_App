package com.example.thilinidineshika.mapdirection;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by OshadhaDeemantha on 3/20/2017.
 */

public class FirebaseMassagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String,String> map =  remoteMessage.getData();
        showNotification(remoteMessage.getData().get("message"));
       }

    private void showNotification(String massage) {
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(this)

                .setAutoCancel(true)
                .setContentTitle("Fleeto Notification")
                .setContentText(massage)
               // .setStyle(new NotificationCompat.BigTextStyle().bigText("Now you are in fleet management system"))
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }

}

