package com.example.ebookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }
    public void updateDefaultAddress(View view) {
        Intent intent1 = new Intent(Main3Activity.this , Main2Activity.class);
        startActivity(intent1);
    }

    public void deleteDefaultAddress(View view) {
        Intent intent2 = new Intent(Main3Activity.this,Main3Activity.class);
        Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        startActivity(intent2);
    }

    public void addNewAddress(View view) {
        Intent intent3 = new Intent(Main3Activity.this , MainActivity.class);
        startActivity(intent3);
    }
}
