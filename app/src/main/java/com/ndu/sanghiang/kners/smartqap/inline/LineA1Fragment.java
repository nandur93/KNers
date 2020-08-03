package com.ndu.sanghiang.kners.smartqap.inline;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ndu.sanghiang.kners.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_BULK_DENSITY;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_CALCIUM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_CHARGES;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_FLOATERS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_MOISTURE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_PH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_SEDIMENT;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_SINKERS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_SOLUBILITY;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_VITAMINC;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_BATCH_NUMB;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_BO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_TOTAL_CHARGES;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QCP_EFORM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_INLINE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_PROCESS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.SMART_QAP;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCPF_SAMPLE_PEMBANDING;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.USERS;

public class LineA1Fragment extends Fragment {

    TextView textViewHeader;
    private DatabaseReference smartQapNodeRef;
    private String pidKey;
    private String pid;
    private String headerDetail;
    private TextInputEditText
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
    private Spinner spinnerNoCharges;
    private String TAG;
    private ArrayAdapter<String> arrayAdapter;
    private String totalCharges;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_line_a1, container, false);

        /*textView*/
        textViewHeader = rootView.findViewById(R.id.textView_header);
        /*editText*/
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


        TAG = "Firebase";
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
        totalCharges = getActivity().getIntent().getStringExtra(INT_QCP_TOTAL_CHARGES);
        Log.d(TAG, "onCreateView: 115 " + totalCharges);
        Log.d(TAG, "onCreateView: 116 " + pid);

        textViewHeader.setText(headerDetail);

        // create array of Strings
        // and store name of courses
        /*int n = Integer.parseInt(totalCharges.replaceAll("[\\D]", ""));*/
        try {

            int intTotalCharges = Integer.parseInt(totalCharges.replaceAll("[\\D]", ""));
            Log.d(TAG, "onCreateView: 126 " + intTotalCharges);
            String[] noCharges = new String[intTotalCharges];
            for (int i = 0; i < intTotalCharges; i++) {
                noCharges[i] = String.valueOf(i + 1);
            }
            // Take the instance of Spinner and
            // apply OnItemSelectedListener on it which
            // tells which item of spinner is clicked
            spinnerNoCharges = rootView.findViewById(R.id.spinner2);

            // Create the instance of ArrayAdapter
            // having the list of courses
            arrayAdapter = new ArrayAdapter<>(
                    Objects.requireNonNull(getActivity()),
                    android.R.layout
                            .simple_spinner_item,
                    noCharges);

            // set simple layout resource file
            // for each item of spinner
            arrayAdapter.setDropDownViewResource(
                    android.R.layout
                            .simple_spinner_dropdown_item);

            // Set the ArrayAdapter (arrayAdapter) data on the
            // Spinner which binds data to spinner
            spinnerNoCharges.setAdapter(arrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

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


        /*//Spinner from firebase
        Query query = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).orderByChild(INT_QCP_BATCH_NUMB);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> titleList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String titlename = dataSnapshot1.child(INT_QCP_BATCH_NUMB).getValue(String.class);
                    titleList.add(titlename);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerNoCharges.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/

        return rootView;
    }

    /*private void loadDataFirebase() {
        smartQapNodeRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot snapEform = snapshot.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(pid);
                        Log.d(TAG, "191 pid snapEform: " + snapEform.getValue());
                        String firebasePidKey = (String) snapEform.child(PID).getValue();
                        String firebaseNoCharges = (String) snapEform.child(INT_QCPF_CHARGES).getValue();
                        String firebaseSamplePembanding = (String) snapEform.child(TXT_QCPF_SAMPLE_PEMBANDING).getValue();
                        String firebasePh = (String) snapEform.child(INT_QCPF_PH).getValue();
                        String firebaseMoisture = (String) snapEform.child(INT_QCPF_MOISTURE).getValue();
                        String firebaseVitaminC = (String) snapEform.child(INT_QCPF_VITAMINC).getValue();
                        String firebaseCalcium = (String) snapEform.child(INT_QCPF_CALCIUM).getValue();
                        String firebaseBulkDensity = (String) snapEform.child(INT_QCPF_BULK_DENSITY).getValue();
                        String firebaseSediment = (String) snapEform.child(INT_QCPF_SEDIMENT).getValue();
                        String firebaseSolubility = (String) snapEform.child(INT_QCPF_SOLUBILITY).getValue();
                        String firebaseSinkers = (String) snapEform.child(INT_QCPF_SINKERS).getValue();
                        String firebaseFloaters = (String) snapEform.child(INT_QCPF_FLOATERS).getValue();

                        Log.d(TAG, "201 pid: " + firebasePidKey);

                        spinnerNoCharges.setSelection(arrayAdapter.getPosition(firebaseNoCharges));
                        editTextSamplePembanding.setText(firebaseSamplePembanding);
                        editTextPh.setText(firebasePh);
                        editTextMoisture.setText(firebaseMoisture);
                        editTextVitaminC.setText(firebaseVitaminC);
                        editTextCalcium.setText(firebaseCalcium);
                        editTextBulkDensity.setText(firebaseBulkDensity);
                        editTextSediment.setText(firebaseSediment);
                        editTextSolubility.setText(firebaseSolubility);
                        editTextSinkers.setText(firebaseSinkers);
                        editTextFloaters.setText(firebaseFloaters);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }*/

}