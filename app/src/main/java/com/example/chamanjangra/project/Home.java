package com.example.chamanjangra.project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.LogRecord;



public class Home extends AppCompatActivity {
    Button button, button2, button3,button4;
    TextView textview, textview2, textView3;
    // SQLiteDatabase sqLiteDatabase;
    GpsTracker gps;
    Calendar calendar = Calendar.getInstance();

    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textview = (TextView) findViewById(R.id.textView);
        textview2 = (TextView) findViewById(R.id.loct);
        textView3 = (TextView) findViewById(R.id.addr);
        //   sqLiteDatabase=openOrCreateDatabase("project",MODE_PRIVATE,null);
        button = (Button) findViewById(R.id.button5);
        button2 = (Button) findViewById(R.id.button7);
        button3 = (Button) findViewById(R.id.button6);
        button4 = (Button) findViewById(R.id.see);
        sharedPreferences = getSharedPreferences("myshared", Context.MODE_PRIVATE);
        String a = sharedPreferences.getString("value", String.valueOf(0));
        final int eid = sharedPreferences.getInt("eid", -1);
        // Cursor c=sqLiteDatabase.rawQuery("select * from projectreg where email='"+a+"'",null);
        gps = new GpsTracker(Home.this);

        //for date
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String dat=df.format(calendar.getTime());

        textview.setText("Hey " + a);

       //url to check for buttons
        new JsonGet().execute("http://fine-friday.000webhostapp.com/apis/checkapi.php?eid="+eid+"&date="+dat+"&submit=Submit");


        //textview2.setText("lat:" + gps.getLatitude() + "\nlong:" + gps.getLongitude() + "\n" + df.format(calendar.getTime()));
               textView3.setText(getCompleteAddressString(gps.getLatitude(), gps.getLongitude()));
               // if(gps.getLatitude()>28.546 && gps.getLatitude()<28.548 && gps.getLongitude()>77.270 && gps.getLongitude()<77.272){
//               if (gps.getLatitude() > 28.674 && gps.getLatitude() < 28.677 && gps.getLongitude() > 77.047 && gps.getLongitude() < 77.050) {
//                    button2.setEnabled(true);
//                    button3.setEnabled(true);
//                }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("myshared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("value");
                editor.apply();
                startActivity(new Intent(Home.this, Login.class));
                finish();
            }
        });

        final String loc=getCompleteAddressString(gps.getLatitude(), gps.getLongitude());

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("swipe","swipein");
                editor.putString("location",loc);
                editor.apply();
                Intent intent = new Intent(Home.this, MainActivity.class);
                            intent.putExtra("btnvalue", "swipein");
                            startActivity(intent);
                            finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("swipe","swipeout");
                editor.putString("location",loc);
                editor.apply();

                Intent intent = new Intent(Home.this, MainActivity.class);
                intent.putExtra("btnvalue", "swipeout");
                startActivity(intent);
                finish();

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,GetAttendance.class));
            }
        });


    }



    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
                strAdd = "No Address returned!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
            strAdd = "Can't get Address!";
        }
        return strAdd;
    }

    public class JsonGet extends AsyncTask<String ,String ,String>{
        HttpURLConnection connection=null;
        BufferedReader reader=null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);

                }
                String finaljson=stringBuffer.toString();
                JSONObject jsonObject=new JSONObject(finaljson);
                final String swipei=jsonObject.getString("swipei");
                final String swipeo=jsonObject.getString("swipeo");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         if(gps.getLatitude()>28.546 && gps.getLatitude()<28.548 && gps.getLongitude()>77.270 && gps.getLongitude()<77.272){

                       // if (gps.getLatitude() > 28.674 && gps.getLatitude() < 28.677 && gps.getLongitude() > 77.047 && gps.getLongitude() < 77.050) {

                            if (swipei == "true") {
                                if (swipeo == "true") {
                                    button2.setEnabled(false);
                                    button3.setEnabled(false);
                                } else {
                                    button3.setEnabled(true);
                                }
                            } else {
                                button2.setEnabled(true);
                            }

                       }
                    }
                });
                return swipei+"----"+swipeo;
            }catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
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
            return null ;
        }

//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
//        }
    }
}


