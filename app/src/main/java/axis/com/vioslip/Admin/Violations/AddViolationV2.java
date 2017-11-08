package axis.com.vioslip.Admin.Violations;

/**
 * Created by Guren on 10/10/2017.
 */

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ImageView;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.UnsupportedEncodingException;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jaredrummler.materialspinner.MaterialSpinner;

import axis.com.vioslip.R;

public class AddViolationV2 extends AppCompatActivity {

    private  Toolbar toolbar;
    String str_violationname="" , str_violationtype , str_sanctioname , str_sanctionsched , str_location;
    EditText ed_vname  , ed_sname , ed_ssched, ed_loc;


    // QR CODE GENERATOR WIDTH
    public final static int QRcodeWidth = 500 ;


    Bitmap bitmap;
    boolean check = true;
    ImageView image;


    ProgressDialog progressDialog ;

    String violation_name = "violation_name";
    String violation_type = "violation_type";
    String sanction_name = "sanction_name";
    String sanction_sched = "sanction_sched";
    String location = "location";
    String ImagePath = "image_path" ; // QR Code

    String ServerUploadPath ="http://dmexia.000webhostapp.com/android/qrcode/qrcode_upload.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_violation_add);


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setBackgroundDrawable(new ColorDrawable(0xffe74c3c));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Violation");
        }
        // Spinner
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner_violationtype);
        spinner.setItems("Choose Violation Type","Minor", "Major", "Grave");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();

                str_violationtype  = item;
            }
        });
        // EDIT TEXT
        ed_vname = (EditText)findViewById(R.id.ed_violationname);
        ed_sname = (EditText)findViewById(R.id.ed_sanctionname);
        ed_ssched = (EditText)findViewById(R.id.ed_santionsched);
        ed_loc = (EditText)findViewById(R.id.ed_location);


        Button btn_submit = (Button)findViewById(R.id.btn_admin_add_vio);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(str_violationtype.equals("Choose Violation Type")){

                    Snackbar.make(v, "Choose Violation Type", Snackbar.LENGTH_LONG).show();

                }
                else {
                    str_violationname = ed_vname.getText().toString();
                    str_sanctioname = ed_sname.getText().toString();
                    str_sanctionsched = ed_ssched.getText().toString();
                    str_location = ed_loc.getText().toString();

                    Toast.makeText(AddViolationV2.this, str_sanctioname, Toast.LENGTH_LONG).show();


                    try{

                        bitmap = TextToImageEncode(str_violationname);
                        generateQRCode();
                    }
                    catch (Exception e){

                        Toast.makeText(AddViolationV2.this, "Failed to create QR Code", Toast.LENGTH_LONG).show();

                    }





                }

            }
        });



        /*
        UploadImageServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetImageNameEditText = imageName.getText().toString();

                ImageUploadToServerFunction();

            }
        });
        */
    }



    public void ImageUploadToServerFunction(){

        // For get Drawable from Image
        Drawable d = image.getDrawable();

        // For Convert Drawable to Bitmap
        Bitmap bitmapOrg = ((BitmapDrawable)d).getBitmap();

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmapOrg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(AddViolationV2.this,"QR Code is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(AddViolationV2.this,string1,Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.



            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(violation_name, str_violationname);
                HashMapParams.put(violation_type, str_violationtype);
               HashMapParams.put(sanction_name, str_sanctioname);
               HashMapParams.put(sanction_sched, str_sanctionsched);
                 HashMapParams.put(location, str_location);
                HashMapParams.put(ImagePath, ConvertImage); // QR CODE

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(40000);

                httpURLConnectionObject.setConnectTimeout(40000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }


    }
    private void generateQRCode(){ // Popup QR Code

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setPositiveButton("Create Violation", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ImageUploadToServerFunction();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.admin_violation_add_qrcode, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);



        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                image.setImageBitmap(bitmap);
                Bitmap icon = BitmapFactory.decodeResource(getApplication().getResources(),
                        R.drawable.appbeta);
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                image.setLayoutParams(layoutParams);


            }
        });
        dialog.show();
    }


    Bitmap TextToImageEncode(String Value) throws WriterException { // STRING value the unique or the name of the qr code
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value, //A Data Matrix code is a two-dimensional barcode consisting of black and white "cells" or modules arranged in either a square or rectangular pattern, also known as a matrix.
                    BarcodeFormat.DATA_MATRIX.QR_CODE, // ito ung code sa paggawa ng qr code
                    QRcodeWidth, QRcodeWidth, null // qr code with is set to 500 px naka declare sa taas globe var
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) { // looping of the making of the qr code until it get 500 x 500 height and width
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor) : getResources().getColor(R.color.QRCodeWhiteColor);
                        // color of the qr code
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        // now make it into image by creating a butmap

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        // set pixel 500 by 500
        return bitmap;
    }
}
