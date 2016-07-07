package vedanshudahiya.present;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BackgroundService extends Service {
    public BackgroundService() {
    }

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private DBHelper db;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
        db=new DBHelper(context);
    }

    private Runnable myTask = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void run() {

            String currDt= new SimpleDateFormat("yyyyMMdd").format(new Date());
            Calendar cal=Calendar.getInstance();
            int cHour = cal.get(Calendar.HOUR_OF_DAY);
            int cMinutes = cal.get(Calendar.MINUTE);

            SharedPreferences settings = getSharedPreferences("PresentVD", 0);
            String fromDate = settings.getString("lastRefreshedDate", "");
            int fromHour = settings.getInt("lastRefreshedHour", 0);
            int fromMinutes = settings.getInt("lastRefreshedMinute",0);

            if(db.updatePending(currDt,cHour,cMinutes,fromDate,fromHour,fromMinutes)){
                settings.edit().putString("lastRefreshedDate",currDt).commit();
                settings.edit().putInt("lastRefreshedHour", cHour).commit();
                settings.edit().putInt("lastRefreshedMinute", cMinutes).commit();
            }

            if(db.DataAlreadyInDB("PendingAttendance","'1'","1")){
                Intent intent = new Intent(context, dashboard.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(dashboard.class);
                stackBuilder.addNextIntent(intent);

                PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification n  = new Notification.Builder(context)
                        .setContentTitle("Mark Attendance")
                        .setContentText("New Class Instances Available !")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .setSound(getAlarmUri())
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .build();
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(0, n);
            }
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

    private Uri getAlarmUri() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // Originally notification and alarm were switched, but my alarm gets annoying very fast. =b
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
}
