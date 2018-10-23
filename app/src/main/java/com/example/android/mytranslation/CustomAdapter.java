package com.example.android.mytranslation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by android on 29/8/18.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    int flags[];
    String[] langName;
    LayoutInflater inflter;
    public CustomAdapter(Context applicationContext, int[] flags,String[] langName){
        this.context = applicationContext;
        this.flags = flags;
        this.langName=langName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_items, null);
        ImageView icon = (ImageView) convertView.findViewById(R.id.imageView);
        TextView names = (TextView) convertView.findViewById(R.id.textView);
        icon.setImageResource(flags[position]);
        names.setText(langName[position]);
        return convertView;
    }
}
