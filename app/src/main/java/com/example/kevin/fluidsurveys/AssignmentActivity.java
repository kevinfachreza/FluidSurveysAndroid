package com.example.kevin.fluidsurveys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AssignmentActivity extends AppCompatActivity {

    ListView lvAssignment;
    String id_project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id_project = getIntent().getStringExtra("PROJECT_ID");

        lvAssignment = (ListView) findViewById(R.id.lvAssignment);

        new GetAssignment().execute();

        FloatingActionButton addTask = (FloatingActionButton) findViewById(R.id.btnAddAssignment);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                Snackbar.make(view, "Add New Project", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void showDialog()
    {
        final AlertDialog builder = new AlertDialog.Builder(AssignmentActivity.this)
                .setView(R.layout.dialog_new_assignment)
                .setCancelable(true)
                .create();
        builder.show();

        final EditText edAssignmentId = (EditText) builder.findViewById(R.id.edAssignmentId);
        final EditText edAssignmentName = (EditText) builder.findViewById(R.id.edAssignmentName);
        final EditText edAssignmentDetail = (EditText) builder.findViewById(R.id.edAssignmentDetail);
        final EditText edAssignmentLocation = (EditText) builder.findViewById(R.id.edAssignmentLocation);
        Button btnSubmit = (Button) builder.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edAssignmentId.getText().toString();
                String name = edAssignmentName.getText().toString();
                String detail = edAssignmentDetail.getText().toString();
                String location = edAssignmentLocation.getText().toString();
                new NewAssignment().execute(id, name, detail, location);
                builder.dismiss();
            }
        });
    }

    class NewAssignment extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public NewAssignment(){
            ProgressDialog = new ProgressDialog(AssignmentActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Creating New Assignment...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")){
                Intent intent = new Intent(AssignmentActivity.this, AssignmentActivity.class);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(AssignmentActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id = params[0];
            String name = params[1];
            String detail = params[2];
            String location = params[3];

            String create_url = "http://mobile4day.com/rk/new_assignment.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"+
                                URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                                URLEncoder.encode("detail","UTF-8")+"="+URLEncoder.encode(detail,"UTF-8")+"&"+
                                URLEncoder.encode("location","UTF-8")+"="+URLEncoder.encode(location,"UTF-8")+"&"+
                                URLEncoder.encode("id_project","UTF-8")+"="+URLEncoder.encode(id_project,"UTF-8");
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
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString().trim();
        }
    }

    public class ListAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> id, name, location, detail, sub;

        private LayoutInflater inflater=null;
        public ListAdapter(android.app.Activity activity, ArrayList<String> name, ArrayList<String> id,
                           ArrayList<String> location, ArrayList<String> detail, ArrayList<String> sub) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.id = id;
            this.name = name;
            this.location = location;
            this.detail = detail;
            this.sub = sub;
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
            TextView tvSub,tvDetail,tvLocation,tvName;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listtask, null);
            holder.tvSub =(TextView) rowView.findViewById(R.id.tvSub);
            holder.tvDetail =(TextView) rowView.findViewById(R.id.tvDetail);
            holder.tvLocation =(TextView) rowView.findViewById(R.id.tvLocation);
            holder.tvName =(TextView) rowView.findViewById(R.id.tvName);

            holder.tvSub.setText(sub.get(position));
            holder.tvDetail.setText(detail.get(position));
            holder.tvLocation.setText(location.get(position));
            holder.tvName.setText(name.get(position));

            return rowView;
        }
    }

    class GetAssignment extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public GetAssignment(){
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
                JSONArray result = jsonObject.getJSONArray("result");

                ArrayList<String> id = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> location = new ArrayList<>();
                ArrayList<String> detail = new ArrayList<>();
                ArrayList<String> sub = new ArrayList<>();
                for(int i = 0; i<result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);
                    id.add(jo.getString("id"));
                    name.add(jo.getString("name"));
                    location.add(jo.getString("location"));
                    detail.add(jo.getString("detail"));
                    sub.add(jo.getString("sub"));
                }
                ListAdapter listAdapter = new ListAdapter(AssignmentActivity.this, name, id, location, detail, sub);
                lvAssignment.setAdapter(listAdapter);
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

            String create_url = "http://mobile4day.com/rk/get_assignment.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id_project", "UTF-8")+"="+URLEncoder.encode(id_project,"UTF-8");
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
