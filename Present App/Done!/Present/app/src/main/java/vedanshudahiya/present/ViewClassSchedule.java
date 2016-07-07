package vedanshudahiya.present;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedaytimepicker.SlideDayTimeListener;
import com.github.jjobes.slidedaytimepicker.SlideDayTimePicker;

import java.util.ArrayList;


public class ViewClassSchedule extends FragmentActivity {
    ArrayList<class_item> classItems;
    DBHelper db;
    classListAdapter newScheduleAdapter;
    Long CCID;
    String CCN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class_schedule);
        db=new DBHelper(getApplicationContext());
        CCN=getIntent().getExtras().getString("className");
        CCID=getIntent().getExtras().getLong("classID");
        String STD=getIntent().getExtras().getString("classSTD");

        TextView scn= (TextView)findViewById(R.id.SingleClassName);
        scn.setText(CCN);
        TextView std= (TextView)findViewById(R.id.sfDate);
        std.setText(class_attendance_sheet_item.formatDate(STD));

        ListView ScheduleList = (ListView)findViewById(R.id.classScheduleList);
        classItems =new ArrayList<class_item>();

        getClassItems();

        newScheduleAdapter= new classListAdapter(ViewClassSchedule.this,classItems);
        ScheduleList.setAdapter(newScheduleAdapter);
    }

    public void getClassItems(){
        classItems.clear();
        classItems.addAll(db.getSchedule(CCID));
    }

    public void addSchedule(View v){
        if(classItems.size()==50)
            Toast.makeText(ViewClassSchedule.this,"Already 50 schedules added per week...I don't think we can handle more of this class !",Toast.LENGTH_LONG).show();
        SlideDayTimeListener listener = new SlideDayTimeListener() {

            @Override
            public void onDayTimeSet(int day, int hour, int minute)
            {
                ConfirmSchedule(day-1, hour, minute);
            }

            @Override
            public void onDayTimeCancel()
            {
                Toast.makeText(ViewClassSchedule.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        };

        new SlideDayTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDay(1)
                .setInitialHour(12)
                .setInitialMinute(00)
                .build()
                .show();
    }

    public void ConfirmSchedule(int day,int hour,int minute){
        final int DFday=day,DFhour=hour,DFminute=minute;
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_add)
                .setTitle("Confirm")
                .setMessage("Add Schedule :\n\nDay - "+class_item.dayToString(DFday)+"\nTime - "+class_item.timeToString(DFhour,DFminute))
                .setPositiveButton("No",null)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<class_item> classItemONE =new ArrayList<class_item>();
                        classItemONE.add(new class_item(DFday, DFhour, DFminute));
                        db.createSchedule(CCID, classItemONE);

                        getClassItems();
                        //Notify Change to Adapter
                        newScheduleAdapter.notifyDataSetInvalidated();
                        newScheduleAdapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    public void destroyClass(View v){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle("Attention !")
                .setMessage("Are you sure?\n\nThis will remove all data related to class '"+CCN+"' including Attendance Sheet !")
                .setPositiveButton("No", null)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteClass(CCID);
                        finish();
                    }
                })
                .show();
    }
}