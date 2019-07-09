package com.ndu.sanghiang.kners;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
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
import com.ndu.sanghiang.kners.projecttrackerfi.ProjectTrackerActivity;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.String.valueOf;

public class DashboardProjectActivity extends AppCompatActivity {
    TextView textViewUser;
    TextView textViewUserName;
    TextView textViewDashboard, textViewTopProjectName;
    ListView listViewProject;
    Button buttonSeeDetailLeft, buttonEditProject, buttonNewProject;
    private SharedPreferences sharedPrefs;
    private String m_Text;
    private ArrayList<String> arrayList, keys;
    private ArrayAdapter<String> adapter;

    //Firebase
    FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference projectRef;
    private FirebaseAuth mAuth;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_project);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        textViewUser = findViewById(R.id.textViewUser);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewDashboard = findViewById(R.id.textViewDashboard);
        textViewTopProjectName = findViewById(R.id.textViewTopProjectName);
        DonutProgress donutProgress = findViewById(R.id.progressBarLeft);
        buttonSeeDetailLeft = findViewById(R.id.buttonSeeDetailLeft);
        buttonEditProject = findViewById(R.id.buttonEditProject);
        buttonNewProject = findViewById(R.id.buttonNewProject);
        listViewProject = findViewById(R.id.listViewProject);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        m_Text = "";
        TAG = "Nandur93";
        // Firebase
        mAuth = FirebaseAuth.getInstance();
        // get current userId
        String userId = mAuth.getCurrentUser().getUid();
        projectRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("projects");

        arrayList = new ArrayList<>();
        keys = new ArrayList<>();
        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        // Here, you set the data in your ListView
        listViewProject.setAdapter(adapter);
        listViewProject.setOnItemClickListener((adapterView, view ,i, l) -> Log.i(TAG, "Selected item "+adapter.getItem(i)));
        listViewProject.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(adapterView.getContext());
            alert.setTitle("Delete");
            alert.setMessage("Are you sure you want to delete  '" + adapter.getItem(i) + "'?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                String projectName = projectRef.child(adapter.getItem(i)).getKey(); //Make
                Query queryRef = projectRef.orderByChild("project_title").equalTo(projectName);
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
                arrayList.remove(i);
                Log.i(TAG, valueOf(i));
                //keys.remove(i);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            });
            alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            try {
                alert.show();
            } catch (Exception ex) {
                Log.d("err", ex.getMessage());
            }
            return true;
        });


        //Import Toolbar
        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Donut progress
        donutProgress.setProgress(75);
        textViewTopProjectName.setText("Project dari database");//TODO load from firebase
        // ketika di load, dapatkan value dari shared pref langsung
        String shName = sharedPrefs.getString("user_name", "");
        textViewUserName.setText(shName);
        buttonNewProject.setOnClickListener(v -> showProjectTitleBuilder());
        buttonEditProject.setOnClickListener(v -> Toast.makeText(this,"Edit project activity",Toast.LENGTH_SHORT).show());
        buttonSeeDetailLeft.setOnClickListener(v -> Toast.makeText(this,"Make details activity",Toast.LENGTH_SHORT).show());
    }


    public void onStart(){
        super.onStart();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listViewProject.setAdapter(adapter);
        projectRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                String val = dataSnapshot.child("project_title").getValue().toString();
                String key = dataSnapshot.child("project_title").getKey();
                Log.i(TAG, "onChildAdded:" + dataSnapshot.child("project_title").getValue().toString()); //value dari "project_title"
                arrayList.add(val); //menambahkan firebase ke listview
                keys.add(key); //menambahkan key
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

                //int ind = (s == null) ? 0 : Integer.parseInt(s);
                //arrayList.set(ind, dataSnapshot.child("project_title").getValue().toString());//
                recreate();
                adapter.notifyDataSetChanged();
                Log.i(TAG, "onChildChanged:" + dataSnapshot.child("project_title").getValue().toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot ) {
                // Refresh main activity upon close of dialog box
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
        });

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
            // this line adds the data of your EditText and puts in your array
            // arrayList.add(m_Text);
            // next thing you have to do is check if your adapter has changed
            // adapter.notifyDataSetChanged();
            // goToProjectTracker();
            addItemToFirebase();

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addItemToFirebase() {
        String key = projectRef.push().getKey();
        projectRef.child(key).child("project_title").setValue(m_Text);
        adapter.notifyDataSetChanged();
    }

    private void goToProjectTracker() {
        Intent projectTrackerIntent = new
                Intent(DashboardProjectActivity.this, ProjectTrackerActivity.class);
        startActivity(projectTrackerIntent);
    }

}

