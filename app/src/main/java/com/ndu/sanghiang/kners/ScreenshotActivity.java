package com.ndu.sanghiang.kners;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.Random;

public class ScreenshotActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        ImageView imageView = findViewById(R.id.screenShot);
        //Menampilkan panah Back ←
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }


        //Menambahkan View Image dari tombol SAVE
        Bundle extras = getIntent().getExtras();
        byte[] b = new byte[0];
        if (extras != null) {
            b = extras.getByteArray("picture");
        }
        Bitmap bitmap = null;
        if (b != null) {
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        imageView.setImageBitmap(bitmap);


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
                //TODO buat popup dan yakinkan kalau gambar mau di save,
                //Karena isi akan terreset
                Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
                SaveImage();
                return true;
            case R.id.action_delete:
                //TODO buat popup yang sama
                DeleteImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void DeleteImage() {

        Toast.makeText(getApplicationContext(), "Image Deleted", Toast.LENGTH_SHORT).show();
//Delete Image methode
        Intent produkIntent = new
                Intent(ScreenshotActivity.this,CodeMatchActivity.class);
        startActivity(produkIntent);
    }


    private void SaveImage() {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/Verifikasi Kode");
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Image-" + n + ".jpg";
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        }

    }
