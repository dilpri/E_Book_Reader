package com.example.introduction1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Introduction extends AppCompatActivity {
    private TextView tx1,tx2,tx3,tx4,tx5,tx6,tx7,tx8,tx9,tx10,tx11,tx12;
    private Button bt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        tx1=findViewById(R.id.t1);
        tx1.setText(R.string.t1);

        tx2=findViewById(R.id.t2);
        tx2.setText(R.string.t2);

        tx3=findViewById(R.id.t3);
        tx3.setText(R.string.t3);

        tx4=findViewById(R.id.t4);
        tx4.setText(R.string.t4);

        tx5=findViewById(R.id.t5);
        tx5.setText(R.string.t5);

        tx6=findViewById(R.id.t6);
        tx6.setText(R.string.t6);

        tx7=findViewById(R.id.t7);
        tx7.setText(R.string.t7);

        tx8=findViewById(R.id.t8);
        tx8.setText(R.string.t8);

        tx9=findViewById(R.id.t9);
        tx9.setText(R.string.t9);

        tx10=findViewById(R.id.t10);
        tx10.setText(R.string.t10);

        tx11=findViewById(R.id.t11);
        tx11.setText(R.string.t11);

        tx12=findViewById(R.id.t12);
        tx12.setText(R.string.t12);
        //button

        bt1=findViewById(R.id.b1);
        bt1.setText(R.string.b1);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten=new Intent(Introduction.this,Second.class);
            }
        });


    }
}
