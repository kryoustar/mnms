package com.example.project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterTwo extends BaseAdapter {

    private Context mContext;
    private String[]  Title;
    ImageView i1;
    public CustomAdapterTwo(Context context, ArrayList<String> text1) {
        mContext = context;
        Title = text1.toArray(new String[0]);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Title.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row;
        row = inflater.inflate(R.layout.row, parent, false);
        TextView title;


        title = (TextView) row.findViewById(R.id.txtTitle);
        title.setText(Title[position]);

        int drawable = mContext.getResources().
                getIdentifier("profile_" + (position+1),"drawable",mContext.getPackageName());
        i1 = (ImageView) row.findViewById(R.id.imgIcon);
        i1.setImageResource(drawable);

        return (row);
    }
}