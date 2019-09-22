package com.example.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebookreader.Model.Book;
import com.example.ebookreader.ViewHolder.BookViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference bookList;
    List<Book> books = new ArrayList<>();
    String categoryId = "";

    //Search Function
    FirebaseRecyclerAdapter<Book, BookViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        bookList = database.getReference("Book");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_book);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //get intent here
        Intent intent = getIntent();
        if (getIntent() != null)
            categoryId = intent.getStringExtra("CategoryID");

        //search
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your book");
        //materialSearchBar.setSpeechMode(false);
        //loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled) {
                    loadListBook();
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(String.valueOf(text));
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Book");
        loadListBook();
    }

    private void startSearch(String text) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            BookViewHolder viewHolder = (BookViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            String name = (viewHolder).book_name.getText().toString();
            if (name.toLowerCase().matches("(.*)" + text.toLowerCase() + "(.*)")) {
                viewHolder.Layout_show();
            } else {
                viewHolder.Layout_hide();
            }
        }
    }

    private void loadListBook() {
        Query firebaseSearchQuery = databaseReference.orderByChild("CategoryId").equalTo(categoryId);

        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(firebaseSearchQuery, Book.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter<Book, BookViewHolder> adapter = new FirebaseRecyclerAdapter<Book, BookViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i, @NonNull Book book) {
                bookViewHolder.book_name.setText(book.getName());
                Picasso.get().load(book.getImage()).into(bookViewHolder.book_image);
                bookViewHolder.book_isbn = book.getIsbn();
            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_item, parent, false);
                return new BookViewHolder(view);
            }
        };

        //set adapter
        Log.d("TAG", "" + adapter.getItemCount());
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}