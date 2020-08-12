package com.example.chamanjangra.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeeAttendance extends AppCompatActivity {
    Button button,button1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_attendance);
        button=(Button) findViewById(R.id.btn1);
        button1=(Button) findViewById(R.id.btn2);
        sharedPreferences = getSharedPreferences("myshared", Context.MODE_PRIVATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("swipeapi","swipein");
                editor.apply();
                startActivity(new Intent(SeeAttendance.this,GetAttendance.class));
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("swipeapi","swipeout");
                editor.apply();
                startActivity(new Intent(SeeAttendance.this,GetAttendance.class));
            }
        });
    }
}
