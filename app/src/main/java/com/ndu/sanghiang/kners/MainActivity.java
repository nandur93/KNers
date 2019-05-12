package com.ndu.sanghiang.kners;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ndu.sanghiang.kners.firebase.LoginActivity;
import com.ndu.sanghiang.kners.indevelopment.HistoryActivity;
import com.ndu.sanghiang.kners.indevelopment.InventoryManagerActivity;
import com.ndu.sanghiang.kners.indevelopment.ProdukKnowledgeActivity;
import com.ndu.sanghiang.kners.ocr.OcrCaptureActivity;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button buttonProdukKnowledge, buttonAbout,
            buttonInventoryManager, buttonBarcodeList,
            buttonOcrCapture, buttonLogin, buttonLogout;
    TextView navEmail, navUserName, textVersionCode, textVersionName;
    ImageView navPhoto;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser userEmail = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseUser userName = FirebaseAuth.getInstance().getCurrentUser();
    Uri userPhoto = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        //Pindahan
        // Sample AdMob app ID: ca-app-pub-4368595636314473~7130779124
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~6300978111");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        buttonProdukKnowledge = findViewById(R.id.button_produk_knowledge);
        buttonInventoryManager = findViewById(R.id.button_inventory_manager);
        buttonAbout = findViewById(R.id.button_about);
        buttonBarcodeList = findViewById(R.id.button_history);
        buttonOcrCapture = findViewById(R.id.button_ocr_activity);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogout = findViewById(R.id.button_logout);
        mAuth = FirebaseAuth.getInstance();
        textVersionCode = findViewById(R.id.nav_version_code);
        textVersionName = findViewById(R.id.nav_version_name);
        /*if (mAuth.getCurrentUser() != null){
            String EMAIL= mAuth.getCurrentUser().getEmail();
            if (!EMAIL.equals("example@gmail.com")){
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        }*/
        //Permission Marshmelo
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.INTERNET},
                1);

        buttonProdukKnowledge.setOnClickListener(view -> {
            Intent produkIntent = new
                    Intent(MainActivity.this, ProdukKnowledgeActivity.class);
            startActivity(produkIntent);
        });

        buttonInventoryManager.setOnClickListener(view -> {
            Intent inventIntent = new
                    Intent(MainActivity.this, InventoryManagerActivity.class);
            startActivity(inventIntent);
        });

        buttonAbout.setOnClickListener(view -> AboutActivity());

        buttonLogin.setOnClickListener(view ->{
            Intent loginIntent = new
                    Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        mAuthListner = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser()==null)
            {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        };

        if (userEmail != null) {
            String userEmail = this.userEmail.getEmail();
            navEmail.setText(userEmail);


        } else {
            // No userEmail is signed in
            navEmail.setText("Logout");
        }
        if (userPhoto != null){

            String url = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()).toString();
            new DownloadImage(navPhoto).execute(url);
        } else {
            navPhoto.findViewById(R.id.image_view_email_photo);
        }
        if(userName !=null){
            navUserName.setText(userName.getDisplayName());
        } else {
            navUserName.setText("KNers");
        }
        buttonLogout.setOnClickListener(v -> mAuth.signOut());

        //End Pindahan
    }
    //get the current version number and name



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Keluar Dari Aplikasi")
                    .setMessage("Semua data dan progress Anda akan tetap tersimpan")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
            //super.onBackPressed();
        }
    }

    public void onInfoVersionCode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title_info_version_code);
        builder.setMessage(R.string.title_info_version_code);
        builder.setIcon(R.drawable.ic_error_outline_black_24dp);
        AlertDialog diag = builder.create();
        //Display the message!
        diag.show();
    }

    public void onInfoVersionName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title_info_version_name);
        builder.setMessage(R.string.sum_info_version_name);
        builder.setIcon(R.drawable.ic_error_outline_black_24dp);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_about){
            AboutActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Keluar?")
                    .setMessage("Semua sesi Anda akan tersimpan")
                    .setPositiveButton("Keluar", (dialog, which) -> mAuth.signOut())
                    .setNegativeButton("Batal", null)
                    .show();

        }else if (id == R.id.nav_about) {
            AboutActivity();

        }else if(id == R.id.nav_version_code){
            onInfoVersionCode();
        }else if(id==R.id.nav_version_name){
            onInfoVersionName();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void AboutActivity(){
        Intent aboutIntent = new
                Intent(MainActivity.this,AboutActivity.class);
        startActivity(aboutIntent);
    }

    public void goToBrowser(View view) {
        //activity baru
        Intent browserIntent = new
                Intent(MainActivity.this,WebViewActivity.class);
        startActivity(browserIntent);
    }

    public void codeMatchActivity(View view) {
        //activity baru
        Intent myIntent = new
                Intent(MainActivity.this,CodeMatchActivity.class);
        startActivity(myIntent);
    }

    public void barcodeListActivity(View view) {
        //activity baru
        Intent myIntent = new
                Intent(MainActivity.this, HistoryActivity.class);
        startActivity(myIntent);
    }

    public void ocrCaptureActivity(View view) {
        //activity baru
        Intent myIntent = new
                Intent(MainActivity.this, OcrCaptureActivity.class);
        startActivity(myIntent);
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
