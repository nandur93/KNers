package com.ndu.sanghiang.kners.projecttrackerfi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.ndu.sanghiang.kners.DashboardProjectActivity;
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

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECTS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_DESC;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_PAGER;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_PROGRESS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_STATUS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_TITLE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.USERS;

public class ProjectTrackerActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ProgressBar progressBar;
    TextView progressText, pidText;
    private DatabaseReference projectRef;
    String projectProgress = "0%";
    String progR = "Progress: ";
    String TAG = "Nandur93";
    private SharedPreferences sharedPrefs;
    private SpeedDialView speedDialView;
    private String aktivitas = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_tracker);
        //Initial toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Initial Shared Prefeferences
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //Initial Firebase
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = Objects.requireNonNull(mCurrentUser).getUid();
        projectRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(PROJECTS);
        //Initial ViewPager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //Initial Progressbar
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progress_text);
        pidText = findViewById(R.id.pid_text);
        // Initial FAB
        speedDialView = findViewById(R.id.speedDialFabTracker);

        String TAG = "Nandur93";

        int extrasPosition = 0;
        String projectTheme, projectDesc, pid;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            //extras meta
            extrasPosition = extras.getInt(PROJECT_PAGER);
            projectProgress = extras.getString(PROJECT_PROGRESS);
            pid = extras.getString(PID);
            //extras Step 1
            projectTheme = extras.getString(PROJECT_TITLE);
            projectDesc = extras.getString(PROJECT_DESC);
            Log.i(TAG, projectTheme+" "+projectDesc+" "+ aktivitas);
            //put to shared pref META
            editor.putString(PID, pid);
            editor.putString(PROJECT_PROGRESS, projectProgress);
            //put to shared pref STEP 1
            editor.putString(PROJECT_TITLE, projectTheme);
            editor.putString(PROJECT_DESC, projectDesc);
            editor.apply();
            //bottom activity meta
            pidText.setText(String.format("Pid: %s", pid));
            progressBar.setProgress(Integer.parseInt(projectProgress));
            progressText.setText(String.format("%s%s%%", progR, projectProgress));
        }
        viewPager.setCurrentItem(extrasPosition); //jika tidak ada extras, maka viewpager otomatis ke index 0 (pertama)

        //FAB trigger
        if (viewPager.getCurrentItem() == extrasPosition) {
            //Fab action trigger
            switch (extrasPosition) {
                case 0:
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                    .setLabel("Next")
                                    .create());
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_tema, R.drawable.ic_mode_edit_white_24dp)
                                    .setLabel("Edit Tema")
                                    .create());
                    break;
                case 1:
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                    .setLabel("Next")
                                    .create());
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_target, R.drawable.ic_mode_edit_white_24dp)
                                    .setLabel("Edit Target")
                                    .create());
                    break;
                case 2:
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                    .setLabel("Next")
                                    .create());
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_anakonda, R.drawable.ic_mode_edit_white_24dp)
                                    .setLabel("Edit Anakonda")
                                    .create());
                    break;
                case 3:
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                    .setLabel("Next")
                                    .create());
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_anaseba, R.drawable.ic_mode_edit_white_24dp)
                                    .setLabel("Edit Anaseba")
                                    .create());
                    break;
                case 4:
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                    .setLabel("Next")
                                    .create());
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_rencana, R.drawable.ic_mode_edit_white_24dp)
                                    .setLabel("Edit Rencana Penanggulangan")
                                    .create());
                    break;
                case 5:
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                    .setLabel("Next")
                                    .create());
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_penanggulangan, R.drawable.ic_mode_edit_white_24dp)
                                    .setLabel("Edit Penanggulangan")
                                    .create());
                    break;
                case 6:
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                    .setLabel("Next")
                                    .create());
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_evaluasi, R.drawable.ic_mode_edit_white_24dp)
                                    .setLabel("Edit Evaluasi Hasil")
                                    .create());
                    break;
                case 7:
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                    .setLabel("Next")
                                    .create());
                    speedDialView.addActionItem(
                            new SpeedDialActionItem.Builder(R.id.fab_standarisasi, R.drawable.ic_mode_edit_white_24dp)
                                    .setLabel("Edit Standarisasi & Rencana Berikut")
                                    .create());
                    break;
                default:
                    speedDialView.open();
            }
            speedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
                @Override
                public boolean onMainActionSelected() {
                    //int currentPost = viewPager.getCurrentItem();
                    //tabLayout.getTabAt(currentPost + 1).select();
                    //Toast.makeText(getApplicationContext(), "GetCurrent Item " + viewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public void onToggleChanged(boolean isOpen) {

                }
            });
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                        .setLabel("Next")
                                        .create(),0);
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_tema, R.drawable.ic_mode_edit_white_24dp)
                                        .setLabel("Edit Tema")
                                        .create(),1);
                        break;
                    case 1:
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                        .setLabel("Next")
                                        .create(),0);
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_target, R.drawable.ic_mode_edit_white_24dp)
                                        .setLabel("Edit Target")
                                        .create(),1);
                        break;
                    case 2:
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                        .setLabel("Next")
                                        .create(),0);
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_anakonda, R.drawable.ic_mode_edit_white_24dp)
                                        .setLabel("Edit Anakonda")
                                        .create(),1);
                        break;
                    case 3:
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                        .setLabel("Next")
                                        .create(),0);
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_anaseba, R.drawable.ic_mode_edit_white_24dp)
                                        .setLabel("Edit Anaseba")
                                        .create(),1);
                        break;
                    case 4:
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                        .setLabel("Next")
                                        .create(),0);
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_rencana, R.drawable.ic_mode_edit_white_24dp)
                                        .setLabel("Edit Rencana Penanggulangan")
                                        .create(),1);
                        break;
                    case 5:
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                        .setLabel("Next")
                                        .create(),0);
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_penanggulangan, R.drawable.ic_mode_edit_white_24dp)
                                        .setLabel("Edit Penanggulangan")
                                        .create(),1);
                        break;
                    case 6:
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                        .setLabel("Next")
                                        .create(),0);
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_evaluasi, R.drawable.ic_mode_edit_white_24dp)
                                        .setLabel("Edit Evaluasi Hasil")
                                        .create(),1);
                        break;
                    case 7:
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                        .setLabel("Next")
                                        .create(),0);
                        speedDialView.replaceActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_standarisasi, R.drawable.ic_mode_edit_white_24dp)
                                        .setLabel("Edit Standarisasi & Rencana Berikut")
                                        .create(),1);
                        break;
                    default:
                        speedDialView.addActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_next, R.drawable.ic_navigate_next_white_24dp)
                                        .setLabel("Next")
                                        .create());
                        speedDialView.addActionItem(
                                new SpeedDialActionItem.Builder(R.id.fab_anakonda, R.drawable.ic_mode_edit_white_24dp)
                                        .setLabel("Edit Anakonda")
                                        .create());
                }
                speedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
                    @Override
                    public boolean onMainActionSelected() {
                        switch (position) {
                            case 0:
                                //updateFirebaseStep1();
                                //tabLayout.getTabAt(1).select();
                                break;
                            case 1: //progressStatus = 25;
                                //progressStatusText= progR+"25%";
                                //tabLayout.getTabAt(2).select();
                                break;
                            case 2: //progressStatus = 38;
                                //progressStatusText= progR+"38%";
                                //tabLayout.getTabAt(3).select();
                                break;
                            case 3: //progressStatus = 50;
                                //progressStatusText= progR+"50%";
                                //viewPager.setCurrentItem(4);
                                break;
                            case 4: //progressStatus = 63;
                                //progressStatusText= progR+"63%";
                                //viewPager.setCurrentItem(5);
                                break;
                            case 5: //progressStatus = 75;
                                //progressStatusText= progR+"75%";
                                //viewPager.setCurrentItem(6);
                                break;
                            case 6: //progressStatus = 88;
                                //progressStatusText= progR+"88%";
                                //viewPager.setCurrentItem(7);
                                break;
                            default: //progressStatus = 100;
                                //progressStatusText= progR+"100%";
                                break;
                        }
                        return false;
                    }

                    @Override
                    public void onToggleChanged(boolean isOpen) {

                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        Log.d(TAG, "position from ProjectTrackerActivity");
        speedDialView.setOnActionSelectedListener(speedDialActionItem -> {
            switch (speedDialActionItem.getId()) {
                case R.id.fab_tema:
                    Toast.makeText(ProjectTrackerActivity.this, "Tema", Toast.LENGTH_SHORT).show();
                    editStep1();
                    return false; // true to keep the Speed Dial open
                case R.id.fab_target:
                    Toast.makeText(ProjectTrackerActivity.this, "Target", Toast.LENGTH_SHORT).show();
                    editStep2();
                    return false;
                case R.id.fab_anakonda:
                    Toast.makeText(ProjectTrackerActivity.this, "Anakonda", Toast.LENGTH_SHORT).show();
                    editStep3();
                    return false;
                case R.id.fab_anaseba:
                    Toast.makeText(ProjectTrackerActivity.this, "Anaseba", Toast.LENGTH_SHORT).show();
                    return false;
                case R.id.fab_rencana:
                    Toast.makeText(ProjectTrackerActivity.this, "Rencana Penanggulangan", Toast.LENGTH_SHORT).show();
                    return false;
                case R.id.fab_penanggulangan:
                    Toast.makeText(ProjectTrackerActivity.this, "Penanggulangan", Toast.LENGTH_SHORT).show();
                    return false;
                case R.id.fab_evaluasi:
                    Toast.makeText(ProjectTrackerActivity.this, "Evaluasi Hasil", Toast.LENGTH_SHORT).show();
                    return false;
                case R.id.fab_standarisasi:
                    Toast.makeText(ProjectTrackerActivity.this, "Standarisasi & Rencana Berikut", Toast.LENGTH_SHORT).show();
                    return false;
                case R.id.fab_next:
                    try {
                        int currentPost = viewPager.getCurrentItem();
                        tabLayout.getTabAt(currentPost + 1).select();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "You reached the end", Toast.LENGTH_SHORT).show();
                    }
            }
            return false;
        });
    }

    private void editStep1() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_fragment_tema, null);
        dialogBuilder.setView(dialogView);

        final EditText title = dialogView.findViewById(R.id.dialogEditTextTema);
        final EditText desc = dialogView.findViewById(R.id.dialogEditTextDescTema);
        //get the existing value
        title.setText(TemaFragment.inputEditTextJudulTema.getText().toString());
        desc.setText(TemaFragment.inputEditTextDescTema.getText().toString());
        dialogBuilder.setTitle(R.string.menentukan_tema);
        dialogBuilder.setPositiveButton(R.string.ok, (dialog, whichButton) -> {
            String titleStr = title.getText().toString().trim();
            String descStr = desc.getText().toString().trim();
            //Dapatkan dan simpan data pid dari sharedpref
            String finalPid = sharedPrefs.getString(PID, ""); //contoh: -LkPHHo260u0OUK6nRZ5

            projectRef.child(finalPid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.i(TAG, finalPid + " exist ");
                        //jika -LkPHHo260u0OUK6nRZ5 ada
                        //update meta
                        projectRef.child(finalPid).child(PROJECT_STATUS).setValue("In Progress");
                        projectRef.child(finalPid).child(PROJECT_PROGRESS).setValue(13);
                        //update step 1
                        projectRef.child(finalPid).child(PROJECT_TITLE).setValue(titleStr);
                        projectRef.child(finalPid).child(PROJECT_DESC).setValue(descStr);
                        //data berhasil di update ke database
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Toast.makeText(ProjectTrackerActivity.this, titleStr + " " + descStr, Toast.LENGTH_SHORT).show();
        });
        dialogBuilder.setNegativeButton(R.string.fui_cancel, (dialog, whichButton) -> {
            //pass
            dialog.cancel();
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void goToDashboard() {
            Intent i = new Intent(ProjectTrackerActivity.this, DashboardProjectActivity.class);
            startActivity(i);
    }

    private void editStep2() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_fragment_target, null);
        dialogBuilder.setView(dialogView);
        //setter
        final EditText titleTarget = dialogView.findViewById(R.id.dialogEditTextTarget);
        final EditText tahunBefore = dialogView.findViewById(R.id.dialog_target_tahun_before);
        final EditText tahunAfter = dialogView.findViewById(R.id.dialog_target_tahun_after);
        final EditText targetBefore = dialogView.findViewById(R.id.dialog_target_before);
        final EditText targetAfter = dialogView.findViewById(R.id.dialog_target_after);

        //getter
        titleTarget.setText(TargetFragment.inputEditTextJudulTarget.getText().toString());
        tahunBefore.setText(TargetFragment.inputEditTextTahunBefore.getText().toString());
        tahunAfter.setText(TargetFragment.inputEditTextTahunAfter.getText().toString());
        targetBefore.setText(TargetFragment.inputEditTextTargetBefore.getText().toString());
        targetAfter.setText(TargetFragment.inputEditTextTargetAfter.getText().toString());
        dialogBuilder.setTitle(R.string.menentukan_target);
        //dialogBuilder.setMessage("please send me to your feedback.");
        dialogBuilder.setPositiveButton(R.string.ok, (dialog, whichButton) -> {
            String titleTargetStr = titleTarget.getText().toString().trim();
            String tahunTargetBfr = tahunBefore.getText().toString().trim();
            String tahunTargetAft = tahunAfter.getText().toString().trim();
            String targetStrBfr = targetBefore.getText().toString().trim();
            String targetStrAft = targetAfter.getText().toString().trim();
            Log.i(TAG, titleTargetStr+" "+tahunTargetBfr+" "+tahunTargetAft+" "+tahunBefore+" "+tahunAfter);
        });
        dialogBuilder.setNegativeButton(R.string.fui_cancel, (dialog, whichButton) -> {
            //pass
            dialog.cancel();
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void editStep3() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_fragment_anakonda, null);
        dialogBuilder.setView(dialogView);

        final EditText materialWSBH = dialogView.findViewById(R.id.dialog_edittext_material_wsbh);
        final EditText materialWAH = dialogView.findViewById(R.id.dialog_edittext_material_wah);
        final EditText machineWSBH = dialogView.findViewById(R.id.dialog_edittext_machine_wsbh);
        final EditText machineWAH = dialogView.findViewById(R.id.dialog_edittext_machine_wah);
        final EditText methodWSBH = dialogView.findViewById(R.id.dialog_edittext_method_wsbh);
        final EditText methodWAH = dialogView.findViewById(R.id.dialog_edittext_method_wah);
        final EditText manWSBH = dialogView.findViewById(R.id.dialog_edittext_man_wsbh);
        final EditText manWAH = dialogView.findViewById(R.id.dialog_edittext_man_wah);
        final EditText environmentWSBH = dialogView.findViewById(R.id.dialog_edittext_environment_wsbh);
        final EditText environmentWAH = dialogView.findViewById(R.id.dialog_edittext_environment_wah);

        materialWSBH.setText(AnakondaFragment.inputEditTextMaterialWSBH.getText().toString());
        materialWAH.setText(AnakondaFragment.inputEditTextMaterialWAH.getText().toString());
        machineWSBH.setText(AnakondaFragment.inputEditTextMachineWSBH.getText().toString());
        machineWAH.setText(AnakondaFragment.inputEditTextMachineWAH.getText().toString());
        methodWSBH.setText(AnakondaFragment.inputEditTextMethodWSBH.getText().toString());
        methodWAH.setText(AnakondaFragment.inputEditTextMethodWAH.getText().toString());
        manWSBH.setText(AnakondaFragment.inputEditTextManWSBH.getText().toString());
        manWAH.setText(AnakondaFragment.inputEditTextManWAH.getText().toString());
        environmentWSBH.setText(AnakondaFragment.inputEditTextEnvironmentWSBH.getText().toString());
        environmentWAH.setText(AnakondaFragment.inputEditTextEnvironmentWAH.getText().toString());
        
        dialogBuilder.setTitle(R.string.anakonda);
        dialogBuilder.setPositiveButton(R.string.ok, (dialog, whichButton) -> {
            String materialWSBHStr = materialWSBH.getText().toString().trim();
            String materialWAHStr = materialWAH.getText().toString().trim();
            String machineWSBHStr = machineWSBH.getText().toString().trim();
            String machineWAHStr = machineWAH.getText().toString().trim();
            String methodWSBHStr = methodWSBH.getText().toString().trim();
            String methodWAHStr = methodWAH.getText().toString().trim();
            String manWSBHStr = manWSBH.getText().toString().trim();
            String manWAHStr = manWAH.getText().toString().trim();
            String environmentWSBHStr = environmentWSBH.getText().toString().trim();
            String environmentWAHStr = environmentWAH.getText().toString().trim();
            Toast.makeText(ProjectTrackerActivity.this, materialWSBHStr + " " + materialWAHStr, Toast.LENGTH_SHORT).show();
        });
        dialogBuilder.setNegativeButton(R.string.fui_cancel, (dialog, whichButton) -> {
            //pass
            dialog.cancel();
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
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
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
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
        //Closes menu if its opened.
        if (speedDialView.isOpen()) {
            speedDialView.close();
        } else if (viewPager.getCurrentItem()!=0){
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1, false);
        } else {
            finish();
        }
    }

}

