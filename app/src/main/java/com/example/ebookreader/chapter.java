package com.example.chapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Chapter extends AppCompatActivity {
    TextView tx13,tx14,tx15,tx16,tx17,tx18;
    Button bt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        tx13=findViewById(R.id.t13);
        tx13.setText(R.string.t13);

        tx14=findViewById(R.id.t14);
        tx14.setText(R.string.t14);

        tx15=findViewById(R.id.t15);
        tx15.setText(R.string.t15);

        tx16=findViewById(R.id.t16);
        tx16.setText(R.string.t16);

        tx17=findViewById(R.id.t17);
        tx17.setText(R.string.t17);

        tx18=findViewById(R.id.t18);
        tx18.setText(R.string.t18);

        bt2=findViewById(R.id.b2);
        bt2.setText(R.string.b2);
    }
}


