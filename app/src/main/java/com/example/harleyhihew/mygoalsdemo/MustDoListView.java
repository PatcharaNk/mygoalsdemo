package com.example.harleyhihew.mygoalsdemo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Harleyhihew on 6/1/2016.
 */
public class MustDoListView extends BaseAdapter {
    private static Activity activity;
    private static LayoutInflater inflater;
    ArrayList<MustDoItem> myMustDoList;

    public MustDoListView(Activity activity, ArrayList<MustDoItem> myMustDoList) {
        this.myMustDoList = myMustDoList;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myMustDoList.size();
    }

    @Override
    public Object getItem(int position) {
        return myMustDoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myMustDoList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        v = inflater.inflate(R.layout.must_do_list_layout, null);
        TextView mustDoText = (TextView) v.findViewById(R.id.mustDo_textView);
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.mustDo_checkBox);
        MustDoItem mustDoItem = myMustDoList.get(position);
        mustDoText.setText(mustDoItem.getMustDoText());
        if (mustDoItem.getCheckMustDo() == 1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        return v;
    }
}
