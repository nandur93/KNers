package com.ndu.sanghiang.kners;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lzyzsd.circleprogress.DonutProgress;

public class DashboardProjectActivity extends AppCompatActivity {
    TextView textViewUser;
    TextView textViewUserName;
    TextView textViewDashboard;
    Button buttonSeeDetailLeft, buttonEditProject, buttonNewProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_project);
        textViewUser = findViewById(R.id.textViewUser);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewDashboard = findViewById(R.id.textViewDashboard);
        DonutProgress donutProgress = findViewById(R.id.progressBarLeft);
        buttonSeeDetailLeft = findViewById(R.id.buttonSeeDetailLeft);
        buttonEditProject = findViewById(R.id.buttonEditProject);
        buttonNewProject = findViewById(R.id.buttonNewProject);

        donutProgress.setProgress(75);

        /*Addbutton.setOnClickListener(v -> {

            ListElementsArrayList.add(GetValue.getText().toString());
            adapter.notifyDataSetChanged();
        });*/
    }

}

