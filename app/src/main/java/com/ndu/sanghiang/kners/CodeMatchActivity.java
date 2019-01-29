package com.ndu.sanghiang.kners;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
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

import static android.widget.Toast.LENGTH_SHORT;


public class CodeMatchActivity extends AppCompatActivity {
    TextView act_code, result;
    EditText verif_code, act_code_edit, no_bo;
    Button save_ss;
    private DrawerLayout mDrawerLayout;
    //    public static final String SCAN_RESULT = "com.ndu.sanghiang.kners";
//    String str_act_code, str_verif_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_match);
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        act_code = findViewById(R.id.text_view_actual_code);
        verif_code = findViewById(R.id.edit_text_code);
        no_bo = findViewById(R.id.edit_text_no_bo);
        act_code_edit = findViewById(R.id.edit_text_actual_code);
        result = findViewById(R.id.textViewResult);
        save_ss=findViewById(R.id.button_save);
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
        actionbar.setDisplayHomeAsUpEnabled(true);
        //Menampilkan garis horizontal 3
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


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

                // String s1 = act_code.getText().toString();
                String s2 = verif_code.getText().toString(); //hasil scan code yang di improve
                String s3 = act_code_edit.getText().toString(); //aktual pembanding kode hasil scan barcode/qrcode

                if (s3.equals("null")){
                    result.setText(getString(R.string.text_null)); //ketika aktual kode kosong, maka "teks kosong"
                // penambahan anti no bo kosong
                    // ketika tombol verifikasi di klik
                    // cek apakah no bo kosong
                } else if("".equals(no_bo.getText().toString().trim())) {
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
//Simpan screenshot pakai tombol
        save_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //klik tombol save_ss
                screenshot(); //jalankanmethode screenshot lalu kirim ke result
            }
        });
    }

   private void screenshot() {//metode screenshot
        try {

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            baos.flush();
            baos.close();
            byte[] b = baos.toByteArray();
            Intent intent=new Intent(CodeMatchActivity.this,ScreenshotActivity.class);
            intent.putExtra("picture", b);
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
            act_code.setText(sharedText.trim());//aktual kode yang seharusnya tidak di improve s1
            act_code_edit.setText(sharedText
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
                verif_code.setText(result
                        .getContents()
                        .toUpperCase()
                        .substring(0, result.getContents().length() - 6)
                        .replace("/", "\n")
                        .replace(" ", "")
                        .trim());
//                String results = verif_code.getText().toString();
                /*intent.putExtra(SCAN_RESULT, results);
                startActivity(intent);*/
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
