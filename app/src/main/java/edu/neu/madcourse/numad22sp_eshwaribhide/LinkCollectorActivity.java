package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LinkCollectorActivity extends AppCompatActivity {
    private ArrayList<ListItem> collectedLinks = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    public static class ListItem implements ListItemClickListener {
        private final Context context;

        private final String linkName;
        private final String linkValue;
        private final String httpUrlValue;

        public ListItem(Context context, String linkName, String linkValue, String httpUrlValue) {
            this.context = context;
            this.linkName = linkName;
            this.linkValue = linkValue;
            this.httpUrlValue = httpUrlValue;
        }

        public void listItemOnClick(int position) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(httpUrlValue));
            context.startActivity(i);
        }


        public String getlinkName() {
            return linkName;
        }

        public String getlinkValue() {
            return linkValue;
        }

        public String getLinkHttpValue() {
            return httpUrlValue;
        }
    }

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

        EditText editLinkName = (EditText) findViewById(R.id.editTextLinkName);
        EditText editLinkValue = (EditText) findViewById(R.id.editTextLinkValue);
        editLinkName.setEnabled(false);
        editLinkValue.setEnabled(false);
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
            outState.putString("LinkHTTPValueKey" + key, collectedLinks.get(i).getLinkHttpValue());
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
                    String linkValue = savedInstanceState.getString("LinkValueKey" + key);
                    String linkHTTPValue = savedInstanceState.getString("LinkHTTPValueKey" + key);

                    ListItem listItem = new ListItem(this, linkName, linkValue, linkHTTPValue);

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

    private void addItem(String linkName, String linkValue, String httpUrlValue) {
        collectedLinks.add(0, new ListItem(this, linkName, linkValue, httpUrlValue));
        recyclerViewAdapter.notifyItemInserted(0);
    }

    public void floatingActionButtonOnClick(View view) {
        EditText editLinkName = (EditText) findViewById(R.id.editTextLinkName);
        EditText editLinkValue = (EditText) findViewById(R.id.editTextLinkValue);
        editLinkName.setEnabled(true);
        editLinkValue.setEnabled(true);
    }

    public void finishedButtonOnClick(View view) throws IOException {
        EditText editLinkName = (EditText) findViewById(R.id.editTextLinkName);
        EditText editLinkValue = (EditText) findViewById(R.id.editTextLinkValue);

        String urlValue = editLinkValue.getText().toString();

        String httpUrlValue = urlValue;

        if (!(httpUrlValue.startsWith("http://")) || (httpUrlValue.startsWith("https://"))) {
            if (httpUrlValue.startsWith("www.")) {
                httpUrlValue = "http://" + urlValue;
            }
            else {
                httpUrlValue = "http://www." + urlValue;
            }

        }

        addItem(editLinkName.getText().toString(),  urlValue,  httpUrlValue);

        Snackbar sb = Snackbar.make(view, "Link Created Successfully", BaseTransientBottomBar.LENGTH_LONG);

        sb.show();

        editLinkName.getText().clear();
        editLinkValue.getText().clear();

        editLinkName.setHint("Enter Link Name");
        editLinkValue.setHint("Enter Link Value");

        editLinkName.setEnabled(false);
        editLinkValue.setEnabled(false);




    }




    }