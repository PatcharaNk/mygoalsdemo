package com.example.harleyhihew.mygoalsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddMustDoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_must_do);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Must Do");

        //add must go
        final String currentgoal = getIntent().getStringExtra("currentGoal");
        final EditText newMustDoText = (EditText) findViewById(R.id.addMustDo_editText);
        final Button addMustDoBtn = (Button) findViewById(R.id.addMustDo_Button);
        addMustDoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MustDoItem mustDoItem = new MustDoItem();
                mustDoItem.setMustDoText(String.valueOf(newMustDoText.getText()));

                GoalsDb goalsDb = new GoalsDb(getApplicationContext());
                goalsDb.open();
                goalsDb.addMustDo(currentgoal,mustDoItem);
                goalsDb.close();
                finish();
            }
        });
    }
}
