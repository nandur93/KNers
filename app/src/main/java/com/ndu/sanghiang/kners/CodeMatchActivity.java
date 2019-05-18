package com.ndu.sanghiang.kners;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
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
import com.ndu.sanghiang.kners.ocr.OcrCaptureActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import static android.widget.Toast.LENGTH_SHORT;
import static com.ndu.sanghiang.kners.R.id.edit_text_actcode;



public class CodeMatchActivity extends AppCompatActivity {
    TextView textViewActCode, textViewResult, textBoNumbTitle;
    EditText editTextStdCode, editTextActCode, editTextNoBo;
    Button buttPreview, buttVerify, buttScan;
    Toolbar tToolbar;
    private DrawerLayout mDrawerLayout;
    private String boNumb;
    //    public static final String SCAN_RESULT = "com.ndu.sanghiang.kners";
    //    String str_act_code, str_verif_code;

    @SuppressLint({"ObsoleteSdkInt", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_match);
        //toolbar
        tToolbar = findViewById(R.id.tToolbar);
        //textview
        textViewActCode = findViewById(R.id.text_view_actcode);
        textViewResult = findViewById(R.id.text_view_result);
        textBoNumbTitle = findViewById(R.id.text_view_nobo_title);
        //edittext
        editTextStdCode = findViewById(R.id.edit_text_stdcode);
        editTextNoBo = findViewById(R.id.edit_text_nobo);
        editTextActCode = findViewById(edit_text_actcode);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        //buttons
        buttVerify = findViewById(R.id.button_verify);
        buttPreview = findViewById(R.id.button_preview);
        buttScan = findViewById(R.id.button_qrscan);


        // Sample AdMob app ID: ca-app-pub-4368595636314473~7130779124
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~6300978111");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //buttPreview.setVisibility(View.VISIBLE);


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.getBoolean("enable_sound",false);

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
        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
            if (extras != null) {
                String value = extras.getString("CODE_RESULT");
                textViewActCode
                        .setText(value);
                assert value != null;
                if (value.length() < 10) {
                    editTextActCode.setText(value.toUpperCase());
                    Toast.makeText(getApplicationContext(), "Teks terlalu pendek, minimal 10 karakter untuk bisa di optimisasi", LENGTH_SHORT).show();
                } else {
                    editTextActCode
                            .setText(value
                                    .toUpperCase()
                                    .substring(0, value.length() - 7)
                                    .replace(" ", ""));//aktual kode yang diimprove
                    //The key argument here must match that used in the other activity
                }
            }
        }
        //Tombol Verifikasi
        buttVerify.setOnClickListener(v -> {

            // String s1 = textViewActCode.getText().toString();
            String stringStdCode = editTextStdCode.getText().toString(); //hasil scan code yang di improve
            String stringActCode = editTextActCode.getText().toString(); //aktual pembanding kode hasil scan barcode/qrcode
            Toast toastBo = Toast.makeText(getApplicationContext(), "No BO tidak boleh kosong", LENGTH_SHORT);
            Toast toastRight = Toast.makeText(getApplicationContext(), "Kode Benar", LENGTH_SHORT);
            Toast toastWrong = Toast.makeText(getApplicationContext(), "Kode Salah", LENGTH_SHORT);

            final MediaPlayer soundCorrect = MediaPlayer.create(this,R.raw.sound_correct);
            final MediaPlayer soundWrong = MediaPlayer.create(this,R.raw.sound_wrong);
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            boolean sndSetting = sharedPrefs.getBoolean("enable_sound", false);



            if (stringActCode.equals("null")){
                textViewResult.setText(getString(R.string.text_code_empty)); //ketika aktual kode kosong, maka "teks kosong"
                // penambahan anti no bo kosong
                // ketika tombol verifikasi di klik
                // cek apakah no bo kosong

            } else if("".equals(editTextNoBo.getText().toString().trim())) {
                // ganti textViewResult text ke
                textViewResult.setText(getString(R.string.bo_null));
                // jika kosong maka toast "no bo tidak boleh kosong"/tambahkan trim untuk blank space
                toastBo.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toastBo.show();

            } else if(stringStdCode.equals(stringActCode)) { //ketika  standard dan actual sama
                textViewResult.setText(getString(R.string.text_code_right)); //tampilkan teks "kode benar"
                textViewResult.setTextColor(Color.BLUE); //warna teks menjadi biru
                toastRight.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toastRight.show();
                //sound from settings
                if (!sndSetting) {
                    soundCorrect.stop();
                } else {
                    // Do something
                    soundCorrect.start();
                }


            } else {
                //ketika stringStdCode dan stringActCode tidak cocok
                textViewResult.setText(getString(R.string.text_code_wrong)); //tampilkan teks "kode salah"
                textViewResult.setTextColor(Color.RED); //warna teks menjadi merah
                toastWrong.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toastWrong.show();
                //sound from settings
                if (!sndSetting) {
                    soundWrong.stop();
                } else {
                    // Do something
                    soundWrong.start();
                }

                /*
                int stdLength = editTextStdCode.getText().toString().trim().length();


                for (int i=0, j=1; i < stdLength; i++, j++) {
                    //setHighLightedText(editTextActCode, 0, 1, editTextStdCode.getText().toString().substring(0,1));
                    //Log.i(TAG, "stringStdCode "+ stringStdCode.substring(i,j));
                    //Log.i(TAG, "stringActCode "+ stringActCode.substring(i,j));
                    String actCodeString = editTextActCode.getText().toString();
                    String stdCodeString = editTextStdCode.getText().toString();
                    //find index secara loop
                    char actCodeChar = actCodeString.charAt(i);
                    char stdCodeChar = stdCodeString.charAt(i);
                    String newActString = String.valueOf(actCodeChar);
                    String newStdString = String.valueOf(stdCodeChar);
                    if (newStdString.equals(newActString)) {
                        Log.i(TAG, "True " + editTextActCode.getText().toString().substring(i,j) + "");
                        //editTextActCode.setText(editTextActCode.getText()+ editTextActCode.getText().toString().substring(i,j) + "");


                    } else{

                        //editTextActCode.setText(editTextActCode.getText()+editTextActCode.getText().toString().substring(i,j) + "");
                        //editTextActCode.setTextColor(Color.RED);
//join string
                        String delimiter = "";
                        StringBuilder result = new StringBuilder();
                        String prefix = "";
                        for (int charInt=0; charInt < stdLength; charInt++) {

                            result.append(prefix).append(stringActCode.charAt(charInt));
                            prefix = delimiter;
                        }

                        Log.i(TAG, "result " + result);
                        Log.i(TAG, "False " + editTextActCode.getText().toString().substring(i,j) + "");
                    }
                }*/



                        //setHighLightedText(editTextActCode, i,j, stringStdCode.substring(i,j));
//Converting string to char array.


                        //Showing char array on screen with Comma.


                /*int stdLength = editTextStdCode.getText().toString().trim().length();
                for (int i=0, j=1; i < stdLength; i++, j++){

                    //Toast toastDebug = Toast.makeText(getApplicationContext(),String.valueOf(stdLength), LENGTH_SHORT);

                    //toastDebug.show();
                    //Get value of Aktual code
                    String actCodeString = editTextActCode.getText().toString();
                    String stdCodeString = editTextStdCode.getText().toString();
                    //find index secara loop
                    char actCodeChar = actCodeString.charAt(i);
                    char stdCodeChar = stdCodeString.charAt(i);
                    //harusnya print
                    //E
                    //G
                    //O
                    //dst...
                    //Log.i(TAG, "setHighLightedText(editTextActCode, 0,i+1, editTextStdCode.getText().toString().substring(0,i+1)); " + actCodeChar);
                    //Convert char to string
                    String newActString = String.valueOf(actCodeChar);
                    String newStdString = String.valueOf(stdCodeChar);
                    if (newStdString.equals(newActString)){
                        editTextActCode.setTextColor(Color.BLACK);
                    } else {
                        editTextActCode.setTextColor(Color.RED);
                    }

                    //setHighLightedText(editTextActCode, (char) i, editTextStdCode.getText().toString().substring(i, j));
                    Log.i(TAG, "newActString "+ newActString);
                    Log.i(TAG, "newStdString "+ newStdString);
                    Log.i(TAG, "newStdString.equals(newActString) "+ newStdString.equals(newActString));

                }
*/

            }
            //vibrate from settings
            boolean vibrator = sharedPrefs.getBoolean("enable_vibrate",false);
            if (!vibrator){
                vib.cancel();
            } else {
                assert vib != null;
                vib.vibrate(100); //getar 100 millisecond
                //no vibrate
            }
        });


