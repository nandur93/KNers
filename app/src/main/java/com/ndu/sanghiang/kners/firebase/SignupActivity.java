package com.ndu.sanghiang.kners.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ndu.sanghiang.kners.MainActivity;
import com.ndu.sanghiang.kners.R;
import com.ndu.sanghiang.kners.service.ConnectivityReceiver;
import com.ndu.sanghiang.kners.service.MyApplication;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private TextInputEditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private static final String TAG = "Nandur93";
    private ProgressBar progressBar;
    private SharedPreferences profileData;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Initialize
        //Import Toolbar
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView textViewSignIn = findViewById(R.id.signin_page);
        Button buttonSignUp = findViewById(R.id.button_signup);
        editTextEmail = findViewById(R.id.input_email);
        progressBar = findViewById(R.id.progressBar);
        editTextPassword = findViewById(R.id.input_password);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail.addTextChangedListener(new MyTextWatcher(editTextEmail));
        editTextPassword.addTextChangedListener(new MyTextWatcher(editTextPassword));

        textViewSignIn.setOnClickListener(v -> goToSignIn());
        buttonSignUp.setOnClickListener(v -> checkConnection());

        profileData = getSharedPreferences("Profile", Context.MODE_PRIVATE);
    }

    private void signUpSystem() {
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        String email;
        email = editTextEmail.getText().toString();
        String password;
        password = editTextPassword.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        //Simpan signin data ke Profile.xml SharedPreferences
                        SharedPreferences.Editor editor = profileData.edit();
                        editor.putString("Email",email);
                        editor.putString("Password",password);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), String.format("Welcome %s!", email), Toast.LENGTH_SHORT).show();
                        //FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignupActivity.this, task.getException().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }
    private boolean validateEmail() {

        if (editTextEmail.getText().toString().trim().isEmpty() || !isValidEmail(editTextEmail.getText().toString().trim())) {
            inputLayoutEmail.setError(getString(R.string.fui_invalid_email_address));
            requestFocus(editTextEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePassword() {
        if (editTextPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.fui_error_invalid_password));
            requestFocus(editTextPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private void goToSignIn() {
        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
        startActivity(intent);
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
        if (isConnected) {
            message = getString(R.string.fui_progress_dialog_loading);
            color = Color.WHITE;
            signUpSystem();
        } else {
            message = getString(R.string.no_connection);
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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }
}