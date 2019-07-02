package com.ndu.sanghiang.kners;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ScreenshotActivity extends AppCompatActivity {

    private String numberBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        ImageView imageView = findViewById(R.id.screenShot);
        //Menampilkan panah Back ←
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Menambahkan View Image dari tombol SAVE

        //Permission Marshmelo
        ActivityCompat.requestPermissions(ScreenshotActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        Bundle extras = getIntent().getExtras();
        String nOBO = null;
        if (extras != null) {
            nOBO = extras.getString("NOBO");
        }
        numberBO = String.valueOf(nOBO);
        byte[] b = new byte[0];
        if (extras != null) {
            b = extras.getByteArray("picture");
        }
        Bitmap bitmap = null;
        if (b != null) {
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        imageView.setImageBitmap(bitmap);


        Objects.requireNonNull(getSupportActionBar()).setTitle("Hasil dari BO: "+nOBO);


    }


    //Permission Marshmelo
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the textViewResult arrays are empty.
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Toast.makeText(getApplicationContext(), "Akses diijinkan", Toast.LENGTH_SHORT).show();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(ScreenshotActivity.this, "Akses ke SDCARD tidak diijinkan, aplikasi tidak bisa menyimpan gambar", Toast.LENGTH_SHORT).show();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //menambahkan menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.screenshot_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Tentukan actionnya setiap klik
        switch (item.getItemId()) {
            case R.id.action_save:
                //Karena isi akan terreset
                new AlertDialog.Builder(this)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Simpan Hasil?")
                        .setMessage("Hasil akan tersimpan dan bisa dilihat pada Gallery")
                        .setPositiveButton("Simpan", (dialog, which) -> saveImage())
                        .setNegativeButton("Batal", null)
                        .show();
                return true;
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Hapus Hasil?")
                        .setMessage("Hasil tidak akan tersimpan")
                        .setPositiveButton("Hapus", (dialog, which) -> deleteImage())
                        .setNegativeButton("Batal", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void deleteImage() {
        Toast.makeText(getApplicationContext(), "Image Deleted", Toast.LENGTH_SHORT).show(); //Delete Image methode
        backToCodeMatch();
    }

    private void backToCodeMatch() {
        Intent codeMatchIntent = new
                Intent(ScreenshotActivity.this,CodeMatchActivity.class);
        codeMatchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(codeMatchIntent);
        finish();
    }

    private void saveImage() {
        Date d = new Date();
        CharSequence timestamp  = DateFormat.format("MM-dd-yy HH:mm:ss", d.getTime());
        int yYear = Calendar.getInstance().get(Calendar.YEAR);
        int wWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/Verifikasi Kode/"+yYear+"/RPS "+wWeek);
            myDir.mkdirs();
/*        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);*/
        Toast.makeText(this, "BO " + numberBO, Toast.LENGTH_LONG).show();

            String fname = "Verifikasi BO " + numberBO + " " + timestamp + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                ImageView imageView;
                imageView = findViewById(R.id.screenShot);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                imageView.setDrawingCacheEnabled(false);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"Hasil disimpan di "+file, Toast.LENGTH_LONG).show();
            // Tell the media scanner about the new file so that it is
            // immediately available to the userEmail.
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                    (path, uri) -> {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    });
        backToCodeMatch();
        }

    }
