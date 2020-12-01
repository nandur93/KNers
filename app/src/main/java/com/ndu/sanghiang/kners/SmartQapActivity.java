package com.ndu.sanghiang.kners;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ndu.sanghiang.kners.indevelopment.CustomGridViewActivity;
import com.ndu.sanghiang.kners.smartqap.inline.QcInlineActivity;

import java.util.Objects;

public class SmartQapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_qap);
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //gridview
        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(SmartQapActivity.this, gridViewString, gridViewImageId);
        androidGridView = findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener((parent, view, i, id) -> {
            //Toast.makeText(GridMenuActivity.this, "GridView Item: " + gridViewString[+i], Toast.LENGTH_SHORT).show();
            Intent myIntent = null;
            if (i == 0) {
                myIntent = new Intent(view.getContext(), QcInlineActivity.class);
            }
            if (i == 1) {
 //               myIntent = new Intent(view.getContext(), AboutActivity.class);
                toaster("Coming Soon");
            }
            if (i == 2) {
//                myIntent = new Intent(view.getContext(), ProdukKnowledgeActivity.class);
                toaster("Coming Soon");
            }
            if (i == 3) {
//                myIntent = new Intent(view.getContext(), CodeMatchActivity.class);
                toaster("Segera");
            }
            if (i == 4) {
//                myIntent = new Intent(view.getContext(), DashboardProjectActivity.class);
                toaster("Coming Soon");
            }
            if (i == 5) {
//                myIntent = new Intent(view.getContext(), Main2Activity.class);
                toaster("Coming Soon");
            }
            try {
                startActivity(myIntent);
            } catch (Exception e) {
                toaster("Coming Soon -t");
                e.printStackTrace();
            }
        });
    }

    private void toaster(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //gridview
    GridView androidGridView;

    String[] gridViewString = {
            "QC Inline", "Incoming", "Hygiene", "System", "Finish Good", "Dashboard"
    };
    int[] gridViewImageId = {
            R.drawable.ic_baseline_assignment_black_24,
            R.drawable.ic_baseline_directions_car_black_24,
            R.drawable.ic_baseline_access_alarm_black_24,
            R.drawable.ic_baseline_access_alarm_black_24,
            R.drawable.ic_baseline_access_alarm_black_24,
            R.drawable.ic_baseline_access_alarm_black_24
    };
}