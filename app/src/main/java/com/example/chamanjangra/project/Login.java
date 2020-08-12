package com.example.chamanjangra.project;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class Login extends AppCompatActivity {

    EditText ed1,ed2;
    Button btn1,btn2;
    ProgressBar progressbar;
    SQLiteDatabase sqLiteDatabase;
    private final String TAG="msg";
    Handler handler;
    SharedPreferences sharedPreferences;
    private static final int RECORD_REQUEST_CODE = 101;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);
        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
       // startService(new Intent(getBaseContext(),NotificationService.class));
        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET};
        if(!hasPermission(this,permissions))
        {
            ActivityCompat.requestPermissions(this,permissions,RECORD_REQUEST_CODE);
        }
        if(!isConnected())
        {
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },3000);
            Toast.makeText(getApplicationContext(),"Please connect to the Internet",Toast.LENGTH_SHORT).show();

        }
       sharedPreferences = getSharedPreferences("myshared", Context.MODE_PRIVATE);
        String a = sharedPreferences.getString("value", "0");
//        String a ="0";
        if (a != String.valueOf(0)) {
            //Toast.makeText(getApplicationContext(),a,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, Home.class));
            finish();
        }
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed1.getText().toString();
                String password = ed2.getText().toString();
                Log.d(TAG, email + " " + password);

                if (email.length() > 0 && password.length() > 0) {

                        new jsonRegister().execute("http://fine-friday.000webhostapp.com/apis/logapi.php?id=" + email + "&pass=" + password + "&sub=Submit");

//                    sqLiteDatabase = openOrCreateDatabase("project", MODE_PRIVATE, null);
//                    Cursor cursor = sqLiteDatabase.rawQuery("select * from projectreg where email='" + email + "'", null);
//                    cursor.moveToFirst();
//
//                    Log.d(TAG, "" + cursor.getCount());
//                    String pas = cursor.getString(2);
//                    String emai = cursor.getString(1);
//                    if (cursor.getCount() == 1) {
//                        Log.d(TAG, cursor.getString(2) + "\n");
//                        if (password.equals(cursor.getString(2))) {
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("value", emai);
//                            editor.apply();
//                            startActivity(new Intent(Login.this, Home.class));
//                            finish();
//                        } else {
//                            Log.d(TAG, password + " " + pas);
//                            Toast.makeText(getApplicationContext(), " Wrong Password", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Email doesnot exist", Toast.LENGTH_SHORT).show();
//                    }

                } else {
                    Toast.makeText(getApplicationContext(), "fill the values", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registration.class));
                finish();
            }
        });
    }

    private class jsonRegister extends AsyncTask<String,String,String> {
        HttpURLConnection httpURLConnection=null;
        BufferedReader reader=null;
        InputStream inputStream;

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream=httpURLConnection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer=new StringBuffer();
                String line="";
                while((line=reader.readLine())!=null)
                {
                    stringBuffer.append(line);
                }
                String finalString=stringBuffer.toString();

                JSONObject jsonObject=new JSONObject(finalString);
                // JSONArray jsonArray=new JSONArray();

                //  StringBuffer readdata=new StringBuffer();
                //for(int i=0;i<jsonArray.length();i++)
                //{
                //   JSONObject jsonObject1=jsonArray.getJSONObject(i);
                final int error=jsonObject.getInt("error");
                final String msg=jsonObject.getString("msg");
                final String mail=jsonObject.getString("email");
                final int eid=jsonObject.getInt("eid");

//                    readdata.append(error);
                if(error==0)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//                            pDialog = new ProgressDialog(Login.this);
//                            pDialog.setMessage("Getting data ...");
//                            pDialog.setIndeterminate(false);
//                            pDialog.setCancelable(true);
//                            pDialog.show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("value", mail);
                            editor.putInt("eid",eid);
                            editor.apply();
                            Intent i=new Intent(Login.this, Home.class);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });}
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                //             }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                if(httpURLConnection!=null)
                {
                    httpURLConnection.disconnect();
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
    public static boolean hasPermission(Context context,String... permissions)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null)
        {
            for(String permission:permissions)
                if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED)
                {
                    return false;

                }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                return;
        }
    }
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean NisConnected = activeNetwork != null && activeNetwork.isConnected();
        if (NisConnected) {
            //  if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE || activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
            else
                return false;
        }
        return false;
    }


}
