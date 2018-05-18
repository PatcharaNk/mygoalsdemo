package com.example.harleyhihew.mygoalsdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Harleyhihew on 5/29/2016.
 */
public class GoalsDb {

    private GoalsDbHelper helper;
    private SQLiteDatabase db;


    //Constructor
    public GoalsDb(Context context) {
        helper = new GoalsDbHelper(context);
    }

    //open
    public void open() {
        db = helper.getWritableDatabase();
    }

    //close
    public void close() {
        helper.close();
    }


    //Add Goal
    public void addGoal(GoalListItem goalListItem) {
        GoalListItem newGoalListItem = new GoalListItem();
        newGoalListItem = goalListItem;

        ContentValues values = new ContentValues();
        values.put(helper.TABLE_KEY_GOALS, goalListItem.getGoalText());
        this.db.insert(helper.TABLE_NAME, null, values);
        Log.d("Goal Demo ::::", "Add OK !!!");

    }

    //Add Mustdo in Goal
    public void addMustDo(String goal, MustDoItem mustDoItem) {
        ContentValues values = new ContentValues();
        values.put(helper.TABLE_KEY_GOALS, goal);
        values.put(helper.TABLE_KEY_MUSTDO, mustDoItem.getMustDoText());
        this.db.insert(helper.TABLE_NAME, null, values);
        Log.d("Goal Demo ::::", "Add OK !!!");
    }

    //Update Goal
    public void updateGoal(String oldGoal, String newGoal) {
        ContentValues values = new ContentValues();
        values.put(helper.TABLE_KEY_GOALS, newGoal);
        String where = String.format("%s = '%s'", helper.TABLE_KEY_GOALS, oldGoal);
        this.db.update(helper.TABLE_NAME, values, where, null);
    }

    //Update MustDo
    public void updateMustDo(String goal, String newMustDo, MustDoItem mustDoItem) {
        ContentValues values = new ContentValues();
        values.put(helper.TABLE_KEY_MUSTDO, newMustDo);
        String where = String.format("%s = '%s' AND %s = '%s'", helper.TABLE_KEY_GOALS, goal, helper.TABLE_KEY_MUSTDO, mustDoItem.getMustDoText());
        this.db.update(helper.TABLE_NAME, values, where, null);
    }

    //Update Percent
    public void updatePercent(String goal, float percent) {
        ContentValues values = new ContentValues();
        values.put(helper.TABLE_KEY_PERCENT, percent);
        String where = String.format("%s = '%s'", helper.TABLE_KEY_GOALS, goal);
        this.db.update(helper.TABLE_NAME, values, where, null);
    }

    //Update Check Must Do
    public void updateCheckMustDo(String goal, MustDoItem mustDoItem) {
        int values;
        if (mustDoItem.getCheckMustDo() == 0) {
            values = 1;
        } else {
            values = 0;
        }
        String where = String.format("%s = '%s' AND %s = '%s'", helper.TABLE_KEY_GOALS, goal, helper.TABLE_KEY_MUSTDO, mustDoItem.getMustDoText());
        String updateSQL = String.format("UPDATE %s SET %s = %d WHERE %s",helper.TABLE_NAME,helper.TABLE_KEY_CHECK,values,where);
        db.execSQL(updateSQL);
    }


    //Delete Goal
    public void deleteGoal(String goal) {
        String deleteGoal = String.format("DELETE FROM %s WHERE %s= '%s' ", helper.TABLE_NAME, helper.TABLE_KEY_GOALS, goal);
        db.execSQL(deleteGoal);
    }

    //Delete Mustdo in Goal
    public void deleteMustDo(String goal, MustDoItem mustDoItem) {
        String deleteMustDo = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s'", helper.TABLE_NAME, helper.TABLE_KEY_GOALS, goal, helper.TABLE_KEY_MUSTDO, mustDoItem.getMustDoText());
        db.execSQL(deleteMustDo);
    }

