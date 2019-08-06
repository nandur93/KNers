package com.ndu.sanghiang.kners.projecttrackerfi.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.tabs.TabLayout;
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

import static com.ndu.sanghiang.kners.DashboardProjectActivity.PID;
import static com.ndu.sanghiang.kners.DashboardProjectActivity.PROJECT_DESC;
import static com.ndu.sanghiang.kners.DashboardProjectActivity.PROJECT_PROGRESS;
import static com.ndu.sanghiang.kners.DashboardProjectActivity.PROJECT_STATUS;
import static com.ndu.sanghiang.kners.DashboardProjectActivity.PROJECT_TITLE;

public class TemaFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText inputEditTextJudulTema;
    private TextInputEditText inputEditTextDescTema;
    private DatabaseReference projectRef;
    private String inputTitle, inputDesc;
    private SharedPreferences sharedPrefs;
    private String TAG;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tema, container, false);
        Button buttonSubmit = view.findViewById(R.id.button_submit_tema);
        inputEditTextJudulTema = view.findViewById(R.id.editTextTema);
        inputEditTextDescTema = view.findViewById(R.id.editTextDescTema);
        buttonSubmit.setOnClickListener(this);
        TAG = "Nandur93";
        // Firebase
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = Objects.requireNonNull(mCurrentUser).getUid();
        projectRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("projects");
        // Load data from selected database
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String finalJudul = sharedPrefs.getString(PROJECT_TITLE, "");
        String finalDesk = sharedPrefs.getString(PROJECT_DESC, "");
        Log.i(TAG, finalJudul+" "+finalDesk+" xyzzzzzzzz");
        if (finalJudul != null) {
            inputEditTextJudulTema.setText(finalJudul);
        } else {
            inputEditTextJudulTema.setText("");
        }
        if (finalDesk != null) {
            inputEditTextDescTema.setText(finalDesk);
        } else {
            inputEditTextDescTema.setText("");
        }

        return view;
    }
    @Override
    public void onClick(View v) {
        inputTitle = inputEditTextJudulTema.getText().toString().trim();
        inputDesc = inputEditTextDescTema.getText().toString();
        if (!inputTitle.equals("")) {
            Toast.makeText(getActivity(), inputEditTextJudulTema.getText().toString(),Toast.LENGTH_SHORT).show();
            updateFirebaseStep1();
            goToNextStep();
        } else {
            Toast.makeText(getActivity(),"Masukkan judul",Toast.LENGTH_SHORT).show();
        }

    }

    private void updateFirebaseStep1() {
        String finalPid = sharedPrefs.getString(PID, ""); //Get current pid from sharedPref

        projectRef.child(finalPid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(getActivity(), finalPid+" exist ", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, finalPid+" exist ");
                    //update meta
                    projectRef.child(finalPid).child(PROJECT_STATUS).setValue("In Progress");
                    projectRef.child(finalPid).child(PROJECT_PROGRESS).setValue(13);
                    //update step 1
                    projectRef.child(finalPid).child(PROJECT_TITLE).setValue(inputTitle);
                    projectRef.child(finalPid).child(PROJECT_DESC).setValue(inputDesc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void goToNextStep() {
        TabLayout tabhost = getActivity().findViewById(R.id.tabs);
        tabhost.getTabAt(1).select();
    }
}
