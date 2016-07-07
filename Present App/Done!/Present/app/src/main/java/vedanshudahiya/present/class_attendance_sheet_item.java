package vedanshudahiya.present;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xyz on 6/21/2016.
 */
public class class_attendance_sheet_item {
    public String date;
    public int hour;
    public int minute;
    public int attendance;
    public int AID;

    public class_attendance_sheet_item(String date, int hour, int minute, int attendance,int AID) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.attendance = attendance;
        this.AID = AID;
    }

    public static String formatDate(String date){      // date -> yyyymmdd
        try {
            Date dtO = new SimpleDateFormat("yyyyMMdd").parse(date);
            String formatted = new SimpleDateFormat("dd MMM,yyyy").format(dtO);
            return formatted;
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error !";
        }
    }

    public static String formatDay(String date){
        try {
            Date dtO = new SimpleDateFormat("yyyyMMdd").parse(date);
            String formatted = new SimpleDateFormat("EEE").format(dtO);
            return formatted;
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static String formatLongDay(String date){
        try {
            Date dtO = new SimpleDateFormat("yyyyMMdd").parse(date);
            String formatted = new SimpleDateFormat("EEEE").format(dtO);
            return formatted;
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error";
        }
    }


    public String formatDate(){
        return formatDate(this.date);
    }
    public String formatDay(){
        return formatDay(this.date);
    }
}