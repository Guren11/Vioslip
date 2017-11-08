package axis.com.vioslip.Admin.Prof;

/**
 * Created by Guren on 9/11/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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


public class ProfAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    // ImageLoader imageLoader;
    ArrayList<HashMap<String, String>> data;
    List<String> mAnimals = new ArrayList<String>();

    HashMap<String, String> resultp = new HashMap<String, String>();

    public ProfAdapter(Context context,
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
        TextView user_id;
        TextView Pfullname;
        TextView Pdepartment;
        TextView Username;

        ImageView picksure;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.admin_prof_item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        user_id = (TextView) itemView.findViewById(R.id.userid);
        Pfullname = (TextView) itemView.findViewById(R.id.fullname);
        Pdepartment = (TextView) itemView.findViewById(R.id.department);
        Username = (TextView) itemView.findViewById(R.id.tx_username);




        // Locate the ImageView in listview_item.xml
        picksure = (ImageView) itemView.findViewById(R.id.flat);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        String wew = resultp.get(ProfessorListActivity.PROFNAME);
        char first = wew.charAt(0);
        String FirstString = Character.toString(first);
        String Final = FirstString.toUpperCase();
        TextDrawable txtdrawable = TextDrawable.builder().buildRound(Final,color1);
        picksure.setImageDrawable(txtdrawable);


        // Capture position and set results to the TextViews

        user_id.setText(resultp.get(ProfessorListActivity.USERID));
        Pdepartment.setText(resultp.get(ProfessorListActivity.DEPARTMENT));
        Pfullname.setText(resultp.get(ProfessorListActivity.PROFNAME));
        Username.setText(resultp.get(ProfessorListActivity.USERNAME));


//        imageLoader.DisplayImage(resultp.get(MainActivity.PICTURE), picksure);



        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class

        // Capture ListView item click
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                mAnimals = new ArrayList<String>();
                mAnimals.add("Professor Name: "+resultp.get(ProfessorListActivity.PROFNAME));
                mAnimals.add("Department: "+resultp.get(ProfessorListActivity.DEPARTMENT));
                Details();


            }
        });
        return itemView;
    }

    public void Details()
    {



        //Create sequence of items
        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        dialogBuilder.setTitle("Details");
        dialogBuilder.setPositiveButton("Got it.", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete

            }
        });


        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();  //Selected item in listview


            }
        });


        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }
}


