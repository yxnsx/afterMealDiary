package com.example.aftermealdiary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service {

    ServiceThread serviceThread;
    Notification serviceNotification ;
    NotificationManager notificationManager;


    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
