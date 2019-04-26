package com.ndu.sanghiang.kners.indevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.ndu.sanghiang.kners.R;
import com.ndu.sanghiang.kners.WebViewActivity;

import java.util.Objects;

public class ProdukKnowledgeActivity extends AppCompatActivity {
    //declarasikan
    Button button_diabetasol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_knowledge);
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //lokasi button
        button_diabetasol = findViewById(R.id.button_diabetasol);

        //respon tombol click
        //tombol diabetasol
        // @Override
        button_diabetasol.setOnClickListener(v -> {
             //activity baru
             Intent myIntent = new
                     Intent(ProdukKnowledgeActivity.this, WebViewActivity.class);
             startActivity(myIntent);
         });



    }
}
