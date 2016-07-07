package vedanshudahiya.present;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class dashboard extends Activity {
    DBHelper db;
    ArrayList<notif_item> Notifs;
    ListView notifList;
    notifAdapter nAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db = new DBHelper(getApplicationContext());

        notifList = (ListView)findViewById(R.id.notifList);

        refreshList(null);
    }

    public void populateUpdateList(){
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
    }

    public void getPendingListData(){
        //add all pending list items to list
        Notifs = db.getPending();

        //if empty say something else
        if(Notifs.size()==0){
            // Write Nothing Pending
            String[] defMsg={"No Pending Tasks !\n\nExpecting Something Here?\n(Try Refreshing)"};
            ListAdapter defAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,defMsg);
            notifList.setAdapter(defAdapter);
        }
        //Display
        else
            setupAdapter();
    }

    public void setupAdapter(){
        nAdapter= new notifAdapter(dashboard.this,Notifs);
        notifList.setAdapter(nAdapter);
    }

    public void refreshList(View v){
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.show();

        populateUpdateList();
        getPendingListData();

        progress.dismiss();
    }

    public void setAttendance(int pos,int val){
        String[] attStr={"Absent","Present","Not Applicable"};
        final int fPos=pos,fVal=val;

        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setIcon(android.R.drawable.ic_input_add)
                .setMessage("Class Name: " + Notifs.get(pos).nClassName
                        + "\nDate : " + class_attendance_sheet_item.formatDate(Notifs.get(pos).nDate)
                        + "\n\n" + attStr[val] + " !")
                .setPositiveButton("No", null)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (db.moveToAttendanceSheet(Notifs.get(fPos).nPID, fVal)) {
                            //Delete from list
                            Notifs.remove(fPos);
                            nAdapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(dashboard.this,"Error Connecting to DB !",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }
}
