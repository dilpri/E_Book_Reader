package com.example.ebookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DeliveryDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_details);
    }
    public void addNewAddress2(View view) {
        Intent intent5 = new Intent(DeliveryDetailsActivity.this , Main3Activity.class);
        Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show();
        startActivity(intent5);
    }
}
