package axis.com.vioslip.Admin.Violations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import axis.com.vioslip.R;

/**
 * Created by Guren on 10/11/2017.
 */

public class ViolationSingleView extends AppCompatActivity {

    TextView ViolationName , ViolationType , Sanction;
    ImageView bigpic;
    String vioname, viotype , sanct , qrcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_violation_single);

        ViolationName = (TextView)findViewById(R.id.tx_vioname);
        ViolationType = (TextView)findViewById(R.id.tx_viotype);
        Sanction = (TextView)findViewById(R.id.tx_sanct);
        ImageView img = (ImageView)findViewById(R.id.img_big);

        ImageLoader imageLoader = new ImageLoader(this);
        Intent i = getIntent();
        // Get the result of rank
        vioname = i.getStringExtra("vioname");
        // Get the result of country
        viotype = i.getStringExtra("viotype");
        // Get the result of population
        sanct = i.getStringExtra("sanct");
        qrcode = i.getStringExtra("pic");

        ViolationName.setText("Violation Name: "+vioname);
        ViolationType.setText("Violation Type:"+viotype);
        Sanction.setText("Sanction: "+sanct);
        imageLoader.DisplayImage(qrcode,img);


    }
}
