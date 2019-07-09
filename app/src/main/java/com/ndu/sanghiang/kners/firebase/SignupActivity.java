package com.ndu.sanghiang.kners.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.ndu.sanghiang.kners.MainActivity;
import com.ndu.sanghiang.kners.R;
import com.ndu.sanghiang.kners.service.ConnectivityReceiver;
import com.ndu.sanghiang.kners.service.MyApplication;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private TextInputEditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private static final String TAG = "";
    private ProgressBar progressBar;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Import Toolbar
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView btnSignUp = findViewById(R.id.signin_page);
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
        });
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.input_email);
        progressBar = findViewById(R.id.progressBar);
        editTextPassword = findViewById(R.id.input_password);
        Button buttonSignUp = findViewById(R.id.button_signup);

        SharedPreferences profileData = getSharedPreferences("Profile", Context.MODE_PRIVATE);
        editor = profileData.edit();

        buttonSignUp.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Please enter email id");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Please enter password");
                return;
            }
            checkConnection();
            editor.putString("Email",email);
            editor.putString("Password",password);
            editor.apply();
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, task -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
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
        TextView textView = sbView.findViewById(R.id.snackbar_text);
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