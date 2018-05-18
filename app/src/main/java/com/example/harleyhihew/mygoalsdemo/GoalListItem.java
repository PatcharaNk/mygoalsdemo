package com.example.harleyhihew.mygoalsdemo;

import java.io.Serializable;

/**
 * Created by Harleyhihew on 5/31/2016.
 */
public class GoalListItem implements Serializable{
    private String goalText;
    private int percent;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoalText() {
        return goalText;
    }

    public void setGoalText(String goalText) {
        this.goalText = goalText;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
