package com.ndu.sanghiang.kners.smartqap.inline;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ndu.sanghiang.kners.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.CHECK_QCI_INSTRUMENT;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.CHECK_QCI_KIMIA;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.CHECK_QCI_MIKRO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.CHECK_QCI_ORGANO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.CHECK_QCI_RETAIN;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.CHECK_QCI_THREE_DAYS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.HRS_QCI_JAM_SAMPLE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.HRS_QCI_JAM_SAMPLE2;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_BERAT_NETTO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_HORIZONTAL_BELAKANG;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_HORIZONTAL_DEPAN;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_JUMLAH_BONGKAR;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_JUMLAH_NG;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_KEGEMBUNGAN;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_MEASURE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_NO_CARTON;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_OXYGEN;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_PANJANG_SACHET;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_PRESSURE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_TOTAL_SAMPLE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCI_VERTICAL_SEAL;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_BO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QCP_EFORM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_INLINE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_PROCESS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_CONFORM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_NEGATIVE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_NOT_CONFORM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_NOT_OK;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_OK;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.RADIO_BUTTON_POSITIVE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.SMART_QAP;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCI_CORRECTIVE_ACTION;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCI_CRACKING;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCI_LEAKING;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCI_METAL;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCI_METAL_CATCHER;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCI_NON_METAL;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCI_ORGANO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCI_ROOT_CAUSE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.USERS;

public class PackingFragment extends Fragment {

