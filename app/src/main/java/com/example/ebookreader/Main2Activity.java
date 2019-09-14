package com.example.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void updateDefaultAddress2(View view) {
        Intent intent4 = new Intent(Main2Activity.this , Main3Activity.class);
        Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show();
        startActivity(intent4);
    }
}
