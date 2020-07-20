package com.ndu.sanghiang.kners;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.leinardi.android.speeddial.SpeedDialView;
import com.ndu.sanghiang.kners.customlistview.adapter.ImageListAdapter;
import com.ndu.sanghiang.kners.customlistview.model.Image;
import com.ndu.sanghiang.kners.projecttrackerfi.ProjectTrackerActivity;
import com.ndu.sanghiang.kners.projecttrackerfi.fragment.BottomSheetFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.*;

public class DashboardProjectActivity extends AppCompatActivity {
    TextView textViewUser;
    TextView textViewUserName;
    TextView textViewTopProjectName;
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
    private static SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_project);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        textViewUser = findViewById(R.id.textViewUser);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewTopProjectName = findViewById(R.id.textViewTopProjectName);
        DonutProgress donutProgress = findViewById(R.id.progressBarLeft);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();
        projectTitleBuilder = "";
        TAG = "Nandur93";


        //Handler handler = new Handler();

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
        projectRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(PROJECTS);

        listViewProject.setAdapter(adapter);
        registerForContextMenu(listViewProject);

        //Import Toolbar
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Donut progress
        // ketika di load, dapatkan value dari shared pref langsung
        String shName = sharedPrefs.getString(USER_NAME, "");
        textViewUserName.setText(shName);
        listViewProject.setOnItemClickListener((adapter, v, position, arg3) -> {
            clearSharedPref();
            final Image image = imageList.get(position);
            String pidKey = image.getPid(); //-Lk5a4w77teyuasdasd
            String sheetStep = image.getStatus();
            String judulThema = image.getTitle();
            String projectProgress = image.getProgress();
            String deskripsiThema = image.getDesc();

            editor.putString(PROJECT_TITLE, judulThema);
            editor.putString(PROJECT_DESC, deskripsiThema);
            editor.putFloat(PROJECT_PROGRESS, Float.parseFloat(projectProgress));
            editor.putString(PROJECT_STATUS, sheetStep);
            editor.putString(PID, pidKey);
            editor.apply();
            showBottomSheetDialogFragment();
            /*Snackbar.make(v, "Click On " + image.getPid(), Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();*/

        });

        SpeedDialView speedDialView = findViewById(R.id.speedDialFab);
        speedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                // Call your main action here
                clearSharedPref();
                showProjectTitleBuilder();
                return false; // true to keep the Speed Dial open
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
                Log.d(TAG, "Speed dial toggle state changed. Open = " + isOpen);
            }
        });

        projectRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String title, status, created, progress, pid, desc;
                try {
                    title = dataSnapshot.child(PROJECT_TITLE).getValue().toString();
                    status = (String) dataSnapshot.child(PROJECT_STATUS).getValue();
                    created = (String) dataSnapshot.child(PROJECT_CREATED).getValue();
                    progress = String.valueOf(dataSnapshot.child(PROJECT_PROGRESS).getValue());
                    float progressFloat = Float.valueOf(progress);
                    pid = (String) dataSnapshot.child(PID).getValue();
                    desc = (String) dataSnapshot.child(PROJECT_DESC).getValue();
                    imageList.add(new Image(R.drawable.ic_launcher_round, progress, title, status, created, progressFloat, pid, desc)); //menambahkan firebase ke listview
                    adapter.notifyDataSetChanged();
                    Query query = projectRef.orderByChild(PROJECT_PROGRESS).limitToLast(1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String proTit = (String) child.child(PROJECT_TITLE).getValue();
                                String proPro = String.valueOf(child.child(PROJECT_PROGRESS).getValue());
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

    public static void clearSharedPref() {
        sharedPrefs.edit().remove(PROJECT_TITLE).apply();
        sharedPrefs.edit().remove(PROJECT_DESC).apply();
        sharedPrefs.edit().remove(PROJECT_PROGRESS).apply();
        sharedPrefs.edit().remove(PROJECT_STATUS).apply();
        sharedPrefs.edit().remove(PID).apply();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose Action");   // Context-menu title
        menu.add(0, v.getId(), 0, "Edit Project");  // Add element "Edit"
        menu.add(0, v.getId(), 1, "Delete Project"); // Add element "Delete"
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
                        DataSnapshot targetRef = dataSnapshot.child(PROJECT_TARGET);
                        String judulTarget = targetRef.child(TARGET_JUDUL).getValue().toString();
                        String tahunBefore = targetRef.child(TARGET_TAHUN_BEFORE).getValue().toString();
                        String tahunAfter = targetRef.child(TARGET_TAHUN_AFTER).getValue().toString();
                        String targetBefore =targetRef.child(TARGET_TOTAL_BEFORE).getValue().toString();
                        String targetAfter = targetRef.child(TARGET_TOTAL_AFTER).getValue().toString();

                        //set item to intent share
                        Intent intent  = new Intent(DashboardProjectActivity.this, ProjectTrackerActivity.class);
                        //fragment thema
                        switch (projectProgress) {
                            case "13":
                                intent.putExtra(PROJECT_PAGER, 0);
                                break;
                            case "25":
                                intent.putExtra(PROJECT_PAGER, 1);
                                break;
                            case "38":
                                intent.putExtra(PROJECT_PAGER, 2);
                                break;
                            case "50":
                                intent.putExtra(PROJECT_PAGER, 3);
                                break;
                            case "63":
                                intent.putExtra(PROJECT_PAGER, 4);
                                break;
                            case "75":
                                intent.putExtra(PROJECT_PAGER, 5);
                                break;
                            case "88":
                                intent.putExtra(PROJECT_PAGER, 6);
                                break;
                            case "100":
                                intent.putExtra(PROJECT_PAGER, 7);
                                break;
                        }
                        intent.putExtra(PROJECT_TITLE, judulThema);
                        intent.putExtra(PROJECT_DESC, deskripsiThema);
                        intent.putExtra(PROJECT_PROGRESS, projectProgress);
                        intent.putExtra(PID, pid);

                        //fragment target
                        intent.putExtra(TARGET_JUDUL, judulTarget);
                        intent.putExtra(TARGET_TAHUN_BEFORE, tahunBefore);
                        intent.putExtra(TARGET_TAHUN_AFTER, tahunAfter);
                        intent.putExtra(TARGET_TOTAL_BEFORE, targetBefore);
                        intent.putExtra(TARGET_TOTAL_AFTER, targetAfter);
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
                        Query queryRef = projectRef.orderByChild(PROJECT_TITLE).equalTo(image.getTitle());
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
                    .setMessage("Data project "+image.getTitle()+" akan terhapus!")
                    .setTitle("Delete Project?")
                    .setPositiveButton(R.string.ok, dialogClickListener)
                    .setNegativeButton(R.string.fui_cancel, dialogClickListener)
                    .show();
        } else
        {
            return false;
        }
        return false;
    }

    public void showBottomSheetDialogFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
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
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
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
        projectRef.child(pidKey).child(PROJECT_DESC).setValue("");
        //step 2 menentukan target
        DatabaseReference childTarget = projectRef.child(pidKey).child(PROJECT_TARGET);
        childTarget.child(PROJECT_CREATED).setValue(date);
        childTarget.child(TARGET_JUDUL).setValue("");
        childTarget.child(TARGET_TAHUN_BEFORE).setValue("");
        childTarget.child(TARGET_TAHUN_AFTER).setValue("");
        childTarget.child(TARGET_TOTAL_BEFORE).setValue("0");
        childTarget.child(TARGET_TOTAL_AFTER).setValue("0");
        //step 3 anakonda
        DatabaseReference childAnakonda = projectRef.child(pidKey).child(PROJECT_ANAKONDA);
        childAnakonda.child(ANAKONDA_MATERIAL).child(MATERIAL_WSBH).setValue("");
        childAnakonda.child(ANAKONDA_MATERIAL).child(MATERIAL_WAH).setValue("");
        childAnakonda.child(ANAKONDA_MATERIAL).child(MATERIAL_STATUS).setValue("");
        childAnakonda.child(ANAKONDA_MACHINE).child(MACHINE_WSBH).setValue("");
        childAnakonda.child(ANAKONDA_MACHINE).child(MACHINE_WAH).setValue("");
        childAnakonda.child(ANAKONDA_MACHINE).child(MACHINE_STATUS).setValue("");
        childAnakonda.child(ANAKONDA_METHOD).child(METHOD_WSBH).setValue("");
        childAnakonda.child(ANAKONDA_METHOD).child(METHOD_WAH).setValue("");
        childAnakonda.child(ANAKONDA_METHOD).child(METHOD_STATUS).setValue("");
        childAnakonda.child(ANAKONDA_MAN).child(MAN_WSBH).setValue("");
        childAnakonda.child(ANAKONDA_MAN).child(MAN_WAH).setValue("");
        childAnakonda.child(ANAKONDA_MAN).child(MAN_STATUS).setValue("");
        childAnakonda.child(ANAKONDA_ENVIRONMENT).child(ENVIRONMENT_WSBH).setValue("");
        childAnakonda.child(ANAKONDA_ENVIRONMENT).child(ENVIRONMENT_WAH).setValue("");
        childAnakonda.child(ANAKONDA_ENVIRONMENT).child(ENVIRONMENT_STATUS).setValue("");
        //STEP 4 ANASEBA
        DatabaseReference childAnaseba = projectRef.child(pidKey).child(PROJECT_ANASEBA);
        childAnaseba.child(ANASEBA_MATERIAL).child(MATERIAL_WHY1).setValue("");
        childAnaseba.child(ANASEBA_MATERIAL).child(MATERIAL_WHY2).setValue("");
        childAnaseba.child(ANASEBA_MATERIAL).child(MATERIAL_WHY3).setValue("");
        childAnaseba.child(ANASEBA_MATERIAL).child(MATERIAL_WHY4).setValue("");
        childAnaseba.child(ANASEBA_MATERIAL).child(MATERIAL_WHY5).setValue("");
        childAnaseba.child(ANASEBA_MACHINE).child(MACHINE_WHY1).setValue("");
        childAnaseba.child(ANASEBA_MACHINE).child(MACHINE_WHY2).setValue("");
        childAnaseba.child(ANASEBA_MACHINE).child(MACHINE_WHY3).setValue("");
        childAnaseba.child(ANASEBA_MACHINE).child(MACHINE_WHY4).setValue("");
        childAnaseba.child(ANASEBA_MACHINE).child(MACHINE_WHY5).setValue("");
        childAnaseba.child(ANASEBA_METHOD).child(METHOD_WHY1).setValue("");
        childAnaseba.child(ANASEBA_METHOD).child(METHOD_WHY2).setValue("");
        childAnaseba.child(ANASEBA_METHOD).child(METHOD_WHY3).setValue("");
        childAnaseba.child(ANASEBA_METHOD).child(METHOD_WHY4).setValue("");
        childAnaseba.child(ANASEBA_METHOD).child(METHOD_WHY5).setValue("");
        childAnaseba.child(ANASEBA_MAN).child(MAN_WHY1).setValue("");
        childAnaseba.child(ANASEBA_MAN).child(MAN_WHY2).setValue("");
        childAnaseba.child(ANASEBA_MAN).child(MAN_WHY3).setValue("");
        childAnaseba.child(ANASEBA_MAN).child(MAN_WHY4).setValue("");
        childAnaseba.child(ANASEBA_MAN).child(MAN_WHY5).setValue("");
        childAnaseba.child(ANASEBA_ENVIRONMENT).child(ENVIRONMENT_WHY1).setValue("");
        childAnaseba.child(ANASEBA_ENVIRONMENT).child(ENVIRONMENT_WHY2).setValue("");
        childAnaseba.child(ANASEBA_ENVIRONMENT).child(ENVIRONMENT_WHY3).setValue("");
        childAnaseba.child(ANASEBA_ENVIRONMENT).child(ENVIRONMENT_WHY4).setValue("");
        childAnaseba.child(ANASEBA_ENVIRONMENT).child(ENVIRONMENT_WHY5).setValue("");
        //step 5 rencana penanggulangan
        DatabaseReference childRencana = projectRef.child(pidKey).child(PROJECT_RENCANA);
        childRencana.child(RENCANA_AKAR_PENYEBAB1).child(PENYEBAB1_TITLE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB1).child(PENYEBAB1_WHAT).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB1).child(PENYEBAB1_WHEN).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB1).child(PENYEBAB1_WHY).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB1).child(PENYEBAB1_WHERE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB1).child(PENYEBAB1_WHO).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB1).child(PENYEBAB1_HOW).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB2).child(PENYEBAB2_TITLE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB2).child(PENYEBAB2_WHAT).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB2).child(PENYEBAB2_WHEN).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB2).child(PENYEBAB2_WHY).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB2).child(PENYEBAB2_WHERE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB2).child(PENYEBAB2_WHO).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB2).child(PENYEBAB2_HOW).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB3).child(PENYEBAB3_TITLE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB3).child(PENYEBAB3_WHAT).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB3).child(PENYEBAB3_WHEN).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB3).child(PENYEBAB3_WHY).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB3).child(PENYEBAB3_WHERE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB3).child(PENYEBAB3_WHO).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB3).child(PENYEBAB3_HOW).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB4).child(PENYEBAB4_TITLE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB4).child(PENYEBAB4_WHAT).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB4).child(PENYEBAB4_WHEN).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB4).child(PENYEBAB4_WHY).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB4).child(PENYEBAB4_WHERE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB4).child(PENYEBAB4_WHO).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB4).child(PENYEBAB4_HOW).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB5).child(PENYEBAB5_TITLE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB5).child(PENYEBAB5_WHAT).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB5).child(PENYEBAB5_WHEN).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB5).child(PENYEBAB5_WHY).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB5).child(PENYEBAB5_WHERE).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB5).child(PENYEBAB5_WHO).setValue("");
        childRencana.child(RENCANA_AKAR_PENYEBAB5).child(PENYEBAB5_HOW).setValue("");
        //STEP 6 penanggulangan
        DatabaseReference childPenanggulangan = projectRef.child(pidKey).child(PROJECT_PENANGGULANGAN);
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM1).child(PENANGGULANGAN_PROBLEM_TITLE1).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM1).child(PENANGGULANGAN_DESC1).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM1).child(PENANGGULANGAN_DUE1).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM1).child(PENANGGULANGAN_PIC1).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM1).child(PENANGGULANGAN_PROGRESS1).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM2).child(PENANGGULANGAN_PROBLEM_TITLE2).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM2).child(PENANGGULANGAN_DESC2).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM2).child(PENANGGULANGAN_DUE2).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM2).child(PENANGGULANGAN_PIC2).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM2).child(PENANGGULANGAN_PROGRESS2).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM3).child(PENANGGULANGAN_PROBLEM_TITLE3).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM3).child(PENANGGULANGAN_DESC3).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM3).child(PENANGGULANGAN_DUE3).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM3).child(PENANGGULANGAN_PIC3).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM3).child(PENANGGULANGAN_PROGRESS3).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM4).child(PENANGGULANGAN_PROBLEM_TITLE4).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM4).child(PENANGGULANGAN_DESC4).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM4).child(PENANGGULANGAN_DUE4).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM4).child(PENANGGULANGAN_PIC4).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM4).child(PENANGGULANGAN_PROGRESS4).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM5).child(PENANGGULANGAN_PROBLEM_TITLE5).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM5).child(PENANGGULANGAN_DESC5).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM5).child(PENANGGULANGAN_DUE5).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM5).child(PENANGGULANGAN_PIC5).setValue("");
        childPenanggulangan.child(PENANGGULANGAN_PROBLEM5).child(PENANGGULANGAN_PROGRESS5).setValue("");
        //step 7 evaluasi hasil
        DatabaseReference childEvaluasi = projectRef.child(pidKey).child(PROJECT_EVALUASI_HASIL);
        childEvaluasi.child(HASIL_QUALITY).child(HASIL_QUALITY_BEFORE).setValue("");
        childEvaluasi.child(HASIL_QUALITY).child(HASIL_QUALITY_AFTER).setValue("");
        childEvaluasi.child(HASIL_COST).child(HASIL_COST_BEFORE).setValue("");
        childEvaluasi.child(HASIL_COST).child(HASIL_COST_AFTER).setValue("");
        childEvaluasi.child(HASIL_DELIVERY).child(HASIL_DELIVERY_BEFORE).setValue("");
        childEvaluasi.child(HASIL_DELIVERY).child(HASIL_DELIVERY_AFTER).setValue("");
        childEvaluasi.child(HASIL_SAFETY).child(HASIL_SAFETY_BEFORE).setValue("");
        childEvaluasi.child(HASIL_SAFETY).child(HASIL_SAFETY_AFTER).setValue("");
        childEvaluasi.child(HASIL_MORAL).child(HASIL_MORAL_BEFORE).setValue("");
        childEvaluasi.child(HASIL_MORAL).child(HASIL_MORAL_AFTER).setValue("");
        childEvaluasi.child(HASIL_PRODUCTIVITY).child(HASIL_PRODUCTIVITY_BEFORE).setValue("");
        childEvaluasi.child(HASIL_PRODUCTIVITY).child(HASIL_PRODUCTIVITY_AFTER).setValue("");
        childEvaluasi.child(HASIL_ENVIRONMENT).child(HASIL_ENVIRONMENT_BEFORE).setValue("");
        childEvaluasi.child(HASIL_ENVIRONMENT).child(HASIL_ENVIRONMENT_AFTER).setValue("");
        //step 8 standarisasi
        DatabaseReference childStandarisasi = projectRef.child(pidKey).child(PROJECT_STANDARISASI);
        childStandarisasi.child(STANDARISASI_STATUS).setValue("");
        childStandarisasi.child(STANDARISASI_TEMA_BERIKUT).setValue("");



        
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

                    //set item to intent share yang akan ditaro ke sharedPref
                    Intent intent  = new Intent(DashboardProjectActivity.this, ProjectTrackerActivity.class);
                    intent.putExtra(PROJECT_STATUS, metaStatus);
                    intent.putExtra(PROJECT_CREATED, metaCreated);
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
                String title = dataSnapshot.child(PROJECT_TITLE).getValue().toString();
                String status = (String) dataSnapshot.child(PROJECT_STATUS).getValue();
                String created = (String) dataSnapshot.child(PROJECT_CREATED).getValue();
                String progress = (String) dataSnapshot.child("project_progress").getValue();
                imageList.add(new Image(R.drawable.ic_launcher_round, title, status, created, progress)); //menambahkan firebase ke listview
                //projectRef.orderByChild("project_progress").orderByValue().limitToLast(100);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                recreate();
                adapter.notifyDataSetChanged();
                Log.i(TAG, "onChildChanged:" + dataSnapshot.child(PROJECT_TITLE).getValue().toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot ) {
                recreate();
                adapter.notifyDataSetChanged();
                Log.i(TAG, "onChildRemoved:" + dataSnapshot.child(PROJECT_TITLE).getValue().toString());
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
                result.put(PROJECT_STATUS, "In Progress");
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