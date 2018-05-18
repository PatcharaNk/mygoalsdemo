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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView goalsListview;
    private ArrayList<GoalListItem> myGoalsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("My Goals");

        //view matching
        goalsListview = (ListView) findViewById(R.id.goals_listView);
        registerForContextMenu(goalsListview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //data
        GoalsDb goalsDb = new GoalsDb(getApplicationContext());
        goalsDb.open();
        myGoalsList = goalsDb.selectAllGoals();

        final GoalListView adapter = new GoalListView(this, myGoalsList);
        //adapter
        goalsListview.setAdapter(adapter);
        goalsDb.close();

        //click list
        goalsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra("goalTopic", adapter.getItem(position));
                startActivity(detailIntent);
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


        switch (item.getItemId()) {
            case R.id.edit_cManu:
                View view = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.edit_dialog_layout, null);
                final EditText editMustDo = (EditText) view.findViewById(R.id.editMustDo_editText);
                editMustDo.setText(myGoalsList.get(info.position).getGoalText());
                builder.setView(view);
                builder.setTitle("Edit Goal");
                builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editText = String.valueOf(editMustDo.getText());
                        GoalsDb goalsDb = new GoalsDb(getApplicationContext());
                        goalsDb.open();
                        goalsDb.updateGoal(myGoalsList.get(info.position).getGoalText(), editText);
                        onResume();
                    }
                });
                builder.setNegativeButton("cancel",null);
                AlertDialog editDialog = builder.create();
                editDialog.show();

                return true;

            case R.id.delect_cManu:
                builder.setTitle("Delete Goal");
                builder.setMessage("Are you sure to deleted ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GoalsDb goalsDb = new GoalsDb(getApplicationContext());
                        goalsDb.open();
                        goalsDb.deleteGoal(myGoalsList.get(info.position).getGoalText());
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

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            Intent addIntent = new Intent(this, AddActivity.class);
            startActivity(addIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
