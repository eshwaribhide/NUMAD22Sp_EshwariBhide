package edu.neu.madcourse.numad22sp_eshwaribhide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ClickyClickyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicky_clicky);
    }

    public void buttonClickA(View v)
    {
        TextView tv = findViewById(R.id.textView8);
        tv.setText(R.string.pressedA);
    }

    public void buttonClickB(View v)
    {
        TextView tv = findViewById(R.id.textView8);
        tv.setText(R.string.pressedB);
    }

    public void buttonClickC(View v)
    {
        TextView tv = findViewById(R.id.textView8);
        tv.setText(R.string.pressedC);
    }

    public void buttonClickD(View v)
    {
        TextView tv = findViewById(R.id.textView8);
        tv.setText(R.string.pressedD);
    }

    public void buttonClickE(View v)
    {
        TextView tv = findViewById(R.id.textView8);
        tv.setText(R.string.pressedE);
    }

    public void buttonClickF(View v)
    {
        TextView tv = findViewById(R.id.textView8);
        tv.setText(R.string.pressedF);
    }

}