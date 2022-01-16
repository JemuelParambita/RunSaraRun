package com.iotph.paa.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.iotph.paa.R;

import org.jetbrains.annotations.NotNull;

public class FirebaseInstance extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    public void showNotification(String title, String body) {

        NotificationManager notificationManager;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), "rsr_channel");

        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setPriority(Notification.PRIORITY_MAX);

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = "rsr_channel";
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId, title, NotificationManager.IMPORTANCE_HIGH
            );

            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(channelId);

        }

        notificationManager.notify(0, builder.build());


    }
}
