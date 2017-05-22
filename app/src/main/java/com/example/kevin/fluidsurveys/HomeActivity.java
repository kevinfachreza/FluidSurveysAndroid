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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ListView lvProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvProject = (ListView) findViewById(R.id.lvProject);
        new GetProject().execute("prasetyon");

        FloatingActionButton addEmp = (FloatingActionButton) findViewById(R.id.btnAddEmp);
        addEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("Employee");
                Snackbar.make(view, "Add New Employee", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton addTask = (FloatingActionButton) findViewById(R.id.btnAddProject);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("Project");
                Snackbar.make(view, "Add New Project", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void showDialog(String process)
    {
        if(process.equals("Project")){
            final AlertDialog builder = new AlertDialog.Builder(HomeActivity.this)
                    .setView(R.layout.dialog_new_project)
                    .setCancelable(true)
                    .create();
            builder.show();

            final EditText edProjectId = (EditText) builder.findViewById(R.id.edProjectId);
            final EditText edProjectName = (EditText) builder.findViewById(R.id.edProjectName);
            final EditText edProjectDetail = (EditText) builder.findViewById(R.id.edProjectDetail);
            final EditText edProjectStart = (EditText) builder.findViewById(R.id.edProjectStart);
            final EditText edProjectFinish = (EditText) builder.findViewById(R.id.edProjectFinish);
            Button btnSubmit = (Button) builder.findViewById(R.id.btnSubmit);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = edProjectId.getText().toString();
                    String name = edProjectName.getText().toString();
                    String detail = edProjectDetail.getText().toString();
                    String start = edProjectStart.getText().toString();
                    String finish = edProjectFinish.getText().toString();
                    new NewProject().execute(id, name, detail, start, finish);
                    builder.dismiss();
                }
            });
        }
        else if(process.equals("Employee")){
            final AlertDialog builder = new AlertDialog.Builder(HomeActivity.this)
                    .setView(R.layout.dialog_new_emp)
                    .setCancelable(true)
                    .create();
            builder.show();

            final EditText edEmpId = (EditText) builder.findViewById(R.id.edEmpId);
            final EditText edEmpName = (EditText) builder.findViewById(R.id.edEmpName);
            final EditText edEmpAddress = (EditText) builder.findViewById(R.id.edEmpAddress);
            final EditText edEmpPhone = (EditText) builder.findViewById(R.id.edEmpPhone);
            final EditText edEmpEmail = (EditText) builder.findViewById(R.id.edEmpEmail);
            Button btnSubmit = (Button) builder.findViewById(R.id.btnSubmit);


            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = edEmpId.getText().toString();
                    String name = edEmpName.getText().toString();
                    String address = edEmpAddress.getText().toString();
                    String phone = edEmpPhone.getText().toString();
                    String email = edEmpEmail.getText().toString();

                    new NewEmployee().execute(id, name, address, phone, email);
                    builder.dismiss();
                }
            });
        }
    }

    class NewProject extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public NewProject(){
            ProgressDialog = new ProgressDialog(HomeActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Creating New Project...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")){
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();
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
            String start = params[3];
            String finish = params[4];
            String username= "prasetyon";

            String create_url = "http://mobile4day.com/rk/new_project.php";
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
                                URLEncoder.encode("start","UTF-8")+"="+URLEncoder.encode(start,"UTF-8")+"&"+
                                URLEncoder.encode("finish","UTF-8")+"="+URLEncoder.encode(finish,"UTF-8")+"&"+
                                URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
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

    class NewEmployee extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public NewEmployee(){
            ProgressDialog = new ProgressDialog(HomeActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Adding Employee...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")){
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();
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
            String address = params[2];
            String phone = params[3];
            String email = params[4];

            String create_url = "http://mobile4day.com/rk/new_employee.php";
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
                                URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"+
                                URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                                URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
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
            LinearLayout llView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listtask, null);
            holder.llView = (LinearLayout) rowView.findViewById(R.id.llView);
            holder.tvSub =(TextView) rowView.findViewById(R.id.tvSub);
            holder.tvDetail =(TextView) rowView.findViewById(R.id.tvDetail);
            holder.tvLocation =(TextView) rowView.findViewById(R.id.tvLocation);
            holder.tvName =(TextView) rowView.findViewById(R.id.tvName);

            holder.tvSub.setText(sub.get(position));
            holder.tvDetail.setText(detail.get(position));
            holder.tvLocation.setText(location.get(position));
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
//            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
//            ProgressDialog.dismiss();
            super.onPostExecute(s);
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                final ArrayList<String> id = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> location = new ArrayList<>();
                ArrayList<String> detail = new ArrayList<>();
                ArrayList<String> sub = new ArrayList<>();
                for(int i = 0; i<result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);
                    id.add(jo.getString("id"));
                    name.add(jo.getString("name"));
                    location.add("");
                    detail.add(jo.getString("detail"));
                    sub.add(jo.getString("sub"));
                }
                ListAdapter listAdapter = new ListAdapter(HomeActivity.this, name, id, location, detail, sub);
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

            String username = params[0];

            String create_url = "http://mobile4day.com/rk/get_project.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
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
