package axis.com.vioslip.Admin.Violations;

/**
 * Created by Guren on 10/10/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import axis.com.vioslip.R;


public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    ArrayList<HashMap<String, String>> data;

    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);



    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView violationid;
        TextView violationname;
        TextView violationtype;
        TextView sanctionname;
        TextView sanctionsched;
        TextView location;

        ImageView picksure;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.admin_violation_item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        violationid = (TextView) itemView.findViewById(R.id.violationid);
        violationname = (TextView) itemView.findViewById(R.id.violationname);
        violationtype = (TextView) itemView.findViewById(R.id.violationtype);
        sanctionname = (TextView) itemView.findViewById(R.id.sanctionname);
        sanctionsched = (TextView) itemView.findViewById(R.id.sanctionsched);
        location = (TextView) itemView.findViewById(R.id.location);

        // Locate the ImageView in listview_item.xml
        picksure = (ImageView) itemView.findViewById(R.id.flat);


        // Capture position and set results to the TextViews
        violationid.setText(resultp.get(ViolationsList.VIOLATION_ID));
        violationname.setText(resultp.get(ViolationsList.VIOLATION_NAME));
        violationtype.setText(resultp.get(ViolationsList.VIOLATION_TYPE));
        sanctionname.setText(resultp.get(ViolationsList.SANCTION_NAME));
        sanctionsched.setText(resultp.get(ViolationsList.SANCTION_SCHED));
        location.setText(resultp.get(ViolationsList.LOCATION));
        imageLoader.DisplayImage(resultp.get(ViolationsList.QRCODE), picksure);



        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class

        // Capture ListView item click
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position

                resultp = data.get(position);


                Intent intent = new Intent(context, ViolationSingleView.class);
                // Pass all data rank
                intent.putExtra("vioname", resultp.get(ViolationsList.VIOLATION_NAME));
                // Pass all data country
                intent.putExtra("viotype", resultp.get(ViolationsList.VIOLATION_TYPE));
                // Pass all data population
                intent.putExtra("sanct",resultp.get(ViolationsList.SANCTION_NAME));

                intent.putExtra("pic",resultp.get(ViolationsList.QRCODE));
                // Start SingleItemView Class
                context.startActivity(intent);


            }
        });
        return itemView;
    }
}
