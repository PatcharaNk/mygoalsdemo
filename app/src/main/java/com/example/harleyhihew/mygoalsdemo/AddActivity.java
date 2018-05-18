package com.example.harleyhihew.mygoalsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Goal");

        //add new goal
        final EditText newGoalText = (EditText) findViewById(R.id.addGoal_editText);
        Button  addGoalBtn = (Button) findViewById(R.id.addGoal_Button);
        addGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoalListItem goalListItem = new GoalListItem();
                goalListItem.setGoalText(String.valueOf(newGoalText.getText()));

                GoalsDb goalsDb = new GoalsDb(getApplicationContext());
                goalsDb.open();
                goalsDb.addGoal(goalListItem);
                goalsDb.close();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
