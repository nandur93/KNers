package com.ndu.sanghiang.kners;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ndu.sanghiang.kners.customlistview.adapter.ImageListAdapter;
import com.ndu.sanghiang.kners.customlistview.model.Image;
import com.ndu.sanghiang.kners.projecttrackerfi.ProjectTrackerActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DashboardProjectActivity extends AppCompatActivity {
    TextView textViewUser;
    TextView textViewUserName;
    TextView textViewTopProjectName;
    Button buttonSeeDetailLeft, buttonEditProject, buttonNewProject;
    private String m_Text;
    //private ArrayAdapter<String> adapter;

    private DatabaseReference projectRef;
    private String TAG;

    // ListView Object
    // ListView listView;
    ListView listViewProject;
    // Image ArrayList
    List<Image> imageList;
    private ImageListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_project);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        textViewUser = findViewById(R.id.textViewUser);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewTopProjectName = findViewById(R.id.textViewTopProjectName);
        DonutProgress donutProgress = findViewById(R.id.progressBarLeft);
        buttonSeeDetailLeft = findViewById(R.id.buttonSeeDetailLeft);
        buttonEditProject = findViewById(R.id.buttonEditProject);
        buttonNewProject = findViewById(R.id.buttonNewProject);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        m_Text = "";
        TAG = "Nandur93";

        Handler handler = new Handler();

        // 1. Initializing ListView And Image ArrayList
        imageList = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();

        listViewProject = findViewById(R.id.listViewProject);
        // 2. Prepare Image ArrayList [Add Some Static Data Into Array]

        //imageList.add(new Image(R.drawable.bg_1_img, "Cloud"));
        // 3. Create ImageListAdapter Object
        //ImageListAdapter adapter = new ImageListAdapter(this, R.layout.list_row, imageList);
        // adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList); //backup
        adapter = new ImageListAdapter(this, R.layout.row_list_item, imageList); //backup

        // 4. Set Adapter Into ListView
        //listView.setAdapter(adapter);

        // Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // get current userId
        String userId = mAuth.getCurrentUser().getUid();
        projectRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("projects");

        listViewProject.setAdapter(adapter);

        //Import Toolbar
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Donut progress
        // ketika di load, dapatkan value dari shared pref langsung
        String shName = sharedPrefs.getString("user_name", "");
        textViewUserName.setText(shName);
        buttonNewProject.setOnClickListener(v -> /*showProjectTitleBuilder()*/{
            goToProjectTracker();
        });
        buttonEditProject.setOnClickListener(v -> Toast.makeText(this,"Edit project activity",Toast.LENGTH_SHORT).show());
        buttonSeeDetailLeft.setOnClickListener(v -> {
            Toast.makeText(this, "Make details activity", Toast.LENGTH_SHORT).show();
        });

        /*projectRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                String title = dataSnapshot.child("project_title").getValue().toString();
                String status = (String) dataSnapshot.child("project_status").getValue();
                String created = (String) dataSnapshot.child("project_created").getValue();
                String progress = (String) dataSnapshot.child("project_progress").getValue();
                imageList.add(new Image(R.drawable.ic_launcher_round, title, status, created, progress)); //menambahkan firebase ke listview
                //projectRef.orderByChild("project_progress").orderByValue().limitToLast(100);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                recreate();
                adapter.notifyDataSetChanged();
                Log.i(TAG, "onChildChanged:" + dataSnapshot.child("project_title").getValue().toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot ) {
                recreate();
                adapter.notifyDataSetChanged();
                Log.i(TAG, "onChildRemoved:" + dataSnapshot.child("project_title").getValue().toString());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        // to update key
 /*       Query query = projectRef.orderByChild("project_progress").endAt(100);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                HashMap<String, Object> result = new HashMap<>();
                result.put("project_status", "In Progress");
                projectRef.child(key).updateChildren(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        /*
        projectRef.child("project_progress").orderByValue();
        textViewTopProjectName.setText(title); //harus diisi most top project
        float number = Float.valueOf(progress);
        donutProgress.setProgress(number); //harus diisi most hihger value */
        //Query query = projectRef.orderByChild("project_progress").startAt(100);

        projectRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String title, status, created, progress, pid;
                try {
                    title = dataSnapshot.child("project_title").getValue().toString();
                    status = (String) dataSnapshot.child("project_status").getValue();
                    created = (String) dataSnapshot.child("project_created").getValue();
                    progress = String.valueOf(dataSnapshot.child("project_progress").getValue());
                    float progressFloat = Float.valueOf(progress);
                    pid = (String) dataSnapshot.child("pid").getValue();
                    imageList.add(new Image(R.drawable.ic_launcher_round, progress, title, status, created, progressFloat, pid)); //menambahkan firebase ke listview
                    adapter.notifyDataSetChanged();
                    Query query = projectRef.orderByChild("project_progress").limitToLast(1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String proTit = (String) child.child("project_title").getValue();
                                String proPro = String.valueOf(child.child("project_progress").getValue());
                                if (title != null){
                                    Log.i(TAG, proTit);
                                    textViewTopProjectName.setText(proTit);//harus diisi most top project
                                } else {
                                    textViewTopProjectName.setText("Data error"); //harus diisi most top project
                                }
                                if (proPro !=null ) {
                                    float number = Float.valueOf(proPro);
                                    donutProgress.setProgress(number); //harus diisi most hihger value
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    /*query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                            Log.i(TAG, key);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                } catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG,e.getMessage());
                }


                listViewProject.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        listViewProject.removeOnLayoutChangeListener(this);
                        Log.i(TAG, "updated");
                    }
                });
                /*final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    // Do something after 5s = 5000ms
                }, 2000);*/
                if (s != null){
                    try {
                        sortArrayList();
                        Toast.makeText(DashboardProjectActivity.this, "Data loaded successfuly", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }catch(Exception e){
                        e.printStackTrace();
                        Log.e(TAG,e.getMessage());
                    }
                } else {
                    Toast.makeText(DashboardProjectActivity.this, "Load failed, please refresh the page", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                recreate();
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                recreate();
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }



    private void sortArrayList() {
        //Collections.sort(imageList, (o1, o2) -> Integer.parseInt(o2.getPercent()) - Integer.parseInt(o1.getPercent()));
        //adapter.notifyDataSetChanged();
        //Collections.sort(imageList, (o1, o2) -> Integer.parseInt(String.valueOf(o2.getPercent())) - Integer.parseInt(String.valueOf(o1.getPercent())));
        Collections.sort(imageList, (o1, o2) -> Integer.parseInt(o2.getProgress()) - Integer.parseInt(o1.getProgress()));
    }


    public void onStart(){
        super.onStart();
        Log.i(TAG, "Onstart Method");
    }

    private void showProjectTitleBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nama Project Baru");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            m_Text = input.getText().toString();
            goToProjectTracker();
            //addItemToFirebase();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addItemToFirebase() {
        String key = projectRef.push().getKey();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        projectRef.child(key).child("project_title").setValue(m_Text);
        projectRef.child(key).child("project_status").setValue("Open");
        projectRef.child(key).child("project_created").setValue(date);
        projectRef.child(key).child("project_progress").setValue("0");
    }

    private void goToProjectTracker() {
        Intent projectTrackerIntent = new
                Intent(DashboardProjectActivity.this, ProjectTrackerActivity.class);
        startActivity(projectTrackerIntent);
    }

}