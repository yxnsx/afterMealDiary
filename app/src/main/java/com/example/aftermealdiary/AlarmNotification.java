package com.example.aftermealdiary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;


public class AlarmNotification extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, HomeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");


        builder.setSmallIcon(R.drawable.circle_orange);

        String channelName = "afterMealDiary";
        String description = "setNotification";
        int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

        NotificationChannel channel = new NotificationChannel("default", channelName, importance);
        channel.setDescription(description);

        if (notificationManager != null) { // 노티피케이션 채널을 시스템에 등록
            notificationManager.createNotificationChannel(channel);

        } else {
            Log.d("디버깅", "AlarmReceiver - onReceive(): notificationManager == null");
        }

        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("")
                .setContentTitle("설정한 알람 시간입니다")
                .setContentText("잊지 말고 식사 챙기세요!")
                .setContentInfo("INFO")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        if (notificationManager != null) {

            // 노티피케이션 동작시킴
            notificationManager.notify(0, builder.build());

            Calendar nextNotifyTime = Calendar.getInstance();

            // 다음날 같은 시간으로 알람시간 결정
            nextNotifyTime.add(Calendar.DATE, 1);
        }
    }
}
