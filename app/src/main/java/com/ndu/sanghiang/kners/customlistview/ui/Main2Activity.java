package com.ndu.sanghiang.kners.customlistview.ui;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.ndu.sanghiang.kners.R;
import com.ndu.sanghiang.kners.customlistview.adapter.ImageListAdapter;
import com.ndu.sanghiang.kners.customlistview.model.Image;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    // ListView Object
    ListView listView;

    // Image ArrayList
    List<Image> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // 1. Initializing ListView And Image ArrayList
        imageList = new ArrayList<>();
        listView = findViewById(R.id.list);

        // 2. Prepare Image ArrayList [Add Some Static Data Into Array]


        // 3. Create ImageListAdapter Object
        ImageListAdapter adapter = new ImageListAdapter(this, R.layout.row_list_item, imageList);

        // 4. Set Adapter Into ListView
        listView.setAdapter(adapter);
    }
}
