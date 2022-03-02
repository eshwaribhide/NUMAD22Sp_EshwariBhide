package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class ServiceActivity extends AppCompatActivity {
    private static final String TAG = "DebugServiceActivity";
    private Handler textHandler = new Handler();
    private Button retrieveJokesButton;
    private TextView jokeCategory;
    private TextView jokeNumber;
    private Integer jokeNumberInt;
    private CheckBox programmingCheckBox;
    private CheckBox punCheckBox;
    private CheckBox xmasCheckBox;
    private ProgressBar spinner;
    private boolean jokesFound = false;


    private ArrayList<ServiceActivity.FoundJoke> foundJokes = new ArrayList<>();

    private RecyclerView recyclerView;
    private ServiceRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    public static class FoundJoke {
        private final int imageSource;
        private final String jokeSetup;
        private final String jokeDelivery;

        public FoundJoke(int imageSource, String jokeSetup, String jokeDelivery) {
            this.imageSource = imageSource;
            this.jokeSetup = jokeSetup;
            this.jokeDelivery = jokeDelivery;
        }

        public int getImageSource() {
            return imageSource;
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

        retrieveJokesButton = findViewById(R.id.retrieveJokes);
        jokeCategory = findViewById(R.id.jokeCategory);
        jokeNumber = findViewById(R.id.jokeNumber);
        programmingCheckBox = findViewById(R.id.programmingCheckBox);
        punCheckBox = findViewById(R.id.punCheckBox);
        xmasCheckBox = findViewById(R.id.xmasCheckBox);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        if (jokesFound) {
            retrieveJokesButton.setVisibility(View.GONE);
            jokeCategory.setVisibility(View.GONE);
            jokeNumber.setVisibility(View.GONE);
            programmingCheckBox.setVisibility(View.GONE);
            punCheckBox.setVisibility(View.GONE);
            xmasCheckBox.setVisibility(View.GONE);
        }



    }

    //checkbox save state
    // add variety

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = 0;

        if (foundJokes != null) {
            size = foundJokes.size();
        }
        outState.putInt("LengthFoundJokes", size);
        outState.putBoolean("JokesFound", jokesFound);

        for (int i = 0; i < size; i++) {
            int keyInt = i + 1;
            String key = Integer.toString(keyInt);
            outState.putInt("JokeImageKey" + key, foundJokes.get(i).getImageSource());
            outState.putString("JokeSetupKey" + key, foundJokes.get(i).getjokeSetup());
            outState.putString("JokeDeliveryKey" + key, foundJokes.get(i).getjokeDelivery());
        }
        super.onSaveInstanceState(outState);

    }

    private void initData(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey("LengthFoundJokes")) {
            jokesFound = savedInstanceState.getBoolean("JokesFound");

            if (foundJokes == null || foundJokes.size() == 0) {
                int size = savedInstanceState.getInt("LengthFoundJokes");


                for (int i = 0; i < size; i++) {
                    int keyInt = i + 1;
                    String key = Integer.toString(keyInt);
                    int jokeImgID = savedInstanceState.getInt("JokeImageKey"+ key);
                    String jokeSetup = savedInstanceState.getString("JokeSetupKey" + key);
                    String jokeDelivery = savedInstanceState.getString("JokeDeliveryKey" + key);

                    ServiceActivity.FoundJoke foundJoke = new ServiceActivity.FoundJoke(jokeImgID, jokeSetup, jokeDelivery);

                    foundJokes.add(foundJoke);
                }
            }


        }

    }

    private void addJokeToRecyclerView(Integer imageId, String jokeSetup, String jokeDelivery) {
        recyclerViewLayoutManager.smoothScrollToPosition(recyclerView, null, 0);
        foundJokes.add(0, new ServiceActivity.FoundJoke(imageId, jokeSetup, jokeDelivery));
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
        if (xmasCheckBox.isChecked()) {
            category.add("Christmas");
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
        jokesFound = true;
        retrieveJokesButton.setVisibility(View.GONE);
        jokeCategory.setVisibility(View.GONE);
        jokeNumber.setVisibility(View.GONE);
        programmingCheckBox.setVisibility(View.GONE);
        punCheckBox.setVisibility(View.GONE);
        xmasCheckBox.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

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
                        String jokeCategory = jObject.getString("category");
                        if (jokeCategory.equals("Christmas")) {
                            textHandler.post(() -> addJokeToRecyclerView(R.drawable.presents, jokeSetup, jokeDelivery));
                        }
                        else if (jokeCategory.equals("Pun")) {
                            textHandler.post(() -> addJokeToRecyclerView(R.drawable.facepalm, jokeSetup, jokeDelivery));
                        }
                        else {
                            textHandler.post(() -> addJokeToRecyclerView(R.drawable.programmer, jokeSetup, jokeDelivery));
                        }

                    }
                    else {
                        Log.e(TAG, "Getting Jokes");
                        JSONArray jArray = jObject.getJSONArray("jokes");
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject joke = jArray.getJSONObject(i);
                            String jokeSetup = joke.getString("setup");
                            String jokeDelivery = joke.getString("delivery");
                            String jokeCategory = joke.getString("category");
                            if (jokeCategory.equals("Christmas")) {
                                textHandler.post(() -> addJokeToRecyclerView(R.drawable.presents, jokeSetup, jokeDelivery));
                            }
                            else if (jokeCategory.equals("Pun")) {
                                textHandler.post(() -> addJokeToRecyclerView(R.drawable.facepalm, jokeSetup, jokeDelivery));
                            }
                            else {
                                textHandler.post(() -> addJokeToRecyclerView(R.drawable.programmer, jokeSetup, jokeDelivery));
                            }
                        }
                    }
                    textHandler.post(() ->spinner.setVisibility(View.GONE));
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