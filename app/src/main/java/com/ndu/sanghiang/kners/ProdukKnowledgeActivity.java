package com.ndu.sanghiang.kners;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ProdukKnowledgeActivity extends AppCompatActivity {
    //declarasikan
    Button button_diabetasol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_knowledge);
        Toolbar tToolbar = (Toolbar) findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //lokasi button
        button_diabetasol = (Button) findViewById(R.id.button_diabetasol);

        //respon tombol click
        //tombol diabetasol
        button_diabetasol.setOnClickListener(new View.OnClickListener() {
           // @Override
            public void onClick(View v) {
                //activity baru
                Intent myIntent = new
                        Intent(ProdukKnowledgeActivity.this,WebViewActivity.class);
                startActivity(myIntent);
            }
        });



    }
}
