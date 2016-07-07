package vedanshudahiya.present;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class checkAttendance extends Activity {

    DBHelper db;
    ArrayList<class_attendance_item> classAttendanceItems;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_check_attendance);

        db = new DBHelper(getApplicationContext());

        getAttendance();

        classAttendanceAdapter AttAdapter= new classAttendanceAdapter(checkAttendance.this,classAttendanceItems);
        ListView classAttendanceList = (ListView)findViewById(R.id.classAttendanceList);
        classAttendanceList.setAdapter(AttAdapter);

        classAttendanceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Check if data available
                if(db.getAttendanceSheetCount(classAttendanceItems.get(position).id)){
                    Intent intent = new Intent(checkAttendance.this, classAttendanceSheet.class);
                    intent.putExtra("classID",classAttendanceItems.get(position).id);
                    intent.putExtra("className",classAttendanceItems.get(position).name);
                    intent.putExtra("tP",classAttendanceItems.get(position).present);
                    intent.putExtra("tA",classAttendanceItems.get(position).absent);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(checkAttendance.this, "No Attendance Records for '" + classAttendanceItems.get(position).name + "' Yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getAttendance(){
        classAttendanceItems = db.getAttendance();
    }

}
