package axis.com.vioslip.Admin.Violators;

/**
 * Created by Guren on 10/11/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import axis.com.vioslip.Admin.JSONfunctions;
import axis.com.vioslip.Admin.Violations.ViolationsList;
import axis.com.vioslip.R;


public class ViolatorsListActivity extends AppCompatActivity {
    // Declare Variables
    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    ViolatorsAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    static String VIOLATOR_ID = "violatorid";
    static String USERID = "userid";
    static String VIOLATIONAME  = "violationame";
    static String PNAME = "pname";
    static String STATUS = "status";
    private Toolbar toolbar;


    //  static String PICTURE = "pic";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.admin_violators_list);


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setBackgroundDrawable(new ColorDrawable(0xff1560bd));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Violators's List");
        }


        // Execute DownloadJSON AsyncTask
        new DownloadJSON().execute();




    }

    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, Help.class);
                startActivity(intent);
                return true;
            case R.id.action_tips:
                Intent intent1 = new Intent(MainActivity.this, Tips.class);
                startActivity(intent1);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    // DownloadJSON AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViolatorsListActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Vioslip");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array
            arraylist = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL("https://dmexia.000webhostapp.com/android/violatorslist.php");

            try {
                // Locate the array name in JSON
                jsonarray = jsonobject.getJSONArray("Violators");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put("violatorid", jsonobject.getString("violatorid"));
                    map.put("violationame", jsonobject.getString("violationame"));
                    map.put("userid", jsonobject.getString("userid"));
                    map.put("pname", jsonobject.getString("pname"));
                    map.put("status",jsonobject.getString("status"));

                   // String user = jsonobject.getString("userlevel");




                        // Set the JSON Objects into the array
                        arraylist.add(map);




                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void args) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview_violator);
            // Pass the results into ListViewAdapter.java
            adapter = new ViolatorsAdapter(ViolatorsListActivity.this, arraylist);
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

}



