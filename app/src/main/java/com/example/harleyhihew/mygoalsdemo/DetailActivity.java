package com.example.harleyhihew.mygoalsdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private TextView goalTopicText;
    private ListView mustDoListView;
    private GoalListItem goalListItem;
    ArrayList<MustDoItem> myMustDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Goal");

        mustDoListView = (ListView) findViewById(R.id.mustDo_listView);
        registerForContextMenu(mustDoListView);

        goalListItem = (GoalListItem) getIntent().getSerializableExtra("goalTopic");

        // Check Must Do
        mustDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder clickBuilder = new AlertDialog.Builder(DetailActivity.this);
                final GoalsDb goalsDb = new GoalsDb(getApplicationContext());
                clickBuilder.setMessage(myMustDoList.get(position).getMustDoText());
                clickBuilder.setMessage("Do you want to \"Check\" or \"Uncheck\" your list ?");
                clickBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goalsDb.open();
                        goalsDb.updateCheckMustDo(String.valueOf(goalTopicText.getText()), myMustDoList.get(position));
                        goalsDb.close();
                        onResume();
                    }
                });
                clickBuilder.setNegativeButton("no", null);
                AlertDialog alertDialog = clickBuilder.create();
                alertDialog.show();
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //topic Goal

        final GoalsDb goalsDb = new GoalsDb(getApplicationContext());
        goalsDb.open();
        goalListItem.setGoalText(goalsDb.selectGoalFormId(goalListItem));
        goalListItem.setPercent((int) goalsDb.selectPercent(goalListItem.getGoalText()));
        goalTopicText = (TextView) findViewById(R.id.goalDetail_textView);
        goalTopicText.setText(goalsDb.selectGoalFormId(goalListItem));


        //Must Do List
        myMustDoList = goalsDb.selectMustdoFormGoal(String.valueOf(goalTopicText.getText()));
        final MustDoListView adapter = new MustDoListView(this, myMustDoList);
        mustDoListView.setAdapter(adapter);

        goalsDb.setPercent(String.valueOf(goalTopicText.getText()));
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pDetai_progessBar);
        TextView progressTextView = (TextView) findViewById(R.id.percent_detailtextView);
        progressBar.setProgress(goalListItem.getPercent());
        progressTextView.setText(goalListItem.getPercent() + "%");

        goalsDb.close();


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.must_do_contextual_manu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);


        switch (item.getItemId()) {
            case R.id.edit_cManu:
                View view = (LayoutInflater.from(DetailActivity.this)).inflate(R.layout.edit_dialog_layout, null);
                final EditText editMustDo = (EditText) view.findViewById(R.id.editMustDo_editText);
                editMustDo.setText((myMustDoList.get(info.position)).getMustDoText());
                builder.setView(view);
                builder.setTitle("Edit Must Do");
                builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editText = String.valueOf(editMustDo.getText());
                        GoalsDb goalsDb = new GoalsDb(getApplicationContext());
                        goalsDb.open();
                        goalsDb.updateMustDo(String.valueOf(goalTopicText.getText()), editText, myMustDoList.get(info.position));
                        goalsDb.close();
                        onResume();
                    }
                });
                builder.setNegativeButton("cancel", null);
                AlertDialog editDialog = builder.create();
                editDialog.show();

                return true;

            case R.id.delect_cManu:
                builder.setTitle("Delete Alert");
                builder.setMessage("Are you sure to deleted ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GoalsDb goalsDb = new GoalsDb(getApplicationContext());
                        goalsDb.open();
                        goalsDb.deleteMustDo(String.valueOf(goalTopicText.getText()), myMustDoList.get(info.position));
                        goalsDb.close();
                        onResume();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog deleteDialog = builder.create();
                deleteDialog.show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.menu_addMustDo) {
            Intent addMustDoIntent = new Intent(this, AddMustDoActivity.class);
            addMustDoIntent.putExtra("currentGoal", goalTopicText.getText());
            startActivity(addMustDoIntent);
            return true;
        }
        if (id == R.id.menu_editGoal) {
            Intent editIntent = new Intent(this, EditGoalActivity.class);
            editIntent.putExtra("oldGoal", goalListItem);
            startActivity(editIntent);
            return true;
        }
        if (id == R.id.menu_delectGoal) {
            GoalsDb goalsDb = new GoalsDb(getApplicationContext());
            goalsDb.open();
            goalsDb.deleteGoal(String.valueOf(goalTopicText.getText()));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
