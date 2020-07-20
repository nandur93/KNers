package com.ndu.sanghiang.kners.projecttrackerfi.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ndu.sanghiang.kners.R;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_DESC;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_PROGRESS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_STATUS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_TITLE;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        TextView textViewProjectTitleBottomSheet = v.findViewById(R.id.text_view_project_title);
        TextView textViewProjectDescBottomSheet = v.findViewById(R.id.text_view_project_desc);
        TextView textViewStepsBottomSheet = v.findViewById(R.id.text_view_steps);
        DonutProgress bottomSheetDonutProgress = v.findViewById(R.id.bottom_sheet_progress);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sheetPid = sharedPrefs.getString(PID, "");
        String sheetTitle = sharedPrefs.getString(PROJECT_TITLE, "");
        String sheetDesc = sharedPrefs.getString(PROJECT_DESC, "");
        float sheetProgress = sharedPrefs.getFloat(PROJECT_PROGRESS, 0);
        String sheetStatus = sharedPrefs.getString(PROJECT_STATUS, "");

        textViewProjectTitleBottomSheet.setText(sheetTitle);
        textViewProjectDescBottomSheet.setText(sheetDesc);
        bottomSheetDonutProgress.setProgress(sheetProgress);
        textViewStepsBottomSheet.setText(sheetStatus);
        return v;
    }
}
