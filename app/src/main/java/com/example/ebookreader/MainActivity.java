package com.example.ebookreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn;
    TextView slogan;
    static final int GOOGLE_SIGN = 123;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button)findViewById(R.id.login);
        //btnSignUp = (Button)findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.progress_circular);
        slogan = (TextView)findViewById(R.id.slogan);
        image = (ImageView)findViewById(R.id.bookicon);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth != null) {
            // User is signed in
            Intent i = new Intent(MainActivity.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }


        /*btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIn = new Intent(MainActivity.this,SignIn.class);
                startActivity(signIn);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signUp = new Intent(MainActivity.this,SignUp.class);
                startActivity(signUp);
            }
        });*/
    }

    void SignInGoogle(){
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,GOOGLE_SIGN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
                //make firebase request
            }catch (ApiException e){
                //e.printStackTrace();
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle :"+account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("TAG","Sign in Success");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            }
            else {
                progressBar.setVisibility(View.INVISIBLE);
                Log.w("TAG","Sign in failure",task.getException());
                Toast.makeText(this, "SignIn Failed!", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }

        });
    }


   private void updateUI(FirebaseUser user) {
       if (user != null){

           String name = user.getDisplayName();
           String email = user.getEmail();
           String photo = String.valueOf(user.getPhotoUrl());

           slogan.append("Info : \n");
           slogan.append(name + "\n");
           slogan.append(email);

           Picasso.get().load(photo).into(image);
           //Intent intent = new Intent(getApplicationContext(), Home.class);
           //startActivity(intent);
           //btnSignIn.setVisibility(View.INVISIBLE);
           //btnLogout.setVisibility(View.VISIBLE);

       } else {

           slogan.setText(getText(R.string.slogan));
           btnSignIn.setVisibility(View.VISIBLE);
           //btnLogout.setVisibility(View.INVISIBLE);
           Picasso.get().load(R.drawable.book).into(image);
       }
    }

}
