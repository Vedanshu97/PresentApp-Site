package vedanshudahiya.present;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class classListAdapter extends ArrayAdapter<class_item>{
        public classListAdapter(Context context, ArrayList<class_item> classes) {
            super(context, 0, classes);
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        class_item citem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.class_item, parent, false);
        }
        // Lookup view for data population
        TextView cn = (TextView) convertView.findViewById(R.id.classNumber);
        TextView day = (TextView) convertView.findViewById(R.id.day);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        // Populate the data into the template view using the data object
        cn.setText(Integer.toString(position+1));
        day.setText(citem.dayToString());
        time.setText(citem.timeToString());

        // Get the Layout Parameters for ListView Current Item View
        ViewGroup.LayoutParams params = convertView.getLayoutParams();

        // Set the height of the Item View
        params.height = 70;
        convertView.setLayoutParams(params);

        // Return the completed view to render on screen
        return convertView;
    }
}