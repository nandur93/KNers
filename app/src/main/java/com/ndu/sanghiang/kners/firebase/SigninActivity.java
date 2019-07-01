package com.ndu.sanghiang.kners.firebase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ndu.sanghiang.kners.MainActivity;
import com.ndu.sanghiang.kners.R;
import com.ndu.sanghiang.kners.service.ConnectivityReceiver;
import com.ndu.sanghiang.kners.service.MyApplication;

import java.util.Objects;


public class SigninActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private static final String TAG = "";
    private EditText inputEmail, inputPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    SignInButton buttonSignInGoogle;
    TextView buttonSignUp;
    Button buttonSignIn;
    android.support.v7.widget.Toolbar tToolbar;
    private final static int RC_SIGN_IN = 123;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthListner;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        //edittext
        inputEmail = findViewById(R.id.edit_text_email);
        inputPassword = findViewById(R.id.edit_text_password);
        //properties
        progressBar = findViewById(R.id.progressBar);
        //Import Toolbar
        tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);

        //button
        buttonSignIn = findViewById(R.id.button_signin);
        buttonSignUp = findViewById(R.id.signup_button);
        buttonSignInGoogle = findViewById(R.id.sign_in_google);
        mAuth = FirebaseAuth.getInstance();

        buttonSignInGoogle.setOnClickListener(v -> signInGoogle());
        buttonSignUp.setOnClickListener(v -> startActivity(new Intent(SigninActivity.this, SignupActivity.class)));

        //check the current user
        if (mAuth.getCurrentUser() != null) {
            //ketika ada yang login, goto MainActivity
            startActivity(new Intent(SigninActivity.this, MainActivity.class));
            finish();
        }

        // Checking the email id and password is Empty
        buttonSignIn.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();
            if (TextUtils.isEmpty(email)) {
                inputEmail.setError("Please enter email id");
                //Toast.makeText(getApplicationContext(), "Please enter email id", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                inputPassword.setError("Enter Password");
                //Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            checkConnection();
            progressBar.setVisibility(View.VISIBLE);
            //authenticate user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SigninActivity.this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // there was an error
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d(TAG, "singInWithEmail:Fail");
                            Toast.makeText(SigninActivity.this, getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }
                    });
        });
        mAuthListner = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(SigninActivity.this, MainActivity.class));
            }
        };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signInGoogle() {
        checkConnection();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        //moveTaskToBack(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(Objects.requireNonNull(account));
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(@NonNull GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                        if (user != null) {
                            Toast.makeText(SigninActivity.this, "Welcome "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(SigninActivity.this, "Aut Fail", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        //updateUI(null);
                    }
                    // ...
                });
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        //color = Color.WHITE;
        //message = "";
        if (isConnected) {
            message = "Loading...";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
