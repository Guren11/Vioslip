package axis.com.vioslip.Admin.Violators;

/**
 * Created by Guren on 10/11/2017.
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
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


public class ViolatorsAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    // ImageLoader imageLoader;
    ArrayList<HashMap<String, String>> data;

    HashMap<String, String> resultp = new HashMap<String, String>();

    public ViolatorsAdapter(Context context,
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
        TextView violatorid;
        TextView userid;
        TextView violationame;
        TextView pname;
        TextView status;

        ImageView picksure;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.admin_violators_item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        violatorid = (TextView) itemView.findViewById(R.id.admin_violatorsid);
        userid = (TextView) itemView.findViewById(R.id.admin_studentnumber);
        violationame = (TextView) itemView.findViewById(R.id.admin_violationame);
        pname = (TextView) itemView.findViewById(R.id.admin_pname);
        status = (TextView) itemView.findViewById(R.id.admin_status);




        // Locate the ImageView in listview_item.xml
        picksure = (ImageView) itemView.findViewById(R.id.flat);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        String wew = resultp.get(ViolatorsListActivity.VIOLATIONAME);
        char first = wew.charAt(0);
        String FirstString = Character.toString(first);
        String Final = FirstString.toUpperCase();
        TextDrawable txtdrawable = TextDrawable.builder().buildRound(Final,color1);
        picksure.setImageDrawable(txtdrawable);


        // Capture position and set results to the TextViews

        violatorid.setText(resultp.get(ViolatorsListActivity.VIOLATOR_ID));
        userid.setText(resultp.get(ViolatorsListActivity.USERID));
        violationame.setText(resultp.get(ViolatorsListActivity.VIOLATIONAME));
        pname.setText(resultp.get(ViolatorsListActivity.PNAME));
        status.setText(resultp.get(ViolatorsListActivity.STATUS));


//        imageLoader.DisplayImage(resultp.get(MainActivity.PICTURE), picksure);



        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class

        // Capture ListView item click
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
            /*
                Intent intent = new Intent(context, DoctorSingleItemview.class);
                // Pass all data rank
                intent.putExtra("userid", resultp.get(DoctorListActivity.USERID));
                intent.putExtra("firstname", resultp.get(DoctorListActivity.FIRSTNAME));
                intent.putExtra("lastname", resultp.get(DoctorListActivity.LASTNAME));
                intent.putExtra("middlename", resultp.get(DoctorListActivity.MIDDLENAME));
                intent.putExtra("email", resultp.get(DoctorListActivity.EMAIL));
                intent.putExtra("address", resultp.get(DoctorListActivity.ADDRESS));
                intent.putExtra("contact", resultp.get(DoctorListActivity.CONTACT));
                intent.putExtra("available", resultp.get(DoctorListActivity.AVAIL));
                intent.putExtra("specialty", resultp.get(DoctorListActivity.SPEC));


                context.startActivity(intent);

                */


            }
        });
        return itemView;
    }
}



