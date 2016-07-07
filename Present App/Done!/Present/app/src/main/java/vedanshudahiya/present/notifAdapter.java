package vedanshudahiya.present;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xyz on 6/24/2016.
 */
public class notifAdapter extends ArrayAdapter<notif_item> {
    final Context context;
    public notifAdapter(Context context, ArrayList<notif_item> notifList) {
        super(context, 0, notifList);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int nfPos=position;
        // Get the data item for this position
        notif_item nItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dashboard_item, parent, false);
        }
        // Lookup view for data population
        TextView nCN = (TextView) convertView.findViewById(R.id.nClassName);
        TextView nDT = (TextView) convertView.findViewById(R.id.nDate);
        TextView nD = (TextView) convertView.findViewById(R.id.nDay);
        TextView nT = (TextView) convertView.findViewById(R.id.nTime);

        // Populate the data into the template view using the data object
        nCN.setText(nItem.nClassName);
        nDT.setText(class_attendance_sheet_item.formatDate(nItem.nDate));
        nD.setText(class_attendance_sheet_item.formatLongDay(nItem.nDate));
        nT.setText(nItem.nTime);

        Button pB=(Button)convertView.findViewById(R.id.present);
        Button aB=(Button)convertView.findViewById(R.id.absent);
        Button nB=(Button)convertView.findViewById(R.id.NA);

        pB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((dashboard)context).setAttendance(nfPos, 1);
            }
        });
        aB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((dashboard)context).setAttendance(nfPos, 0);
            }
        });
        nB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((dashboard)context).setAttendance(nfPos, 2);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
