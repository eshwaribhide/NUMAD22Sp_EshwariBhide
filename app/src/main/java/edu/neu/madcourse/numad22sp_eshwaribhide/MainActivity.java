package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String PKG_NAME = "edu.neu.madcourse.numad22sp_eshwaribhide.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Method is called when the user taps the Service button */
    public void serviceOnClick(View view) {
        Intent intent = new Intent(this, ServiceActivity.class);
        startActivity(intent);
    }

    /** Method is called when the user taps the Locator button */
    public void locatorOnClick(View view) {
        Intent intent = new Intent(this, LocatorActivity.class);
        startActivity(intent);
    }

    /** Method is called when the user taps the Link Collector button */
    public void linkCollectorOnClick(View view) {
        Intent intent = new Intent(this, LinkCollectorActivity.class);
        startActivity(intent);
    }

    /** Method is called when the user taps the Clicky Clicky button */
    public void clickyClickyOnClick(View view) {
        Intent intent = new Intent(this, ClickyClickyActivity.class);
        startActivity(intent);
    }

    /** Method is called when the user taps the About Me button */
    public void aboutMeOnClick(View view) {
        Intent intent = new Intent(this, AboutMeActivity.class);
        String name = "Eshwari Bhide, bhide.e@northeastern.edu";
        intent.putExtra(PKG_NAME, name);
        startActivity(intent);
    }
}