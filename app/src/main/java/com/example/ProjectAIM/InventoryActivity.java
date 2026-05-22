package com.example.ProjectAIM;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

// Main screen for viewing, adding, and editing inventory items
public class InventoryActivity extends AppCompatActivity {

    private ArrayList<Item> itemList;       // holds all items to show in list
    private InventoryAdapter adapter;       // connects data to recycler view
    private DatabaseHelper dbHelper;        // manages local database actions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // link the layout views
        RecyclerView recyclerView = findViewById(R.id.recyclerInventory);
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddItem);
        ImageButton fabNotifications = findViewById(R.id.fabNotifications);

        // create database and load items
        dbHelper = new DatabaseHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = dbHelper.getAllItems();

        // set adapter to display items in the recycler
        adapter = new InventoryAdapter(itemList, dbHelper);
        recyclerView.setAdapter(adapter);

        // add new items using a pop-up dialog
        fabAddItem.setOnClickListener(v -> showAddItemDialog());

        // open notification settings page
        fabNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationActivity.class)));
    }

    // shows the pop-up form to add a new item to the list
    private void showAddItemDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.add_item, null, false);

        // connect input fields
        EditText inputName = view.findViewById(R.id.inputName);
        EditText inputQty  = view.findViewById(R.id.inputQty);
        EditText inputDesc = view.findViewById(R.id.inputDesc);
        Button buttonSave  = view.findViewById(R.id.buttonSaveItem);
        Button buttonCancel= view.findViewById(R.id.buttonCancel);

        // create and show dialog
        androidx.appcompat.app.AlertDialog dialog =
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setView(view)
                        .create();

        // close without saving
        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        // save to database when user confirms
        buttonSave.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String qtyStr = inputQty.getText().toString().trim();
            String desc = inputDesc.getText().toString().trim();

            // check for empty fields
            if (name.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Please enter a name and quantity.", Toast.LENGTH_SHORT).show();
                return;
            }

            // convert quantity and insert into database
            int qty = Integer.parseInt(qtyStr);
            dbHelper.addItem(name, qty, desc);

            // refresh recycler view
            itemList.clear();
            itemList.addAll(dbHelper.getAllItems());
            adapter.notifyItemInserted(itemList.size() - 1);

            dialog.dismiss();
        });

        // show the dialog and make it use the full screen width for better layout
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
