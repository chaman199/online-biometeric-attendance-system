package com.example.chamanjangra.project;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chamanjangra on 06/08/2017.
 */

public class CustomArray extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> dat;
    private final ArrayList<String> timin;
    private final ArrayList<String> timout;
    public CustomArray(Activity context, ArrayList<String> dat, ArrayList<String> timin,ArrayList<String> timout) {
        super(context, R.layout.custom_layout,dat);
        this.context = context;
        this.dat = dat;
        this.timin = timin;
        this.timout = timout;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(R.layout.custom_layout,null,true);
        TextView textView=(TextView) view.findViewById(R.id.datetext);
        TextView textView1=(TextView) view.findViewById(R.id.timetextin);
        TextView textView2=(TextView) view.findViewById(R.id.timetextout);
        textView.setText(dat.get(position));
        textView1.setText("  "+timin.get(position));
        textView2.setText("   "+timout.get(position));
        return view;
    }
}
