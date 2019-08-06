package com.ndu.sanghiang.kners;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.material.snackbar.Snackbar;
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
    Button buttonEditProject, buttonNewProject;
    private String projectTitleBuilder;
    //private ArrayAdapter<String> adapter;

    private DatabaseReference projectRef;
    private String TAG;

    // ListView Object
    // ListView listView;
    ListView listViewProject;
    // Image ArrayList
    List<Image> imageList;
    private ImageListAdapter adapter;
    private SharedPreferences sharedPrefs;
    public static String PID, PROJECT_STATUS, PROJECT_CREATED, PROJECT_PROGRESS, PROJECT_TITLE, PROJECT_TARGET, PROJECT_DESC;
    public static String PROJECT_PAGER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_project);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        textViewUser = findViewById(R.id.textViewUser);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewTopProjectName = findViewById(R.id.textViewTopProjectName);
        DonutProgress donutProgress = findViewById(R.id.progressBarLeft);
        buttonEditProject = findViewById(R.id.buttonEditProject);
        buttonNewProject = findViewById(R.id.buttonNewProject);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        projectTitleBuilder = "";
        TAG = "Nandur93";
        PID = "pid";
        PROJECT_STATUS = "project_status";
        PROJECT_CREATED = "project_created";
        PROJECT_PROGRESS = "project_progress";
        PROJECT_TITLE = "project_title";
        PROJECT_TARGET = "project_target";
        PROJECT_DESC = "project_desc";
        PROJECT_PAGER = "viewpager_position";
        Handler handler = new Handler();

        // 1. Initializing ListView And Image ArrayList
        imageList = new ArrayList<>();

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
        registerForContextMenu(listViewProject);

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
            clearSharedPref();
            showProjectTitleBuilder();
        });
        buttonEditProject.setOnClickListener(v -> Toast.makeText(this,"Edit project activity",Toast.LENGTH_SHORT).show());
        listViewProject.setOnItemClickListener((adapter, v, position, arg3) -> {
            final Image image = imageList.get(position);
            Snackbar.make(v, "Click On " + image.getPid(), Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        });
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
                        @SuppressLint("SetTextI18n")
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

    private void clearSharedPref() {
        sharedPrefs.edit().remove(PROJECT_TITLE).apply();
        sharedPrefs.edit().remove(PROJECT_DESC).apply();
        sharedPrefs.edit().remove(PID).apply();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose Action");   // Context-menu title
        menu.add(0, v.getId(), 0, "Edit Project");  // Add element "Edit"
        menu.add(0, v.getId(), 1, "Delete Project");        // Add element "Delete"
        menu.add(0, v.getId(), 2, "Detail Project");
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        final Image image = imageList.get(index);
        if (item.getOrder() == 0) // "Edit" chosen
        {
            Toast.makeText(DashboardProjectActivity.this, "Edit Project", Toast.LENGTH_SHORT).show();
            // Do stuff
            //TODO edit stuff
            //buka fi project tracker dengan 8 step lalu load progress
            String pidKey = image.getPid(); //-Lk5a4w77teyuasdasd
            projectRef.child(pidKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        //get item from firebase
                        //meta
                        String projectProgress = dataSnapshot.child(PROJECT_PROGRESS).getValue().toString();
                        //Fragment thema
                        String judulThema = dataSnapshot.child(PROJECT_TITLE).getValue().toString();
                        String deskripsiThema = dataSnapshot.child(PROJECT_DESC).getValue().toString();
                        String pid = dataSnapshot.child(PID).getValue().toString();

                        //Fragment target
                        String judulTarget = dataSnapshot.child(PROJECT_TARGET).child("judul_target").getValue().toString();
                        String tahunBefore = dataSnapshot.child(PROJECT_TARGET).child("tahun_before").getValue().toString();
                        String tahunAfter = dataSnapshot.child(PROJECT_TARGET).child("tahun_after").getValue().toString();
                        String targetBefore = dataSnapshot.child(PROJECT_TARGET).child("target_before").getValue().toString();
                        String targetAfter = dataSnapshot.child(PROJECT_TARGET).child("target_after").getValue().toString();

                        //set item to intent share
                        Intent intent  = new Intent(DashboardProjectActivity.this, ProjectTrackerActivity.class);
                        //fragment thema
                        intent.putExtra(PROJECT_PAGER, 0);
                        intent.putExtra(PROJECT_TITLE, judulThema);
                        intent.putExtra(PROJECT_DESC, deskripsiThema);
                        intent.putExtra(PROJECT_PROGRESS, projectProgress);
                        intent.putExtra(PID, pid);

                        //fragment target
                        intent.putExtra("judul_target", judulTarget);
                        intent.putExtra("tahun_before", tahunBefore);
                        intent.putExtra("tahun_after", tahunAfter);
                        intent.putExtra("judul_target", targetBefore);
                        intent.putExtra("target_after", targetAfter);
                        startActivity(intent);

                        Log.i(TAG, judulThema+" "+deskripsiThema+" From dashboard with tema and target");


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //jika progress 13 maka -> load step 1
            //judul tema, deskripsi tema
            //jika progress 25 maka -> load step 2

        } else if (item.getOrder() == 1)  // "Delete" chosen
        {
            // Show dialog
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        // Delete if yes
                        Toast.makeText(DashboardProjectActivity.this, "Delete Project", Toast.LENGTH_SHORT).show();
                        Query queryRef = projectRef.orderByChild("project_title").equalTo(image.getName());
                        queryRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChild) {
                                dataSnapshot.getRef().setValue(null);
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardProjectActivity.this);
            builder
                    .setMessage("Data project "+image.getName()+" akan terhapus!")
                    .setTitle("Delete Project?")
                    .setPositiveButton(R.string.ok, dialogClickListener)
                    .setNegativeButton(R.string.fui_cancel, dialogClickListener)
                    .show();
        } else if (item.getOrder() == 2)  // "Detail" chosen
        {
            //Pass data to fragment
            if (image.getPid() != null) {
                //String title = projectRef.child("pid").getKey(); //pid
                //String pid = projectRef.child("project_title").getKey(); //project_title
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        } else
        {
            return false;
        }
        return false;
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
        builder.setTitle(R.string.project_title);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            projectTitleBuilder = input.getText().toString();
            createNewProject();
            //addItemToFirebase();
        });
        builder.setNegativeButton(R.string.fui_cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void createNewProject() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        //membuat data template node
        String pidKey = projectRef.push().getKey();
        //metadata
        projectRef.child(pidKey).child(PID).setValue(pidKey);
        projectRef.child(pidKey).child(PROJECT_STATUS).setValue("Open");
        projectRef.child(pidKey).child(PROJECT_CREATED).setValue(date);
        projectRef.child(pidKey).child(PROJECT_PROGRESS).setValue(0);
        //step 1 menentukan tema
        projectRef.child(pidKey).child(PROJECT_TITLE).setValue(projectTitleBuilder);
        projectRef.child(pidKey).child("project_desc").setValue("");
        //step 2 menentukan target
        DatabaseReference childTarget = projectRef.child(pidKey).child(PROJECT_TARGET);
        childTarget.child(PROJECT_CREATED).setValue(date);
        childTarget.child("judul_target").setValue("");
        childTarget.child("tahun_before").setValue("");
        childTarget.child("tahun_after").setValue("");
        childTarget.child("target_before").setValue("0");
        childTarget.child("target_after").setValue("0");
        projectRef.child(pidKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //get item from firebase
                    //get metadata
                    String pid = dataSnapshot.child(PID).getValue().toString();
                    String metaStatus = dataSnapshot.child(PROJECT_STATUS).getValue().toString();
                    String metaCreated = dataSnapshot.child(PROJECT_CREATED).getValue().toString();
                    String metaProgress = dataSnapshot.child(PROJECT_PROGRESS).getValue().toString();

                    //Fragment thema step 1
                    String judulThema = dataSnapshot.child(PROJECT_TITLE).getValue().toString();

                    //set item to intent share
                    Intent intent  = new Intent(DashboardProjectActivity.this, ProjectTrackerActivity.class);
                    intent.putExtra(PID, pid);
                    intent.putExtra(PROJECT_TITLE, judulThema);
                    intent.putExtra(PROJECT_PROGRESS, metaProgress);
                    startActivity(intent);

                    Log.i(TAG, judulThema+" from Dashboard if exist methode");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


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