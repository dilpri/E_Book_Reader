package com.example.ebookreader;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ebookreader.Model.Book;
import com.example.ebookreader.Model.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class Introduction extends AppCompatActivity {
    private TextView tx1, tx2, tx3, tx4, tx6;
    private TextView intro;
    private EditText commenttext;
    private ImageView bookImage;
    private int bookIsbn;
    private Book book;
    private ListView comments;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> commentsList = new ArrayList<>();
    private ArrayList<Comment> commentsListObj = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String commentKey;
    private boolean comment_isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intraduction);

        tx1 = findViewById(R.id.t1);
        tx1.setText(R.string.t1);

        tx2 = findViewById(R.id.t2);
        tx2.setText(R.string.t2);

        tx3 = findViewById(R.id.t3);
        tx3.setText(R.string.t3);

        tx4 = findViewById(R.id.t4);
        tx4.setText(R.string.t4);

        tx6 = findViewById(R.id.t6);
        tx6.setText(R.string.t6);

        bookImage = findViewById(R.id.imageView2);
        comments = findViewById(R.id.comments);
        commenttext = findViewById(R.id.postText);
        intro = findViewById(R.id.intro);
        registerForContextMenu(comments);

        Intent intent = getIntent();
        if (getIntent() != null)
            bookIsbn = Integer.parseInt(intent.getStringExtra("bookIsbn"));

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Book");
        user = FirebaseAuth.getInstance().getCurrentUser();

        // fill fields from database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    ref = database.getReference("Book/" + bookSnapshot.getKey());

                    book = bookSnapshot.getValue(Book.class);
                    if (book != null && book.getIsbn() == (bookIsbn)) {
                        tx1.setText(book.getName());
                        tx2.setText(book.getAuthor());
                        Picasso.get().load(book.getImage()).into(bookImage);
                        intro.setText(book.getIntroduction());

                        // show comments
                        arrayAdapter = new ArrayAdapter<String>(comments.getContext(), android.R.layout.simple_list_item_1, commentsList);
                        comments.setAdapter(arrayAdapter);
                        try {
                            commentsList.clear();
                            commentsListObj.clear();
                            for (DataSnapshot comment : bookSnapshot.child("comments").getChildren()) {
                                if (comment != null) {
                                    Comment com = comment.getValue(Comment.class);
                                    commentsList.add(com.toString());
                                    commentsListObj.add(com);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    // popup menu functions
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.commentpopupmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // get selected comment
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        Comment comment = commentsListObj.get(listPosition);

        if (comment.getUid().equals(user.getUid())) {

            // selected option
            switch (item.getItemId()) {
                case R.id.popupUpdate: {

                    ref.child("comments").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot commentSanp : dataSnapshot.getChildren()) {
                                Comment comment1 = commentSanp.getValue(Comment.class);
                                if (comment1.equals(comment)) {
                                    commentKey = commentSanp.getKey();

                                    commenttext.setText(comment.getComment());
                                    commenttext.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                    comment_isUpdate = true;
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    return true;
                }
                case R.id.popupDelete: {
                    ref.child("comments").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot commentSanp : dataSnapshot.getChildren()) {
                                Comment comment1 = commentSanp.getValue(Comment.class);
                                if (comment1.equals(comment)) {
                                    commentKey = commentSanp.getKey();

                                    ref.child("comments").child(commentKey).removeValue();
                                    Toast.makeText(commenttext.getContext(), "Comment Deleted", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    return true;
                }
            }
        } else {
            Toast.makeText(commenttext.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    // read now button
    public void readNow(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(book.getUrl()));
        startActivity(intent);
    }

    // order now button
    public void orderNow(View view) {
        Intent intent = new Intent(this, Main3Activity.class);
        intent.putExtra("bookIsbn", String.valueOf(bookIsbn));
        startActivity(intent);
    }

    // save comments to the database
    public void postComment(View view) {
        if (TextUtils.isEmpty(commenttext.getText().toString())) {
            commenttext.setError("Please fill this field");
        } else {
            Comment comment = new Comment(commenttext.getText().toString(), Calendar.getInstance().getTime().toString(), user.getDisplayName(), user.getUid());

            if (!comment_isUpdate) {
                ref.child("comments").child(ref.push().getKey()).setValue(comment);
                Toast.makeText(commenttext.getContext(), "Comment posted", Toast.LENGTH_SHORT).show();
            } else {
                ref.child("comments").child(commentKey).setValue(comment);
                Toast.makeText(commenttext.getContext(), "Comment updated", Toast.LENGTH_SHORT).show();
                comment_isUpdate = false;
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(commenttext.getWindowToken(), 0);
            commenttext.getText().clear();

        }
    }
}
