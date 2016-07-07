package vedanshudahiya.present;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xyz on 6/20/2016.
 */
public class classAttendanceAdapter extends ArrayAdapter<class_attendance_item> {

    public classAttendanceAdapter(Context context, ArrayList<class_attendance_item> classAIList) {
        super(context, 0, classAIList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        class_attendance_item cAI = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.attendance_list_item, parent, false);
        }
        // Lookup view for data population
        TextView cn = (TextView) convertView.findViewById(R.id.className);
        TextView att = (TextView) convertView.findViewById(R.id.Attendance);
        TextView total = (TextView) convertView.findViewById(R.id.total);
        TextView present = (TextView) convertView.findViewById(R.id.present);
        TextView absent = (TextView) convertView.findViewById(R.id.absent);

        DBHelper db=new DBHelper(getContext());
        int tP=cAI.present+db.countAttendance(cAI.id,1);
        int tA=cAI.absent+db.countAttendance(cAI.id,0);
        int tot =tA+tP;
        float attP=(tot==0)?-1:(float)(tP)/(tot);
        attP*=100;

        // Populate the data into the template view using the data object
        cn.setText(cAI.name);

        if(attP>=0)
            att.setText(String.format("%.2f", attP)+" %");
        else
            att.setText("NA");

        total.setText(Integer.toString(tot));
        present.setText(Integer.toString(tP));
        absent.setText(Integer.toString(tA));

        //Check Danger
        if(attP>=0&&attP<75){
            cn.setTextColor(Color.parseColor("#FF4500"));
            att.setTextColor(Color.parseColor("#FF4500"));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
