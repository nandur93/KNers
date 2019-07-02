package com.ndu.sanghiang.kners;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.ndu.sanghiang.kners.firebase.SigninActivity;
import com.ndu.sanghiang.kners.indevelopment.HistoryActivity;
import com.ndu.sanghiang.kners.indevelopment.InventoryManagerActivity;
import com.ndu.sanghiang.kners.indevelopment.ProdukKnowledgeActivity;
import com.ndu.sanghiang.kners.ocr.OcrCaptureActivity;
import com.ndu.sanghiang.kners.projecttrackerfi.ProjectTrackerActivity;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button buttonProdukKnowledge, buttonBrowser, buttonInventoryManager, buttonCodeMatch, buttonGridMenu,
            buttonOcrCapture, buttonProjectTracker, buttonHistory, buttonUnmountOtg;
    TextView navEmail, navUserName;
    ImageView navPhoto;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser userEmail = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseUser userName = FirebaseAuth.getInstance().getCurrentUser();
    Uri userPhoto = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
    private DrawerLayout drawer;
    private Handler handler;
    private String versName;
    int versCode;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.getCurrentUser();
        updateUI();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        navEmail = headerView.findViewById(R.id.text_view_nav_email);
        navPhoto = headerView.findViewById(R.id.image_view_email_photo);
        navUserName = headerView.findViewById(R.id.text_view_nav_username);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Sample AdMob app ID: ca-app-pub-4368595636314473~7130779124
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~6300978111");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        handler = new Handler();
        buttonProdukKnowledge = findViewById(R.id.button_produk_knowledge);
        buttonBrowser = findViewById(R.id.button_browser);
        buttonInventoryManager = findViewById(R.id.button_inventory_manager);
        buttonCodeMatch = findViewById(R.id.button_code_match_activity);
        buttonGridMenu = findViewById(R.id.button_grid_menu);
        buttonHistory = findViewById(R.id.button_history);
        buttonOcrCapture = findViewById(R.id.button_ocr_activity);
        buttonProjectTracker = findViewById(R.id.button_project_tracker);
        buttonUnmountOtg = findViewById(R.id.button_unmount_otg);
        mAuth = FirebaseAuth.getInstance();

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Permission Marshmelo
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.INTERNET},
                1);
        buttonProdukKnowledge.setOnClickListener(v -> goToProdukKnowledge());
        buttonBrowser.setOnClickListener(v -> goToBrowser());
        buttonInventoryManager.setOnClickListener(v -> goToInventoryManager());
        buttonCodeMatch.setOnClickListener(v -> goToCodematch());
        buttonHistory.setOnClickListener(v -> goToHistory());
        buttonOcrCapture.setOnClickListener(v -> goToOcrCapture());
        buttonProjectTracker.setOnClickListener(v -> goToProjectTracker());
        buttonGridMenu.setOnClickListener(v -> goToGridMenu());
        buttonUnmountOtg.setOnClickListener(v -> goToUnmountOTG());
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        mAuthListner = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser()==null)
            {
                startActivity(new Intent(MainActivity.this, SigninActivity.class));
            }
        };

        if (userEmail != null) {
            String userEmail = this.userEmail.getEmail();
            navEmail.setText(userEmail);
        } else {
            // No userEmail is signed in
            navEmail.setText("Signed Out");
        }

        if (userPhoto != null){
            String url = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()).toString();
            new DownloadImage(navPhoto).execute(url);
        } else {
            navPhoto.findViewById(R.id.image_view_email_photo);
            //navPhoto = headerView.findViewById(R.id.image_view_email_photo);
        }

        if(userName != null){
            navUserName.setText(userName.getDisplayName());
        } else {
            for (UserInfo userInfo: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (userInfo.getProviderId().equals("password")) {
                    navUserName.setText("KNers");
                }
            }
            navUserName.setText("KNers");
        }

        //getVersionName
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versName = pInfo.versionName;
            versCode = pInfo.versionCode;
            editor.putString("version_name",versName);
            editor.putInt("version_code", versCode);
            editor.apply();
            Log.d("MyApp", "Version Name : " + versName + "\n Version Code : " + versCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("MyApp", "PackageManager Catch : " + e.toString());
        }
        // get menu from navigationView
        Menu menu = navigationView.getMenu();
        // find MenuItem you want to change
        MenuItem nav_appversion = menu.findItem(R.id.nav_version_name);
        // set new title to the MenuItem
        nav_appversion.setTitle(versName);
    }

    private void goToGridMenu() {
        Intent gridMenu = new
                Intent(MainActivity.this, InventoryManagerActivity.class);
        startActivity(gridMenu);
    }

    private void goToCodematch() {
        Intent codeMatchIntent = new
                Intent(MainActivity.this, CodeMatchActivity.class);
        startActivity(codeMatchIntent);
    }

    private void goToProdukKnowledge()  {
        Intent produkIntent = new
                Intent(MainActivity.this, ProdukKnowledgeActivity.class);
        startActivity(produkIntent);
    }

    private void goToInventoryManager() {
        Intent inventIntent = new
                Intent(MainActivity.this, InventoryManagerActivity.class);
        startActivity(inventIntent);
    }

    private void goToProjectTracker() {
        Intent projectTracker = new
                Intent(MainActivity.this, ProjectTrackerActivity.class);
        startActivity(projectTracker);
    }

    public void aboutActivity(){
        Intent aboutIntent = new
                Intent(MainActivity.this, AboutActivity.class);
        startActivity(aboutIntent);
    }

    public void goToBrowser() {
        Intent browserIntent = new
                Intent(MainActivity.this, WebViewActivity.class);
        startActivity(browserIntent);
    }

    public void goToHistory() {
        Intent historyIntent = new
                Intent(MainActivity.this, HistoryActivity.class);
        startActivity(historyIntent);
    }

    public void goToOcrCapture() {
        Intent ocrIntent = new
                Intent(MainActivity.this, OcrCaptureActivity.class);
        startActivity(ocrIntent);
    }

    public void goToUnmountOTG(){
        Intent i = new Intent(android.provider.Settings.ACTION_MEMORY_CARD_SETTINGS);
        startActivity(i);
    }
    //get the current version number and name



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Keluar Dari Aplikasi")
                    .setMessage("Semua data dan progress Anda akan tetap tersimpan")
                    .setPositiveButton("Yes", (dialog, which) -> closeActivity())
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    private void closeActivity() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }

    public void onInfoVersionName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_version);
        builder.setMessage("Verison Name: "+versName+"\n"+"Version Code: "+versCode);
        builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        AlertDialog diag = builder.create();
        //Display the message!
        diag.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            settingsActivity();
            return true;
        }
        if (id == R.id.action_about){
            aboutActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void settingsActivity() {
        Intent settingsIntent = new
                Intent(MainActivity.this,SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_settings) {
            closeDrawer();
            handler.postDelayed(this::settingsActivity, 250);
        }else if (id == R.id.nav_signout) {
            closeDrawer();
            handler.postDelayed(this::signOutAlert, 250);
        }else if (id == R.id.nav_about) {
            closeDrawer();
            // Do something after 5s = 5000ms
            handler.postDelayed(this::aboutActivity, 250);
        }else if(id==R.id.nav_version_name){
            closeDrawer();
            handler.postDelayed(this::onInfoVersionName, 250);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOutAlert() {
        new AlertDialog.Builder(this)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Keluar?")
                .setMessage("Semua sesi Anda akan tersimpan")
                .setPositiveButton("Keluar", (dialog, which) -> signOut())
                .setNegativeButton("Batal", null)
                .show();
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI());
    }

    private void updateUI() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Permission Marshmelo
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the textViewResult arrays are empty.
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                //Toast.makeText(MainActivity.this, "Internet diijinkan", Toast.LENGTH_SHORT).show();
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(MainActivity.this, "Ijin untuk mengakses internet ditolak", Toast.LENGTH_SHORT).show();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    public void closeDrawer(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
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
