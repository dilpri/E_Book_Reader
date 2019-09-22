
/*
 * Copyright (c) 2019.
 * Gigara @ G Soft Solutions
 */

package com.example.ebookreader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ebookreader.Model.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Upload extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    EditText bookName;
    EditText bookAuthor;
    EditText bookISBN;
    EditText bookDescription;
    private ImageView imageView;
    private Spinner categorySpinner;
    private HashSet<Integer> books = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        bookName = findViewById(R.id.book_name);
        bookAuthor = findViewById(R.id.book_author);
        bookISBN = findViewById(R.id.book_isbn);
        bookDescription = findViewById(R.id.book_description);
        imageView = findViewById(R.id.image);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // category selector
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        List<String> list = new ArrayList<String>();
        list.add("Romance");
        list.add("Mystery");
        list.add("Science Fiction");
        list.add("Young Adult");
        list.add("History");
        list.add("Thriller");
        // Category selector hint text
        list.add("Category");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);
        categorySpinner.setSelection(dataAdapter.getCount());

        //get already available books
        databaseReference.child("Book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot book : dataSnapshot.getChildren()) {
                    books.add((book.getValue(Book.class)).getIsbn());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void uploadNow(View view) {
        boolean empty = false;
        if (TextUtils.isEmpty(bookName.getText().toString())) {
            empty = true;
            bookName.setError("Please fill this field");
            bookName.requestFocus();
        }
        if (TextUtils.isEmpty(bookISBN.getText().toString())) {
            empty = true;
            bookISBN.setError("Please fill this field");
            bookISBN.requestFocus();
        }
        if (TextUtils.isEmpty(bookAuthor.getText().toString())) {
            empty = true;
            bookAuthor.setError("Please fill this field");
            bookAuthor.requestFocus();
        }
        if (TextUtils.isEmpty(bookDescription.getText().toString())) {
            empty = true;
            bookDescription.setError("Please fill this field");
            bookDescription.requestFocus();
        }
        // isbn validation
        if (books.contains(Integer.parseInt(bookISBN.getText().toString()))) {
            empty = true;
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Oops!");
            builder1.setMessage("This Book is Already Available");
            builder1.setIcon(R.drawable.ic_error_outline_red_24dp);
            builder1.setCancelable(true);
            AlertDialog alert = builder1.create();
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alert.dismiss();
                }
            });

            alert.show();
        }

        if (!empty)
            uploadFile();
    }

    // show pdf selection window
    private void uploadFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Book PDF"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uploadPDF(data.getData());
        } else if (requestCode == 333 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //  upload the pdf to the database
    private void uploadPDF(Uri uri) {
        final ProgressDialog progressDialog = new ProgressDialog(bookName.getContext());
        progressDialog.show();
        progressDialog.setTitle("Uploading");
        StorageReference reference = mStorageRef.child("user_uploads/" + user.getUid() + "/" + bookISBN.getText() + ".pdf");
        reference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> file = taskSnapshot.getStorage().getDownloadUrl();
                        while (!file.isComplete()) ;
                        Uri url = file.getResult();

                        // upload image
                        // Get the data from an ImageView as bytes
                        imageView.setDrawingCacheEnabled(true);
                        imageView.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        StorageReference imageRef = mStorageRef.child("user_uploads/" + user.getUid() + "/" + bookISBN.getText() + ".jpg");
                        UploadTask uploadTask = imageRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> img = taskSnapshot.getStorage().getDownloadUrl();
                                while (!img.isComplete()) ;
                                Uri ImageUrl = img.getResult();

                                //save book to db
                                Book book = new Book(bookName.getText().toString(), ImageUrl.toString(),
                                        bookAuthor.getText().toString(),
                                        String.valueOf(categorySpinner.getSelectedItemId() + 1),
                                        bookDescription.getText().toString(),
                                        Integer.parseInt(bookISBN.getText().toString()),
                                        url.toString());
                                databaseReference.child("Book").child(databaseReference.push().getKey()).setValue(book);

                                progressDialog.dismiss();
                                Snackbar mySnackbar = Snackbar.make(bookName.getRootView(), "Successfully Uploaded", 2000);
                                mySnackbar.show();

                                bookName.getText().clear();
                                bookISBN.getText().clear();
                                bookAuthor.getText().clear();
                                bookDescription.getText().clear();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                // show progress
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage("Uploading " + (int) progress + "%");
                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        // show progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploading " + (int) progress + "%");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressDialog.dismiss();
                    }
                });
    }

    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 333);
    }

}
