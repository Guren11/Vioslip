package axis.com.vioslip.Student;

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
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import axis.com.vioslip.R;


public class MyVioAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    // ImageLoader imageLoader;
    ArrayList<HashMap<String, String>> data;

    HashMap<String, String> resultp = new HashMap<String, String>();

    public MyVioAdapter(Context context,
                       ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        //   imageLoader = new ImageLoader(context);



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
        TextView violatiorid;
        TextView violationname;
        TextView status;
        TextView userid;
        TextView pname;

        ImageView picksure;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.stud_myviolation_item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        violatiorid = (TextView) itemView.findViewById(R.id.tx_violatorid);
        violationname = (TextView) itemView.findViewById(R.id.tx_violationname);
        status = (TextView) itemView.findViewById(R.id.tx_status);
        userid = (TextView) itemView.findViewById(R.id.tx_userid);
        pname = (TextView) itemView.findViewById(R.id.tx_pname);




        // Locate the ImageView in listview_item.xml
        picksure = (ImageView) itemView.findViewById(R.id.flat);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        String wew = resultp.get(MyViolationActivity.VIOLATION_NAME);
        char first = wew.charAt(0);
        String FirstString = Character.toString(first);
        String Final = FirstString.toUpperCase();
        TextDrawable txtdrawable = TextDrawable.builder().buildRound(Final,color1);
        picksure.setImageDrawable(txtdrawable);


        // Capture position and set results to the TextViews

        violatiorid.setText(resultp.get(MyViolationActivity.VIOLATOR_ID));
        violationname.setText(resultp.get(MyViolationActivity.VIOLATION_NAME));
        status.setText(resultp.get(MyViolationActivity.STATUS));
        userid.setText(resultp.get(MyViolationActivity.STUD_ID));
        pname.setText(resultp.get(MyViolationActivity.PNAME));


//        imageLoader.DisplayImage(resultp.get(MainActivity.PICTURE), picksure);



        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class

        // Capture ListView item click
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);

                Intent intent = new Intent(context, MyViolationSingleView.class);
                // Pass all data rank
                intent.putExtra("userid", resultp.get(MyViolationActivity.STUD_ID));
                intent.putExtra("violationname", resultp.get(MyViolationActivity.VIOLATION_NAME));
                intent.putExtra("status", resultp.get(MyViolationActivity.STATUS));
                intent.putExtra("pname", resultp.get(MyViolationActivity.PNAME));


                context.startActivity(intent);




            }
        });
        return itemView;
    }
}



