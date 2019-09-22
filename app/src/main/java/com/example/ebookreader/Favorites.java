/*
 * Copyright (c) 2019.
 * Gigara @ G Soft Solutions
 */

package com.example.ebookreader;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ebookreader.Model.Book;
import com.example.ebookreader.Model.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {
    private ArrayAdapter<String> arrayAdapter;
    private ListView favorites;
    private ArrayList<String> favoritesList = new ArrayList<>();
    private ArrayList<Comment> favoritesListObj = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favorites = findViewById(R.id.favorites);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = database.getReference();

        ref.child("User").child(user.getUid()).child("favorites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // show comments
                arrayAdapter = new ArrayAdapter<String>(favorites.getContext(), android.R.layout.simple_list_item_1, favoritesList);
                favorites.setAdapter(arrayAdapter);
                try {
                    favoritesList.clear();
                    favoritesListObj.clear();
                    for (DataSnapshot isbn : dataSnapshot.getChildren()) {
                        if (isbn != null) {
                            Query query = ref.child("Book").orderByChild("Isbn").equalTo(isbn.getValue(Integer.class));
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                                        Book book = bookSnapshot.getValue(Book.class);
                                        favoritesList.add(book.toString());
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
