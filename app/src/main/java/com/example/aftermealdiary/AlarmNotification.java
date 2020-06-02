package com.example.aftermealdiary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;


@RequiresApi(api = Build.VERSION_CODES.N)
public class AlarmNotification extends BroadcastReceiver {

    NotificationChannel channel;
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    String description = "setNotification";
    String channelName = "alarmNotification"; // 푸시 그룹들로 묶인 채널을 사용자가 받을지 안받을지 제어할 수 있도록 하는 역할
    int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        // 노티피케이션 클릭시 홈 액티비티로 이동하는 인텐트 설정
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // notificationBuilder에 포함될 pendingIntent 설정
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 노티피케이션 설정
        notificationBuilder = new NotificationCompat.Builder(context, "default");
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.circle_orange)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("")
                .setContentTitle("설정한 알람 시간입니다")
                .setContentText("잊지 말고 식사 챙기세요!")
                .setContentInfo("INFO")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        // 노티피케이션 채널 설정 - 푸시 그룹들로 묶인 채널을 사용자가 받을지 안받을지 제어할 수 있도록 하는 역할
        channel = new NotificationChannel("default", channelName, importance);
        channel.setDescription(description);

        // 노티피케이션 매니저 설정
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {

            // 노티피케이션 동작시킴
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0, notificationBuilder.build());

            Calendar nextNotifyTime = Calendar.getInstance();

            // 다음날 같은 시간으로 알람시간 결정
            nextNotifyTime.add(Calendar.DATE, 1);
        }

        // 알람 소리를 출력하는 서비스로 인텐트 전달
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startForegroundService(serviceIntent);
    }
}
