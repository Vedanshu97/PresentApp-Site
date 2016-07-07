package vedanshudahiya.present;

public class class_item {
    public int dayINT;
    public int hourINT;
    public int minuteINT;
    public boolean isSet;

    public class_item() {
        this.isSet=false;
        this.dayINT=-1;
        this.hourINT=-1;
        this.minuteINT=-1;
    }

    public class_item(int dayINT, int hourINT, int minuteINT) {
        this.dayINT = dayINT;
        this.hourINT = hourINT;
        this.minuteINT = minuteINT;
    }

    public void setDayINT(int dayINT) {
        this.dayINT = dayINT;
    }

    public void setHourINT(int hourINT) {
        this.hourINT = hourINT;
    }

    public void setMinuteINT(int minuteINT) {
        this.minuteINT = minuteINT;
    }

    public static String dayToString(int x){
        if (x<0)return "<unset>";
        final String[] days={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        return days[x];
    }

    public String dayToString(){
        return dayToString(this.dayINT);
    }

    public static String timeToString(int hour,int minute){

        if(hour==-1||minute==-1)return "<unset>";

        String cTime=(hour/12>0)?"PM":"AM";
        hour%=12;
        if(hour==0)hour=12;
        String shour=""+hour;
        String smin=""+minute;
        if(shour.length()==1)shour="0"+shour;
        if(smin.length()==1)smin="0"+smin;
        cTime=shour+":"+smin+" "+cTime;

        return cTime;
    }

    public String timeToString(){
        return timeToString(this.hourINT,this.minuteINT);
    }

    public void isSetOk(){
        this.isSet=true;
    }
}
