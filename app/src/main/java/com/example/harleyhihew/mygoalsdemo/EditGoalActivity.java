package com.example.harleyhihew.mygoalsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Goal");

        final GoalListItem goalListItem  = (GoalListItem) getIntent().getSerializableExtra(("oldGoal"));
        final String goal = goalListItem.getGoalText();
        final EditText editText = (EditText) findViewById(R.id.editGoal_editText);
        editText.setText(goal);

        Button editBtn = (Button) findViewById(R.id.editGoal_Button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newGoal = String.valueOf(editText.getText());
                goalListItem.setGoalText(newGoal);
                GoalsDb goalsDb = new GoalsDb(getApplicationContext());
                goalsDb.open();
                goalsDb.updateGoal(goal, newGoal);
                goalsDb.close();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
