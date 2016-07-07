package vedanshudahiya.present;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xyz on 6/21/2016.
 */
public class classAttendanceSheetAdapter extends ArrayAdapter<class_attendance_sheet_item> {
    public classAttendanceSheetAdapter(Context context, ArrayList<class_attendance_sheet_item> classAS) {
        super(context, 0, classAS);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        class_attendance_sheet_item cASi = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.attendance_sheet_list_item, parent, false);
        }
        // Lookup view for data population
        TextView date = (TextView) convertView.findViewById(R.id.dateAS);
        TextView day = (TextView) convertView.findViewById(R.id.dayAS);
        TextView time = (TextView) convertView.findViewById(R.id.timeAS);
        TextView value = (TextView) convertView.findViewById(R.id.valueAS);
        LinearLayout containerAS = (LinearLayout) convertView.findViewById(R.id.containerAS);

        // Populate the data into the template view using the data object

        date.setText(cASi.formatDate());
        day.setText(cASi.formatDay());
        time.setText(class_item.timeToString(cASi.hour,cASi.minute));

        String APNA,color;
        switch(cASi.attendance){
            case 0: APNA = "Ab";
                    color="#CD5C5C";
                    break;
            case 1: APNA = "P";
                    color="#70DBDB";
                    break;
            default:APNA = "NA" ;
                    color="#FFFFAA";
        }
        value.setText(APNA);
        containerAS.setBackgroundColor(Color.parseColor(color));
        // Return the completed view to render on screen
        return convertView;
    }}
