package axis.com.vioslip.Admin.Violations;

/**
 * Created by Guren on 10/10/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import axis.com.vioslip.R;


public class ViolationsList extends AppCompatActivity{
    // Declare Variables
    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    static String VIOLATION_ID = "violationid";
    static String VIOLATION_NAME = "violationname";
    static String VIOLATION_TYPE = "violationtype";
    static String SANCTION_NAME = "sanctionname";
    static String SANCTION_SCHED = "sanctionsched";
    static String LOCATION = "location";
    static String QRCODE = "qrcode";
    Button addvio;
    String permission = "android.permission.READ_EXTERNAL_STORAGE";
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.admin_violation_list);

        // Execute DownloadJSON AsyncTask
        new DownloadJSON().execute();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setBackgroundDrawable(new ColorDrawable(0xFFF44336));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Violations");
        }




        int perm = PermissionChecker.checkSelfPermission(ViolationsList.this, permission);

        if (perm == PermissionChecker.PERMISSION_GRANTED) {
            // good to go

        } else {
            ActivityCompat.requestPermissions(ViolationsList.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            // permission not granted, you decide what to do
        }
        addvio = (Button)findViewById(R.id.add_vio);

        addvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ViolationsList.this, AddViolationV2.class);
                startActivity(i);
            }
        });
    }




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
            mProgressDialog = new ProgressDialog(ViolationsList.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parsing Violations");
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
                    .getJSONfromURL("http://dmexia.000webhostapp.com/android/violationlist.php");

            try {
                // Locate the array name in JSON
                jsonarray = jsonobject.getJSONArray("Violations");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put("violationid", jsonobject.getString("violationid"));
                    map.put("violationname", jsonobject.getString("violationname"));
                    map.put("violationtype", jsonobject.getString("violationtype"));
                    map.put("sanctionname",jsonobject.getString("sanctionsched"));
                    map.put("location",jsonobject.getString("location"));
                    map.put("qrcode",jsonobject.getString("qrcode"));


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
            listview = (ListView) findViewById(R.id.listview_violate);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(ViolationsList.this, arraylist);
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ViolationsList.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}

