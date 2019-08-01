package com.example.demotaskmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class TaskReminder extends BroadcastReceiver {

    int notifReqCode = 123;

    @Override
    public void onReceive(Context context, Intent i) {

        int id = i.getIntExtra("id", -1);
        String name = i.getStringExtra("name");
        String desc = i.getStringExtra("desc");

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context , MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context , 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new
                NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,
                desc,
                pendingIntent).build();

        NotificationCompat.WearableExtender extender = new
                NotificationCompat.WearableExtender();
        extender.addAction(action);

        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(context , "default");
        builder.setContentText(desc);
        builder.setContentTitle(name);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        // Attach the action for Wear notification created above
        builder.extend(extender);

        Notification notification = builder.build();

        notificationManager.notify(id, notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel("default", "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("This is for default notification");
            notificationManager.createNotificationChannel(channel);
        }
    }

}