package com.ndu.sanghiang.kners.projecttrackerfi.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ndu.sanghiang.kners.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECTS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_TARGET;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TARGET_JUDUL;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TARGET_TAHUN_AFTER;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TARGET_TAHUN_BEFORE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TARGET_TOTAL_AFTER;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TARGET_TOTAL_BEFORE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.USERS;

public class TargetFragment extends Fragment {

    public static TextInputEditText inputEditTextJudulTarget;
    public static TextInputEditText inputEditTextTahunBefore, inputEditTextTahunAfter, inputEditTextTargetBefore, inputEditTextTargetAfter;
    private DatabaseReference projectRefproject_title;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_target, container, false);
        // Firebase
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = Objects.requireNonNull(mCurrentUser).getUid();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String pid = sharedPrefs.getString(PID, "");
        projectRefproject_title = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(PROJECTS).child(pid).child(PROJECT_TARGET);
        inputEditTextJudulTarget = view.findViewById(R.id.editTextTarget);
        inputEditTextTahunBefore = view.findViewById(R.id.target_tahun_before);
        inputEditTextTahunAfter = view.findViewById(R.id.target_tahun_after);
        inputEditTextTargetBefore = view.findViewById(R.id.target_before);
        inputEditTextTargetAfter = view.findViewById(R.id.target_after);
        BarChart barChartTarget = view.findViewById(R.id.barchart_target);
        // Load data dari sharedpref
        String finalJudulTarget = sharedPrefs.getString(TARGET_JUDUL, "Membuat Project Android");
        String finalTahunBefore = sharedPrefs.getString(TARGET_TAHUN_BEFORE, "2019");
        String finalTahunAfter = sharedPrefs.getString(TARGET_TAHUN_AFTER, "2020");
        String finalTargetBefore = sharedPrefs.getString(TARGET_TOTAL_BEFORE, "20");
        String finalTargetAfter = sharedPrefs.getString(TARGET_TOTAL_AFTER, "10");
        // Load all data to editText
        if (finalJudulTarget != null) {
            inputEditTextJudulTarget.setText(finalJudulTarget);
        } else {
            inputEditTextJudulTarget.setText("");
        }
        if (finalTahunBefore != null) {
            inputEditTextTahunBefore.setText(finalTahunBefore);
        } else {
            inputEditTextTahunBefore.setText("");
        }
        if (finalTahunAfter != null) {
            inputEditTextTahunAfter.setText(finalTahunAfter);
        } else {
            inputEditTextTahunAfter.setText("");
        }
        if (finalTargetBefore != null) {
            inputEditTextTargetBefore.setText(finalTargetBefore);
        } else {
            inputEditTextTargetBefore.setText("0");
        }
        if (finalTargetAfter != null) {
            inputEditTextTargetAfter.setText(finalTargetAfter);
        } else {
            inputEditTextTargetAfter.setText("0");
        }
        //barChart
        barChartTarget.setDrawBarShadow(false);
        barChartTarget.setDrawValueAboveBar(true);
        barChartTarget.setMaxVisibleValueCount(50);
        barChartTarget.setPinchZoom(false);
        barChartTarget.setDrawGridBackground(true);
        barChartTarget.getDescription().setText("MPChart Target");
        //barChartTarget.setFitBars(true);
        String before = inputEditTextTargetBefore.getText().toString();
        String after = inputEditTextTargetAfter.getText().toString();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, Float.parseFloat(before+"f")));
        barEntries.add(new BarEntry(1f, Float.parseFloat(after+"f")));

        BarDataSet barDataSet = new BarDataSet(barEntries, finalJudulTarget);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add(finalTahunBefore);
        xVals.add(finalTahunAfter);
        //X-axis
        XAxis xAxis = barChartTarget.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        //xAxis.setAxisMaximum(2);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        barChartTarget.setData(data);
        return view;
    }

    private void goToNextStep() {
        TabLayout tabhost = getActivity().findViewById(R.id.tabs);
        tabhost.getTabAt(2).select();
    }

    private void addItemToFirebase() {
        //String key = projectRefproject_title.push().getKey();
        String inputTargetTitle = inputEditTextJudulTarget.getText().toString().trim();
        String inputTahunBefore = inputEditTextTahunBefore.getText().toString();
        String inputTahunAfter = inputEditTextTahunAfter.getText().toString();
        String inputTargetBefore = inputEditTextTargetBefore.getText().toString();
        String inputTargetAfter = inputEditTextTargetAfter.getText().toString();

        projectRefproject_title.child(TARGET_JUDUL).setValue(inputTargetTitle);
        projectRefproject_title.child(TARGET_TAHUN_BEFORE).setValue(inputTahunBefore);
        projectRefproject_title.child(TARGET_TAHUN_AFTER).setValue(inputTahunAfter);
        projectRefproject_title.child(TARGET_TOTAL_BEFORE).setValue(inputTargetBefore);
        projectRefproject_title.child(TARGET_TOTAL_AFTER).setValue(inputTargetAfter);
    }
}