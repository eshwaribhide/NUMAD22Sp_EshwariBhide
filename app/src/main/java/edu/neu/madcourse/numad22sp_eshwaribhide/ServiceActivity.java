package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

//Background execution not allowed: receiving Intent
public class ServiceActivity extends AppCompatActivity {
    private static final String TAG = "DebugServiceActivity";
    private Handler textHandler = new Handler();
    private TextView jokeNumber;
    private Integer jokeNumberInt;
    private CheckBox programmingCheckBox;
    private CheckBox punCheckBox;
    private CheckBox scaryCheckBox;

    private ArrayList<ServiceActivity.FoundJoke> foundJokes = new ArrayList<>();

    private RecyclerView recyclerView;
    private ServiceRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    public static class FoundJoke {

        private final String jokeSetup;
        private final String jokeDelivery;

        public FoundJoke(String jokeSetup, String jokeDelivery) {
            this.jokeSetup = jokeSetup;
            this.jokeDelivery = jokeDelivery;
        }

        public String getjokeSetup() {
            return jokeSetup;
        }
        public String getjokeDelivery() {
            return jokeDelivery;
        }
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initSavedInstanceState(savedInstanceState);

        jokeNumber = findViewById(R.id.jokeNumber);
        programmingCheckBox = findViewById(R.id.programmingCheckBox);
        punCheckBox = findViewById(R.id.punCheckBox);
        scaryCheckBox = findViewById(R.id.scaryCheckBox);

    }

    //checkbox save state

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = 0;

        if (foundJokes != null) {
            size = foundJokes.size();
        }
        outState.putInt("LengthFoundJokes", size);

        for (int i = 0; i < size; i++) {
            int keyInt = i + 1;
            String key = Integer.toString(keyInt);
            outState.putString("JokeSetupKey" + key, foundJokes.get(i).getjokeSetup());
            outState.putString("JokeDeliveryKey" + key, foundJokes.get(i).getjokeDelivery());
        }
        super.onSaveInstanceState(outState);

    }

    private void initData(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey("LengthFoundJokes")) {
            if (foundJokes == null || foundJokes.size() == 0) {

                int size = savedInstanceState.getInt("LengthFoundJokes");

                for (int i = 0; i < size; i++) {
                    int keyInt = i + 1;
                    String key = Integer.toString(keyInt);
                    String jokeSetup = savedInstanceState.getString("JokeSetupKey" + key);
                    String jokeDelivery = savedInstanceState.getString("JokeDeliveryKey" + key);

                    ServiceActivity.FoundJoke foundJoke = new ServiceActivity.FoundJoke(jokeSetup, jokeDelivery);

                    foundJokes.add(foundJoke);
                }
            }
        }

    }

    private void addJokeToRecyclerView(String jokeSetup, String jokeDelivery) {
        recyclerViewLayoutManager.smoothScrollToPosition(recyclerView, null, 0);
        jokeNumber.setVisibility(View.GONE);
        programmingCheckBox.setVisibility(View.GONE);
        punCheckBox.setVisibility(View.GONE);
        scaryCheckBox.setVisibility(View.GONE);
        foundJokes.add(0, new ServiceActivity.FoundJoke(jokeSetup, jokeDelivery));
        recyclerViewAdapter.notifyItemInserted(0);

    }
    private void generateRecyclerView() {
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new ServiceRecyclerViewAdapter(foundJokes);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

    }

    private void initSavedInstanceState(Bundle savedInstanceState) {
        initData(savedInstanceState);
        generateRecyclerView();
    }

    public String constructURL() {
        StringBuilder urlStr = new StringBuilder("https://v2.jokeapi.dev/joke/");
        ArrayList<String> category = new ArrayList<>();
        if (programmingCheckBox.isChecked()) {
            category.add("Programming");
        }
        if (punCheckBox.isChecked()) {
            category.add("Pun");
        }
        if (scaryCheckBox.isChecked()) {
            category.add("Spooky");
        }
        if (category.size() == 0) {
            category.add("Programming");
        }

        for (int i = 0; i < category.size(); i++) {
            if (i==0) {
                urlStr.append(category.get(i));
            }
            else{
                urlStr.append(",").append(category.get(i));
            }
        }

        urlStr.append("?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&type=twopart&amount=");

        // Is used later on when appending to recycler view
        if (jokeNumber.getText().toString().matches("")) {
            jokeNumberInt = 1;
        }
        else {
            jokeNumberInt = Integer.parseInt(jokeNumber.getText().toString());
        }
        urlStr.append(jokeNumber.getText().toString());

        return urlStr.toString();
    }


    public void serviceOnClick(View view) {
        String urlStr = constructURL();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jObject = new JSONObject();
                try {
                    URL url = new URL(urlStr);
                    Log.e(TAG, urlStr);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    HttpURLConnection req = (HttpURLConnection) url.openConnection();
                    req.setRequestMethod("GET");
                    req.setDoInput(true);
                    req.connect();

                    Scanner s = new Scanner(req.getInputStream()).useDelimiter("\\A");
                    String resp = s.hasNext() ? s.next() : "";
                    jObject = new JSONObject(resp);

                    if (jokeNumberInt == 1) {
                        String jokeSetup = jObject.getString("setup");
                        String jokeDelivery = jObject.getString("delivery");
                        textHandler.post(() -> addJokeToRecyclerView(jokeSetup, jokeDelivery));
                    }
                    else {
                        Log.e(TAG, "Getting Jokes");
                        JSONArray jArray = jObject.getJSONArray("jokes");
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject joke = jArray.getJSONObject(i);
                            String jokeSetup = joke.getString("setup");
                            String jokeDelivery = joke.getString("delivery");
                            textHandler.post(() -> addJokeToRecyclerView(jokeSetup, jokeDelivery));
                        }
                    }
                } catch (MalformedURLException e) {
                    Log.e(TAG, "MalformedURLException");
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    Log.e(TAG, "ProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG, "IOException");
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}