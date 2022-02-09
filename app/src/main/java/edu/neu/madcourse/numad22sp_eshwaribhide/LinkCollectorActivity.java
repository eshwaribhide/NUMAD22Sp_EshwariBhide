package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class LinkCollectorActivity extends AppCompatActivity {
    private ArrayList<ListItem> collectedLinks = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_collector);

        initSavedInstanceState(savedInstanceState);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String deleteMsg = "Deleted Link from List";
                Toast toast = Toast.makeText(LinkCollectorActivity.this, deleteMsg, Toast.LENGTH_SHORT);
                toast.show();

                collectedLinks.remove(viewHolder.getLayoutPosition());

                recyclerViewAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = 0;

        if (collectedLinks != null) {
            size = collectedLinks.size();
        }
        outState.putInt("LengthLinksCollected", size);

        for (int i = 0; i < size; i++) {
            int keyInt = i + 1;
            String key = Integer.toString(keyInt);
            outState.putString("LinkNameKey" + key, collectedLinks.get(i).getlinkName());
            outState.putString("LinkValueKey" + key, collectedLinks.get(i).getlinkValue());
        }
        super.onSaveInstanceState(outState);

    }

    private void initData(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey("LengthLinksCollected")) {
            if (collectedLinks == null || collectedLinks.size() == 0) {

                int size = savedInstanceState.getInt("LengthLinksCollected");

                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {
                    int keyInt = i + 1;
                    String key = Integer.toString(keyInt);
                    String linkName = savedInstanceState.getString("LinkNameKey" + key);
                    String linkValue = savedInstanceState.getString("LinkNameKey" + key);

                    ListItem listItem = new ListItem(linkName, linkValue);

                    collectedLinks.add(listItem);
                }
            }
        }

    }

    private void generateRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new RecyclerViewAdapter(collectedLinks);
        ListItemClickListener itemClickListener = position -> {
            collectedLinks.get(position).listItemOnClick(position);
            recyclerViewAdapter.notifyItemChanged(position);
        };
        recyclerViewAdapter.setOnItemClickListener(itemClickListener);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initSavedInstanceState(Bundle savedInstanceState) {
        initData(savedInstanceState);
        generateRecyclerView();
    }

    private void addItem() {
        collectedLinks.add(0, new ListItem("No Logo item", "Item id: " ));
        recyclerViewAdapter.notifyItemInserted(0);
    }

    public void floatingActionButtonOnClick(View view) {
        addItem();
    }
}