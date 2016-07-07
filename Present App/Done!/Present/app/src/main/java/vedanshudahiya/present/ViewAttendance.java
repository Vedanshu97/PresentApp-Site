package vedanshudahiya.present;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ViewAttendance extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
    }

    public void goToCheckAttendance(View v){
        Intent intent = new Intent(ViewAttendance.this, checkAttendance.class);
        startActivity(intent);
    }

    public void  goToDashboard(View v){
        Intent intent = new Intent(ViewAttendance.this, dashboard.class);
        startActivity(intent);
    }
}
