package axis.com.vioslip.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import axis.com.vioslip.Admin.Prof.ProfessorListActivity;
import axis.com.vioslip.Admin.Student.StudentListActivity;
import axis.com.vioslip.Admin.Violations.AddViolationV2;
import axis.com.vioslip.Admin.Violations.ViolationsList;
import axis.com.vioslip.Admin.Violators.ViolatorsListActivity;
import axis.com.vioslip.MainActivity;
import axis.com.vioslip.R;

/**
 * Created by Guren on 9/5/2017.
 */

public class AdminActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Button btnstudent, btnprof, btnviolator , btnviolation;
    int backpress = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Administrator");
        }

        btnstudent = (Button)findViewById(R.id.btn_student);
        btnprof = (Button)findViewById(R.id.btn_prof);
        btnviolation = (Button)findViewById(R.id.btn_violation);
        btnviolator = (Button)findViewById(R.id.btn_violators);



       btnprof.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               startActivity(new Intent(AdminActivity.this, ProfessorListActivity.class));
           }
       });

        btnviolation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(AdminActivity.this, ViolationsList.class));
            }
        });
        btnviolator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AdminActivity.this, ViolatorsListActivity.class));
            }
        });
        btnstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AdminActivity.this, StudentListActivity.class));
            }
        });

    }


    public void onBackPressed(){
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), "Press back again to exit.", Toast.LENGTH_SHORT).show();

        if (backpress>1) {
            this.finish();
        }
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
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AdminActivity.this);
                SharedPreferences.Editor edit = sp.edit();
                edit.clear();
                edit.apply();
                finish();
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);

                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
