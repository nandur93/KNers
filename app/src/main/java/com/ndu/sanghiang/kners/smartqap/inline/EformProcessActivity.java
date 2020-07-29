package com.ndu.sanghiang.kners.smartqap.inline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ndu.sanghiang.kners.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Objects;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.BIT_ACTIVE_DRAFT;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.DTM_QCP_EXPIRED_DATE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_BATCH_NUMB;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_BO;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_QUANTITY_FLUSHING;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.INT_QCP_TOTAL_CHARGES;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.PID;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QCP_EFORM;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_INLINE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.QC_PROCESS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.SMART_QAP;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCP_CHANGE_OVER;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCP_ITEM_CODE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCP_ITEM_DESC;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCP_LAST_PRODUCT;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCP_LINE;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.TXT_QCP_MATERIAL_FLUSHING;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.USERS;

public class EformProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Firebase";
    private ExpandableLayout expandableLayout0;
    private ExpandableLayout expandableLayout1;
    private DatabaseReference smartQapNodeRef;
    private TextInputEditText editTextLine;
    private TextInputEditText intBo;
    private TextInputEditText txtItemCode;
    private TextInputEditText txtItemDesc;
    private TextInputEditText intTotalCharges;
    private TextInputEditText txtBatchNumb;
    private TextInputEditText dtmExpiredDate;

    private TextInputEditText txtLastProduct;
    private TextInputEditText txtChangeOver;
    private TextInputEditText txtMaterialFlushing;
    private TextInputEditText txtQtyFlushing;
    private DatabaseReference qcProcess;
    private String pidKey;
    private String pid;
    private Switch switchWip;
    private int switchWipValue;
    private String editTextLineValue;
    private String intBoValue;
    private String txtItemCodeValue;
    private String txtItemDescValue;
    private String intTotalChargesValue;
    private String txtBatchNumbValue;
    private String dtmExpiredDateValue;
    private String txtLastProductValue;
    private String txtChangeOverValue;
    private String txtMaterialFlushingValue;
    private String txtQtyFlushingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eform_process);
        expandableLayout0 = findViewById(R.id.expandable_layout_product_id);
        expandableLayout1 = findViewById(R.id.expandable_layout_change_over);

        Button clearForm = findViewById(R.id.button_clear_form);
        Button proceed = findViewById(R.id.button_proceed);

        editTextLine = findViewById(R.id.edittext_line);
        intBo = findViewById(R.id.edittext_bo);
        txtItemCode = findViewById(R.id.edittext_item_code);
        txtItemDesc = findViewById(R.id.edittext_item_desc);
        intTotalCharges = findViewById(R.id.edittext_total_charges);
        txtBatchNumb = findViewById(R.id.edittext_batch_number);
        dtmExpiredDate = findViewById(R.id.edittext_expired_date);

        txtLastProduct = findViewById(R.id.edittext_last_product);
        txtChangeOver = findViewById(R.id.edittext_change_over);
        txtMaterialFlushing = findViewById(R.id.edittext_material_flushing);
        txtQtyFlushing = findViewById(R.id.edittext_qty_flushing);
        switchWip = findViewById(R.id.switchWipActive);

        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // get current userId
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        smartQapNodeRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(SMART_QAP);
        pidKey = smartQapNodeRef.push().getKey();

        expandableLayout0.setOnExpansionUpdateListener((expansionFraction, state) -> Log.d("ExpandableLayout0", "State: " + state));
        expandableLayout1.setOnExpansionUpdateListener((expansionFraction, state) -> Log.d("ExpandableLayout1", "State: " + state));
        expandableLayout1.setOnExpansionUpdateListener((expansionFraction, state) -> Log.d("ExpandableLayout2", "State: " + state));

        findViewById(R.id.expand_button_product_id).setOnClickListener(this);
        findViewById(R.id.expand_button_change_over).setOnClickListener(this);

        expandableLayout0.expand();
        expandableLayout1.collapse();

        proceed.setOnClickListener(view -> {
            Toast.makeText(this, "Proceed", Toast.LENGTH_SHORT).show();
            createDataProcess();
        });

        clearForm.setOnClickListener(view -> Toast.makeText(this, "Clear Form", Toast.LENGTH_SHORT).show());

        // Data dari Wip
        pid = getIntent().getStringExtra(PID);
        Log.d(TAG, "onCreate: pid from wip " + pid);

        if (pid != null) {
            loadDataWip();
            Log.d(TAG, "133 pid: tidak null " + pid);
        } else {
            Log.d(TAG, "pid: null 135");
            loadBlankData();
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey));
        }

        smartQapNodeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        switchWip.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (buttonView, isChecked) -> {
            // do something, the isChecked will be
            // true if the switch is in the On position
            if (isChecked) {
                Log.d(TAG, "onCreate: Checked true");
                switchWipValue = 1;
            } else {
                Log.d(TAG, "onCreate: Checked false");
                switchWipValue = 0;
            }
        });
    }

    private void loadBlankData() {
        Log.d(TAG, "pid: No Data");
    }

    private void loadDataWip() {
        smartQapNodeRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot snapEform = snapshot.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(pid);
                        Log.d(TAG, "191 pid snapEform: " + snapEform.getValue());
                        String firebasePidKey = (String) snapEform.child(PID).getValue();
                        String firebaseLine = (String) snapEform.child(TXT_QCP_LINE).getValue();
                        String firebaseBo = (String) snapEform.child(INT_QCP_BO).getValue();
                        String firebaseItemCode = (String) snapEform.child(TXT_QCP_ITEM_CODE).getValue();
                        String firebaseItemDesc = (String) snapEform.child(TXT_QCP_ITEM_DESC).getValue();
                        String firebaseTotalCharges = (String) snapEform.child(INT_QCP_TOTAL_CHARGES).getValue();
                        String firebaseBatchNumber = (String) snapEform.child(INT_QCP_BATCH_NUMB).getValue();
                        String firebaseExpiredDate = (String) snapEform.child(DTM_QCP_EXPIRED_DATE).getValue();

                        Log.d(TAG, "201 pid: " + firebasePidKey);

                        long firebaseBitDraft = (long) snapEform.child(BIT_ACTIVE_DRAFT).getValue();
                        for (DataSnapshot zoneSnapshot : snapEform.getChildren()) {
                            Log.i(TAG, String.valueOf(zoneSnapshot.child(PID).getValue()));
                        }

                        editTextLine.setText(firebaseLine);
                        intBo.setText(firebaseBo);
                        txtItemCode.setText(firebaseItemCode);
                        txtItemDesc.setText(firebaseItemDesc);
                        intTotalCharges.setText(firebaseTotalCharges);
                        txtBatchNumb.setText(firebaseBatchNumber);
                        dtmExpiredDate.setText(firebaseExpiredDate);

                        if (firebaseBitDraft == 1) {
                            switchWip.setChecked(true);
                        } else {
                            switchWip.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void createDataProcess() {

        //Nodenya adalah smart_qap -> qc_inline -> qc_process -> eform -> pid -> line

        //membuat data template node
        //metadata
        editTextLineValue = Objects.requireNonNull(editTextLine.getText()).toString();
        Log.d(TAG, "createDataProcess: " + editTextLineValue);
        intBoValue = Objects.requireNonNull(intBo.getText()).toString();
        txtItemCodeValue = Objects.requireNonNull(txtItemCode.getText()).toString();
        txtItemDescValue = Objects.requireNonNull(txtItemDesc.getText()).toString();
        intTotalChargesValue = Objects.requireNonNull(intTotalCharges.getText()).toString();
        txtBatchNumbValue = Objects.requireNonNull(txtBatchNumb.getText()).toString();
        dtmExpiredDateValue = Objects.requireNonNull(dtmExpiredDate.getText()).toString();

        txtLastProductValue = Objects.requireNonNull(txtLastProduct.getText()).toString();
        txtChangeOverValue = Objects.requireNonNull(txtChangeOver.getText()).toString();
        txtMaterialFlushingValue = Objects.requireNonNull(txtMaterialFlushing.getText()).toString();
        txtQtyFlushingValue = Objects.requireNonNull(txtQtyFlushing.getText()).toString();


        if (pid != null) {
            Log.d(TAG, "253 pid: tidak null " + pid);
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pid));
            goToLine(pid);
        } else {
            Log.d(TAG, "pid: null 257");
            //load data from latest
            qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey));
            goToLine(pidKey);
        }

        qcProcess.child(TXT_QCP_LINE).setValue(editTextLineValue);
        qcProcess.child(INT_QCP_BO).setValue(intBoValue);
        qcProcess.child(TXT_QCP_ITEM_CODE).setValue(txtItemCodeValue);
        qcProcess.child(TXT_QCP_ITEM_DESC).setValue(txtItemDescValue);
        qcProcess.child(INT_QCP_TOTAL_CHARGES).setValue(intTotalChargesValue);
        qcProcess.child(INT_QCP_BATCH_NUMB).setValue(txtBatchNumbValue);
        qcProcess.child(DTM_QCP_EXPIRED_DATE).setValue(dtmExpiredDateValue);

        qcProcess.child(TXT_QCP_LAST_PRODUCT).setValue(txtLastProductValue);
        qcProcess.child(TXT_QCP_CHANGE_OVER).setValue(txtChangeOverValue);
        qcProcess.child(TXT_QCP_MATERIAL_FLUSHING).setValue(txtMaterialFlushingValue);
        qcProcess.child(INT_QCP_QUANTITY_FLUSHING).setValue(txtQtyFlushingValue);

        qcProcess.child(BIT_ACTIVE_DRAFT).setValue(switchWipValue);

    }

    private void goToLine(String key) {
        Intent intent = new
                Intent(this, EformLineActivity.class);
        intent.putExtra(PID, key);
        intent.putExtra(INT_QCP_BO, intBoValue + "\n" +
                txtItemCodeValue + " - " +
                txtItemDescValue + "\n" +
                txtBatchNumbValue + " / " +
                dtmExpiredDateValue + "\n" +
                intTotalChargesValue + " Charges");
        intent.putExtra(TXT_QCP_LINE, editTextLineValue);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expand_button_product_id:
                expandableLayout0.expand();
                expandableLayout1.collapse();
                break;
            case R.id.expand_button_change_over:
                expandableLayout1.expand();
                expandableLayout0.collapse();
                break;
            default:
                expandableLayout0.collapse();
                expandableLayout1.collapse();
                break;
        }
    }
}