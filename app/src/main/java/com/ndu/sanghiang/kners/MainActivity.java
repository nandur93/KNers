package com.ndu.sanghiang.kners;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    Button button_produk_knowledge;
    Button button_about;
    Button button_inventory_manager;
    Button button_camera_ocr;
    Button button_barcode_list;
    Button button_ocr_capture;
    Button button_face_tracker;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Sample AdMob app ID: ca-app-pub-4368595636314473~7130779124
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~6300978111");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        button_produk_knowledge = (Button) findViewById(R.id.button_produk_knowledge);
        button_inventory_manager=(Button) findViewById(R.id.button_inventory_manager);
        button_about = (Button) findViewById(R.id.button_about);
        button_camera_ocr = (Button)findViewById(R.id.show_camera_activity_java_surface_view);
        button_barcode_list = (Button)findViewById(R.id.button_history);
        button_ocr_capture=(Button)findViewById(R.id.button_ocr_activity);
        Toolbar tToolbar = (Toolbar) findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);


        button_produk_knowledge.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent produkIntent = new
                        Intent(MainActivity.this,ProdukKnowledgeActivity.class);
                startActivity(produkIntent);
            }
        });

        button_inventory_manager.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent inventIntent = new
                        Intent(MainActivity.this,InventoryManagerActivity.class);
                startActivity(inventIntent);
            }
        });

        button_camera_ocr.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent inventIntent = new
                        Intent(MainActivity.this,CameraOcrActivity.class);
                startActivity(inventIntent);
            }
        });

        button_about.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                Intent aboutIntent = new
                        Intent(MainActivity.this,AboutActivity.class);
                startActivity(aboutIntent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);

        /*/Searchable
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(SearchManager
                .getSearchableinfo(getComponentName()));*/

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Tentukan actionnya setiap klik
        switch (item.getItemId()){
            case R.id.action_search:
                return true;
            case R.id.action_about:
                AboutActivity();
                return true;
            case R.id.action_settings:
                SettingsActivity();
                return true;
            /*case R.id.action_check_updates:
                in developement */
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void AboutActivity(){
        Intent aboutIntent = new
                Intent(MainActivity.this,AboutActivity.class);
        startActivity(aboutIntent);
    }

    private void SettingsActivity(){
        Intent settingsIntent = new
                Intent(MainActivity.this,SettingsActivity.class);
        startActivity(settingsIntent);
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
                Intent(MainActivity.this,OcrCaptureActivity.class);
        startActivity(myIntent);
    }

}
