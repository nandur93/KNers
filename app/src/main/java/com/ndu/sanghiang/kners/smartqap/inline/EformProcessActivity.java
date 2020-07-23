package com.ndu.sanghiang.kners.smartqap.inline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.ndu.sanghiang.kners.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Objects;

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
    private TextInputEditText txtLine;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eform_process);
        expandableLayout0 = findViewById(R.id.expandable_layout_product_id);
        expandableLayout1 = findViewById(R.id.expandable_layout_change_over);

        Button clearForm = findViewById(R.id.button_clear_form);
        Button proceed = findViewById(R.id.button_proceed);

        txtLine = findViewById(R.id.edittext_line);
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

        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // get current userId
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        smartQapNodeRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(SMART_QAP);

        Log.d(TAG, "onCreate: " + userId);

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
    }

    private void createDataProcess() {
        //membuat data template node
        String pidKey = smartQapNodeRef.push().getKey();
        //metadata
        String txtLineValue = Objects.requireNonNull(txtLine.getText()).toString();
        String intBoValue = Objects.requireNonNull(intBo.getText()).toString();
        String txtItemCodeValue = Objects.requireNonNull(txtItemCode.getText()).toString();
        String txtItemDescValue = Objects.requireNonNull(txtItemDesc.getText()).toString();
        String intTotalChargesValue = Objects.requireNonNull(intTotalCharges.getText()).toString();
        String txtBatchNumbValue = Objects.requireNonNull(txtBatchNumb.getText()).toString();
        String dtmExpiredDateValue = Objects.requireNonNull(dtmExpiredDate.getText()).toString();

        String txtLastProductValue = Objects.requireNonNull(txtLastProduct.getText()).toString();
        String txtChangeOverValue = Objects.requireNonNull(txtChangeOver.getText()).toString();
        String txtMaterialFlushingValue = Objects.requireNonNull(txtMaterialFlushing.getText()).toString();
        String txtQtyFlushingValue = Objects.requireNonNull(txtQtyFlushing.getText()).toString();

        //Nodenya adalah smart_qap -> qc_inline -> qc_process -> eform -> pid -> line
        DatabaseReference qcProcess = smartQapNodeRef.child(QC_INLINE).child(QC_PROCESS).child(QCP_EFORM).child(Objects.requireNonNull(pidKey));
        qcProcess.child(PID).setValue(pidKey);
        qcProcess.child(TXT_QCP_LINE).setValue(txtLineValue);
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
    }

    private void goToEformLine() {
        Intent goToEformLine = new
                Intent(this, EformLineActivity.class);
        startActivity(goToEformLine);
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