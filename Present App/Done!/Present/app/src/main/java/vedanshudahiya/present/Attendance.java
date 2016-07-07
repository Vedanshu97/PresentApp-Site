package vedanshudahiya.present;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Attendance extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBHelper db=new DBHelper(getApplicationContext());
        if(db.DataAlreadyInDB("", "", ""))//check if any class is added
        {
            Intent intent = new Intent(Attendance.this, ViewAttendance.class);
            startActivity(intent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
    }

    public void goToTimeTable(View view)
    {
        Intent intent = new Intent(Attendance.this, TimeTable.class);
        startActivity(intent);
        finish();
    }
}
