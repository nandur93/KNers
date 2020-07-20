package com.ndu.sanghiang.kners.projecttrackerfi.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ndu.sanghiang.kners.R;

import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECTS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_DESC;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PROJECT_TITLE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.USERS;

public class TemaFragment extends Fragment {

    public static TextInputEditText inputEditTextJudulTema;
    public static TextInputEditText inputEditTextDescTema;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tema, container, false);
        // Init edittext
        inputEditTextJudulTema = view.findViewById(R.id.editTextTema);
        inputEditTextDescTema = view.findViewById(R.id.editTextDescTema);
        //Initial Firebase
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = Objects.requireNonNull(mCurrentUser).getUid();
        DatabaseReference projectRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(PROJECTS);
        String TAG = "Nandur93";

        // Load data from selected database
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //Dapatkan dan simpan data pid dari sharedpref
        String finalPid = sharedPrefs.getString(PID, ""); //contoh: -LkPHHo260u0OUK6nRZ5
        //String finalJudul = sharedPrefs.getString(PROJECT_TITLE, "");
        //String finalDesk = sharedPrefs.getString(PROJECT_DESC, "");
        //Log.i(TAG, finalJudul+" "+finalDesk+" Tema Fragment");
        projectRef.child(finalPid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i(TAG, finalPid + " exist ");
                    //jika -LkPHHo260u0OUK6nRZ5 ada
                    //update meta
                    //String projectStatusStr = dataSnapshot.child(finalPid).child(PROJECT_STATUS).getValue().toString();
                    //String projectProgressStr = dataSnapshot.child(finalPid).child(PROJECT_PROGRESS).getValue().toString();
                    //update step 1
                    String projectTitleStr = dataSnapshot.child(PROJECT_TITLE).getValue().toString();
                    String projectDescStr = dataSnapshot.child(PROJECT_DESC).getValue().toString();
                    //data berhasil di update ke database
                    //Check for null value
                    if (projectTitleStr != null) {
                        inputEditTextJudulTema.setText(projectTitleStr);
                    } else {
                        inputEditTextJudulTema.setText("");
                    }
                    if (projectDescStr != null) {
                        inputEditTextDescTema.setText(projectDescStr);
                    } else {
                        inputEditTextDescTema.setText("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
