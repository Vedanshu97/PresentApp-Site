package vedanshudahiya.present;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class classAttendanceSheet extends Activity {

    ArrayList<class_attendance_sheet_item> classAttendanceSheetItems;
    DBHelper db;
    int CCID;
    TextView pText,aText;
    classAttendanceSheetAdapter newAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_attendance_sheet);
        String CCN= getIntent().getExtras().getString("className");
        CCID= getIntent().getExtras().getInt("classID");
        int tP=getIntent().getExtras().getInt("tP");
        int tA=getIntent().getExtras().getInt("tA");

        TextView scn= (TextView)findViewById(R.id.SingleClassName);
        pText= (TextView)findViewById(R.id.MAP);
        aText= (TextView)findViewById(R.id.MAA);
        scn.setText(CCN + " Att. Sheet");
        pText.setText(""+tP);
        aText.setText(""+tA);

        db=new DBHelper(getApplicationContext());
        classAttendanceSheetItems=new ArrayList<class_attendance_sheet_item>();
        getClassAttendanceSheet();

        ListView ScheduleList = (ListView)findViewById(R.id.classAttendanceSheet);
        newAdapter= new classAttendanceSheetAdapter(classAttendanceSheet.this,classAttendanceSheetItems);
        ScheduleList.setAdapter(newAdapter);

        ScheduleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                setupChangeRequest(pos);
                return true;
            }
        });
    }

    public void getClassAttendanceSheet(){
        classAttendanceSheetItems.clear();
        classAttendanceSheetItems.addAll(db.getAttendanceSheet(CCID));
    }

    public void ManualStuff(View v){
        int target=0,change=0;
        switch (v.getId()){
            case (R.id.addAbsent):
                target=0;
                change=1;
                break;
            case (R.id.subAbsent):
                target=0;
                change=-1;
                break;
            case (R.id.addPresent):
                target=1;
                change=1;
                break;
            case (R.id.subPresent):
                target=1;
                change=-1;
                break;
        }
        TextView changeTextTarget=(target==1)?pText:aText;
        int toBeSet=Integer.parseInt(changeTextTarget.getText().toString())+change;

        if(toBeSet<0)return;

        db.manualUpdateAttendance(change, CCID, target);
        changeTextTarget.setText(Integer.toString(toBeSet));
    }

    public void setupChangeRequest(int pos){
        final int xPos=pos;
        String[] attVal={"Absent","Present","Not Applicable"};
        int hour=classAttendanceSheetItems.get(pos).hour;
        int minute=classAttendanceSheetItems.get(pos).minute;
        final  int AID=classAttendanceSheetItems.get(xPos).AID;
        final int cAtt=classAttendanceSheetItems.get(pos).attendance;

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_edit)
                .setTitle("Change Attendance")
                .setMessage("Set new Attendance value for -\n\nDate :"+classAttendanceSheetItems.get(pos).formatDate()
                           +"\nTime :"+class_item.timeToString(hour,minute)
                           +"\n\nCurrent Status :" + attVal[cAtt])
        .setPositiveButton("N.A.", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                db.changeAttendance(AID,cAtt, 2);
                refreshAttSheet();
                    }
                })
                .setNeutralButton("Absent",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        db.changeAttendance(AID,cAtt,0);
                        refreshAttSheet();
                    }
                })
                .setNegativeButton("Present", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.changeAttendance(AID,cAtt,1);
                        refreshAttSheet();
                    }
                })
                .show();
    }

    public void refreshAttSheet(){
        getClassAttendanceSheet();

        newAdapter.notifyDataSetInvalidated();
        newAdapter.notifyDataSetChanged();

        Toast.makeText(classAttendanceSheet.this,"Changed !\n( Press back key to see change in Attendance % ).",Toast.LENGTH_SHORT).show();
    }
}
