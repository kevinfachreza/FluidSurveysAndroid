package com.example.kevin.fluidsurveys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    ListView lvProject;
    String USER_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvProject = (ListView) findViewById(R.id.lvProject);
        new GetProject().execute();
    }

    public class ListAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> id, name, company;

        private LayoutInflater inflater=null;
        public ListAdapter(android.app.Activity activity, ArrayList<String> id, ArrayList<String> name,
                           ArrayList<String> company) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.id = id;
            this.name = name;
            this.company = company;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return name.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView tvCompany,tvName;
            LinearLayout llView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listproject, null);
            holder.llView = (LinearLayout) rowView.findViewById(R.id.llView);
            holder.tvCompany =(TextView) rowView.findViewById(R.id.tvCompany);
            holder.tvName =(TextView) rowView.findViewById(R.id.tvName);

            holder.tvCompany.setText(company.get(position));
            holder.tvName.setText(name.get(position));

            holder.llView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, AssignmentActivity.class);
                    intent.putExtra("PROJECT_ID", id.get(position));
                    startActivity(intent);
                }
            });

            return rowView;
        }
    }

    class GetProject extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public GetProject(){
            ProgressDialog = new ProgressDialog(HomeActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Loading Project List...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject;

            ProgressDialog.dismiss();
            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("project");

                final ArrayList<String> id = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> company = new ArrayList<>();
                for(int i = 0; i<result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);
                    id.add(jo.getString("id"));
                    name.add(jo.getString("name"));
                    company.add(jo.getString("company"));
                }
                ListAdapter listAdapter = new ListAdapter(HomeActivity.this, id, name, company);
                lvProject.setAdapter(listAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://fluidsurveys.dapatbuku.com/api/project/get/all";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(USER_ID,"UTF-8");
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}