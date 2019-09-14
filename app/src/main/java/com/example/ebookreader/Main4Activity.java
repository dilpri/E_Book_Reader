package com.example.ebookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity {
    TextView tx1,tx2;
    RatingBar rb1;
    Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        tx1=findViewById(R.id.txt1);
        tx2=findViewById(R.id.txt2);
        rb1=findViewById(R.id.rbr1);
        btn1=findViewById(R.id.bttn1);
        btn2=findViewById(R.id.bttn2);
    }
}
