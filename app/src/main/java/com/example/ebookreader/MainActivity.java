package com.example.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    static final int RC_SIGN_IN = 123;
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
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            // User is signed in
            updateUI(mAuth.getCurrentUser());
        }


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInGoogle();

            }
        });

    }

    void SignInGoogle(){
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (account != null)
                firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
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
           Intent intent = new Intent(getApplicationContext(), Home.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
           startActivity(intent);

       } else {

           slogan.setText(getText(R.string.slogan));
           btnSignIn.setVisibility(View.VISIBLE);
           //btnLogout.setVisibility(View.INVISIBLE);
           Picasso.get().load(R.drawable.book).into(image);
       }
    }

}
