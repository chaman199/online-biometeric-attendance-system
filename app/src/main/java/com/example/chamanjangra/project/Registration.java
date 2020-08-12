package com.example.chamanjangra.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Registration extends AppCompatActivity {

    EditText ed3,ed4,ed5,ed6;
    Button btn3,btn4;
    SQLiteDatabase sqLiteDatabase;
    private final String TAG="msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ed3=(EditText) findViewById(R.id.editText3);
        ed4=(EditText) findViewById(R.id.editText4);
        ed5=(EditText) findViewById(R.id.editText5);
        ed6=(EditText) findViewById(R.id.editText6);
        btn3=(Button) findViewById(R.id.button3);
        btn4=(Button) findViewById(R.id.button4);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=ed3.getText().toString();
                String email=ed4.getText().toString();
                String password=ed5.getText().toString();
                String phone=ed6.getText().toString();
                Log.d(TAG,name+" "+email+" "+password+" "+phone);
                if(name.length()>0 && email.length()>0 && password.length()>0 && phone.length()>0) {
                    new jsonRegister().execute("http://fine-friday.000webhostapp.com/apis/fetchreg.php?name="+name+"&email="+email+"&password="+password+"&mobileno="+phone+"&sub=Submit");

//                    sqLiteDatabase = openOrCreateDatabase("project", MODE_PRIVATE, null);
//                    sqLiteDatabase.execSQL("create table if not exists projectreg(name varchar,email varchar primary key,password varchar,phone varchar)");
//                    Cursor cursor = sqLiteDatabase.rawQuery("select * from projectreg where email='"+email+"'", null);
//                    if (cursor.getCount() == 0) {
//                        sqLiteDatabase.execSQL("insert into projectreg values('" + name + "','" + email + "','" + password + "','" + phone + "')");
//                        Toast.makeText(getApplicationContext(),"Successfully inserted",Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                        Toast.makeText(getApplicationContext(),"email already exist",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Fill the form ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this,Login.class));
                finish();
            }
        });
    }
    class jsonRegister extends AsyncTask<String,String,String>{
        HttpURLConnection connection=null;
        BufferedReader bufferedReader=null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                String finaljson = stringBuffer.toString();

                JSONObject jsonObject = new JSONObject(finaljson);
                // JSONArray jsonArray=new JSONArray("result");

                //StringBuffer readdata=new StringBuffer();
                //for(int i=0;i<jsonArray.length();i++)
                // {
                //   JSONObject jsonObject1=jsonArray.getJSONObject(i);
                final String msg = jsonObject.getString("msg");
                // readdata.append(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
