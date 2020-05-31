package com.example.aftermealdiary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NotificationService extends Service {

    ServiceHandler serviceHandler;
    ServiceThread serviceThread;

    NotificationManager notificationManager;
    NotificationChannel channel;
    NotificationCompat.Builder notificationBuilder;

    String channelName = "serviceNotification"; // 푸시 그룹들로 묶인 채널을 사용자가 받을지 안받을지 제어할 수 있도록 하는 역할
    String description = "setNotification";
    int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌


    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        serviceHandler = new ServiceHandler();
        serviceThread = new ServiceThread(serviceHandler);
        serviceThread.start();
        serviceThread.stopForever();*/

        return START_STICKY; // Service가 강제로 종료되었을 경우, 시스템이 다시 Service를 재시작하면서 intent 값을 null로 초기화
        // return START_NOT_STICKY; // Service가 강제로 종료되었을 경우, 시스템이 다시 Service를 재시작하지 않음
        // return START_REDELIVER_INTENT; // Service가 강제로 종료되었을 경우, 시스템이 다시 Service를 재시작하면서 intent 값을 그대로 유지
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onDestroy() { // 서비스가 종료될 때 할 작업
        serviceHandler = new ServiceHandler();
        serviceThread = new ServiceThread(serviceHandler);
        serviceThread.start();

        Log.d("디버깅", "NotificationService - onDestroy(): 서비스 종료됨");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    class ServiceHandler extends Handler {

        public void handleMessage(android.os.Message msg) {
            Intent notificationIntent = new Intent(NotificationService.this, HomeActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Log.d("디버깅", "ServiceHandler - handleMessage(): ServiceHandler 실행됨");

            // 노티피케이션 설정
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "default");
            notificationBuilder.setAutoCancel(true)
                    .setSmallIcon(R.drawable.circle_orange)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                   // .setWhen(System.currentTimeMillis() + 5000) // 앱 종료 5초 후 실행
                    .setTicker("")
                    .setContentTitle("오늘도 식사 잘 챙기셨나요?")
                    .setContentText("앱에 매일의 식사를 기록해보세요!")
                    .setContentInfo("INFO")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);

            // 노티피케이션 채널 설정
            channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            // 노티피케이션 매니저 설정
            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {

                // 노티피케이션 동작시킴
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(0, notificationBuilder.build());
            }

            // 노티피케이션 시간 설정
            Calendar notificationTime = Calendar.getInstance();
            notificationTime.setTimeInMillis(System.currentTimeMillis() + 5000);

            // 알람매니저 설정
            setAlarmManager(notificationTime, pendingIntent);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        void setAlarmManager(Calendar alarmTime, PendingIntent pendingIntent) {

            // 알람매니저 설정
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Log.d("디버깅", "ServiceHandler - setAlarmManager(): AlarmManager 설정됨");

            if (alarmManager != null) {
                // pendingIntent를 포함해서 알람매니저에 저장
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);

            } else {
                Log.d("디버깅", "NotificationService - setAlarmManager(): alarmManager == null");
            }
        }

        @Override
        public void publish(LogRecord record) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };


}
