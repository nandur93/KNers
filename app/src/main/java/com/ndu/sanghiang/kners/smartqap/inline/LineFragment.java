package com.ndu.sanghiang.kners.smartqap.inline;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.database.ValueEventListener;
import com.ndu.sanghiang.kners.R;

import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.ARR_QCPF_CHARGES;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_BULK_DENSITY;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_CALCIUM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_FLOATERS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_FOREIGN;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_LARUTAN;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_MOISTURE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_PH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_POWDER;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_SEDIMENT;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_SINKERS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_SOLUBILITY;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_TESTTYPE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCPF_VITAMINC;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_BO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_TOTAL_CHARGES;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QCP_EFORM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_INLINE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_PROCESS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_COMPLETE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_CONFORM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_NEGATIVE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_NOT_CONFORM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_POSITIVE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_REPEAT;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.SMART_QAP;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCPF_SAMPLE_PEMBANDING;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.USERS;

public class LineFragment extends Fragment {

    TextView textViewHeader;
    private DatabaseReference smartQapNodeRef;
    private String pidKey;
    private String pid;
    private String headerDetail;
    private TextInputEditText editTextSamplePembanding, editTextPh, editTextMoisture, editTextVitaminC,
            editTextCalcium, editTextBulkDensity, editTextSediment, editTextSolubility, editTextSinkers,
            editTextFloaters;
    private Spinner spinnerNoCharges;
    private String TAG;
    private ArrayAdapter<String> arrayAdapter;
    private String totalCharges;
    private DatabaseReference qcProcess;
    private DatabaseReference chargesFb;
    private String charges;
    private String testTypeValue, powderValue, larutanValue, foreignMatterValue, samplePembanding,
            phMeter, moisture, vitaminC, calcium, bulkDensity, sediment, solubility, sinkers, floaters;
    private RadioGroup radioGroupTestType;
    private RadioGroup radioGroupPowder;
    private RadioGroup radioGroupLarutan;
    private RadioGroup radioGroupForeignMatter;
    private String selectedBatchNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_line, container, false);

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
        pid = requireActivity().getIntent().getStringExtra(PID);
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
                    requireActivity(),
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
            spinnerNoCharges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.v("floor", (String) parent.getItemAtPosition(position));
                    selectedBatchNumber = (String) parent.getItemAtPosition(position);
                    loadDataDetails();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (pid != null) {
            loadDataDetails();
            Log.d(TAG, "133 pid: tidak null " + pid);
        } else {
            Log.d(TAG, "pid: null 135");
            //loadBlankData();
            //load data from latest
            //qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey));
        }

        radioGroupTestType = rootView.findViewById(R.id.radio_testType);
        radioGroupPowder = rootView.findViewById(R.id.radio_powder);
        radioGroupLarutan = rootView.findViewById(R.id.radio_larutan);
        radioGroupForeignMatter = rootView.findViewById(R.id.radio_foreignMatter);
        Button btnSubmit = rootView.findViewById(R.id.button_submit);

        btnSubmit.setOnClickListener(v -> {
            samplePembanding = Objects.requireNonNull(editTextSamplePembanding.getText()).toString();
            phMeter = Objects.requireNonNull(editTextPh.getText()).toString().trim();
            moisture = Objects.requireNonNull(editTextMoisture.getText()).toString().trim();
            vitaminC = Objects.requireNonNull(editTextVitaminC.getText()).toString().trim();
            calcium = Objects.requireNonNull(editTextCalcium.getText()).toString().trim();
            bulkDensity = Objects.requireNonNull(editTextBulkDensity.getText()).toString().trim();
            sediment = Objects.requireNonNull(editTextSediment.getText()).toString().trim();
            solubility = Objects.requireNonNull(editTextSolubility.getText()).toString().trim();
            sinkers = Objects.requireNonNull(editTextSinkers.getText()).toString().trim();
            floaters = Objects.requireNonNull(editTextFloaters.getText()).toString().trim();

            // get selected radio button from radioGroupTestType
            int testTypeCheckedRadioButtonId = radioGroupTestType.getCheckedRadioButtonId();
            int powderCheckedRadioButtonId = radioGroupPowder.getCheckedRadioButtonId();
            int larutanCheckedRadioButtonId = radioGroupLarutan.getCheckedRadioButtonId();
            int foreignMatterCheckedRadioButtonId = radioGroupForeignMatter.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButtonTestType = rootView.findViewById(testTypeCheckedRadioButtonId);
            RadioButton radioButtonPowder = rootView.findViewById(powderCheckedRadioButtonId);
            RadioButton radioButtonLarutan = rootView.findViewById(larutanCheckedRadioButtonId);
            RadioButton radioButtonForeignMatter = rootView.findViewById(foreignMatterCheckedRadioButtonId);
            try {
                testTypeValue = radioButtonTestType.getText().toString();
                powderValue = radioButtonPowder.getText().toString();
                larutanValue = radioButtonLarutan.getText().toString();
                foreignMatterValue = radioButtonForeignMatter.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG, "onCreateView: " + testTypeValue);

            try {
                charges = spinnerNoCharges.getSelectedItem().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            dataToFirebase();
            goToQCInline();
            Toast.makeText(getActivity(), "Data submitted", Toast.LENGTH_SHORT).show();
        });

        // Data dari Wip
        pid = requireActivity().getIntent().getStringExtra(PID);
        Log.d(TAG, "onCreate: pid from wip 181 " + pid);

        if (pid != null) {
            Log.d(TAG, "188 pid: tidak null " + pid);
        } else {
            Log.d(TAG, "pid: null 190 " + pidKey);
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey));
        }
        return rootView;
    }

    private void goToQCInline() {
        Intent goToQCInline = new
                Intent(getActivity(), QcInlineActivity.class);
        startActivity(goToQCInline);
    }

    private void loadDataDetails() {
        smartQapNodeRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (selectedBatchNumber == null) {
                            selectedBatchNumber = "1";
                        }
                        DataSnapshot snapEform = snapshot.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(pid).child(ARR_QCPF_CHARGES).child(selectedBatchNumber);
                        Log.d(TAG, "266 pid snapEform: " + snapEform.getValue());

                        //RadioButtons
                        String testType = (String) snapEform.child(INT_QCPF_TESTTYPE).getValue();
                        String powder = (String) snapEform.child(INT_QCPF_POWDER).getValue();
                        String larutan = (String) snapEform.child(INT_QCPF_LARUTAN).getValue();
                        String foreignMatter = (String) snapEform.child(INT_QCPF_FOREIGN).getValue();

                        //EditText
                        editTextSamplePembanding.setText((String) snapEform.child(TXT_QCPF_SAMPLE_PEMBANDING).getValue());
                        editTextPh.setText((String) snapEform.child(INT_QCPF_PH).getValue());
                        editTextMoisture.setText((String) snapEform.child(INT_QCPF_MOISTURE).getValue());
                        editTextVitaminC.setText((String) snapEform.child(INT_QCPF_VITAMINC).getValue());
                        editTextCalcium.setText((String) snapEform.child(INT_QCPF_CALCIUM).getValue());
                        editTextBulkDensity.setText((String) snapEform.child(INT_QCPF_BULK_DENSITY).getValue());
                        editTextSediment.setText((String) snapEform.child(INT_QCPF_SEDIMENT).getValue());
                        editTextSolubility.setText((String) snapEform.child(INT_QCPF_SOLUBILITY).getValue());
                        editTextSinkers.setText((String) snapEform.child(INT_QCPF_SINKERS).getValue());
                        editTextFloaters.setText((String) snapEform.child(INT_QCPF_FLOATERS).getValue());

                        Log.d(TAG, "269 pid: " + testType);

                        if (Objects.equals(testType, RADIO_BUTTON_COMPLETE)) {
                            radioGroupTestType.check(R.id.radioButton_complete);
                        } else if (Objects.equals(testType, RADIO_BUTTON_REPEAT)) {
                            radioGroupTestType.check(R.id.radioButton_repeat);
                        } else {
                            radioGroupTestType.clearCheck();
                        }

                        if (Objects.equals(powder, RADIO_BUTTON_CONFORM)) {
                            radioGroupPowder.check(R.id.radioButton_powderConform);
                        } else if (Objects.equals(powder, RADIO_BUTTON_NOT_CONFORM)) {
                            radioGroupPowder.check(R.id.radioButton_powderNotConform);
                        } else {
                            radioGroupPowder.clearCheck();
                        }

                        if (Objects.equals(larutan, RADIO_BUTTON_CONFORM)) {
                            radioGroupLarutan.check(R.id.radioButton_larutanConform);
                        } else if (Objects.equals(larutan, RADIO_BUTTON_NOT_CONFORM)) {
                            radioGroupLarutan.check(R.id.radioButton_larutanNotConform);
                        } else {
                            radioGroupLarutan.clearCheck();
                        }

                        if (Objects.equals(foreignMatter, RADIO_BUTTON_NEGATIVE)) {
                            radioGroupForeignMatter.check(R.id.radioButton_foreignMatterNegatif);
                        } else if (Objects.equals(foreignMatter, RADIO_BUTTON_POSITIVE)) {
                            radioGroupForeignMatter.check(R.id.radioButton_foreignMatterPositif);
                        } else {
                            radioGroupForeignMatter.clearCheck();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void dataToFirebase() {

        if (pid != null) {
            Log.d(TAG, "233 pid: tidak null " + pid);
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pid));
            chargesFb = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pid)).child(ARR_QCPF_CHARGES).child(charges);
        } else {
            Log.d(TAG, "pid: null 238 " + pidKey);
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey));
            chargesFb = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey)).child(ARR_QCPF_CHARGES).child(charges);
        }
        Log.d(TAG, "dataToFirebase: charges " + charges);
        chargesFb.child(INT_QCPF_TESTTYPE).setValue(testTypeValue);
        chargesFb.child(INT_QCPF_POWDER).setValue(powderValue);
        chargesFb.child(INT_QCPF_LARUTAN).setValue(larutanValue);
        chargesFb.child(INT_QCPF_FOREIGN).setValue(foreignMatterValue);
        chargesFb.child(TXT_QCPF_SAMPLE_PEMBANDING).setValue(samplePembanding);
        chargesFb.child(INT_QCPF_PH).setValue(phMeter);
        chargesFb.child(INT_QCPF_MOISTURE).setValue(moisture);
        chargesFb.child(INT_QCPF_VITAMINC).setValue(vitaminC);
        chargesFb.child(INT_QCPF_CALCIUM).setValue(calcium);
        chargesFb.child(INT_QCPF_BULK_DENSITY).setValue(bulkDensity);
        chargesFb.child(INT_QCPF_SEDIMENT).setValue(sediment);
        chargesFb.child(INT_QCPF_SOLUBILITY).setValue(solubility);
        chargesFb.child(INT_QCPF_SINKERS).setValue(sinkers);
        chargesFb.child(INT_QCPF_FLOATERS).setValue(floaters);

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