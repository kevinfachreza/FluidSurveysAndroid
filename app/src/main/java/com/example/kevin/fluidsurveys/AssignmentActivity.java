package com.example.kevin.fluidsurveys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import static android.view.View.GONE;

public class AssignmentActivity extends AppCompatActivity {

    ListView lvAssignment;
    String id_project;
    TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        id_project = getIntent().getStringExtra("PROJECT_ID");
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);
        tvEmpty.setVisibility(GONE);

        lvAssignment = (ListView) findViewById(R.id.lvAssignment);

        new getAssignment().execute();
    }

    public class ListAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> id, name, location, detail;

        private LayoutInflater inflater=null;
        public ListAdapter(android.app.Activity activity, ArrayList<String> id, ArrayList<String> name, ArrayList<String> detail,
                           ArrayList<String> location) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.id = id;
            this.name = name;
            this.location = location;
            this.detail = detail;
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
            TextView tvDetail,tvLocation,tvName;
            LinearLayout llView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listassignment, null);
            holder.tvDetail =(TextView) rowView.findViewById(R.id.tvDetail);
            holder.tvName =(TextView) rowView.findViewById(R.id.tvName);
            holder.tvLocation =(TextView) rowView.findViewById(R.id.tvLocation);
            holder.llView =(LinearLayout) rowView.findViewById(R.id.llView);

            holder.tvDetail.setText(detail.get(position));
            holder.tvLocation.setText(location.get(position));
            holder.tvName.setText(name.get(position));

            holder.llView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AssignmentActivity.this, ReportQuestActivity.class);
                    intent.putExtra("ASSIGNMENT_ID", id.get(position));
                    startActivity(intent);
                }
            });

            return rowView;
        }
    }

    class getAssignment extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getAssignment(){
            ProgressDialog = new ProgressDialog(AssignmentActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Loading Assignment List...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject;

            ProgressDialog.dismiss();
            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("assignment");

                ArrayList<String> id = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> location = new ArrayList<>();
                ArrayList<String> detail = new ArrayList<>();
                for(int i = 0; i<result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);
                    id.add(jo.getString("id"));
                    name.add(jo.getString("title"));
                    location.add(jo.getString("place_name")+" - "+jo.getString("place_address"));
                    detail.add(jo.getString("desc"));
                }
                if(result.length()==0) tvEmpty.setVisibility(View.VISIBLE);
                else{
                    ListAdapter listAdapter = new ListAdapter(AssignmentActivity.this, id, name, detail, location);
                    lvAssignment.setAdapter(listAdapter);
                }
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

            String create_url = "http://fluidsurveys.dapatbuku.com/api/assignment/get/all";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("project", "UTF-8")+"="+URLEncoder.encode(id_project,"UTF-8");
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
}