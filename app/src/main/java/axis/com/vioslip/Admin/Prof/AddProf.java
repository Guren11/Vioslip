package axis.com.vioslip.Admin.Prof;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import axis.com.vioslip.JSONParser;
import axis.com.vioslip.R;

/**
 * Created by Guren on 9/11/2017.
 */

public class AddProf extends AppCompatActivity {

    private Toolbar toolbar;
    EditText pname , pusername , ppassword ,pconfirmpass;

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
    private static final String LOGIN_URL = "https://dmexia.000webhostapp.com/android/addprof.php";

    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/register.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String str_pname , str_pdep , str_username , str_password , str_confirmpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_prof_add);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setBackgroundDrawable(new ColorDrawable(0xff1560bd));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Professor");
        }

        pname = (EditText)findViewById(R.id.prof_add_pname);
        pusername = (EditText)findViewById(R.id.prof_add_username);
        ppassword = (EditText)findViewById(R.id.prof_add_pass);
        pconfirmpass = (EditText)findViewById(R.id.ed_admin_prof_confirm);
        Button btn_submit = (Button)findViewById(R.id.btn_admin_addprof);

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.department_spinner);
        spinner.setItems("Choose Department","IT/CS Department", "Engineering Department", "HRM Department", "Tourism Department");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {


            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();

                str_pdep  = item;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_password = ppassword.getText().toString();
                str_confirmpass = pconfirmpass.getText().toString();

                if(!str_password.equals(str_confirmpass)){

                    Snackbar.make(v, "Password does not match", Snackbar.LENGTH_LONG).show();

                }
                else if(str_pdep.equals("Choose Department")){

                    Snackbar.make(v, "Please choose department", Snackbar.LENGTH_LONG).show();
                }
                else{
                    str_username = pusername.getText().toString();
                    str_pname = pname.getText().toString();
                    str_password = ppassword.getText().toString();



                    new CreateUser().execute();

                }



            }
        });

    }


    private class CreateUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddProf.this);
            pDialog.setMessage("Registering Professor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            String userlevel = "prof";


            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", str_username));
                params.add(new BasicNameValuePair("password", str_password));
                params.add(new BasicNameValuePair("pname", str_pname));
                params.add(new BasicNameValuePair("pdepartment", str_pdep));
                params.add(new BasicNameValuePair("userlevel", userlevel));

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
                Toast.makeText(AddProf.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

}
