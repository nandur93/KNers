package com.ndu.sanghiang.kners.projecttrackerfi.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ndu.sanghiang.kners.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class TemaFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText inputEditTextJudulTema, inputEditTextDescTema;
    private DatabaseReference projectRef;
    private String inputTitle, inputDesc;
    private String pid, judul;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tema, container, false);
        Button buttonSubmit = view.findViewById(R.id.button_submit_tema);
        inputEditTextJudulTema = view.findViewById(R.id.editTextTema);
        inputEditTextDescTema = view.findViewById(R.id.editTextDescTema);
        buttonSubmit.setOnClickListener(this);
        // Firebase
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = Objects.requireNonNull(mCurrentUser).getUid();
        projectRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("projects");
        // Load data from selected database
        if (getArguments() != null) {
            pid = getArguments().getString("pid");
            judul = getArguments().getString("judul_theme");

        }
        if (pid != null){
            inputEditTextJudulTema.setText(judul);
        } else {
            inputEditTextJudulTema.setText(null);
        }
        return view;

    }



    @Override
    public void onClick(View v) {
        inputTitle = inputEditTextJudulTema.getText().toString().trim();
        inputDesc = inputEditTextDescTema.getText().toString();
        if (inputTitle != null) {
            Toast.makeText(getActivity(), inputEditTextJudulTema.getText().toString(),Toast.LENGTH_SHORT).show();
            addItemToFirebase();
            goToNextStep();

        } else {
            Toast.makeText(getActivity(),"Masukkan judul",Toast.LENGTH_SHORT).show();
        }

    }

    private void goToNextStep() {
        TabLayout tabhost = getActivity().findViewById(R.id.tabs);
        tabhost.getTabAt(1).select();
    }

    private void addItemToFirebase() {
        String key = projectRef.push().getKey();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        projectRef.child(key).child("pid").setValue(key);
        projectRef.child(key).child("project_title").setValue(inputTitle);
        projectRef.child(key).child("project_desc").setValue(inputDesc);
        projectRef.child(key).child("project_status").setValue("Open");
        projectRef.child(key).child("project_created").setValue(date);
        projectRef.child(key).child("project_progress").setValue(13);
    }
}
