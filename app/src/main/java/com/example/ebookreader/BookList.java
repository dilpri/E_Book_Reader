package com.example.ebookreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ebookreader.Interface.ItemClickListener;
import com.example.ebookreader.Model.Book;
import com.example.ebookreader.Model.Category;
import com.example.ebookreader.ViewHolder.BookViewHolder;
import com.example.ebookreader.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference bookList;

    String categoryId="";
    FirebaseRecyclerAdapter<Book, BookViewHolder> firebaseUsersAdapter = null;
    private Query query;
     FirebaseRecyclerAdapter<Book, BookViewHolder> adapter;

    //Search Function
     FirebaseRecyclerAdapter<Book, BookViewHolder> searchAdapter;
     List<String> suggestList = new ArrayList<>();
     MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        bookList = database.getReference("Book");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_book);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get intent here
        if (getIntent() != null)
            categoryId=getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null){
            loadListBook(categoryId);
        }

        //search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your book");
        //materialSearchBar.setSpeechMode(false);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestList){
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener( ) {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>( )
                        .setQuery(query, Book.class)
                        .build( );
        final Query menuId = bookList.orderByChild("MenuId").equalTo(text.toString());

        searchAdapter = new FirebaseRecyclerAdapter<Book, BookViewHolder>(options)
         {
            @Override
            protected void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i, @NonNull Book book) {
                bookViewHolder.book_name.setText(book.getName());
                Picasso.get().load(book.getImage()).into(bookViewHolder.book_image);

                final Book local = book;
                bookViewHolder.setItemClickListener((view, position, isLongClick) -> Toast.makeText(BookList.this,""+local.getName(), Toast.LENGTH_SHORT).show());
            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_item, parent, false);

                return new BookViewHolder(view);
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        bookList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                            Book item = postSnapshot.getValue(Book.class);
                            suggestList.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadListBook(String categoryId) {

        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>( )
                        .setQuery(query, Book.class)
                        .build( );
        final Query menuId = bookList.orderByChild("MenuId").equalTo(categoryId);

        adapter = new FirebaseRecyclerAdapter<Book, BookViewHolder>(options) {
            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i, @NonNull Book book) {

                bookViewHolder.book_name.setText(book.getName());
                Picasso.get().load(book.getImage()).into(bookViewHolder.book_image);

                final Book local = book;
                bookViewHolder.setItemClickListener((view, position, isLongClick) -> Toast.makeText(BookList.this,""+local.getName(), Toast.LENGTH_SHORT).show());
            }
        };

        //set adapter
        Log.d("TAG",""+adapter.getItemCount() );
        recyclerView.setAdapter(adapter);



    }

}
