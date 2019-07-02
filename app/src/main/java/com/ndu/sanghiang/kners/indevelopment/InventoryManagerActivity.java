package com.ndu.sanghiang.kners.indevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ndu.sanghiang.kners.AboutActivity;
import com.ndu.sanghiang.kners.CodeMatchActivity;
import com.ndu.sanghiang.kners.DashboardProjectActivity;
import com.ndu.sanghiang.kners.R;

import java.util.Objects;

public class InventoryManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_manager);
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //gridview
        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(InventoryManagerActivity.this, gridViewString, gridViewImageId);
        androidGridView= findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener((parent, view, i, id) -> {
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
                myIntent = new Intent(view.getContext(), DashboardProjectActivity.class);
            }

            //lain2



            startActivity(myIntent);
        });
    }

    //gridview
    GridView androidGridView;

    String[] gridViewString = {
            "Item Baru", "About", "Produk", "Catatan Transaksi", "Dashboard Project", "Feedback",

    };
    int[] gridViewImageId = {
            R.drawable.ic_launcher, R.drawable.ic_info_outline_black_24dp, R.drawable.ic_help_outline_black_24dp, R.drawable.ic_settings_black_24dp, R.drawable.ic_launcher_round, R.drawable.ic_launcher,

    };

}