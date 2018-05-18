package com.example.harleyhihew.mygoalsdemo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Harleyhihew on 5/31/2016.
 */
public class GoalListView extends BaseAdapter {
    private static Activity activity;
    private static LayoutInflater inflater;
    ArrayList<GoalListItem> myGoalList;

    public GoalListView(Activity activity, ArrayList<GoalListItem> myGoalList) {
        this.myGoalList = myGoalList;
        this.activity = activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return myGoalList.size();
    }

    @Override
    public GoalListItem getItem(int position) {
        return myGoalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myGoalList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        v = inflater.inflate(R.layout.goal_list_layout, null);
        TextView textView = (TextView)v.findViewById(R.id.goal_textView);
        TextView progressText = (TextView) v.findViewById(R.id.percent_textView);
        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.goal_progressBar);
        GoalListItem goalListItem = myGoalList.get(position);
        textView.setText(goalListItem.getGoalText());
        progressText.setText(goalListItem.getPercent()+"%");
        progressBar.setProgress(goalListItem.getPercent());
        return v;
    }

    public String getGoalText(int position){
        return myGoalList.get(position).getGoalText();
    }
}
