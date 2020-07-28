package com.ndu.sanghiang.kners.smartqap.inline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ndu.sanghiang.kners.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.BIT_ACTIVE_DRAFT;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_BO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QCP_EFORM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_INLINE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_PROCESS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.SMART_QAP;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.USERS;

public class LastSavedActivity extends AppCompatActivity {

    private static final String TAG = "Firebase";
    private ListView listView;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DatabaseReference smartQapNodeRef;
    private String pidKey;
    private DatabaseReference qcProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_saved);
        listView = findViewById(R.id.listViewLast);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // get current userId
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        smartQapNodeRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(SMART_QAP);
        pidKey = smartQapNodeRef.push().getKey();

        // data dari firebase
        showDraftList();
        // showAllList();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {

            String intNoBO = (String) ((TextView) view).getText();
            Log.d(TAG, "onCreate: onListClick " + intNoBO);

            Query query = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).orderByChild(INT_QCP_BO).equalTo(intNoBO);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                        Log.d(TAG, Objects.requireNonNull(key));
                        goToEform(key);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            };
            query.addListenerForSingleValueEvent(valueEventListener);
        });

    }

    private void showAllList() {
        //load data from database
        smartQapNodeRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot zoneSnapshot : snapshot.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).getChildren()) {
                    Log.d(TAG, "onDataChange: 76 " + zoneSnapshot.child(PID).getValue());
                    String pidKeyList = (String) zoneSnapshot.child(PID).getValue();
                    String boList = (String) zoneSnapshot.child(INT_QCP_BO).getValue();
                    list.add(boList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }

    private void showDraftList() {
        Query acoffeeQuery = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).orderByChild(BIT_ACTIVE_DRAFT).equalTo(1);
        acoffeeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    String boList = (String) ds.child(INT_QCP_BO).getValue();
                    Log.d(TAG, "onDataChange: 123" + boList);
                    list.add(boList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void goToEform(String key) {
        Intent intent = new
                Intent(this, EformProcessActivity.class);
        intent.putExtra(PID, key);
        startActivity(intent);
    }

}