        //Simpan buttPreview pakai tombol
        buttPreview.setOnClickListener(v -> { //klik tombol buttPreview
            hideElements();
            //(new Handler()).postDelayed(this::preView, 2000);
            preView();
            //jalankanmethode buttPreview lalu kirim ke textViewResult
        });

        buttScan.setOnClickListener(v -> scanQR());
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            textViewActCode.setText(sharedText.trim());//aktual kode yang seharusnya tidak diimprove
            editTextActCode.setText(sharedText
                    .toUpperCase()
                    .substring(0, sharedText.length() - 7)
                    .replace(" ", ""));//aktual kode yang diimprove

            Toast.makeText(this, "Text recognized", LENGTH_SHORT).show();

        }
    }


    void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }

    /**
     *
     * @param startSpan
     * @param endSpan
     */

    private void highlightChar(Integer startSpan, Integer endSpan) {
        SpannableString spannableStr = new SpannableString(editTextActCode.getText().toString());
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.GREEN);
        spannableStr.setSpan(backgroundColorSpan, startSpan, endSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        editTextActCode.setText(spannableStr);

    }


    /**
     *
     * @param editText
     * @param fIndex
     * @param lIndex
     * @param textToHighlight
     */
    public void setHighLightedText(EditText editText, Integer fIndex, Integer lIndex, String textToHighlight) {
        String tvt = editText.getText().toString().substring(fIndex,lIndex);
        int ofe = tvt.indexOf(textToHighlight);
        Spannable wordToSpan = new SpannableString(editText.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                wordToSpan.setSpan(new BackgroundColorSpan(0xFFFFFF00), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the textViewResult arrays are empty.
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                qrScanner();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(CodeMatchActivity.this, "Kamera tidak diijinkan, SCAN QR Code tidak akan bisa digunakan", LENGTH_SHORT).show();
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
        showElements();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showElements();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //hideElements();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void hideElements() {
        //Toolbar tToolbar = findViewById(R.id.tToolbar);
        tToolbar.setVisibility(View.GONE);
        buttPreview.setVisibility(View.GONE);
        buttVerify.setVisibility(View.GONE);
        buttScan.setVisibility(View.GONE);
    }

    private void showElements(){
        //Toolbar tToolbar = findViewById(R.id.tToolbar);
        tToolbar.setVisibility(View.VISIBLE);
        buttPreview.setVisibility(View.VISIBLE);
        buttVerify.setVisibility(View.VISIBLE);
        buttScan.setVisibility(View.VISIBLE);
    }


    private void preView() {//metode buttPreview

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
            intent.putExtra("NOBO", boNumb);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);

            startActivity(intent);

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


    public void onScannerLaunch(View view) {
        textViewResult.setText(getString(R.string.text_verify_result));
        textViewResult.setTextColor(Color.BLACK);
        Intent smartLens = getPackageManager().getLaunchIntentForPackage("com.duyp.vision.textscanner");
        Intent googleVision = new Intent(CodeMatchActivity.this, OcrCaptureActivity.class);
        if (smartLens != null) {
            startActivity(smartLens);//null pointer check in case package name was not found
        }   else {
            startActivity(googleVision);
        }
        finish();
    }

    private void scanQR() {
        //Permission Marshmello
        ActivityCompat.requestPermissions(CodeMatchActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);

    }

    private void qrScanner() {

        textViewResult.setText(getString(R.string.text_verify_result));
        textViewResult.setTextColor(Color.BLACK);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Arahkan ke Barcode atau QR Code untuk SCAN");
        //integrator.setOrientationLocked(false);
        integrator.initiateScan(); // `this` is the current Activity
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                editTextNoBo.setText(result.getContents().toUpperCase().substring(result.getContents().lastIndexOf("/")+1));
                boNumb = String.valueOf(editTextNoBo.getText());
                int length = 6 + editTextNoBo.length();
                editTextStdCode.setText(result
                        .getContents()
                        .toUpperCase()
                        .substring(0, result.getContents().length() - length)
                        .replace("/", "\n")
                        .replace(" ", "")
                        .trim());
                //test chars
                /*char charStd1 = substringResult.charAt(0);
                char charStd2 = substringResult.charAt(1);
                char charStd3 = substringResult.charAt(2);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    editTextStdCode.setText(String.join("",String.valueOf(charStd1),String.valueOf(charStd2),String.valueOf(charStd3)));
                }*/

                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
