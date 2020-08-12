package com.example.chamanjangra.project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

public class NotificationService extends Service {
    NotificationCompat.Builder mBuilder;
    int mNotificationId = 001;
    GpsTracker location;
    public NotificationService() {
    }
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        location=new GpsTracker(NotificationService.this);
        mBuilder =new NotificationCompat.Builder(this);
        mBuilder.setAutoCancel(true);
        //if(location.getLatitude()>28.546 && location.getLatitude()<28.548 && location.getLongitude()>77.270 && location.getLongitude()<77.272) {
        if (location.getLatitude() > 28.674 && location.getLatitude() < 28.677 && location.getLongitude() > 77.047 && location.getLongitude() < 77.050) {

            mBuilder.setSmallIcon(R.drawable.notification_icon);
            mBuilder.setContentTitle("My notification");
            mBuilder.setContentText("Hello World!");
            mBuilder.setTicker("this is the ticker");
            mBuilder.setWhen(System.currentTimeMillis());


            Intent resultIntent = new Intent(this,Login.class);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
            PendingIntent resultPendingIntent =PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
// Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
