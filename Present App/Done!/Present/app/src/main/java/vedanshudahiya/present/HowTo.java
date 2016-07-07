package vedanshudahiya.present;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class HowTo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);
    }

    public void footerClicked(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Contact Me!")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setMessage("Vedanshu Dahiya\nIIT-R\n\nGmail : vedanshudahiya@gamil.com\n\nYahoo : vedanshudahiya@yahoo.com\n\n\nClick below to select contact medium :\n")
                .setPositiveButton("Facebook", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendToUrl("https://www.facebook.com/vedanshu.dahiya");
                    }
                })
                .setNeutralButton("Google+", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendToUrl("https://plus.google.com/u/0/100802874957626835586/about");
                    }
                })
                .show();
    }

    public void sendToUrl(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}

