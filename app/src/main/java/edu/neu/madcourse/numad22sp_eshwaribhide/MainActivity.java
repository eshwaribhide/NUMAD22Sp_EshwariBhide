package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showToast(String toastMsg) {
        Toast toast = Toast.makeText(this, toastMsg, Toast.LENGTH_LONG);
        toast.show();
    }

    public void displayToastMsg(View v) {
        showToast("Eshwari Bhide, bhide.e@northeastern.edu");
    }
}