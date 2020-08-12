package com.example.chamanjangra.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SuccessAuth extends AppCompatActivity {

    Button button;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss");
    GpsTracker gpsTracker;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_auth);
        button=(Button) findViewById(R.id.button8);
        gpsTracker=new GpsTracker(SuccessAuth.this);

        sharedPreferences = getSharedPreferences("myshared", Context.MODE_PRIVATE);
        String table = sharedPreferences.getString("swipe", String.valueOf(0));
        int eid=sharedPreferences.getInt("eid",-1);
        String loc=sharedPreferences.getString("location",String.valueOf(0));
        double lat=gpsTracker.getLatitude();
        double lon=gpsTracker.getLongitude();

        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String dat=df.format(new Date());
        final String currentDateTimeString = sdf.format(calendar.getTime());
        //Toast.makeText(getApplicationContext(),table+"   "+eid+" "  + dat  +"--"+currentDateTimeString+"\n"+loc,Toast.LENGTH_SHORT).show();
        new markAttend().execute("http://fine-friday.000webhostapp.com/apis/"+table+"api.php?eid="+eid+"&date="+dat+"&time="+currentDateTimeString+"&lat="+lat+"&long="+lon+"&stat=present&submit=Submit");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessAuth.this,Home.class));
                finish();
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SuccessAuth.this,Home.class));
        finish();
    }

    public class markAttend extends AsyncTask<String,String,String>
    {
        HttpURLConnection connection=null;
        BufferedReader reader=null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                connection=(HttpURLConnection)url.openConnection();

                connection.connect();
                InputStream inputStream=connection.getInputStream();
                reader= new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer=new StringBuffer();
                String line="";

                while((line=reader.readLine())!=null)
                {
                    stringBuffer.append(line);
                }
                String finaljson=stringBuffer.toString();
                JSONObject jsonObject=new JSONObject(finaljson);
                return null;
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
