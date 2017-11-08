package axis.com.vioslip.Professor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import axis.com.vioslip.Admin.JSONfunctions;
import axis.com.vioslip.MainActivity;
import axis.com.vioslip.R;
import axis.com.vioslip.Student.StudentActivity;

/**
 * Created by Guren on 9/5/2017.
 */

public class ProfActivity extends AppCompatActivity {


    TextView profname , profdep;
    ImageView profdp;

    JSONObject jsonobject;
    JSONArray jsonarray;

    String pid, pname , pdepartment , pusername , ppassword ;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;

    String Usernamepref, usernameobj;

    private Toolbar toolbar;

    Button btn_violate ,changecreden,btn_view_vio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setBackgroundDrawable(new ColorDrawable(0xff1560bd));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Professor");
        }


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Usernamepref = sp.getString("Username", "anon");

        profname = (TextView)findViewById(R.id.tx_prof_name);
        profdep = (TextView)findViewById(R.id.tx_profdep);
        profdp = (ImageView)findViewById(R.id.img_dp_prof);
        btn_view_vio = (Button)findViewById(R.id.btn_prof_view);
        new UpdateJSON().execute();


        btn_violate = (Button)findViewById(R.id.btn_violate_student);
        changecreden = (Button)findViewById(R.id.btn_change_credential);

        btn_violate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfActivity.this, ViolateStudent.class);
                intent.putExtra("pname",profname.getText().toString());
                startActivity(intent);

            }
        });
        changecreden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfActivity.this, ProfChangePass.class);
                intent.putExtra("pid",pid);
                intent.putExtra("password",ppassword);
                startActivity(intent);
            }
        });
        btn_view_vio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfActivity.this, ViolationListProf.class);
           //     intent.putExtra("pname",profname.getText().toString());
                startActivity(intent);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ProfActivity.this);
                SharedPreferences.Editor edit = sp.edit();
                edit.clear();
                edit.apply();
                finish();
                Intent intent = new Intent(ProfActivity.this, MainActivity.class);
                startActivity(intent);

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private class UpdateJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ProfActivity.this);
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
                    .getJSONfromURL("https://dmexia.000webhostapp.com/android/professorlist.php");

            try {
                // Locate the array name in JSON
                jsonarray = jsonobject.getJSONArray("Prof");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects


                    usernameobj = jsonobject.getString("username");
                    // Set the JSON Objects into the array


                    if (usernameobj.equals(Usernamepref)) {
                       pid = jsonobject.getString("userid");
                       ppassword = jsonobject.getString("password");
                       pname = jsonobject.getString("pname");
                       pdepartment = jsonobject.getString("pdepartment");

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

            if(mProgressDialog != null){

                mProgressDialog.dismiss();
               profname.setText("Name:"+pname);
                profdep.setText("Department:"+pdepartment);

            }
            //   mUpContact.setText(upcontact);

            /*
            firstletter = studentlname.charAt(0);
            String firststring = Character.toString(firstletter);
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable txtdrawable = TextDrawable.builder().buildRoundRect(firststring,color1,10);
            dp_student.setImageDrawable(txtdrawable);
            */


        }
    }




}
