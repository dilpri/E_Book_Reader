package com.example.ebookreader.ViewHolder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebookreader.BookList;
import com.example.ebookreader.R;

import java.io.Serializable;

public class MenuViewHolder extends RecyclerView.ViewHolder implements Serializable {

    public TextView txtMenuName;
    public ImageView imageView;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName = itemView.findViewById(R.id.menu_name);
        imageView = itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View itemView) {

                Intent intent = new Intent(itemView.getContext(), BookList.class);

                // get position
                int pos = getAdapterPosition();

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    intent.putExtra("CategoryID", String.valueOf(pos + 1));
                    itemView.getContext().startActivity(intent);
                }

            }
        });
    }
}
