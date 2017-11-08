package axis.com.vioslip.Student;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import axis.com.vioslip.Admin.JSONfunctions;
import axis.com.vioslip.MainActivity;
import axis.com.vioslip.R;

/**
 * Created by Guren on 9/5/2017.
 */

public class StudentActivity extends AppCompatActivity {

        TextView tx_student_number , tx_student_name;
        String Usernamepref, usernameobj;
        JSONObject jsonobject;
        JSONArray jsonarray;
    int usernotif;

    public static final int NOTIFICATION_ID = 1;

    String studentid, studentfname , studentmname, studentlname ,studentusername,studentpassword ;
    ProgressDialog mProgressDialog;
    ImageView dp_student;
    char firstletter;
    Button myvio ,studchange;

    ArrayList<HashMap<String, String>> arraylist;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.stud_main);

            tx_student_name = (TextView)findViewById(R.id.tx_student_name);
            tx_student_number = (TextView)findViewById(R.id.tx_student_number);
            dp_student = (ImageView)findViewById(R.id.img_dp);
            myvio = (Button)findViewById(R.id.btn_student_violations);
            studchange = (Button)findViewById(R.id.btn_student_changepass);

            myvio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(StudentActivity.this, MyViolationActivity.class);
                    intent.putExtra("myuserid",studentid);
                    startActivity(intent);
                  //  Toast.makeText(StudentActivity.this,studentid,Toast.LENGTH_LONG).show();
                }
            });
            studchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(StudentActivity.this, StudentChangePass.class);
                    intent.putExtra("myuserid",studentid);
                    intent.putExtra("studpass",studentpassword);
                    startActivity(intent);

                }
            });


            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            Usernamepref = sp.getString("Username", "anon");

      //      Toast.makeText(StudentActivity.this, Usernamepref,Toast.LENGTH_LONG).show();

               new UpdateJSON().execute();


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
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(StudentActivity.this);
                SharedPreferences.Editor edit = sp.edit();
                edit.clear();
                edit.apply();
                finish();
                Intent intent = new Intent(StudentActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    // Load profile
    private class UpdateJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(StudentActivity.this);
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


                   usernameobj = jsonobject.getString("username");

                    // Set the JSON Objects into the array


                    if (usernameobj.equals(Usernamepref)) {

                        studentid = jsonobject.getString("userid");
                        studentfname = jsonobject.getString("studfname");
                        studentmname = jsonobject.getString("studmname");
                        studentlname = jsonobject.getString("studlname");
                        studentusername = jsonobject.getString("username");
                        studentpassword = jsonobject.getString("password");

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
                tx_student_name.setText("Name: "+studentlname+" , "+studentfname+" , "+studentmname);
                tx_student_number.setText("Student Number: "+studentid);
                new  STUDNotif().execute();

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

    public void Usernotif() {

        // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //icon appears in device notification bar and right hand corner of notification
        builder.setSmallIcon(R.drawable.applogo);
        builder.setPriority(Notification.PRIORITY_HIGH);

        // This intent is fired when notification is clicked
        Intent intent = new Intent(this,MyViolationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.applogo));

        // Content title, which appears in large type at the top of the notification
        builder.setContentTitle("Vioslip");

        // Content text, which appears in smaller text below the title
        builder.setContentText("You have violated rule");

        // The subtext, which appears under the text on newer devices.
        // This will show-up in the devices with Android 4.2 and above only
        builder.setSubText("Tap Here to see");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // DownloadJSON AsyncTask
    private class STUDNotif extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(StudentActivity.this);
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
                  String userid = jsonobject.getString("userid");

                    if(userid.equals(studentid)){

                        usernotif = usernotif +  1;

                    }
                    // String user = jsonobject.getString("userlevel");



                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void args) {

            int old ;
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(StudentActivity.this);
            old = sp.getInt("old", 0);



            if(usernotif > old){
                old = usernotif;

                Usernotif();

                SharedPreferences notif = PreferenceManager.getDefaultSharedPreferences(StudentActivity.this);
                SharedPreferences.Editor edit = notif.edit();
                edit.remove("old");
                edit.putInt("old",old);
                edit.apply();




                mProgressDialog.dismiss();


            }


            // Close the progressdialog
            mProgressDialog.dismiss();
            /*
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview_violator);
            // Pass the results into ListViewAdapter.java
            adapter = new ViolatorsAdapter(ViolatorsListActivity.this, arraylist);
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            */
        }
    }

}
