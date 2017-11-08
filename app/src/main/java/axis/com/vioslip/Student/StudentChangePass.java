package axis.com.vioslip.Student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import axis.com.vioslip.Config;
import axis.com.vioslip.MainActivity;
import axis.com.vioslip.R;

/**
 * Created by Guren on 10/12/2017.
 */

public class StudentChangePass extends AppCompatActivity {

    String userid;
    String ppassword;
    EditText oldpass , newpass , confirmpass;
    Button btn_prof_change;
    String et_oldpass,et_newpass ,et_conpass;
    String finalpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stud_changepass);


        Intent intent = getIntent();
        userid = intent.getStringExtra("myuserid");
        ppassword = intent.getStringExtra("studpass");
        oldpass = (EditText)findViewById(R.id.ed_stud_oldpass);
        newpass = (EditText)findViewById(R.id.ed_stud_newpass);
        confirmpass = (EditText)findViewById(R.id.ed_stud_confirmpass);
        btn_prof_change = (Button)findViewById(R.id.btn_stud_change_submit);


        btn_prof_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_oldpass = oldpass.getText().toString();
                et_newpass = newpass.getText().toString();
                et_conpass = confirmpass.getText().toString();


                if(et_oldpass.equals(ppassword)){

                    if (et_newpass.equals(et_conpass)){

                        ChangePassProf();


                    }
                    else{

                        Toast.makeText(StudentChangePass.this,"Password does not match", Toast.LENGTH_SHORT).show();
                    }

                }
                else{

                    Toast.makeText(StudentChangePass.this,"Old password does not match", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    private void ChangePassProf(){



        class ChangePassProf extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(StudentChangePass.this, "Updating..", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //   loading.dismiss();
                Toast.makeText(StudentChangePass.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_USERID, userid);
                hashMap.put(Config.KEY_PASSWORD, et_newpass);


                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_PROF_CHANGEPASS, hashMap);
                return s;
            }
        }


        try{

            ChangePassProf ue = new ChangePassProf();
            ue.execute();
            finish();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(StudentChangePass.this);
            SharedPreferences.Editor edit = sp.edit();
            edit.clear();
            edit.apply();
            finish();
            Toast.makeText(StudentChangePass.this, "Password Changed Successfully Please Login Again", Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, MainActivity.class));

        }
        catch (Exception e){


        }


    }

}

