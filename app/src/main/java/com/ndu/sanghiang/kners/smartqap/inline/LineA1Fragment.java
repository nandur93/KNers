package com.ndu.sanghiang.kners.smartqap.inline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
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
    private TextInputEditText editTextNoCharges,
            editTextSamplePembanding,
            editTextPh,
            editTextMoisture,
            editTextVitaminC,
            editTextCalcium,
            editTextBulkDensity,
            editTextSediment,
            editTextSolubility,
            editTextSinkers,
            editTextFloaters;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_line_a1, container, false);

        /*textView*/
        textViewHeader = rootView.findViewById(R.id.textView_header);
        /*editText*/
        editTextNoCharges = rootView.findViewById(R.id.edittext_no_charges);
        editTextSamplePembanding = rootView.findViewById(R.id.edittext_sample_pembanding);
        editTextPh = rootView.findViewById(R.id.edittext_ph);
        editTextMoisture = rootView.findViewById(R.id.edittext_moisture);
        editTextVitaminC = rootView.findViewById(R.id.edittext_vitaminC);
        editTextCalcium = rootView.findViewById(R.id.edittext_calcium);
        editTextBulkDensity = rootView.findViewById(R.id.edittext_bulkDensity);
        editTextSediment = rootView.findViewById(R.id.edittext_sediment);
        editTextSolubility = rootView.findViewById(R.id.edittext_solubility);
        editTextSinkers = rootView.findViewById(R.id.edittext_sinkers);
        editTextFloaters = rootView.findViewById(R.id.edittext_floaters);
        /*radioButton*/


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

        RadioGroup radioGroup = rootView.findViewById(R.id.radio_testType);
        Button btnSubmit = rootView.findViewById(R.id.button_submit);

        btnSubmit.setOnClickListener(v -> {

            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = rootView.findViewById(selectedId);

            Toast.makeText(getActivity(),
                    radioButton.getText(), Toast.LENGTH_SHORT).show();

        });
        return rootView;
    }

}