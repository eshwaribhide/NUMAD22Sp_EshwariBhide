package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.neu.madcourse.numad22sp_eshwaribhide.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Clicky Clicky button */
    public void clickyClickyOnClick(View view) {
        Intent intent = new Intent(this, ClickyClickyActivity.class);
        startActivity(intent);
    }


    /** Called when the user taps the About Me button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String msg = "Eshwari Bhide, bhide.e@northeastern.edu";
        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
    }

//    public void showToast(String toastMsg) {
//        Toast toast = Toast.makeText(this, toastMsg, Toast.LENGTH_LONG);
//        toast.show();
//    }
//
//    public void displayToastMsg(View v) {
//        showToast("Eshwari Bhide, bhide.e@northeastern.edu");
//    }
}