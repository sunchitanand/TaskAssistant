package com.example.sunchitanand.taskassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String>  items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems(); //on App startup, read data from file
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items); /*takes in
                                                                            (activity, type of object it is wrapping, object)*/

        lvItems = (ListView) findViewById(R.id.lvItems); /*Passing the already existing view in activity main and then cast to
                                                            List view*/

        lvItems.setAdapter(itemsAdapter); //set adapter to list view

        //mock data
        //items.add("First Item");
        //items.add("Second Item");

        setupListViewListener();

    }

    public void onAddItem(View v) { //Add Item

        EditText etNewItem = (EditText) findViewById(R.id.etNewItem); //get an object for the edit text in view
        String itemText = etNewItem.getText().toString(); //get the value from the edit text object created above
        itemsAdapter.add(itemText); //add the text to the items adapter
        etNewItem.setText(""); //clear the edit text box
        writeItems();
        Toast.makeText(getApplicationContext(), "Task Added", Toast.LENGTH_SHORT).show(); //TOAST when task is added

    }

    private void setupListViewListener(){ //List View Listener to handle long press on list
        Log.i("MainActivity", "Setting up Listener on List View"); //Log to keep track of events and debugging - can be found in Logcat
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //Long press on lvItems

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Task Remove from List");
                items.remove(position); //In this case, the position exactly corresponds to subscript
                itemsAdapter.notifyDataSetChanged(); //Notify
                writeItems();
                return true; //return true if long press
            }
        });
    }

    private File getDataFile(){ //return the data file containing tasks
        return new File(getFilesDir(),"tasks.txt");
    }

    private void readItems(){ //read from the data file
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset())); //read line by line using apache io dependency - FileUtils
        } catch (IOException e) {
            Log.e("MainActivity","Error reading file",e);
            items = new ArrayList<>();
        }
    }

    private void writeItems(){ //write to file
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writing file",e);
        }

    }
}
