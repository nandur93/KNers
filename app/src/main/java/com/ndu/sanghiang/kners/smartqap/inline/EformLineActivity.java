package com.ndu.sanghiang.kners.smartqap.inline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.ndu.sanghiang.kners.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_BO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCP_LINE;

public class EformLineActivity extends AppCompatActivity {

    private String lineActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eform_line);
        AppBarLayout _appbar = findViewById(R.id.app_bar);
        _appbar.setExpanded(false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
//      https://stackoverflow.com/questions/28438030/how-to-make-back-icon-to-behave-same-as-physical-back-button-in-android
        }

        toolbar.setNavigationOnClickListener(view -> finish());

        lineActive = getIntent().getStringExtra(TXT_QCP_LINE);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Line " + lineActive.toUpperCase(), LineA1Fragment.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }
}