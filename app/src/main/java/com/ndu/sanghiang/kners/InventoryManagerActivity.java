package com.ndu.sanghiang.kners;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class InventoryManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_manager);
        Toolbar tToolbar = (Toolbar) findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //gridview
        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(InventoryManagerActivity.this, gridViewString, gridViewImageId);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                //Toast.makeText(InventoryManagerActivity.this, "GridView Item: " + gridViewString[+i], Toast.LENGTH_SHORT).show();
                Intent myIntent = null;
                if(i == 0){
                    myIntent = new Intent(view.getContext(), NewItemActivity.class);
                }
                if(i == 1){
                    myIntent = new Intent(view.getContext(), AboutActivity.class);
                }
                if(i == 2){
                    myIntent = new Intent(view.getContext(), ProdukKnowledgeActivity.class);
                }
                if(i == 3){
                    myIntent = new Intent(view.getContext(), CodeMatchActivity.class);
                }
                if(i == 4){
                    myIntent = new Intent(view.getContext(), SettingsActivity.class);
                }

                //lain2



                startActivity(myIntent);
            }
        });
    }

    //gridview
    GridView androidGridView;

    String[] gridViewString = {
            "Item Baru", "About", "Produk", "Catatan Transaksi", "Pengaturan", "Feedback",

    };
    int[] gridViewImageId = {
            R.drawable.ic_launcher, R.drawable.ic_action_about, R.drawable.ic_action_help, R.drawable.ic_keyboard_arrow_left_white_24dp, R.drawable.ic_launcher, R.drawable.ic_launcher,

    };

}