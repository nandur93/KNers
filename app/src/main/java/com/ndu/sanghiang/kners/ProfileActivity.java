package com.ndu.sanghiang.kners;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ndu.sanghiang.kners.service.User;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private TextView txtDetails;
    private TextInputEditText editTextUserName, editTextNik, editTextEmail, editTextDept;
    private Button buttonSaveChanges;
    private DatabaseReference mFirebaseDatabase;
    private String userId;
    FirebaseUser userEmail = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseUser userName = FirebaseAuth.getInstance().getCurrentUser();
    Uri userPhoto = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();

    Toolbar tToolbar;
    Spinner spinnerDept;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // TextInput
        /*TextInputLayout textInputLayoutUsername = findViewById(R.id.text_input_layout_username);
        TextInputLayout textInputLayoutNik = findViewById(R.id.text_input_layout_nik);
        TextInputLayout textInputLayoutEmail = findViewById(R.id.text_input_layout_email);*/
        editTextUserName = findViewById(R.id.edit_text_username);
        editTextNik = findViewById(R.id.edit_text_nik);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextDept = findViewById(R.id.edit_text_dept);
        // editTextUserName = new TextInputEditText(textInputLayoutUsername.getContext());
        // Spinner element
        spinnerDept = findViewById(R.id.spinner);
        buttonSaveChanges = findViewById(R.id.button_save_changes);
        txtDetails = findViewById(R.id.text_view_details);
        ImageView profileImage = findViewById(R.id.imageViewProfile);
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        editTextDept.setText("Pilih Departemen");

        //Import Toolbar
        tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Spinner click listener
        spinnerDept.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("Admin");
        categories.add("MS Plant");
        categories.add("Quality Assurance");
        categories.add("Produksi");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerDept.setAdapter(dataAdapter);

        // Firebase
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        DatabaseReference profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        if(userName != null){
            editTextUserName.setText(userName.getDisplayName());
        } else {
            for (UserInfo userInfo: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (userInfo.getProviderId().equals("password")) {
                    editTextUserName.setText(R.string.app_name);
                }
            }
            editTextUserName.setText(R.string.app_name);
        }

        if (userPhoto != null){
            String url = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()).toString();
            new ProfileActivity.DownloadImage(profileImage).execute(url);
        } else {
            profileImage.findViewById(R.id.imageViewProfile);
            //navPhoto = headerView.findViewById(R.id.image_view_email_photo);
        }

        if (userEmail != null) {
            String userEmail = this.userEmail.getEmail();
            editTextEmail.setText(userEmail);
        } else {
            // No userEmail is signed in
            editTextEmail.setText(R.string.button_signout);
        }


        // store app title to 'app_title' node
        // mFirebaseInstance.getReference("app_title").setValue("Realtime Database");
        // Save / update the user
        buttonSaveChanges.setOnClickListener(view -> {
            String name = editTextUserName.getText().toString();
            String nik = editTextNik.getText().toString();
            String email = editTextEmail.getText().toString();
            String dept = spinnerDept.getSelectedItem().toString();

            // Check for already existed userId
            if (TextUtils.isEmpty(userId)) {
                createUser(name, nik, email, dept);
            } else {
                updateUser(name, nik, email, dept);
            }
        });

        editTextDept.setOnClickListener(view -> spinnerDept.performClick());

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String myName = dataSnapshot.child("name").getValue().toString();
                    String myNik = dataSnapshot.child("nik").getValue().toString();
                    String myEmail = dataSnapshot.child("email").getValue().toString();
                    //String myDept = dataSnapshot.child("dept").getValue().toString();

                    editTextUserName.setText(myName);
                    editTextNik.setText(myNik);
                    editTextEmail.setText(myEmail);
                    //spinnerDept.setPrompt(myDept);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toggleButton();
    }

    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            buttonSaveChanges.setText("Save");
        } else {
            buttonSaveChanges.setText("Update");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        editTextDept.setText(item);

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String name, String nik, String email, String dept) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name, nik, email, dept);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.name + ", " + user.nik + ", " + user.dept);

                // Display newly updated name and email
                txtDetails.setText(user.name + ", " + user.nik + ", " + user.dept);

                // clear edit text
                editTextUserName.setText("");
                editTextNik.setText("");
                //spinnerDept.setPrompt("");

                toggleButton();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String name, String nik, String email, String dept) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(nik))
            mFirebaseDatabase.child(userId).child("nik").setValue(nik);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);

        if (!TextUtils.isEmpty(dept))
            mFirebaseDatabase.child(userId).child("dept").setValue(dept);

        Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("StaticFieldLeak")
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.d("Error", Arrays.toString(e.getStackTrace()));

            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
