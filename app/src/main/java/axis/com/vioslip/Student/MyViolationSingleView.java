package axis.com.vioslip.Student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import axis.com.vioslip.Admin.JSONfunctions;
import axis.com.vioslip.Config;
import axis.com.vioslip.R;


/**
 * Created by Guren on 10/10/2017.
 */

public class MyViolationSingleView extends AppCompatActivity {

    private  Toolbar toolbar;
    TextView vioname , viopname , viostatus;
    String pname , violationname , status;
    Button cqrcode ,vioinfos;
    String userid;

    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    JSONObject jsonobject;
    JSONArray jsonarray;

    // VIOLATION INFO
    List<String> mAnimals = new ArrayList<String>();

    // QR CODE
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stud_myviolation_singleview);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Violation Single View");
        }
        // Declaration
        vioname = (TextView)findViewById(R.id.myviosingle_violationname);
        viopname = (TextView)findViewById(R.id.myviosingle_pname);
        viostatus = (TextView)findViewById(R.id.myviosingle_status);
        cqrcode = (Button) findViewById(R.id.btn_qrcode);
        vioinfos = (Button)findViewById(R.id.btn_vioinfo);

        // INTENT GET EXTRA
        Intent intent = getIntent();
        violationname = intent.getStringExtra("violationname");
        pname = intent.getStringExtra("pname");
        status = intent.getStringExtra("status");
        userid = intent.getStringExtra("userid");
        // SET TEXT
        vioname.setText("Violation Name: "+violationname);
        viopname.setText("Professor who reported: "+pname);
        viostatus.setText("Status: "+status);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        cqrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

        vioinfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ParseViolation().execute();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    String qrcodes = obj.getString("name");




                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                //    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                    if(violationname.contains(
                            result.getContents())){
                        Toast.makeText(this, "et viola! ", Toast.LENGTH_LONG).show();
                        updatever();

                    }
                    else{

                        Toast.makeText(this, "Did not match", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // UPDATE VIO
    // Verified User
    private void updatever(){



            class UpdateEmployee extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(MyViolationSingleView.this, "Updating..", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(MyViolationSingleView.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(Config.KEY_USERID, userid);
                    hashMap.put(Config.KEY_VERIFIED, "Cleared");


                    RequestHandler rh = new RequestHandler();
                    String s = rh.sendPostRequest(Config.URL_UPDATE_VIO, hashMap);
                    return s;
                }
            }



            UpdateEmployee ue = new UpdateEmployee();
            ue.execute();
            finish();
            startActivity(new Intent(this, StudentActivity.class));

        }



    // DIALOG FOR VIOLATION INFORMATION
    public void ViolationInformation()
    {




        //Create sequence of items
        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyViolationSingleView.this, R.style.MyDialogTheme);
        dialogBuilder.setTitle("Violation Information");

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

    // Parse Violation
    private class ParseViolation extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MyViolationSingleView.this);
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
                    String violationname1 = jsonobject.getString("violationname");

                    if(violationname.contains(violationname1)){
                        mAnimals = new ArrayList<String>();
                        String vioname1 = jsonobject.getString("violationname");
                        String viotype1 = jsonobject.getString("violationtype");
                        String sanct1 = jsonobject.getString("sanctionname");
                        String sanctsched1 = jsonobject.getString("sanctionsched");
                        String location = jsonobject.getString("location");

                        mAnimals.add("Violation Name: "+vioname1);
                        mAnimals.add("Violation Type: "+viotype1);
                        mAnimals.add("Sanction: "+sanct1);
                        mAnimals.add("Sanction Schedule: "+sanctsched1);
                        mAnimals.add("Location: "+location);

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


            // Close the progressdialog
            mProgressDialog.dismiss();
            ViolationInformation();


        }
    }


}
