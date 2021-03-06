package com.example.kevin.fluidsurveys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ReportQuestActivity extends AppCompatActivity {

    String id_assignment;
    TextView tvEmpty;
    ListView lvReportQuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_quest);

        id_assignment = getIntent().getStringExtra("ASSIGNMENT_ID");

        tvEmpty = (TextView) findViewById(R.id.tvEmpty);
        lvReportQuest = (ListView) findViewById(R.id.lvReportQuest);

        tvEmpty.setVisibility(View.GONE);

        new getReportQuest().execute();
    }

    public class ListAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> id, question, created;

        private LayoutInflater inflater=null;
        public ListAdapter(android.app.Activity activity, ArrayList<String> id, ArrayList<String> question, ArrayList<String> created) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.id = id;
            this.question = question;
            this.created = created;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return question.size();
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
            TextView tvQuestion, tvCreated;
            LinearLayout llView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listquest, null);
            holder.tvQuestion =(TextView) rowView.findViewById(R.id.tvQuestion);
            holder.tvCreated =(TextView) rowView.findViewById(R.id.tvCreated);
            holder.llView =(LinearLayout) rowView.findViewById(R.id.llView);

            holder.tvQuestion.setText(question.get(position));
            holder.tvCreated.setText(created.get(position));

            holder.llView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ReportQuestActivity.this, ReportDetailActivity.class);
                    intent.putExtra("QUEST_ID", id.get(position));
                    intent.putExtra("ASSIGNMENT_ID", id_assignment);
                    intent.putExtra("QUESTION", question.get(position));
                    startActivity(intent);
                }
            });

            return rowView;
        }
    }

    class getReportQuest extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getReportQuest(){
            ProgressDialog = new ProgressDialog(ReportQuestActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Loading Report Quest...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject;

            ProgressDialog.dismiss();
            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("questions");

                ArrayList<String> id = new ArrayList<>();
                ArrayList<String> created = new ArrayList<>();
                ArrayList<String> question = new ArrayList<>();
                for(int i = 0; i<result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);
                    id.add(jo.getString("id"));
                    created.add(jo.getString("created_at"));
                    question.add(jo.getString("question"));
                }
                if(result.length()==0) tvEmpty.setVisibility(View.VISIBLE);
                else{
                    ListAdapter listAdapter = new ListAdapter(ReportQuestActivity.this, id, question, created);
                    lvReportQuest.setAdapter(listAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tvEmpty.setText(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://fluidsurveys.dapatbuku.com/api/assignment/quest/get/all";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id_assignment,"UTF-8");
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
