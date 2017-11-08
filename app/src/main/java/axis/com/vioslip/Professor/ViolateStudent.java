package axis.com.vioslip.Professor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import axis.com.vioslip.Admin.JSONfunctions;
import axis.com.vioslip.JSONParser;
import axis.com.vioslip.R;

/**
 * Created by Guren on 9/13/2017.
 */

public class ViolateStudent extends AppCompatActivity {

    private Toolbar toolbar;
    Button select_vio ,submit;

    JSONObject jsonobject;
    JSONArray jsonarray;


    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    AlertDialog levelDialog;

    List<String> listItems = new ArrayList<String>();
    String selectedviolation= "";
    String toinput; // violationname
    String pname;
    String str_userid = "Nothing";
    String str_fullname;
    String fname , lname , mname;
    EditText ed_sid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/register.php";

    //testing on Emulator:
    private static final String LOGIN_URL = "http://dmexia.000webhostapp.com/android/violatesomeone.php";

    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/register.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_violatestudent);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setBackgroundDrawable(new ColorDrawable(0xff1560bd));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create Violation for Student");
        }

        Bundle extras = getIntent().getExtras();
        pname = extras.getString("pname").replace("Name:","");

        select_vio = (Button)findViewById(R.id.btn_select_vio);
        submit = (Button)findViewById(R.id.btn_submit);
        ed_sid = (EditText)findViewById(R.id.ed_studentfullname);



        select_vio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listItems.clear();
                new ParseViolation().execute();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] seperated = selectedviolation.split(":");
                String wew = seperated[1];
                toinput = wew.replace("Type","").replace("\n","");

                // full name of the student
               str_fullname =  ed_sid.getText().toString();

                try{
                    String[] separated = str_fullname.split(" ");
                    fname = separated[0]; // FIRST NAME
                    lname = separated[1]; // LAST NAME
                    mname = separated[2]; // MIDDLE NAME
                    fname.replaceAll(" ","");
                    lname.replaceAll(" ","");
                    mname.replaceAll(" ","");
                }
                catch (Exception e){

                    str_fullname =  ed_sid.getText().toString();
                }


                new CheckStudent().execute();



            }
        });

    }

    // Parse Violation
    private class ParseViolation extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViolateStudent.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Vioslip");
            // Set progressdialog message
            mProgressDialog.setMessage("Parsing Violations");
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
                    .getJSONfromURL("https://dmexia.000webhostapp.com/android/violationlist.php");

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
                    map.put("sanctionname",jsonobject.getString("sanctionname"));
                    map.put("sanctionsched",jsonobject.getString("sanctionsched"));
                    map.put("location",jsonobject.getString("location"));


                    String vname = "Violation Name: "+jsonobject.getString("violationname")+"\n Type: "+jsonobject.getString("violationtype")+"\n Sanction: "+jsonobject.getString("sanctionname");
                    listItems.add(vname);








                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void args) {


            // Close the progressdialog
            mProgressDialog.dismiss();
            ViolationList();

        }
    }

    // VIOLATION NAMES
    public void ViolationList(){

        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);


        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Violation");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

              selectedviolation =  listItems.get(item);

              //  tx_violationtype.setText();

                Toast.makeText(ViolateStudent.this, selectedviolation, Toast.LENGTH_LONG).show();
                select_vio.setText(selectedviolation);

                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();

    }

    // ADDDING VIOLATOR
    private class Violator extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViolateStudent.this);
            pDialog.setMessage("Submitting...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            String viostatus = "pending";


            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("studfullname", str_fullname));
                params.add(new BasicNameValuePair("userid", str_userid));
                params.add(new BasicNameValuePair("violationame", toinput));
                params.add(new BasicNameValuePair("pname", pname));
                params.add(new BasicNameValuePair("status", viostatus));


                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(ViolateStudent.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

    // CHECK IF THE STUDENT IS EXISTING
    private class CheckStudent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViolateStudent.this);
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
                    .getJSONfromURL("https://dmexia.000webhostapp.com/android/studentjson.php");

            try {
                // Locate the array name in JSON
                jsonarray = jsonobject.getJSONArray("Students");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects


                    String studfname = jsonobject.getString("studfname");
                    String studlname = jsonobject.getString("studlname");
                    String studmname = jsonobject.getString("studmname");

                    // Set the JSON Objects into the array
                    if(studfname.equals(fname) && studlname.equals(lname) && studmname.equals(mname)){


                        str_userid = jsonobject.getString("userid");
                    }

                }

            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            if(str_userid.equals("Nothing")){

                Toast.makeText(ViolateStudent.this,"Student Not Found",Toast.LENGTH_LONG).show();

            }
            else {

                // adding of violator
                new Violator().execute();
            }


        }
    }

}
