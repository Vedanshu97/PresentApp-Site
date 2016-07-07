package vedanshudahiya.present;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedaytimepicker.SlideDayTimeListener;
import com.github.jjobes.slidedaytimepicker.SlideDayTimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class AddClass extends FragmentActivity {

    ListView classList;
    SlideDayTimeListener listener;
    int cpos;
    DBHelper db;
    ArrayList<class_item> classItems;
    classListAdapter newAdapter;
    String sDate;
    int sDay,sMonth,sYear;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String[] defMsg={"# of classes not set yet"};
        classItems = new ArrayList<class_item>();
        ListAdapter defAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,defMsg);
        classList = (ListView)findViewById(R.id.classList);
        classList.setAdapter(defAdapter);
        db = new DBHelper(getApplicationContext());

        //Set today's date
        calendar = Calendar.getInstance();
        sYear = calendar.get(Calendar.YEAR);
        sMonth = calendar.get(Calendar.MONTH)+1;
        sDay = calendar.get(Calendar.DAY_OF_MONTH);
        setStartDateText();

        setupDialogListener();
    }

    public void setClassList(View v) {
        EditText noc=(EditText)findViewById(R.id.noc);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        String snoc=noc.getText().toString().trim();
        if(snoc.isEmpty()){
            Toast.makeText(AddClass.this,"Enter Something !",Toast.LENGTH_SHORT).show();
            return;
        }

        noc.clearFocus();

        int inoc=Integer.parseInt(snoc);

        if(inoc<=0||inoc>50){
            Toast.makeText(AddClass.this,"Number of Classes must be >0 and <51 !",Toast.LENGTH_SHORT).show();
            return;
        }

        classItems.clear();
        for(int i=1;i<=inoc;i++){
          classItems.add(new class_item());
        }

        newAdapter= new classListAdapter(AddClass.this,classItems);
        classList.setAdapter(newAdapter);

        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                cpos = position;
                int iniDay = classItems.get(position).dayINT + 1;
                if(iniDay==0)iniDay=2;
                int iniHour = classItems.get(position).hourINT;
                if(iniHour==-1)iniHour=12;
                int iniMin = classItems.get(position).minuteINT;
                if(iniMin==-1)iniMin=0;

                new SlideDayTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDay(iniDay)
                        .setInitialHour(iniHour)
                        .setInitialMinute(iniMin)
                        .build()
                        .show();
            }
        });
    }

    public void setupDialogListener(){
        listener = new SlideDayTimeListener() {

            @Override
            public void onDayTimeSet(int day, int hour, int minute)
            {
                class_item cCI=classItems.get(cpos);

                //Set Day
                cCI.setDayINT(day - 1);
                //Set Time
                cCI.setHourINT(hour);
                cCI.setMinuteINT(minute);

                cCI.isSetOk();

                //Notify Change to Adapter
                newAdapter.notifyDataSetChanged();

                Toast.makeText(AddClass.this, "Set...Tap to change again !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDayTimeCancel()
            {
                Toast.makeText(AddClass.this, "Cancelled.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void addHeadache(View v){
        EditText eClassName=(EditText)findViewById(R.id.className);

        final String ClassName=eClassName.getText().toString().trim();
        if(ClassName.isEmpty()){
            Toast.makeText(AddClass.this,"Please Enter Class Name",Toast.LENGTH_SHORT).show();
            return;
        }
        int n=classItems.size();
        if(n==0){
            Toast.makeText(AddClass.this,"Please Enter # of Classes/Week and Click Set!",Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i=0;i<n;i++){
            if(classItems.get(i).isSet==false){
                Toast.makeText(AddClass.this,"Class #"+Integer.toString(i + 1)+" is not Set !",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Show Confirm Dialog box
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Add Class")
                    .setIcon(android.R.drawable.ic_input_add)
                    .setMessage("Class Name: " + ClassName +"\n\nStart Date : "+class_attendance_sheet_item.formatDate(sDate)+"\n\nNumber of classes\nper week: " + n)
                    .setPositiveButton("No", null)
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addToDatabase(ClassName,sDate);
                        }
                    })
                    .show();
    }

    void addToDatabase(String className,String sDate){
        long cid=db.createClass(className,sDate);
        if(cid<0){
                Toast.makeText(AddClass.this,"Sorry! This Class already exists...",Toast.LENGTH_SHORT).show();
                return;
        }
        db.createSchedule(cid, classItems);
        Intent intent = new Intent(AddClass.this, ViewTimeTable.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("All progress will be lost...Sure you want to go back ?")
                .setPositiveButton("No", null)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    public void selectStartDate(View v){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        sYear=year;
                        sMonth=monthOfYear+1;
                        sDay=dayOfMonth;

                        setStartDateText();
                    }
                }, sYear,(sMonth-1),sDay);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void setStartDateText(){
        String d = Integer.toString(sDay);
        if(d.length()==1)d="0"+d;

        String m = Integer.toString(sMonth);
        if(m.length()==1)m="0"+m;

        String y = Integer.toString(sYear);
        while(y.length()<4)y="0"+y;

        sDate = y+m+d;

        TextView startDate=(TextView)findViewById(R.id.startDateText);
        startDate.setText(class_attendance_sheet_item.formatDate(sDate));
    }
}