package axis.com.vioslip.Student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import axis.com.vioslip.JSONParser;
import axis.com.vioslip.R;

/**
 * Created by Guren on 9/6/2017.
 */

public class StudentRegister extends AppCompatActivity {

    private Toolbar toolbar;

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
    private static final String LOGIN_URL = "https://dmexia.000webhostapp.com/android/register.php";

    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/register.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    TextInputLayout TIfname , TImname , TIlname , TIusername , TIpassword;
    EditText sfname , smname , slname , username , password ,confirmpass;
    Button register_student;
    String str_sfname , str_smname , str_slname, str_username , str_password,str_confirmpass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register Student");
        }


        TIfname = (TextInputLayout)findViewById(R.id.ed_layout_sfname);
        TImname = (TextInputLayout)findViewById(R.id.ed_layout_smname);
        TIlname = (TextInputLayout)findViewById(R.id.ed_layout_slname);
        TIusername = (TextInputLayout)findViewById(R.id.ed_layout_username);
        TIpassword = (TextInputLayout)findViewById(R.id.ed_layout_password);

        sfname = (EditText)findViewById(R.id.ed_sfname);
        smname = (EditText)findViewById(R.id.ed_smname);
        slname = (EditText)findViewById(R.id.ed_slname);
        username = (EditText)findViewById(R.id.ed_username);
        password = (EditText)findViewById(R.id.ed_password);
        confirmpass = (EditText)findViewById(R.id.ed_confirmpass);
        register_student = (Button)findViewById(R.id.button_rstudent);

        register_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_sfname = sfname.getText().toString();
                str_smname = smname.getText().toString();
                str_slname = slname.getText().toString();
                str_username = username.getText().toString();
                str_password = password.getText().toString();
                str_confirmpass = confirmpass.getText().toString();


                if (str_password.equals(str_confirmpass)){

                    Confirmation();

                }
                else{


                    Snackbar.make(v,"Password does not match", Snackbar.LENGTH_LONG).show();
                }



            }
        });



    }


    private void Confirmation() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StudentRegister.this,R.style.MyDialogTheme);

        alertDialogBuilder.setTitle("Vioslip");
        alertDialogBuilder.setIcon(R.drawable.alerdialog);
        alertDialogBuilder.setMessage("Are you sure you want to register?");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Yes
                new CreateUser().execute();


            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                // No

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }


    private class CreateUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StudentRegister.this);
            pDialog.setMessage("Registering Student...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            String userlevel = "student";


            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", str_username));
                params.add(new BasicNameValuePair("password", str_password));
                params.add(new BasicNameValuePair("studfname", str_sfname));
                params.add(new BasicNameValuePair("studmname", str_smname));
                params.add(new BasicNameValuePair("studlname", str_slname));
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
                Toast.makeText(StudentRegister.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }


}