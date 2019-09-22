package com.example.ebookreader.ViewHolder;

import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebookreader.Interface.ItemClickListener;
import com.example.ebookreader.Introduction;
import com.example.ebookreader.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class BookViewHolder extends RecyclerView.ViewHolder implements Serializable {

    public TextView book_name;
    public ImageView book_image;
    public int book_isbn;
    private ToggleButton toggleButton;
    private RelativeLayout layout;
    private FrameLayout.LayoutParams params;

    private ItemClickListener itemClickListener;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private boolean isDelete;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);

        book_name = (TextView) itemView.findViewById(R.id.book_name);
        book_image = (ImageView) itemView.findViewById(R.id.book_image);
        toggleButton = itemView.findViewById(R.id.toggleButton);
        layout = itemView.findViewById(R.id.bookListLayout);
        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = database.getReference("User").child(user.getUid());
        ref.child("favorites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot favourite : dataSnapshot.getChildren()) {
                    if (favourite != null && Integer.valueOf(favourite.getValue().toString()) == (book_isbn)) {
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_favorite_gold));
                        toggleButton.setChecked(true);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_favorite_border_black_24dp));
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleButton.isChecked()) {
                    isDelete = false;
                    ref.child("favorites").child(ref.push().getKey()).setValue(book_isbn);
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_favorite_gold));
                    Snackbar mySnackbar = Snackbar.make(toggleButton.getRootView(), "Added to Favorites", 2000);
                    mySnackbar.show();
                } else {
                    isDelete = true;
                    ref.child("favorites").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (isDelete) {
                                for (DataSnapshot favourite : dataSnapshot.getChildren()) {
                                    if (favourite != null && Integer.valueOf(favourite.getValue().toString()) == (book_isbn)) {
                                        ref.child("favorites").child(favourite.getKey()).removeValue();
                                        Snackbar mySnackbar = Snackbar.make(toggleButton.getRootView(), "Removed from Favorites", 2000);
                                        mySnackbar.show();
                                        break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_favorite_border_black_24dp));
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View itemView) {

                Intent intent = new Intent(itemView.getContext(), Introduction.class);

                // get position
                int pos = getAdapterPosition();

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    intent.putExtra("bookIsbn", String.valueOf(book_isbn));
                    itemView.getContext().startActivity(intent);
                }

            }
        });
    }

    public void Layout_hide() {
        params.height = 0;
        layout.setLayoutParams(params);

    }

    public void Layout_show() {
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, itemView.getResources().getDisplayMetrics());
        ;
        layout.setLayoutParams(params);

    }
}
