package com.ndu.sanghiang.kners.smartqap.inline;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.ndu.sanghiang.kners.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCP_LINE;
import static com.ndu.sanghiang.kners.smartqap.inline.QcInlineFragment.LIST_FROM_KEY;
import static com.ndu.sanghiang.kners.smartqap.inline.QcInlineFragment.PACKING;
import static com.ndu.sanghiang.kners.smartqap.inline.QcInlineFragment.PROCESS;

public class EformLineActivity extends AppCompatActivity {

    private static final String TAG = EformLineActivity.class.getSimpleName();
    private String lineActive;
    private String comeFrom;

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
        comeFrom = getIntent().getStringExtra(LIST_FROM_KEY);
        Log.d(TAG, "onCreate: " + comeFrom);

        if (Objects.equals(comeFrom, PACKING)) {
            Log.d(TAG, "onCreate: " + PACKING);
            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), FragmentPagerItems.with(this)
                    .add("Line " + lineActive.toUpperCase(), PackingFragment.class)
                    .create());

            ViewPager viewPager = findViewById(R.id.viewpager);
            viewPager.setAdapter(adapter);

            SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
            viewPagerTab.setViewPager(viewPager);

        } else if (Objects.equals(comeFrom, PROCESS)) {
            Log.d(TAG, "onCreate: " + PROCESS);
            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), FragmentPagerItems.with(this)
                    .add("Line " + lineActive.toUpperCase(), LineFragment.class)
                    .create());

            ViewPager viewPager = findViewById(R.id.viewpager);
            viewPager.setAdapter(adapter);

            SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
            viewPagerTab.setViewPager(viewPager);
        }
    }
}