package com.ndu.sanghiang.kners;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.widget.Toast.LENGTH_SHORT;


public class CodeMatchActivity extends AppCompatActivity {
    TextView actCode, result;
    EditText verifCode, actCodeEdit, noBo;
    Button preView;
    private DrawerLayout mDrawerLayout;
    //    public static final String SCAN_RESULT = "com.ndu.sanghiang.kners";
//    String str_act_code, str_verif_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_match);
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        actCode = findViewById(R.id.text_view_actual_code);
        verifCode = findViewById(R.id.edit_text_code);
        noBo = findViewById(R.id.edit_text_no_bo);
        actCodeEdit = findViewById(R.id.edit_text_actual_code);
        result = findViewById(R.id.textViewResult);
        preView = findViewById(R.id.button_preview);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Sample AdMob app ID: ca-app-pub-4368595636314473~7130779124
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~6300978111");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final MediaPlayer correct = MediaPlayer.create(this,R.raw.sound_correct);
        final MediaPlayer wrong = MediaPlayer.create(this,R.raw.sound_wrong);

        setSupportActionBar(tToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(actionbar).setDisplayHomeAsUpEnabled(true);
        }
        //Menampilkan garis horizontal 3
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(actionbar).setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }


        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent //memerima text yang dikirim dari aplikasi lain
            }
        }


        Button button = findViewById(R.id.button_verify);
        button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // String s1 = actCode.getText().toString();
                String s2 = verifCode.getText().toString(); //hasil scan code yang di improve
                String s3 = actCodeEdit.getText().toString(); //aktual pembanding kode hasil scan barcode/qrcode

                if (s3.equals("null")){
                    result.setText(getString(R.string.text_null)); //ketika aktual kode kosong, maka "teks kosong"
                // penambahan anti no bo kosong
                    // ketika tombol verifikasi di klik
                    // cek apakah no bo kosong
                } else if("".equals(noBo.getText().toString().trim())) {
                    // ganti result text ke
                    result.setText(getString(R.string.bo_null));
                    // jika kosong maka toast "no bo tidak boleh kosong"/tambahkan trim untuk blank space
                    Toast.makeText(getApplicationContext(), "No BO tidak boleh kosong", Toast.LENGTH_SHORT).show();

                } else if(s2.equals(s3)) { //ketika  s2 dan s3 sama
                    result.setText(getString(R.string.text_equal)); //tampilkan teks "kode benar"
                    result.setTextColor(Color.BLUE); //warna teks menjadi biru
                    Toast.makeText(getApplicationContext(), "Kode Benar", Toast.LENGTH_SHORT).show();
                    correct.start(); //bunyikan suara benar

                } else if (!s2.equals(s3)) { //ketika s2 dan s3 tidak cocok
                    result.setText(getString(R.string.text_not_equal)); //tampilkan teks "kode salah"
                    result.setTextColor(Color.RED); //warna teks menjadi merah
                    Toast.makeText(getApplicationContext(), "Kode Salah", Toast.LENGTH_SHORT).show();

                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    assert vib != null;
                    vib.vibrate(100); //getar 100 millisecond

                    wrong.start(); //bunyikan suara salah
                } else {
                    Toast.makeText(getApplicationContext(), "Error", LENGTH_SHORT).show();
                }
            }
        });
//Simpan preView pakai tombol
        preView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //klik tombol preView
                hideToolbar();
                preView(); //jalankanmethode preView lalu kirim ke result
            }
        });
    }
//Permission Result
@Override
public void onRequestPermissionsResult(int requestCode,
                                       String permissions[], int[] grantResults) {
    switch (requestCode) {
        case 1: {

            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                qrScanner();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(CodeMatchActivity.this, "Kamera tidak diijinkan, SCAN QR Code tidak akan bisa digunakan", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }
}

    @Override
    protected void onRestart() {
        super.onRestart();
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        tToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        tToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void hideToolbar() {
            Toolbar tToolbar = findViewById(R.id.tToolbar);
            tToolbar.setVisibility(View.GONE);
    }

    private void preView() {//metode preView
        try {

            // create bitmap screen capture
            View rootView = getWindow().getDecorView().getRootView();
            rootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
            rootView.setDrawingCacheEnabled(false);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            baos.flush();
            baos.close();
            byte[] b = baos.toByteArray();
            Intent intent=new Intent(CodeMatchActivity.this,ScreenshotActivity.class);
            intent.putExtra("picture", b);
            intent.putExtra("noBo", String.valueOf(noBo));
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);

            startActivity(intent);

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            actCode.setText(sharedText.trim());//aktual kode yang seharusnya tidak di improve s1
            actCodeEdit.setText(sharedText
                    .toUpperCase()
                    .substring(0, sharedText.length() - 7)
                    .replace(" ", ""));//aktual kode yang di improve s2
            Toast.makeText(this, "Text recognized", Toast.LENGTH_SHORT).show();
        }
    }

    public void onScannerLaunch(View view) {
        result.setText(getString(R.string.text_result));
        result.setTextColor(Color.BLACK);
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.duyp.vision.textscanner");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

    public void onClickScan(View view) {
        //Permission Marshmello
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(CodeMatchActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
    }

    private void qrScanner() {

        result.setText(getString(R.string.text_result));
        result.setTextColor(Color.BLACK);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Arahkan ke Barcode atau QR Code untuk SCAN");
        //integrator.setOrientationLocked(false);
        integrator.initiateScan(); // `this` is the current Activity
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //new seder test
//                Intent intent = new Intent(this, HistoryActivity.class);
                verifCode.setText(result
                        .getContents()
                        .toUpperCase()
                        .substring(0, result.getContents().length() - 6)
                        .replace("/", "\n")
                        .replace(" ", "")
                        .trim());
//                String results = verifCode.getText().toString();
                /*intent.putExtra(SCAN_RESULT, results);
                startActivity(intent);*/
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
