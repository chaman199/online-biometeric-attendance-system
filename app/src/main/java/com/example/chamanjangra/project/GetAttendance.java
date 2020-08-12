package com.example.chamanjangra.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GetAttendance extends AppCompatActivity {
    ListView listView;
    ArrayList<String> dat=new ArrayList<>();
    ArrayList<String> timin=new ArrayList<>();
    ArrayList<String> timout=new ArrayList<>();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_attendance);
        sharedPreferences = getSharedPreferences("myshared", Context.MODE_PRIVATE);
        final int eid = sharedPreferences.getInt("eid", -1);
       // final String swipe=sharedPreferences.getString("swipeapi",String.valueOf(0));
        new jsonGetAttend().execute("http://fine-friday.000webhostapp.com/apis/apiforattend.php?eid="+eid+"&submit=Submit");

    }

    public class jsonGetAttend extends AsyncTask<String,String,String>{
        HttpURLConnection connection=null;
        BufferedReader reader=null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                connection=(HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream inputStream=connection.getInputStream();

                reader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer=new StringBuffer();
                String line="";

                while((line=reader.readLine())!=null)
                {
                    stringBuffer.append(line);
                }
                String finaljson=stringBuffer.toString();
                JSONObject jsonObject=new JSONObject(finaljson);

                JSONArray jsonArray=jsonObject.getJSONArray("swipein");
                JSONArray jsonArray1=jsonObject.getJSONArray("swipeout");
                int i=0;
                JSONObject jsonObject1,jsonObject2;
                for(i=0;i<jsonArray.length();i++)
                {
                    jsonObject1=jsonArray.getJSONObject(i);
                    jsonObject2=jsonArray1.getJSONObject(i);
                    dat.add(i,jsonObject1.getString("date"));
                    timin.add(i,jsonObject1.getString("time"));
                    timout.add(i,jsonObject2.getString("time"));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CustomArray customArray=new CustomArray(GetAttendance.this,dat,timin,timout);
                        listView=(ListView) findViewById(R.id.listview);
                        listView.setAdapter(customArray);
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                if(connection!=null)
                {
                    connection.disconnect();
                }
                if(reader!=null)
                {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }
}