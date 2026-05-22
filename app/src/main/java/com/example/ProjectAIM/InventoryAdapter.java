package com.example.ProjectAIM;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Adapter that connects the item list to the RecyclerView
public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private final ArrayList<Item> items;   // all items shown in list
    private final DatabaseHelper dbHelper; // used for saving changes or deleting

    public InventoryAdapter(ArrayList<Item> items, DatabaseHelper dbHelper) {
        this.items = items;
        this.dbHelper = dbHelper;
    }

    // creates a single row for the list
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(v);
    }

    // fills the row with item info and sets up button actions
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.textItemName.setText(item.getName());
        holder.editItemQty.setText(String.valueOf(item.getQuantity()));

        // updates database when quantity field loses focus
        holder.editItemQty.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String s = holder.editItemQty.getText().toString().trim();
                if (!s.isEmpty()) {
                    int newQty = Integer.parseInt(s);
                    item.setQuantity(newQty);
                    dbHelper.updateQuantity(item.getId(), newQty);
                    Toast.makeText(v.getContext(), "Quantity updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // deletes the item when delete button is clicked
        holder.buttonDelete.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            Item toRemove = items.get(pos);
            dbHelper.deleteItem(toRemove.getId());
            items.remove(pos);
            notifyItemRemoved(pos);
            Toast.makeText(v.getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
        });
    }

    // returns total item count for recycler view
    @Override
    public int getItemCount() {
        return items.size();
    }

    // holds each row view in memory for smoother scrolling
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName;   // shows item name
        EditText editItemQty;    // lets user change quantity
        ImageButton buttonDelete;// removes item from list

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            editItemQty = itemView.findViewById(R.id.editItemQty);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
