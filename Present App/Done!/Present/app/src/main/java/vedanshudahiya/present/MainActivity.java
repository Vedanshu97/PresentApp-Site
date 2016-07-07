package vedanshudahiya.present;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DBHelper(getApplicationContext());

        SharedPreferences settings = getSharedPreferences("PresentVD", 0);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Calendar cal=Calendar.getInstance();
            int cHour = cal.get(Calendar.HOUR_OF_DAY);
            int cMinutes = cal.get(Calendar.MINUTE);
            String firstDate=new SimpleDateFormat("yyyyMMdd").format(new Date());

            settings.edit().putString("lastRefreshedDate", firstDate).commit();
            settings.edit().putInt("lastRefreshedHour", cHour).commit();
            settings.edit().putInt("lastRefreshedMinute", cMinutes).commit();

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Hello")
                    .setMessage("Hey, New User!\n\nPlease go through \"How To\" to understand the app better." +
                            "\n\nThank You.\n\n-Vedanshu Dahiya")
                    .setPositiveButton("Got It !", null)
                    .show();

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

        registerAlarm(this);
    }

    public void goToAttendance(View view)
    {
        Intent intent = new Intent(MainActivity.this, Attendance.class);
        startActivity(intent);
    }

    public void goToTimeTable(View view)
    {

        Intent intent = new Intent(MainActivity.this, TimeTable.class);
        startActivity(intent);
    }

    public void goToHowTo(View view)
    {
        Intent intent = new Intent(MainActivity.this, HowTo.class);
        startActivity(intent);
    }

    public void goodbye(View v){
        onBackPressed();
    }

    public void onBackPressed(){
        Runnable r = new Runnable() {
            @Override
            public void run(){
                finish();
                System.exit(0);
            }
        };

        Toast.makeText(MainActivity.this,"Goodbye :)",Toast.LENGTH_LONG).show();

        Handler h = new Handler();
        h.postDelayed(r, 1000); // <-- the "1000" is the delay time in miliseconds
    }

    public void registerAlarm(Context currContext){
        Intent alarm = new Intent(currContext, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(currContext, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(currContext, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            int min60=3600000;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),min60,pendingIntent);
        }
    }

}