    //Select All Goals
    public ArrayList<GoalListItem> selectAllGoals() {
        ArrayList<GoalListItem> goalsList = new ArrayList<GoalListItem>();
        Cursor cursor = db.rawQuery(String.format("SELECT * ,MIN(%s) FROM %s GROUP BY %s ORDER BY %s ASC", helper.TABLE_KEY_ID, helper.TABLE_NAME, helper.TABLE_KEY_GOALS, helper.TABLE_KEY_ID), null);
        cursor.moveToFirst();
        GoalListItem goalListItem;
        while (!cursor.isAfterLast()) {
            goalListItem = new GoalListItem();
            goalListItem.setPercent(cursor.getInt(cursor.getColumnIndex(helper.TABLE_KEY_PERCENT)));
            goalListItem.setId(cursor.getInt(cursor.getColumnIndex(helper.TABLE_KEY_ID)));
            goalListItem.setGoalText(cursor.getString(cursor.getColumnIndex(helper.TABLE_KEY_GOALS)));
            goalsList.add(goalListItem);
            cursor.moveToNext();
        }
        cursor.close();
        return goalsList;
    }

    //Select All Must Do form goal
    public ArrayList<MustDoItem> selectMustdoFormGoal(String goal) {
        ArrayList<MustDoItem> mustDoList = new ArrayList<MustDoItem>();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s' AND %s <> '' ORDER BY %S ASC", helper.TABLE_NAME, helper.TABLE_KEY_GOALS, goal, helper.TABLE_KEY_MUSTDO,helper.TABLE_KEY_CHECK), null);
        cursor.moveToFirst();
        MustDoItem mustDoItem;
        while (!cursor.isAfterLast()) {
            mustDoItem = new MustDoItem();
            mustDoItem.setCheckMustDo(cursor.getInt(cursor.getColumnIndex(helper.TABLE_KEY_CHECK)));
            mustDoItem.setMustDoText(cursor.getString(cursor.getColumnIndex(helper.TABLE_KEY_MUSTDO)));
            mustDoList.add(mustDoItem);
            cursor.moveToNext();
        }
        cursor.close();
        return mustDoList;
    }

    //Select Goal From Id
    public String selectGoalFormId(GoalListItem goalListItem) {
        String currentGoal;
        Cursor cursor = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s = %d", helper.TABLE_KEY_GOALS, helper.TABLE_NAME, helper.TABLE_KEY_ID, goalListItem.getId()), null);
        cursor.moveToFirst();
        currentGoal = cursor.getString(cursor.getColumnIndex(helper.TABLE_KEY_GOALS));
        return currentGoal;
    }

    //Select percent Form goal
    public float selectPercent(String goal) {
        float percent;
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s'", helper.TABLE_NAME, helper.TABLE_KEY_GOALS, goal), null);
        cursor.moveToFirst();
        percent = cursor.getFloat(cursor.getColumnIndex(helper.TABLE_KEY_PERCENT));
        return percent;
    }

    //Select MustDo is Done
    public ArrayList<MustDoItem> selectMustDoIsDone(String goal){
        ArrayList<MustDoItem> mustDoList = new ArrayList<MustDoItem>();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s' AND %s <> '' AND %s = 1 ", helper.TABLE_NAME, helper.TABLE_KEY_GOALS, goal, helper.TABLE_KEY_MUSTDO,helper.TABLE_KEY_CHECK), null);
        cursor.moveToFirst();
        MustDoItem mustDoItem;
        while (!cursor.isAfterLast()) {
            mustDoItem = new MustDoItem();
            mustDoItem.setCheckMustDo(cursor.getInt(cursor.getColumnIndex(helper.TABLE_KEY_CHECK)));
            mustDoItem.setMustDoText(cursor.getString(cursor.getColumnIndex(helper.TABLE_KEY_MUSTDO)));
            mustDoList.add(mustDoItem);
            cursor.moveToNext();
        }
        cursor.close();
        return mustDoList;
    }

    public void setPercent(String goal) {
        float allMustDo = selectMustdoFormGoal(goal).size();
        float doneMustDo = selectMustDoIsDone(goal).size();

        if (allMustDo != 0 && doneMustDo!= 0 ){
            updatePercent(goal,(doneMustDo*100/allMustDo));
        }else updatePercent(goal, 0);

    }

}
