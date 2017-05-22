package com.example.kevin.fluidsurveys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Kevin on 5/22/2017.
 */

public class ExampleLogin  extends AppCompatActivity {
    private RequestQueue queue;
    private ScrollView mLoginFormView;
    private LinearLayout mProgressView;
    private EditText emailText;
    private EditText passwordText;
    private String hasil = "";
    private Boolean akhir = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        mLoginFormView = (ScrollView) findViewById(R.id.login_page);
//        mProgressView = (LinearLayout) findViewById(R.id.progress_page);
//        emailText = (EditText) findViewById(R.id.editTextEmail);
//        passwordText = (EditText) findViewById(R.id.editTextPassword);
        queue= Volley.newRequestQueue(this);
//        try {
//            JSONObject input = readFromFile(LoginActivity.this);
//            input.accumulate("token", showDeviceToken());
//        /*    mAuthTask = new UserLoginTask(input);
//            mAuthTask.execute((Void) null);
//        */
//            String url = getString(R.string.baseUrl)+"/homeintended";
//            showProgress(true);
//            JsonObjectRequest req= new JsonObjectRequest(Request.Method.GET, url, input,new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.w("hasil", "oakokaok"+response.toString());
//                    hasil = response.toString()+"coba";
//
//                    showProgress(false);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.w("login error", Log.getStackTraceString(error));
//                    showProgress(false);
//                }
//            });
//            req.setRetryPolicy(new DefaultRetryPolicy(
//                    2000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(req);
//            queue.start();
//            Log.w("udah kirim", "km kenapaaa seee");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



//        Button login = (Button) findViewById(R.id.buttonLogin);
//        TextView regis = (TextView) findViewById(R.id.regis);
//        TextView lupa = (TextView) findViewById(R.id.lupa);
//
//        lupa.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Lupa();
//            }
//        });
//        login.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                LoginUser();
//            }
//        });
//        regis.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                RegisterUser();
//            }
//        });

    }

    private void Lupa() {
//        Intent view = new Intent(this, LupaPassActivity.class);
//        startActivity(view);
    }

    private void RegisterUser() {
//        Intent view = new Intent(this, RegisterActivity.class);
//        startActivity(view);
    }

    private void LoginUser() {



//        try {
//            showProgress(true);
//            JSONObject input = new JSONObject();
//            input.accumulate("email_user", emailText.getText().toString().trim());
//            input.accumulate("pass_user", passwordText.getText().toString());
//            input.accumulate("token", showDeviceToken());
//
//        /*  mAuthTask = new UserLoginTask(input);
//            mAuthTask.execute((Void) null);
//        */
//            String url = getString(R.string.baseUrl)+"/homeintended";
//            JsonObjectRequest req= new JsonObjectRequest(Request.Method.GET, url, input,new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.w("hasil", "oakokaok"+response.toString());
//                    hasil = response.toString()+"coba";
//                    showProgress(false);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.w("login error", Log.getStackTraceString(error));
//                    showProgress(false);
//                }
//            });
//            req.setRetryPolicy(new DefaultRetryPolicy(
//                    2000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(req);
//            queue.start();
//            Log.w("udah kirim", "km kenapaaa seee");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

//    private String showDeviceToken() {
//        return FirebaseInstanceId.getInstance().getToken();
//    }


//    private JSONObject readFromFile(Context context) throws JSONException {
//
//        String ret = "";
//
//        try {
//            InputStream inputStream = context.openFileInput("info");
//
//            if ( inputStream != null ) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString;
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
//        }
//        catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        }
//
//        return new JSONObject(ret);
//    }

//    private void showProgress(final boolean show) {
//        // The ViewPropertyAnimator APIs are not available, so simply show
//        // and hide the relevant UI components.
//        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//    }
}
