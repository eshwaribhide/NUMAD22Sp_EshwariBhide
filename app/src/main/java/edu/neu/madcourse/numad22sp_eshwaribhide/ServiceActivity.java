package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

//Background execution not allowed: receiving Intent
public class ServiceActivity extends AppCompatActivity {
    private static final String TAG = "DebugServiceActivity";
    TextView serviceValue;
    private Handler textHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        serviceValue = findViewById(R.id.serviceValue);
    }


    public void serviceOnClick(View view) {
//        serviceRun s = new serviceRun();
//        new Thread(s).start();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();

                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                       }

                        String x = stringBuilder.toString();
                        String msg = "WEBSERVICEASDF" + x;
                        Log.e(TAG, msg);
                    }
                    catch (Exception e) {
                        Log.e(TAG, "WEBSERVICE");

                        e.printStackTrace();
                    }
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    String line = null;
//                    while ((line = reader.readLine()) != null) {
//                        stringBuilder.append(line).append("\n");
//                    }

                    //String x = stringBuilder.toString();
//                    URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
//                    HttpURLConnection req = (HttpURLConnection) url.openConnection();
//                    req.setRequestMethod("GET");
//                    req.setDoInput(true);
//                    req.connect();

                    //Scanner s = new Scanner(req.getInputStream()).useDelimiter("\\A");
//        String res = s.hasNext() ? s.next() : "";
//                jObject = new JSONObject(resp);
//                String category = jObject.getString("category");
//
//                Log.i("CATEGORYWEBSERVICEACTIVITY",category);
//
//                textHandler.post(() -> serviceValue.setText(category));

                } catch (MalformedURLException e) {
                    Log.e(TAG, "MalformedURLException");
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    Log.e(TAG, "ProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG, "IOException");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

//
//
////    class serviceRun implements Runnable {
////
////        public void run() {
////            Log.i(TAG, "HELLO");
////            JSONObject jObject = new JSONObject();
////            try {
////                URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
////                HttpURLConnection req = (HttpURLConnection) url.openConnection();
////                req.setRequestMethod("GET");
////                req.setDoInput(true);
//////                req.connect();
////
//////        Scanner s = new Scanner(req.getInputStream()).useDelimiter("\\A");
//////        String res = s.hasNext() ? s.next() : "";
//////                jObject = new JSONObject(resp);
//////                String category = jObject.getString("category");
//////
//////                Log.i("CATEGORYWEBSERVICEACTIVITY",category);
//////
//////                textHandler.post(() -> serviceValue.setText(category));
////
////            } catch (MalformedURLException e) {
////                Log.e(TAG, "MalformedURLException");
////                e.printStackTrace();
////            } catch (ProtocolException e) {
////                Log.e(TAG, "ProtocolException");
////                e.printStackTrace();
////            } catch (IOException e) {
////                Log.e(TAG, "IOException");
////                e.printStackTrace();
//////            } catch (JSONException e) {
//////                Log.e(TAG,"JSONException");
//////                e.printStackTrace();
//////            }
////
////            }
////        }
////    }
//}
}