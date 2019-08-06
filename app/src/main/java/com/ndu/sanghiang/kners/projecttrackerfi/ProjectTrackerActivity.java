package com.ndu.sanghiang.kners.projecttrackerfi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ndu.sanghiang.kners.R;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.AnakondaFragment;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.AnasebaFragment;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.EvaluasiHasilFragment;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.PenanggulanganFragment;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.RencanaFragment;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.StandarisasiFragment;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.TargetFragment;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.TemaFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ndu.sanghiang.kners.DashboardProjectActivity.PID;
import static com.ndu.sanghiang.kners.DashboardProjectActivity.PROJECT_DESC;
import static com.ndu.sanghiang.kners.DashboardProjectActivity.PROJECT_PAGER;
import static com.ndu.sanghiang.kners.DashboardProjectActivity.PROJECT_PROGRESS;
import static com.ndu.sanghiang.kners.DashboardProjectActivity.PROJECT_TITLE;

public class ProjectTrackerActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ProgressBar progressBar;
    TextView progressText, pidText;
    String projectProgress = "0%";
    String progR = "Progress: ";
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_tracker);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progress_text);
        pidText = findViewById(R.id.pid_text);
        tabLayout.setupWithViewPager(viewPager);

        int position = 0;
        String projectTheme, projectDesc, pid;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            position = extras.getInt(PROJECT_PAGER);
            projectTheme = extras.getString(PROJECT_TITLE);
            projectDesc = extras.getString(PROJECT_DESC);
            projectProgress = extras.getString(PROJECT_PROGRESS);
            pid = extras.getString(PID);
            String TAG = "Nandur93";
            Log.i(TAG, projectTheme+" "+projectDesc+" from tracker");
            editor.putString(PROJECT_TITLE, projectTheme);
            editor.putString(PROJECT_DESC, projectDesc);
            editor.putString(PID, pid);
            editor.putString(PROJECT_PROGRESS, projectProgress);
            editor.apply();
            pidText.setText(String.format("Pid: %s", pid));
            progressBar.setProgress(Integer.parseInt(projectProgress));
            progressText.setText(String.format("%s%s%%", progR, projectProgress));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    //progressStatus = 13;
                    //progressStatusText = progR+"13%";
                }
                if (position == 1) {
                    //progressStatus = 25;
                    //progressStatusText= progR+"25%";
                }
                if (position == 2) {
                    //progressStatus = 38;
                    //progressStatusText= progR+"38%";
                }
                if (position == 3) {
                    //progressStatus = 50;
                    //progressStatusText= progR+"50%";
                }
                if (position == 4) {
                    //progressStatus = 63;
                    //progressStatusText= progR+"63%";
                }
                if (position == 5) {
                    //progressStatus = 75;
                    //progressStatusText= progR+"75%";
                }
                if (position == 6) {
                    //progressStatus = 88;
                    //progressStatusText= progR+"88%";
                }
                if (position == 7) {
                    //progressStatus = 100;
                    //progressStatusText= progR+"100%";
                }
                //progressBar.setProgress(progressStatus);
                //progressText.setText(progressStatusText);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.setCurrentItem(position); // this way if there is no extras
        // you will get first position of viewpager
        // otherwise you will get the position of viewpager whichever was passed in the intent.
    }

    private void setupViewPager(@NonNull ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TemaFragment(), getString(R.string.menentukan_tema));
        adapter.addFragment(new TargetFragment(),getString(R.string.menentukan_target));
        adapter.addFragment(new AnakondaFragment(), getString(R.string.anakonda));
        adapter.addFragment(new AnasebaFragment(),getString(R.string.anaseba));
        adapter.addFragment(new RencanaFragment(), getString(R.string.rencana_penanggulangan));
        adapter.addFragment(new PenanggulanganFragment(), getString(R.string.penanggulangan));
        adapter.addFragment(new EvaluasiHasilFragment(),getString(R.string.evaluasi_hasil));
        adapter.addFragment(new StandarisasiFragment(), getString(R.string.standarisasi_dan_tindak_lanjut));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed(){
        if (viewPager.getCurrentItem()!=0){
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1, false);
        } else {
            finish();
        }
    }

}

