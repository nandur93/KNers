package com.ndu.sanghiang.kners;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ndu.sanghiang.kners.service.ConnectivityReceiver;
import com.ndu.sanghiang.kners.service.CopyLinkBroadcastReceiver;
import com.ndu.sanghiang.kners.service.MyApplication;
import com.ndu.sanghiang.kners.service.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private TextInputEditText editTextNik;
    private TextInputEditText editTextDept;
    private TextInputEditText editTextName;
    private TextInputEditText editTextEmail;
    private DatabaseReference profileUserRef;
    FirebaseUser userEmail = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseUser userName = FirebaseAuth.getInstance().getCurrentUser();

    Toolbar tToolbar;
    Spinner spinnerDept;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private User user;
    private String TAG;
    private Uri uri;
    private ImageView profileImage;
    private static final String tag = "Nandur93";
    private ArrayAdapter<String> dataAdapter;

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // TextInput
        /*TextInputLayout textInputLayoutUsername = findViewById(R.id.text_input_layout_username);
        TextInputLayout textInputLayoutNik = findViewById(R.id.text_input_layout_nik);
        TextInputLayout textInputLayoutEmail = findViewById(R.id.text_input_layout_email);*/
        TAG = ProfileActivity.class.getSimpleName();
        uri = Uri.parse("https://accounts.google.com/SignOutOptions?hl=en&continue=https://aboutme.google.com/");
        editTextName = findViewById(R.id.edit_text_username);
        editTextNik = findViewById(R.id.edit_text_nik);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextDept = findViewById(R.id.edit_text_dept);
        // editTextUserName = new TextInputEditText(textInputLayoutUsername.getContext());
        // Spinner element
        spinnerDept = findViewById(R.id.spinner);
        Button buttonUpdate = findViewById(R.id.button_update);
        profileImage = findViewById(R.id.imageViewProfile);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean browser = sharedPrefs.getBoolean("browser_customtab", true);
        editor = sharedPrefs.edit();


        //Import Toolbar
        tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Spinner click listener
        spinnerDept.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("UNREGISTERED");
        categories.add("Admin");
        categories.add("MS Plant");
        categories.add("Quality Assurance");
        categories.add("Produksi");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerDept.setAdapter(dataAdapter);

        // Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // get current userId
        String userId = mAuth.getCurrentUser().getUid();
        // Load photo
        loadImageFromPicasso();

        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("profile");
        // Load data langsung dari firebase
        // Load nik dan email
        checkConnection();

        // Trigger spinner
        editTextDept.setOnClickListener(view -> spinnerDept.performClick());

        // Save / update the user
        buttonUpdate.setOnClickListener(view -> {
            //TODO NIK and Dept
            //TODO isi dept dengan password

            // Simpan ke firebase
            String name, email, nik, dept;
            name = editTextName.getText().toString();
            email = editTextEmail.getText().toString();
            nik = editTextNik.getText().toString();
            dept = spinnerDept.getSelectedItem().toString();
            updateFirebaseUser(name, nik, email, dept);
            // Simpan ke sharedPreferences
            updateSharedPref();
            // Langsung ke mainMenu
            gotoMainMenu();
        });

        profileImage.setOnClickListener(v -> {
            //sound from settings
            if (!browser) {
                externalBrowser();
            } else {
                // Do something
                customTabBrowser();
            }
        });
    } // <---- batas onCreate -------

    private void externalBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void customTabBrowser() {
        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        // and launch the desired Url with CustomTabsIntent.launchUrl()
        Intent intent = new Intent(this, CopyLinkBroadcastReceiver.class);
        String menuItemCopy = getResources().getString(R.string.copy_link);
        PendingIntent menuItemCopyIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, uri);

        // Changes the background color for the omnibox. colorInt is an int
        // that specifies a Color.

        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.addMenuItem(menuItemCopy, menuItemCopyIntent);
        builder.setStartAnimations(this, R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
        builder.setExitAnimations(this, R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_navigate_before_black_24dp));
        builder.addDefaultShareMenuItem();
        builder.setShowTitle(true);
    }

    private void updateFirebaseUser(String name, String nik, String email, String dept) {
        createUser(name, nik, email, dept);
        // updating the user via child nodes / ngupdate ke firebase
        profileUserRef.child("name").setValue(name);
        profileUserRef.child("nik").setValue(nik);
        profileUserRef.child("email").setValue(email);
        profileUserRef.child("dept").setValue(dept);

        Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
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
            loadFirebaseUserData();
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            loadOfflineData();
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

    private void loadFirebaseUserData() {
        // Load nama
        if (userName != null) {
            String userName = this.userName.getDisplayName();
            editTextName.setText(userName);
        }
        editTextName.setText(userName.getDisplayName());
        // Load email
        if (userEmail != null) {
            String userEmail = this.userEmail.getEmail();
            editTextEmail.setText(userEmail);
        }
        // Configuration
        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String myName = dataSnapshot.child("name").getValue().toString();
                    String myNik = dataSnapshot.child("nik").getValue().toString();
                    String myEmail = dataSnapshot.child("email").getValue().toString();
                    String myDept = dataSnapshot.child("dept").getValue().toString();
                    // Load nama
                    editTextName.setText(userName.getDisplayName());
                    String shNik = sharedPrefs.getString("user_nik", "UNREGISTERED");
                    String shName = sharedPrefs.getString("user_name", "KNers");
                    String shEmail = sharedPrefs.getString("user_email", "");
                    String shDept = sharedPrefs.getString("user_dept", "UNREGISTERED");
                    //Apakah user login pakai password/email?
                    for (UserInfo userInfo : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                        if (userInfo.getProviderId().equals("password"))
                        //Ya
                        {//jika ada username
                            editTextName.setFocusable(true);
                            editTextName.setFocusableInTouchMode(true);
                            if (dataSnapshot.exists()) {
                                editTextNik.setText(myNik);
                            } else {
                                editTextNik.setText(shNik);
                            }

                            if (dataSnapshot.exists()) {
                                editTextEmail.setText(myEmail);
                            } else {
                                editTextEmail.setText(shEmail);
                            }

                            if (dataSnapshot.exists()) {
                                editTextDept.setText(myDept);
                            } else {
                                editTextDept.setText(shDept);
                            }
                            spinnerDept.setAdapter(dataAdapter);
                            if (myDept != null) {
                                int spinnerPosition = dataAdapter.getPosition(myDept);
                                spinnerDept.setSelection(spinnerPosition);
                            } else {
                                int spinnerPosition = dataAdapter.getPosition(shDept);
                                spinnerDept.setSelection(spinnerPosition);
                            }
                            if (dataSnapshot.exists()) {
                                editTextName.setText(myName);
                            } else {
                                editTextName.setText(shName);
                            }
                            //Tidak (pakai google login)
                        } else {
                            //load name dan nik dari firebase
                            editTextNik.setText(myNik); //jika shared preferences kosong maka akan muncul UNREGISTERED
                            editTextEmail.setText(myEmail);
                            editTextDept.setText(myDept);
                            spinnerDept.setAdapter(dataAdapter);
                            if (myDept != null) {
                                int spinnerPosition = dataAdapter.getPosition(myDept);
                                spinnerDept.setSelection(spinnerPosition);
                            }
                            editTextName.setFocusable(false);
                            editTextName.setFocusableInTouchMode(false);
                        }
                    }
                    updateSharedPref();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSharedPref() {
        String name, email, nik, dept;
        name = editTextName.getText().toString();
        email = editTextEmail.getText().toString();
        nik = editTextNik.getText().toString();
        dept = spinnerDept.getSelectedItem().toString();
        editor.putString("user_name", name);
        editor.putString("user_email", email);
        editor.putString("user_nik", nik);
        editor.putString("user_dept", dept);
        editor.apply();
        Log.i(tag, "Data stored to shared pref");
    }

    private void loadImageFromPicasso() {
        //picasso
        Uri userPhoto = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
        if (userPhoto != null) {
            String originalPieceOfUrl = "s96-c/photo.jpg";
            // Variable holding the new String portion of the url that does the replacing, to improve image quality
            String newPieceOfUrlToAdd = "s400-c/photo.jpg";
            String hdUserPhoto = userPhoto.toString();
            String hdFinalUser = hdUserPhoto.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
            Picasso.get()
                    .load(hdFinalUser)
                    .placeholder(R.drawable.ic_launcher_round)
                    .error(R.drawable.ic_launcher_round)
                    .into(profileImage);
        } else {
            loadNoImage();
        }
    }

    private void loadNoImage() {
        Uri uri = Uri.parse("https://lh3.googleusercontent.com/5Lfd3sDr2_KkGw2TV5ccn57xnZyTbGQHHJUxipkHf64W8ryfGHqncL5xDh2Yxwo8hKL81fPXeMYqab_khBRnQggmTsVEmTutWUf_sQkxLwBxiOTBcTohNAJeTJfXv9Pmzjyh_cuJYuU3yO1Pgm5SuSSjO-o_ivPgTQndfJR9AvMOPIC8GxWvzTnqom7uPNdFh7U-4ebQz4sAJOsccPZcLSzBMZIVD31KZJSxZIu9XKJksJuRCyFpDQ3j1PhlyP8fYCjM7fWa0pjLOFHS_ofrGIUFUZImqa9LA5bXfZOwB0mChOmficpYy4ET2patEAK9HHNXlRTwo7aSPfryg3lY5Cwz8X5ptf6gy_2UkKOIc1N8ACKfVj1cOPiR40gAHPl7saNjbqYCdk6UjqTcPDn1xkk7-M5bt8dOYbBFuunEV8zEZq1RU1QPelJ4GIHfzpgcbnNljsxK4bRjh79LFOnTnnHB8A9tgj9n4GLL6ekfF3Q1n6wWLN4W4wZN_SrY9raKn8jhl5E2fXczWNNKj4gSkY2qTW69y1M-iSwqtqdricbiwdMbEDVTM_2whSzkNOxBIg3opJiaddrEhDowlGuF-LvJeQ_JjXOOKkA2punukporIxHXTg8SX1fK8LI2EvP-NtK9WOWmlmtRQFfSLseTv3ACJTTdyeo=s72-no");
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.ic_launcher_round)
                .error(R.drawable.ic_launcher_round)
                .resize(240, 240)
                .into(profileImage);
    }

    private void loadOfflineData() {
        // ketika di load, dapatkan value dari shared pref langsung
        String shNik = sharedPrefs.getString("user_nik", "UNREGISTERED");
        String shName = sharedPrefs.getString("user_name", "KNers");
        String shEmail = sharedPrefs.getString("user_email", "");
        String shDept = sharedPrefs.getString("user_dept", "UNREGISTERED");
        int shDeptSpin = sharedPrefs.getInt("user_dept_spinner", 0);

        editTextName.setText(shName);
        editTextNik.setText(shNik);
        editTextEmail.setText(shEmail);
        editTextDept.setText(shDept);
        spinnerDept.setSelection(shDeptSpin);
        Log.i(tag, "Loaded Offline Data");
    }

    private void gotoMainMenu() {
        Intent gotoMainMenu = new
                Intent(ProfileActivity.this, MainActivity.class);
        startActivity(gotoMainMenu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // Shared Pref
        String item = parent.getItemAtPosition(position).toString();
        editTextDept.setText(item);
        int selectedPosition = spinnerDept.getSelectedItemPosition();
        editor.putInt("user_dept_spinner", selectedPosition);
        editor.apply();

        // Showing selected spinner item
        // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // Toast.makeText(ProfileActivity.this, "Cancel", Toast.LENGTH_LONG).show();
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String name, String nik, String email, String dept) {
        User user = new User(name, nik, email, dept);
        profileUserRef.setValue(user);
        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }
                Log.e(TAG, "User data is changed!" + user.name + ", " + user.nik + ", " + user.dept);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }
}

    // Download userId Image
    /*@SuppressLint("StaticFieldLeak")
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
    }*/




/*        if (userPhoto != null){
            String url = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()).toString();
            new ProfileActivity.DownloadImage(profileImage).execute(url);
        } else {
            for (UserInfo userInfo: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (userInfo.getProviderId().equals("password")) {
                    profileImage.setBackgroundResource(R.drawable.ic_launcher_round);
                } else {
                    //
                }
            }
        }*/
    // Load email
