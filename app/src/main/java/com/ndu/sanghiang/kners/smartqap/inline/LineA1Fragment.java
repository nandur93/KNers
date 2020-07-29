package com.ndu.sanghiang.kners.smartqap.inline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ndu.sanghiang.kners.R;

import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_BO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.SMART_QAP;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.USERS;

public class LineA1Fragment extends Fragment {

    TextView textViewHeader;
    private DatabaseReference smartQapNodeRef;
    private String pidKey;
    private String pid;
    private String headerDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_line_a1, container, false);
        textViewHeader = rootView.findViewById(R.id.textView_header);
        String TAG = "Firebase";
        /*Data dari firebase*/
        // Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // get current userId
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        smartQapNodeRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(SMART_QAP);
        pidKey = smartQapNodeRef.push().getKey();

        // Data dari lastSaved
        pid = Objects.requireNonNull(getActivity()).getIntent().getStringExtra(PID);
        headerDetail = getActivity().getIntent().getStringExtra(INT_QCP_BO);
        Log.d(TAG, "onCreateView: 49 " + pid);

        textViewHeader.setText(headerDetail);
        return rootView;
    }
}