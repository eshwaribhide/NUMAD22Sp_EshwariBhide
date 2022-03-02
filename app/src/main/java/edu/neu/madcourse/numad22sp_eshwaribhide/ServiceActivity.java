package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    TextView jokeNumber;
    CheckBox programmingCheckBox;
    CheckBox punCheckBox;
    CheckBox scaryCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        jokeNumber = findViewById(R.id.jokeNumber);
        programmingCheckBox = findViewById(R.id.programmingCheckBox);
        punCheckBox = findViewById(R.id.punCheckBox);
        scaryCheckBox = findViewById(R.id.scaryCheckBox);

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

        urlStr.append("?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&amount=");

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
                    String category = jObject.getString("category");

                   //textHandler.post(() -> serviceValue.setText(category));

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