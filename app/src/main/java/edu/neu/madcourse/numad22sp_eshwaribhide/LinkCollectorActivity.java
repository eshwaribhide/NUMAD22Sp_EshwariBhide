package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Enter Link Name and Link Value");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editLinkName = new EditText(this);
        editLinkName.setHint("Enter Link Name");
        layout.addView(editLinkName);

        final EditText editLinkValue = new EditText(this);
        editLinkValue.setHint("Enter Link Value");
        layout.addView(editLinkValue);

        editLinkName.setTextColor(Color.parseColor("#9C27B0"));
        editLinkValue.setTextColor(Color.parseColor("#9C27B0"));

        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setPositiveButton("OK", (dialog, whichButton) -> {
            boolean success = true;
            StringBuilder snackbarMessage = new StringBuilder("Cannot Add Link: ");
            String urlValue = editLinkValue.getText().toString();
            String httpUrlValue = urlValue;

            // Successfully added only if user inputted a valid URL, and does not have the same
            // name or value for the link (X.com is equivalent to http://X.com, http://www.X.com,
            // https://X.com, https://www.X.com, and www.X.com).

            if (Patterns.WEB_URL.matcher(urlValue).matches()) {
                if (!(httpUrlValue.startsWith("http://")) && !(httpUrlValue.startsWith("https://"))) {
                    httpUrlValue = generateHTTPURLValue(httpUrlValue, urlValue);
                }
                String strippedHTTPURL = stripHTTPURL(httpUrlValue);
                boolean doBreak = false;
                for (ListItem collectedLink : collectedLinks) {
                    String collectedLinkName = collectedLink.getlinkName();
                    String strippedcollectedLinkHTTPValue = stripHTTPURL(collectedLink.getLinkHttpValue());

                    if (collectedLinkName.equals(editLinkName.getText().toString())) {
                        success = false;
                        snackbarMessage.append("Link Name Already Exists | ");
                        // Cannot break here because also want to check if the inputted link value is duplicated
                        doBreak = true;
                    }

                    if (strippedcollectedLinkHTTPValue.contains(strippedHTTPURL) || strippedHTTPURL.contains(strippedcollectedLinkHTTPValue)) {
                        success = false;
                        snackbarMessage.append("URL Already Exists");
                        break;
                    }

                    if (doBreak) {
                        break;
                    }
                }
            }

            else {
                success = false;
                snackbarMessage.append("Invalid URL Format (should use X.com, www.X.com, http://X.com, etc.)");
            }

            if (snackbarMessage.toString().endsWith("| ")) {
                snackbarMessage = new StringBuilder(snackbarMessage.substring(0, snackbarMessage.length() - 2));
            }

            if (success) {
                snackbarMessage = new StringBuilder("Link Added Successfully");
                addItem(editLinkName.getText().toString(), urlValue, httpUrlValue);
            }
            Snackbar snackbar = Snackbar.make(view, snackbarMessage.toString(), BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();

        });

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    String generateHTTPURLValue(String httpUrlValue, String urlValue) {
            if (httpUrlValue.startsWith("www.")) {
                return "http://" + urlValue;
            } else {
                return "http://www." + urlValue;
            }
    }

    String stripHTTPURL(String urlToStrip) {
        if (urlToStrip.startsWith("http://")) {
            return urlToStrip.replaceFirst("^http://", "");
        }
        else {
            return urlToStrip.replaceFirst("^https://", "");
        }
    }

    }