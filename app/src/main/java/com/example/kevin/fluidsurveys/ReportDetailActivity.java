package com.example.kevin.fluidsurveys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ReportDetailActivity extends AppCompatActivity {
    String id_assignment, id_quest, encodeImage;
    TextView tvImage, tvQuest, tvLocation;
    EditText edAnswer;
    ImageView ivImage;
    Button btnImage, btnSubmit;
    Double lon, lat;

    private static final int RESULT_SELECT_IMAGE = 1;
    private static final int INITIAL_REQUEST=1337;
    private static final String TAG = "Debug";

    private LocationManager locationManager =null;
    private LocationListener locationListener=null;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        id_assignment = getIntent().getStringExtra("ASSIGNMENT_ID");
        id_quest = getIntent().getStringExtra("QUEST_ID");
        String question = getIntent().getStringExtra("QUESTION");
        tvImage = (TextView) findViewById(R.id.tvImage);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvQuest = (TextView) findViewById(R.id.tvQuest);
        edAnswer = (EditText) findViewById(R.id.edAnswer);
        btnImage = (Button) findViewById(R.id.btnImage);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        ivImage = (ImageView) findViewById(R.id.ivImage);

        tvLocation.setVisibility(View.GONE);
        tvQuest.setText(question);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
//        locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new MyLocationListener();
//
//        locationManager.requestLocationUpdates(LocationManager
//                .GPS_PROVIDER, 5000, 10,locationListener);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = edAnswer.getText().toString();
                new setReport().execute(answer, encodeImage, "1", "jpg");
            }
        });
    }

    class setReport extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public setReport(){
            ProgressDialog = new ProgressDialog(ReportDetailActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Submitting Report...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ProgressDialog.dismiss();
            String result="";

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(s);
                result = jsonObject.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProgressDialog.dismiss();
            if(result.equals("1")){
                Intent intent = new Intent(ReportDetailActivity.this, ReportQuestActivity.class);
                intent.putExtra("ASSIGNMENT_ID", id_assignment);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(ReportDetailActivity.this, s, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String answer = params[0];
            String img = params[1];
            String id = params[2];
            String img_ext = params[3];

            String create_url = "http://fluidsurveys.dapatbuku.com/api/assignment/report/detail/create";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("assignment_quest_id", "UTF-8")+"="+URLEncoder.encode(id_quest,"UTF-8") + "&" +
                                URLEncoder.encode("assignment_report_id", "UTF-8") + "=" + URLEncoder.encode(id_assignment, "UTF-8") + "&" +
                                URLEncoder.encode("answer", "UTF-8") + "=" + URLEncoder.encode(answer, "UTF-8") + "&" +
                                URLEncoder.encode("img", "UTF-8") + "=" + URLEncoder.encode(img, "UTF-8") + "&" +
                                URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                                URLEncoder.encode("img_ext", "UTF-8") + "=" + URLEncoder.encode(img_ext, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while((line = bufferedReader.readLine())!= null){
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString().trim();
        }
    }

//    private class MyLocationListener implements LocationListener {
//
//        @Override
//        public void onLocationChanged(Location loc) {
//            tvLocation.setText("");
//            Toast.makeText(
//                    getBaseContext(),
//                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//            String longitude = "Longitude: " + loc.getLongitude();
//            Log.v(TAG, longitude);
//            String latitude = "Latitude: " + loc.getLatitude();
//            Log.v(TAG, latitude);
//
//            /*------- To get city name from coordinates -------- */
//            String cityName = null;
//            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//            List<Address> addresses;
//            try {
//                addresses = gcd.getFromLocation(loc.getLatitude(),
//                        loc.getLongitude(), 1);
//                if (addresses.size() > 0) {
//                    System.out.println(addresses.get(0).getLocality());
//                    cityName = addresses.get(0).getLocality();
//                }
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                    + cityName;
//            lon = loc.getLongitude();
//            lat = loc.getLatitude();
//            String location = getAddress(loc.getLatitude(),loc.getLongitude());
//            tvLocation.setText(location);
//
//            return;
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {}
//
//        @Override
//        public void onProviderEnabled(String provider) {}
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//    }
//
//    public String getAddress(double lat, double lng) {
//        String ada = new String();
//        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
//            Address obj = addresses.get(0);
//            ada = obj.getAddressLine(0);
//            //ada = ada + "\n" + obj.getCountryName();
//            //ada = ada + "\n" + obj.getCountryCode();
//            //ada = ada + "\n" + obj.getAdminArea();
//            //ada = ada + "\n" + obj.getPostalCode();
//            //ada = ada + "\n" + obj.getSubAdminArea();
//            //ada = ada + "\n" + obj.getLocality();
//            //ada = ada + "\n" + obj.getSubThoroughfare();
//
//            Log.v("IGA", "Address" + ada);
//            // Toast.makeText(this, "Address=>" + add,
//            // Toast.LENGTH_SHORT).show();
//
//            // TennisAppActivity.showDialog(add);
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        return ada;
//    }
//
//    private Boolean displayGpsStatus() {
//        ContentResolver contentResolver = getBaseContext()
//                .getContentResolver();
//        boolean gpsStatus = Settings.Secure
//                .isLocationProviderEnabled(contentResolver,
//                        LocationManager.GPS_PROVIDER);
//        if (gpsStatus) {
//            return true;
//
//        } else {
//            return false;
//        }
//    }

    //function to select a image
    private void selectImage(){
        //open album to select image
        Intent gallaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallaryIntent, RESULT_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SELECT_IMAGE && resultCode == RESULT_OK && data != null){
            //set the selected image to image variable
            Uri image = data.getData();
            ivImage.setImageURI(image);

            Bitmap images = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            images.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */
            encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
            tvImage.setText(encodeImage);
            //get the current timeStamp and strore that in the time Variable
//            Long tsLong = System.currentTimeMillis() / 1000;
//            timestamp = tsLong.toString();

//            Toast.makeText(getApplicationContext(),timestamp,Toast.LENGTH_SHORT).show();
        }
    }
}
