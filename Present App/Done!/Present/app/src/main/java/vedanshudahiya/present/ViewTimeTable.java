package vedanshudahiya.present;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class ViewTimeTable extends Activity {
    ListView TTclassList;
    DBHelper db;
    ListAdapter TTAdapter;
    ArrayList<String> classNamesList;
    ArrayList<classIDname> mainList;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_view_time_table);

        db = new DBHelper(getApplicationContext());
        getClassNames();
        if(classNamesList.size()==0)finish();

        TTclassList =(ListView)findViewById(R.id.TTclassList);
        TTAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,classNamesList);
        TTclassList.setAdapter(TTAdapter);

        TTclassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedClass= classNamesList.get(position);

                Intent intent = new Intent(ViewTimeTable.this, ViewClassSchedule.class);
                intent.putExtra("classID",mainList.get(position).id);
                intent.putExtra("className",mainList.get(position).name);
                intent.putExtra("classSTD",mainList.get(position).std);
                startActivity(intent);
            }
        });
    }

    public void getClassNames(){
        classNamesList=new ArrayList<String>();

        mainList = db.getClassNames();

        for(int i=0;i<mainList.size();i++){
            classNamesList.add(Integer.toString(i+1)+". "+mainList.get(i).name);
        }
    }

    public void addClasses(View view)
    {
        Intent intent = new Intent(ViewTimeTable.this, AddClass.class);
        startActivity(intent);
        finish();
    }
}