    private static final String TAG = "Firebase";
    private TextView textViewHeader;
    private TextInputEditText editTextCartonBox;
    private TextInputEditText editTextHorizontalDepan;
    private TextInputEditText editTextHorizontalBelakang;
    private TextInputEditText editTextVertikalSeal;
    private TextInputEditText editTextOxygen;
    private TextInputEditText editTextPressure;
    private TextInputEditText editTextMeasure;
    private TextInputEditText editTextPanjangSachet;
    private TextInputEditText editTextKegembungan;
    private TextInputEditText editTextBeratNetto;
    private TextInputEditText editTextTotalSample;
    private TextInputEditText editTextJumlahBongkar;
    private TextInputEditText editTextRootCause;
    private TextInputEditText editTextCorrectiveAction;
    private TextInputEditText editTextJumlahNg;
    private TextInputEditText editTextJamSample;
    private TextInputEditText editTextJamSample2;
    private CheckBox checkBoxMicro;
    private CheckBox checkBoxKimia;
    private CheckBox checkBoxRetain;
    private CheckBox checkBoxThreeDays;
    private CheckBox checkBoxInstrument;
    private CheckBox checkBoxOrgano;
    private RadioGroup radioGroupLeaking;
    private RadioGroup radioGroupCracking;
    private RadioGroup radioGroupOrgano;
    private RadioGroup radioGroupMetal;
    private RadioGroup radioGroupNonMetal;
    private RadioGroup radioGroupMetalCathcer;
    private RadioButton radioButtonLeakingOk;
    private RadioButton radioButtonLeakingNok;
    private RadioButton radioButtonCrackingOk;
    private RadioButton radioButtonCrackingNok;
    private RadioButton radioButtonOrganoConform;
    private RadioButton radioButtonOrganoNotConform;
    private RadioButton radioButtonMetalNeg;
    private RadioButton radioButtonMetalPos;
    private RadioButton radioButtonNonMetalNeg;
    private RadioButton radioButtonNonMetalPos;
    private RadioButton radioButtonMetalCatcherNeg;
    private RadioButton radioButtonMetalCatcherPos;
    private DatabaseReference smartQapNodeRef;
    private String pidKey;
    private String pid;
    private String headerDetail;
    private Button buttonSubmit;
    private Button buttonClearForm;
    private DatabaseReference qcProcess;
    private DatabaseReference noCartonFb;
    private String noCarton;
    private int sampleMikro;
    private int sampleKimia;
    private int sampleRetain;
    private int sampleInstrument;
    private int sampleThreeDays;
    private int sampleOrgano;
    private String horizontalDepan;
    private String horizontalBelakang;
    private String vertikalSeal;
    private String oxygen;
    private String pressure;
    private String measure;
    private String panjangSachet;
    private String kegembungan;
    private String beratNetto;
    private String totalSample;
    private String jumlahBongkar;
    private String rooCause;
    private String correctiveAction;
    private String jumlahNg;
    private String jamSample;
    private String jamSample2;
    private String leakingValue;
    private String crackingValue;
    private String organoValue;
    private String metalValue;
    private String nonMetalValue;
    private String metalCatcherValue;
    private Spinner spinnerCheckedBox;
    private ArrayAdapter<String> arrayAdapterCheckedBox;
    private String selectedCartonNumber;
    private Spinner cartonSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_packing, container, false);
        //Textview
        textViewHeader = rootView.findViewById(R.id.textView_header);
        //Edittext
        editTextCartonBox = rootView.findViewById(R.id.edittext_carton_box);
        editTextHorizontalDepan = rootView.findViewById(R.id.edittext_horizontal_depan);
        editTextHorizontalBelakang = rootView.findViewById(R.id.edittext_horizontal_belakang);
        editTextVertikalSeal = rootView.findViewById(R.id.edittext_vertical_seal);
        editTextOxygen = rootView.findViewById(R.id.edittext_oxygen);
        editTextPressure = rootView.findViewById(R.id.edittext_presure);
        editTextMeasure = rootView.findViewById(R.id.edittext_measure);
        editTextPanjangSachet = rootView.findViewById(R.id.edittext_panjang_sachet);
        editTextKegembungan = rootView.findViewById(R.id.edittext_kegembungan);
        editTextBeratNetto = rootView.findViewById(R.id.edittext_berat_netto);
        editTextTotalSample = rootView.findViewById(R.id.edittext_total_sample);
        editTextJumlahBongkar = rootView.findViewById(R.id.edittext_jumlah_bongkar);
        editTextRootCause = rootView.findViewById(R.id.edittext_root_cause);
        editTextCorrectiveAction = rootView.findViewById(R.id.edittext_corrective_acction);
        editTextJumlahNg = rootView.findViewById(R.id.edittext_jumlah_ng);
        editTextJamSample = rootView.findViewById(R.id.edittext_jam_sample);
        editTextJamSample2 = rootView.findViewById(R.id.edittext_jam_sample2);
        //Checkbox
        checkBoxMicro = rootView.findViewById(R.id.checkBox_micro);
        checkBoxKimia = rootView.findViewById(R.id.checkBox_chemical);
        checkBoxRetain = rootView.findViewById(R.id.checkBox_retain);
        checkBoxInstrument = rootView.findViewById(R.id.checkBox_instrumen);
        checkBoxThreeDays = rootView.findViewById(R.id.checkBox_three_days);
        checkBoxOrgano = rootView.findViewById(R.id.checkBox_organo);
        //RadioGroup
        radioGroupLeaking = rootView.findViewById(R.id.radio_leaking);
        radioGroupCracking = rootView.findViewById(R.id.radio_cracking);
        radioGroupOrgano = rootView.findViewById(R.id.radio_organo);
        radioGroupMetal = rootView.findViewById(R.id.radio_metal);
        radioGroupNonMetal = rootView.findViewById(R.id.radio_nonmetal);
        radioGroupMetalCathcer = rootView.findViewById(R.id.radio_metal_catcher);
        //RadioButton
        radioButtonLeakingOk = rootView.findViewById(R.id.radioButton_leakingOk);
        radioButtonLeakingNok = rootView.findViewById(R.id.radioButton_leakingNok);
        radioButtonCrackingOk = rootView.findViewById(R.id.radioButton_crackingOk);
        radioButtonCrackingNok = rootView.findViewById(R.id.radioButton_crackingNok);
        radioButtonOrganoConform = rootView.findViewById(R.id.radioButton_organoConform);
        radioButtonOrganoNotConform = rootView.findViewById(R.id.radioButton_organoNotConform);
        radioButtonMetalNeg = rootView.findViewById(R.id.radioButton_metalNeg);
        radioButtonMetalPos = rootView.findViewById(R.id.radioButton_metalPos);
        radioButtonNonMetalNeg = rootView.findViewById(R.id.radioButton_nonMetalNeg);
        radioButtonNonMetalPos = rootView.findViewById(R.id.radioButton_nonMetalPos);
        radioButtonMetalCatcherNeg = rootView.findViewById(R.id.radioButton_metalCatcherNeg);
        radioButtonMetalCatcherPos = rootView.findViewById(R.id.radioButton_metalCathcerPos);
        //Spinner

        //Button
        buttonSubmit = rootView.findViewById(R.id.button_submit);
        buttonClearForm = rootView.findViewById(R.id.button_clear_form);
        buttonClearForm.setOnClickListener(v -> clearForm());

        /*Data dari firebase*/
        // Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // get current userId
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        smartQapNodeRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(SMART_QAP);
        pidKey = smartQapNodeRef.push().getKey();

        // Data dari lastSaved
        pid = requireActivity().getIntent().getStringExtra(PID);
        headerDetail = requireActivity().getIntent().getStringExtra(INT_QCP_BO);

        Log.d(TAG, "onCreateView: " + pid + " " + pidKey + " " + headerDetail);

        textViewHeader.setText(headerDetail);
        editTextCartonBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(requireActivity(), "Changed", Toast.LENGTH_SHORT).show();
                //TODO: Change text
                clearForm();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (pid != null) {
            Log.d(TAG, "233 pid: tidak null " + pid);
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pid));
            noCartonFb = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pid)).child(INT_QCI_NO_CARTON);
        } else {
            Log.d(TAG, "pid: null 238 " + pidKey);
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey));
            noCartonFb = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey)).child(INT_QCI_NO_CARTON);
        }
        // https://stackoverflow.com/questions/38492827/how-to-get-a-string-list-from-firebase-to-fill-a-spinner
        noCartonFb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                try {

                    final List<String> cartons = new ArrayList<>();

                    for (DataSnapshot cartonSnapshot : dataSnapshot.getChildren()) {
                        String noCarton = cartonSnapshot.child(INT_QCI_NO_CARTON).getValue(String.class);
                        cartons.add(noCarton);
                    }

                    cartonSpinner = (Spinner) rootView.findViewById(R.id.spinnerCheckedBox);
                    ArrayAdapter<String> cartonsAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, cartons);
                    cartonsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cartonSpinner.setAdapter(cartonsAdapter);
                    cartonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.v("floor", (String) parent.getItemAtPosition(position));
                            selectedCartonNumber = (String) parent.getItemAtPosition(position);
                            loadDataDetails();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        buttonSubmit.setOnClickListener(v -> {

            //No Carton Box
            noCarton = Objects.requireNonNull(editTextCartonBox.getText()).toString().trim();
            horizontalDepan = Objects.requireNonNull(editTextHorizontalDepan.getText()).toString().trim();
            horizontalBelakang = Objects.requireNonNull(editTextHorizontalBelakang.getText()).toString().trim();
            vertikalSeal = Objects.requireNonNull(editTextVertikalSeal.getText()).toString().trim();
            oxygen = Objects.requireNonNull(editTextOxygen.getText()).toString().trim();
            pressure = Objects.requireNonNull(editTextPressure.getText()).toString().trim();
            measure = Objects.requireNonNull(editTextMeasure.getText()).toString().trim();
            panjangSachet = Objects.requireNonNull(editTextPanjangSachet.getText()).toString().trim();
            kegembungan = Objects.requireNonNull(editTextKegembungan.getText()).toString().trim();
            beratNetto = Objects.requireNonNull(editTextBeratNetto.getText()).toString().trim();
            totalSample = Objects.requireNonNull(editTextTotalSample.getText()).toString().trim();
            jumlahBongkar = Objects.requireNonNull(editTextJumlahBongkar.getText()).toString().trim();
            rooCause = Objects.requireNonNull(editTextRootCause.getText()).toString().trim();
            correctiveAction = Objects.requireNonNull(editTextCorrectiveAction.getText()).toString().trim();
            jumlahNg = Objects.requireNonNull(editTextJumlahNg.getText()).toString().trim();
            jamSample = Objects.requireNonNull(editTextJamSample.getText()).toString().trim();
            jamSample2 = Objects.requireNonNull(editTextJamSample2.getText()).toString().trim();

            // get selected radio button from radioGroupTestType
            int leakingCheckedRadioButtonId = radioGroupLeaking.getCheckedRadioButtonId();
            int crackingCheckedRadioButtonId = radioGroupCracking.getCheckedRadioButtonId();
            int organoCheckedRadioButtonId = radioGroupOrgano.getCheckedRadioButtonId();
            int metalCheckedRadioButtonId = radioGroupMetal.getCheckedRadioButtonId();
            int nonMetalCheckedRadioButtonId = radioGroupNonMetal.getCheckedRadioButtonId();
            int metalCatcherCheckedRadioButtonId = radioGroupMetalCathcer.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButtonLeaking = rootView.findViewById(leakingCheckedRadioButtonId);
            RadioButton radioButtonCracking = rootView.findViewById(crackingCheckedRadioButtonId);
            RadioButton radioButtonOrgano = rootView.findViewById(organoCheckedRadioButtonId);
            RadioButton radioButtonMetal = rootView.findViewById(metalCheckedRadioButtonId);
            RadioButton radioButtonNonMetal = rootView.findViewById(nonMetalCheckedRadioButtonId);
            RadioButton radioButtonMetalCatcher = rootView.findViewById(metalCatcherCheckedRadioButtonId);

            try {
                leakingValue = radioButtonLeaking.getText().toString();
                crackingValue = radioButtonCracking.getText().toString();
                organoValue = radioButtonOrgano.getText().toString();
                metalValue = radioButtonMetal.getText().toString();
                nonMetalValue = radioButtonNonMetal.getText().toString();
                metalCatcherValue = radioButtonMetalCatcher.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            submitDataToFirebase();
        });

        return rootView;
    }

    private void clearForm() {
        editTextHorizontalDepan.setText("");
        editTextHorizontalBelakang.setText("");
        editTextVertikalSeal.setText("");
        editTextOxygen.setText("");
        editTextPressure.setText("");
        editTextMeasure.setText("");
        editTextPanjangSachet.setText("");
        editTextKegembungan.setText("");
        editTextBeratNetto.setText("");
        editTextTotalSample.setText("");
        editTextJumlahBongkar.setText("");
        editTextRootCause.setText("");
        editTextCorrectiveAction.setText("");
        editTextJumlahNg.setText("");
        editTextJamSample.setText("");
        editTextJamSample2.setText("");
        //Checkbox
        checkBoxMicro.setChecked(false);
        checkBoxKimia.setChecked(false);
        checkBoxRetain.setChecked(false);
        checkBoxInstrument.setChecked(false);
        checkBoxThreeDays.setChecked(false);
        checkBoxOrgano.setChecked(false);
        //RadioGroup
        radioGroupLeaking.clearCheck();
        radioGroupCracking.clearCheck();
        radioGroupOrgano.clearCheck();
        radioGroupMetal.clearCheck();
        radioGroupNonMetal.clearCheck();
        radioGroupMetalCathcer.clearCheck();

    }

    private void loadDataDetails() {
        smartQapNodeRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (selectedCartonNumber == null) {
                            selectedCartonNumber = "1";
                        }
                        DataSnapshot snapEform = snapshot.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(pid).child(INT_QCI_NO_CARTON).child(selectedCartonNumber);
                        Log.d(TAG, "266 pid snapEform: " + snapEform.getValue());

                        //RadioButtons
                        String leaking = (String) snapEform.child(TXT_QCI_LEAKING).getValue();
                        String cracking = (String) snapEform.child(TXT_QCI_CRACKING).getValue();
                        String organo = (String) snapEform.child(TXT_QCI_ORGANO).getValue();
                        String metal = (String) snapEform.child(TXT_QCI_METAL).getValue();
                        String nonMetal = (String) snapEform.child(TXT_QCI_NON_METAL).getValue();
                        String metalCatcher = (String) snapEform.child(TXT_QCI_METAL_CATCHER).getValue();

                        //Checked
                        long _mikro = (long) snapEform.child(CHECK_QCI_MIKRO).getValue();
//                        String _retain = (String) snapEform.child(CHECK_QCI_RETAIN).getValue();
//                        String _threeDays = (String) snapEform.child(CHECK_THREE_DAYS).getValue();
//                        String _kimia = (String) snapEform.child(CHECK_QCI_KIMIA).getValue();
//                        String _instrumen = (String) snapEform.child(CHECK_QCI_INSTRUMENT).getValue();
//                        String _organo = (String) snapEform.child(CHECK_QCI_ORGANO).getValue();

                        //EditText
                        editTextCartonBox.setText((String) snapEform.child(INT_QCI_NO_CARTON).getValue());
                        editTextHorizontalDepan.setText((String) snapEform.child(INT_QCI_HORIZONTAL_DEPAN).getValue());
                        editTextHorizontalBelakang.setText((String) snapEform.child(INT_QCI_HORIZONTAL_BELAKANG).getValue());
                        editTextVertikalSeal.setText((String) snapEform.child(INT_QCI_VERTICAL_SEAL).getValue());
                        editTextOxygen.setText((String) snapEform.child(INT_QCI_OXYGEN).getValue());
                        editTextPressure.setText((String) snapEform.child(INT_QCI_PRESSURE).getValue());
                        editTextMeasure.setText((String) snapEform.child(INT_QCI_MEASURE).getValue());
                        editTextPanjangSachet.setText((String) snapEform.child(INT_QCI_PANJANG_SACHET).getValue());
                        editTextKegembungan.setText((String) snapEform.child(INT_QCI_KEGEMBUNGAN).getValue());
                        editTextBeratNetto.setText((String) snapEform.child(INT_QCI_BERAT_NETTO).getValue());
                        editTextTotalSample.setText((String) snapEform.child(INT_QCI_TOTAL_SAMPLE).getValue());
                        editTextJumlahBongkar.setText((String) snapEform.child(INT_QCI_JUMLAH_BONGKAR).getValue());
                        editTextRootCause.setText((String) snapEform.child(TXT_QCI_ROOT_CAUSE).getValue());
                        editTextCorrectiveAction.setText((String) snapEform.child(TXT_QCI_CORRECTIVE_ACTION).getValue());
                        editTextJumlahNg.setText((String) snapEform.child(INT_QCI_JUMLAH_NG).getValue());
                        editTextJamSample.setText((String) snapEform.child(HRS_QCI_JAM_SAMPLE).getValue());
                        editTextJamSample2.setText((String) snapEform.child(HRS_QCI_JAM_SAMPLE2).getValue());


                        if (Objects.equals(leaking, RADIO_BUTTON_OK)) {
                            radioGroupLeaking.check(R.id.radioButton_leakingOk);
                        } else if (Objects.equals(leaking, RADIO_BUTTON_NOT_OK)) {
                            radioGroupLeaking.check(R.id.radioButton_leakingNok);
                        } else {
                            radioGroupLeaking.clearCheck();
                        }

                        if (Objects.equals(cracking, RADIO_BUTTON_OK)) {
                            radioGroupCracking.check(R.id.radioButton_crackingOk);
                        } else if (Objects.equals(leaking, RADIO_BUTTON_NOT_OK)) {
                            radioGroupCracking.check(R.id.radioButton_crackingNok);
                        } else {
                            radioGroupCracking.clearCheck();
                        }

                        if (Objects.equals(organo, RADIO_BUTTON_CONFORM)) {
                            radioGroupOrgano.check(R.id.radioButton_organoConform);
                        } else if (Objects.equals(organo, RADIO_BUTTON_NOT_CONFORM)) {
                            radioGroupOrgano.check(R.id.radioButton_organoNotConform);
                        } else {
                            radioGroupOrgano.clearCheck();
                        }

                        if (Objects.equals(metal, RADIO_BUTTON_NEGATIVE)) {
                            radioGroupMetal.check(R.id.radioButton_metalNeg);
                        } else if (Objects.equals(metal, RADIO_BUTTON_POSITIVE)) {
                            radioGroupMetal.check(R.id.radioButton_metalPos);
                        } else {
                            radioGroupMetal.clearCheck();
                        }

                        if (Objects.equals(nonMetal, RADIO_BUTTON_NEGATIVE)) {
                            radioGroupNonMetal.check(R.id.radioButton_nonMetalNeg);
                        } else if (Objects.equals(nonMetal, RADIO_BUTTON_POSITIVE)) {
                            radioGroupNonMetal.check(R.id.radioButton_nonMetalPos);
                        } else {
                            radioGroupNonMetal.clearCheck();
                        }

                        if (Objects.equals(metalCatcher, RADIO_BUTTON_NEGATIVE)) {
                            radioGroupMetalCathcer.check(R.id.radioButton_metalCatcherNeg);
                        } else if (Objects.equals(metalCatcher, RADIO_BUTTON_POSITIVE)) {
                            radioGroupMetalCathcer.check(R.id.radioButton_metalCathcerPos);
                        } else {
                            radioGroupMetalCathcer.clearCheck();
                        }

                        checkBoxMicro.setChecked(_mikro == 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void submitDataToFirebase() {

        if (pid != null) {
            Log.d(TAG, "233 pid: tidak null " + pid);
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pid));
            noCartonFb = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pid)).child(INT_QCI_NO_CARTON).child(noCarton);
        } else {
            Log.d(TAG, "pid: null 238 " + pidKey);
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey));
            noCartonFb = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey)).child(INT_QCI_NO_CARTON).child(noCarton);
        }

        //check mikro
        if (checkBoxMicro.isChecked()) {
            sampleMikro = 1;
        } else {
            sampleMikro = 0;
        }

        if (checkBoxKimia.isChecked()) {
            sampleKimia = 1;
        } else {
            sampleKimia = 0;
        }

        if (checkBoxRetain.isChecked()) {
            sampleRetain = 1;
        } else {
            sampleRetain = 0;
        }

        if (checkBoxThreeDays.isChecked()) {
            sampleThreeDays = 1;
        } else {
            sampleThreeDays = 0;
        }

        if (checkBoxInstrument.isChecked()) {
            sampleInstrument = 1;
        } else {
            sampleInstrument = 0;
        }

        if (checkBoxOrgano.isChecked()) {
            sampleOrgano = 1;
        } else {
            sampleOrgano = 0;
        }

        noCartonFb.child(INT_QCI_NO_CARTON).setValue(noCarton);
        noCartonFb.child(CHECK_QCI_MIKRO).setValue(sampleMikro);
        noCartonFb.child(CHECK_QCI_KIMIA).setValue(sampleKimia);
        noCartonFb.child(CHECK_QCI_RETAIN).setValue(sampleRetain);
        noCartonFb.child(CHECK_QCI_INSTRUMENT).setValue(sampleInstrument);
        noCartonFb.child(CHECK_QCI_THREE_DAYS).setValue(sampleThreeDays);
        noCartonFb.child(CHECK_QCI_ORGANO).setValue(sampleOrgano);

        noCartonFb.child(TXT_QCI_LEAKING).setValue(leakingValue);
        noCartonFb.child(TXT_QCI_CRACKING).setValue(crackingValue);
        noCartonFb.child(TXT_QCI_ORGANO).setValue(organoValue);
        noCartonFb.child(TXT_QCI_METAL).setValue(metalValue);
        noCartonFb.child(TXT_QCI_NON_METAL).setValue(nonMetalValue);
        noCartonFb.child(TXT_QCI_METAL_CATCHER).setValue(metalCatcherValue);

        noCartonFb.child(INT_QCI_HORIZONTAL_DEPAN).setValue(horizontalDepan);
        noCartonFb.child(INT_QCI_HORIZONTAL_BELAKANG).setValue(horizontalBelakang);
        noCartonFb.child(INT_QCI_VERTICAL_SEAL).setValue(vertikalSeal);
        noCartonFb.child(INT_QCI_OXYGEN).setValue(oxygen);
        noCartonFb.child(INT_QCI_PRESSURE).setValue(pressure);
        noCartonFb.child(INT_QCI_MEASURE).setValue(measure);
        noCartonFb.child(INT_QCI_PANJANG_SACHET).setValue(panjangSachet);
        noCartonFb.child(INT_QCI_KEGEMBUNGAN).setValue(kegembungan);
        noCartonFb.child(INT_QCI_BERAT_NETTO).setValue(beratNetto);
        noCartonFb.child(INT_QCI_TOTAL_SAMPLE).setValue(totalSample);
        noCartonFb.child(INT_QCI_JUMLAH_BONGKAR).setValue(jumlahBongkar);
        noCartonFb.child(TXT_QCI_ROOT_CAUSE).setValue(rooCause);
        noCartonFb.child(TXT_QCI_CORRECTIVE_ACTION).setValue(correctiveAction);
        noCartonFb.child(INT_QCI_JUMLAH_NG).setValue(jumlahNg);
        noCartonFb.child(HRS_QCI_JAM_SAMPLE).setValue(jamSample);
        noCartonFb.child(HRS_QCI_JAM_SAMPLE2).setValue(jamSample2);
    }
}