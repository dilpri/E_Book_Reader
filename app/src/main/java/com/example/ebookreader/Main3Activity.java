package com.example.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ebookreader.Model.Contact;
import com.example.ebookreader.Model.Order;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Main3Activity extends AppCompatActivity {

    private Button addBtn;
    private Button orderBtn;
    private EditText addressL1;
    private EditText addressL2;
    private EditText province;
    private EditText postalCode;
    private EditText number;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseUser user;
    private int bookIsbn;
    private boolean hasAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_main3);
        addBtn = findViewById(R.id.updateBtn);
        orderBtn = findViewById(R.id.orderBtn);
        addressL1 = findViewById(R.id.address1);
        addressL2 = findViewById(R.id.address2);
        province = findViewById(R.id.address3);
        postalCode = findViewById(R.id.address4);
        number = findViewById(R.id.address5);

        Intent intent = getIntent();
        if (getIntent() != null)
            bookIsbn = Integer.parseInt(intent.getStringExtra("bookIsbn"));

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = database.getReference("User/" + user.getUid() + "/contact");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Contact contact = dataSnapshot.getValue(Contact.class);
                if (contact != null) {
                    hasAddress = true;
                    addressL1.setText(contact.getFirstLine());
                    addressL2.setText(contact.getSecondLine());
                    province.setText(contact.getProvince());
                    postalCode.setText(String.valueOf(contact.getPostalCode()));
                    number.setText(String.valueOf(contact.getNumber()));
                } else {
                    hasAddress = false;
                    addBtn.setText("Add");
                    addressL1.setText(null);
                    addressL2.setText(null);
                    province.setText(null);
                    postalCode.setText(null);
                    number.setText(null);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void delete(View view) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ref.setValue(null);
        Snackbar mySnackbar = Snackbar.make(addBtn.getRootView(), "Address Deleted Successfully", 2000);
        mySnackbar.show();
    }

    public void update(View view) {
        boolean empty = false;
        if (TextUtils.isEmpty(addressL1.getText().toString())) {
            empty = true;
            addressL1.setError("Please fill this field");
            addressL1.requestFocus();
        }
        if (TextUtils.isEmpty(addressL2.getText().toString())) {
            empty = true;
            addressL2.setError("Please fill this field");
            addressL2.requestFocus();
        }
        if (TextUtils.isEmpty(province.getText().toString())) {
            empty = true;
            province.setError("Please fill this field");
            province.requestFocus();
        }
        if (TextUtils.isEmpty(postalCode.getText().toString())) {
            empty = true;
            postalCode.setError("Please fill this field");
            postalCode.requestFocus();
        }
        if (TextUtils.isEmpty(number.getText().toString())) {
            empty = true;
            number.setError("Please fill this field");
            number.requestFocus();
        }

        if (!empty) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            Contact contact = new Contact(addressL1.getText().toString(), addressL2.getText().toString(),
                    province.getText().toString(), Integer.valueOf(postalCode.getText().toString()),
                    Integer.valueOf(number.getText().toString()));
            ref.setValue(contact);
            addBtn.setText("Update");
            Snackbar mySnackbar = Snackbar.make(addBtn.getRootView(), "Address Successfully Updated", 2000);
            mySnackbar.show();

        }
    }

    public void orderNow(View view) {
        if (hasAddress) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            Order order = new Order(bookIsbn, user.getUid(), String.valueOf(Calendar.getInstance().getTime()));
            ref = database.getReference("Orders");
            ref.child(ref.push().getKey()).setValue(order);
            Snackbar mySnackbar = Snackbar.make(addBtn.getRootView(), "Order Successfully Placed", 2000);
            mySnackbar.show();
        } else {
            Toast toast = Toast.makeText(this, "Please add your address", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
