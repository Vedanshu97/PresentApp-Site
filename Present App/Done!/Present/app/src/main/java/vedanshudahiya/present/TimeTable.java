package vedanshudahiya.present;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TimeTable extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBHelper db=new DBHelper(getApplicationContext());
        if(db.DataAlreadyInDB("", "", ""))//check if any class is added
        {
            Intent intent = new Intent(TimeTable.this, ViewTimeTable.class);
            startActivity(intent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
    }

    public void addClasses(View view)
    {
        Intent intent = new Intent(TimeTable.this, AddClass.class);
        startActivity(intent);
        finish();
    }
}
