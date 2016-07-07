package vedanshudahiya.present;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xyz on 6/18/2016.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "presentVD";

    // Table Names
    private static final String TABLE_CLASS = "ClassNames";
    private static final String TABLE_SCHEDULE = "Schedule";
    private static final String TABLE_ATTENDANCE = "AttendanceSheet";
    private static final String TABLE_PENDING = "PendingAttendance";

    //Common Column Names
    private static final String KEY_ID = "id";

    // Class TABLE - column names
    private static final String KEY_CLASS_NAME = "className";
    private static final String KEY_CLASS_PRESENT = "present";
    private static final String KEY_CLASS_ABSENT = "absent";
    private static final String KEY_CLASS_START_DATE = "startDate";

    // Schedule TABLE - column names
    private static final String KEY_SCHEDULE_PARENT = "parentClass";
    private static final String KEY_SCHEDULE_DAY = "day";
    private static final String KEY_SCHEDULE_HOUR = "hour";
    private static final String KEY_SCHEDULE_MINUTE = "minute";

    // Attendance TABLE - column names
    private static final String KEY_ATTENDANCE_DATE = "date";
    private static final String KEY_ATTENDANCE_SHID = "scheduleID";
    private static final String KEY_ATTENDANCE_VALUE = "value";

    // Table Create Statements
    // CLASS table create statement
    private static final String CREATE_TABLE_CLASS = "CREATE TABLE IF NOT EXISTS " + TABLE_CLASS
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + KEY_CLASS_NAME + " TEXT UNIQUE NOT NULL, "
            + KEY_CLASS_PRESENT + " INTEGER, "
            + KEY_CLASS_ABSENT + " INTEGER, "
            + KEY_CLASS_START_DATE + " TEXT "
            + ")";

    // SCHEDULE table create statement
    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCHEDULE
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + KEY_SCHEDULE_PARENT + " INTEGER NOT NULL, "
            + KEY_SCHEDULE_DAY + " INTEGER NOT NULL, "
            + KEY_SCHEDULE_HOUR + " INTEGER NOT NULL, "
            + KEY_SCHEDULE_MINUTE + " INTEGER NOT NULL"
            +")";

    // ATTENDANCE table create statement
    private static final String CREATE_TABLE_ATTENDANCE = "CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDANCE
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + KEY_ATTENDANCE_DATE + " TEXT, "
            + KEY_ATTENDANCE_SHID + " INTEGER NOT NULL, "
            + KEY_ATTENDANCE_VALUE + " INTEGER"                 // 0 -> absent , 1 -> present , 2 - > NA
            + ")";

    // PENDING table create statement
    private static final String CREATE_TABLE_PENDING = "CREATE TABLE IF NOT EXISTS " + TABLE_PENDING
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + KEY_ATTENDANCE_DATE + " TEXT, "
            + KEY_ATTENDANCE_SHID + " INTEGER NOT NULL"
            + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_CLASS);
        db.execSQL(CREATE_TABLE_SCHEDULE);
        db.execSQL(CREATE_TABLE_ATTENDANCE);
        db.execSQL(CREATE_TABLE_PENDING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING);

        // create new tables
        onCreate(db);
    }

    public long createClass(String className,String STD) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(DataAlreadyInDB(TABLE_CLASS,KEY_CLASS_NAME,className))return -1;

        ContentValues values = new ContentValues();
        values.put(KEY_CLASS_NAME, className);
        values.put(KEY_CLASS_PRESENT, 0);
        values.put(KEY_CLASS_ABSENT, 0);
        values.put(KEY_CLASS_START_DATE, STD);

        // insert row
        long classID=db.insert(TABLE_CLASS, null, values);

        return classID;
    }

    public ArrayList<classIDname> getClassNames(){
        ArrayList<classIDname> classNames =new ArrayList<classIDname>();

        String selectQuery = "SELECT  "+KEY_ID +" , "+KEY_CLASS_NAME+" , "+KEY_CLASS_START_DATE+" FROM " + TABLE_CLASS +" order by " + KEY_CLASS_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                long ID= c.getLong(c.getColumnIndex(KEY_ID));
                String name = c.getString(c.getColumnIndex(KEY_CLASS_NAME));
                String std=c.getString(c.getColumnIndex(KEY_CLASS_START_DATE));
                classIDname CIN = new classIDname(ID,name,std);

                // adding to list
                classNames.add(CIN);
            } while (c.moveToNext());
        }
        return classNames;
    }

    public ArrayList<class_attendance_item> getAttendance(){
        ArrayList<class_attendance_item> classAttList =new ArrayList<class_attendance_item>();

        String selectQuery = "SELECT  * FROM " + TABLE_CLASS +" order by " + KEY_CLASS_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                int id=c.getInt(c.getColumnIndex(KEY_ID));
                String name=c.getString(c.getColumnIndex(KEY_CLASS_NAME));
                int present=c.getInt(c.getColumnIndex(KEY_CLASS_PRESENT));
                int absent=c.getInt(c.getColumnIndex(KEY_CLASS_ABSENT));

                class_attendance_item CAtt = new class_attendance_item(id,name,present,absent);

                // adding to list
                classAttList.add(CAtt);
            } while (c.moveToNext());
        }
        return classAttList;
    }

    public void createSchedule(long parent,ArrayList<class_item> CI) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < CI.size(); i++) {

            ContentValues values = new ContentValues();
            values.put(KEY_SCHEDULE_PARENT, parent);
            values.put(KEY_SCHEDULE_DAY,CI.get(i).dayINT);
            values.put(KEY_SCHEDULE_HOUR,CI.get(i).hourINT);
            values.put(KEY_SCHEDULE_MINUTE,CI.get(i).minuteINT);

            // insert row
            db.insert(TABLE_SCHEDULE, null, values);
        }
    }

    public ArrayList<class_item> getSchedule(long cid){
        ArrayList<class_item> classItems = new ArrayList<class_item>();

        String selectQuery = "SELECT  * FROM " + TABLE_SCHEDULE +" where " + KEY_SCHEDULE_PARENT + " = "+ cid
                            +" order by "+ KEY_SCHEDULE_DAY +", "+KEY_SCHEDULE_HOUR+", "+KEY_SCHEDULE_MINUTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                class_item CIN = new class_item();

                CIN.setDayINT(c.getInt(c.getColumnIndex(KEY_SCHEDULE_DAY)));
                CIN.setHourINT(c.getInt(c.getColumnIndex(KEY_SCHEDULE_HOUR)));
                CIN.setMinuteINT(c.getInt(c.getColumnIndex(KEY_SCHEDULE_MINUTE)));

                // adding to list
                classItems.add(CIN);
            } while (c.moveToNext());
        }

        return classItems;
    }

    public boolean DataAlreadyInDB(String TableName,String dbfield,String fieldValue) {
        String Query;

        if(TableName=="") {
            Query="Select * from "+TABLE_CLASS+" LIMIT 1";
        }
        else
        Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue + "' LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean getAttendanceSheetCount(int id){
        String Query="Select "+KEY_ID+" from "+TABLE_ATTENDANCE+" where "+KEY_ATTENDANCE_SHID+" in " +
                     "( select "+KEY_ID+" from "+TABLE_SCHEDULE+" where "+KEY_SCHEDULE_PARENT+" = "+id+" ) LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<class_attendance_sheet_item> getAttendanceSheet(long givenID){
        ArrayList<class_attendance_sheet_item> AttendanceSheet=new ArrayList<class_attendance_sheet_item>();

        String selectQuery = "SELECT "+TABLE_ATTENDANCE+"."+KEY_ID+" , "
                                      +KEY_ATTENDANCE_DATE+" , "
                                      +KEY_SCHEDULE_HOUR+" , "
                                      +KEY_SCHEDULE_MINUTE+" , "
                                      +KEY_ATTENDANCE_VALUE
                            +" FROM " +TABLE_SCHEDULE+" , "+TABLE_ATTENDANCE
                            +" WHERE "+KEY_SCHEDULE_PARENT+" = "+givenID+" AND "+TABLE_SCHEDULE+"."+KEY_ID+" = "+KEY_ATTENDANCE_SHID
                            +" ORDER BY "+KEY_ATTENDANCE_DATE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String date = c.getString(c.getColumnIndex(KEY_ATTENDANCE_DATE));
                int hour = c.getInt(c.getColumnIndex(KEY_SCHEDULE_HOUR));
                int minute = c.getInt(c.getColumnIndex(KEY_SCHEDULE_MINUTE));
                int value = c.getInt(c.getColumnIndex(KEY_ATTENDANCE_VALUE));
                int AID = c.getInt(c.getColumnIndex(KEY_ID));

                class_attendance_sheet_item CAS = new class_attendance_sheet_item(date,hour,minute,value,AID);
                // adding to list
                AttendanceSheet.add(CAS);
            } while (c.moveToNext());
        }

        return AttendanceSheet;
    }

    public ArrayList<notif_item> getPending(){
        ArrayList<notif_item> notifs = new ArrayList<notif_item>();

        String selectQuery ="SELECT "+TABLE_PENDING+"."+KEY_ID+" , "+KEY_CLASS_NAME+" , "+KEY_ATTENDANCE_DATE
                           +" , "+KEY_SCHEDULE_HOUR+" , "+KEY_SCHEDULE_MINUTE
                           +" FROM "+TABLE_CLASS+" , "+TABLE_SCHEDULE+" , "+TABLE_PENDING
                           +" WHERE "+TABLE_SCHEDULE+"."+KEY_ID+" = "+KEY_ATTENDANCE_SHID
                           +" AND "+TABLE_CLASS+"."+KEY_ID+" = "+KEY_SCHEDULE_PARENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String nCN=c.getString(c.getColumnIndex(KEY_CLASS_NAME));
                String dt=c.getString(c.getColumnIndex(KEY_ATTENDANCE_DATE));
                int hour=c.getInt(c.getColumnIndex(KEY_SCHEDULE_HOUR));
                int minute=c.getInt(c.getColumnIndex(KEY_SCHEDULE_MINUTE));
                long PID=c.getLong(c.getColumnIndex(KEY_ID));

                // adding to list
                notif_item nItem = new notif_item(nCN,dt,class_item.timeToString(hour,minute),PID);
                notifs.add(nItem);
            } while (c.moveToNext());
        }
        return notifs;
    }

    public boolean updatePending(String toDate,int toHour,int toMinutes,String fromDate,int fromHour,int fromMinutes){
        if(fromDate==toDate&&fromHour==toHour&&fromMinutes==toMinutes)return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date frDT = sdf.parse(fromDate);
            Date toDT = sdf.parse(toDate);
            Calendar calen=Calendar.getInstance();
            calen.setTime(frDT);
            Calendar calen2=Calendar.getInstance();
            calen2.setTime(toDT);

            int cDay=calen.get(Calendar.DAY_OF_WEEK)-1;

            if(calen.compareTo(calen2)==0){
                String selectQuery ="SELECT "+TABLE_SCHEDULE+"."+KEY_ID+" FROM "+TABLE_SCHEDULE
                        +" WHERE "+KEY_SCHEDULE_DAY+" = "+cDay+" AND "+KEY_SCHEDULE_PARENT
                        +" IN ( SELECT "+TABLE_CLASS+"."+KEY_ID+" FROM "+TABLE_CLASS
                        +" WHERE "+KEY_CLASS_START_DATE+" <= "+sdf.format(calen.getTime())+" )";

                selectQuery+=" AND ( ( "+KEY_SCHEDULE_HOUR+"*60 + "+KEY_SCHEDULE_MINUTE+" ) BETWEEN ( "+fromHour+"*60 + "+fromMinutes+" + 1 )"
                            +" AND ( "+toHour+"*60 + "+toMinutes+" ) )";
                runMoveToPendingQuery(selectQuery, sdf.format(calen.getTime()));

                return true;
            }

            //else
            boolean first=true;
            while(calen.compareTo(calen2)<=0){
                String selectQuery ="SELECT "+TABLE_SCHEDULE+"."+KEY_ID+" FROM "+TABLE_SCHEDULE
                        +" WHERE "+KEY_SCHEDULE_DAY+" = "+cDay+" AND "+KEY_SCHEDULE_PARENT
                        +" IN ( SELECT "+TABLE_CLASS+"."+KEY_ID+" FROM "+TABLE_CLASS
                        +" WHERE "+KEY_CLASS_START_DATE+" <= "+sdf.format(calen.getTime())+" )";
                if(first){
                    selectQuery+=" AND ( ( "+KEY_SCHEDULE_HOUR+"*60 + "+KEY_SCHEDULE_MINUTE+" ) > ( "+fromHour+"*60 +"+fromMinutes+" ) )";
                    first=false;
                }
                else if(calen.compareTo(calen2)==0){
                    selectQuery+=" AND ( ( "+KEY_SCHEDULE_HOUR+"*60 + "+KEY_SCHEDULE_MINUTE+" ) <= ( "+toHour+"*60 +"+toMinutes+" ) )";
                }

                runMoveToPendingQuery(selectQuery,sdf.format(calen.getTime()));

                //update day and date
                cDay=(cDay+1)%7;
                calen.add(Calendar.DATE,1);
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public boolean moveToAttendanceSheet(long PID,int val){
        String selectQuery ="SELECT "+TABLE_SCHEDULE+"."+KEY_SCHEDULE_PARENT+" , "+KEY_ATTENDANCE_SHID+" , "+KEY_ATTENDANCE_DATE
                           +" FROM "+TABLE_PENDING+" , "+TABLE_SCHEDULE+" WHERE "+TABLE_PENDING+"."+KEY_ID+" = "+PID
                           +" AND "+TABLE_SCHEDULE+"."+KEY_ID+" = "+TABLE_PENDING+"."+KEY_ATTENDANCE_SHID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
                String dt=c.getString(c.getColumnIndex(KEY_ATTENDANCE_DATE));
                long SHID=c.getInt(c.getColumnIndex(KEY_ATTENDANCE_SHID));
                long parentID=c.getLong(c.getColumnIndex(KEY_SCHEDULE_PARENT));

            ContentValues values = new ContentValues();
            values.put(KEY_ATTENDANCE_DATE, dt);
            values.put(KEY_ATTENDANCE_SHID, SHID);
            values.put(KEY_ATTENDANCE_VALUE, val);

            // insert row
            db.insert(TABLE_ATTENDANCE, null, values);

            //delete from pending
            db.delete(TABLE_PENDING, KEY_ID+"=?", new String[]{Long.toString(PID)});

            return true;
        }
        else return false;
    }

    public void runMoveToPendingQuery(String selectQuery,String currDT){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()){
            do{
                long SHID=c.getInt(c.getColumnIndex(KEY_ID));

                ContentValues values = new ContentValues();
                values.put(KEY_ATTENDANCE_DATE, currDT);
                values.put(KEY_ATTENDANCE_SHID, SHID);
                // insert row
                db.insert(TABLE_PENDING, null, values);

            }while(c.moveToNext());
        }
    }

    public void manualUpdateAttendance(int change,long updateID,int target){
        if(target!=0 && target!=1)return;

        String targetColumn =KEY_CLASS_ABSENT;
        if(target==1) targetColumn=KEY_CLASS_PRESENT;

        String Query="UPDATE "+TABLE_CLASS+" SET "+targetColumn+" = "+targetColumn+"+"+change+" WHERE "+KEY_ID+" = "+updateID
                    +" AND ("+change+">0 OR "+targetColumn+">0)";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(Query);
    }

    public void deleteClass(long delID){
        String query1,query2,query3;
        query1="DELETE FROM ";
        query2=" WHERE "+KEY_ATTENDANCE_SHID+" IN (SELECT "+KEY_ID+" FROM "+TABLE_SCHEDULE+" WHERE "+KEY_SCHEDULE_PARENT+"="+delID+")";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(query1+TABLE_PENDING+query2);
        db.execSQL(query1+TABLE_ATTENDANCE+query2);

        db.execSQL(query1+TABLE_SCHEDULE+" WHERE "+KEY_SCHEDULE_PARENT+"="+delID);
        db.execSQL(query1+TABLE_CLASS+" WHERE "+KEY_ID+"="+delID);
    }

    public void changeAttendance(int AID,int fromVal,int toVal){
        if (fromVal==toVal)return;
        String query = "UPDATE "+TABLE_ATTENDANCE+" SET "+KEY_ATTENDANCE_VALUE+"="+toVal+" WHERE "+KEY_ID+"="+AID;
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(query);
    }

    public int countAttendance(int targetID,int val){
        String query="SELECT count(*) from "+TABLE_ATTENDANCE+" WHERE "+KEY_ATTENDANCE_SHID+" IN "
                    +"(SELECT "+KEY_ID+" from "+TABLE_SCHEDULE+" where "+KEY_SCHEDULE_PARENT+"="+targetID+")"
                    +" AND "+KEY_ATTENDANCE_VALUE+"="+val;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        return c.getInt(0);
    }
}