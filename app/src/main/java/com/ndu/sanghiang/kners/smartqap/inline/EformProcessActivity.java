package com.ndu.sanghiang.kners.smartqap.inline;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ndu.sanghiang.kners.R;
import com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.DatabaseHelper;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.ndu.sanghiang.kners.NandurLibs.nduDialog;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.BIT_ACTIVE_DRAFT;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.BIT_ACTIVE_DRAFT_PACKING;
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
import static com.ndu.sanghiang.kners.smartqap.inline.QcInlineFragment.LIST_FROM_KEY;
import static com.ndu.sanghiang.kners.smartqap.inline.QcInlineFragment.PROCESS;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_BARCODE;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_CAP_BOX;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_CAP_RASA;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_GRAMASI_PERPCS;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_IS_KWG;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_ITEM_CODE;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_ITEM_DESC;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_JUMLAH_DOOS_PERBOX;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_JUMLAH_PCS_PERBOX;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_KET;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_KODE_PABRIK;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_KODE_SACHET;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_LOKAL_EXPORT;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_SINGKATAN;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_TANGGAL_PIC_PENGUPDATE;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_UMUR_PRODUK;

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
    private Drawable dialogIcon;
    private DatabaseHelper db;

    @SuppressLint("UseCompatLoadingForDrawables")
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
        dialogIcon = getResources().getDrawable(R.drawable.ic_info_outline_black_24dp);

        txtLastProduct = findViewById(R.id.edittext_last_product);
        txtChangeOver = findViewById(R.id.edittext_change_over);
        txtMaterialFlushing = findViewById(R.id.edittext_material_flushing);
        txtQtyFlushing = findViewById(R.id.edittext_qty_flushing);
        switchWip = findViewById(R.id.switchWipActive);

        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        // Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // get current userId
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        smartQapNodeRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(userId).child(SMART_QAP);
        pidKey = smartQapNodeRef.push().getKey();

        /*Storage permission*/
        runDexter();
        /*Create Asset table in Asset.db*/
        try {
            db.createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        txtItemCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Cursor rs = db.getData(txtItemCode.getText().toString().trim().toUpperCase());
                    rs.moveToFirst();

                    String itemDesc = rs.getString(rs.getColumnIndex(COLUMN_ITEM_DESC));
                    Log.d(TAG, "onCreate: " + itemDesc);
                    txtItemDesc.setText(itemDesc);

                    if (!rs.isClosed()) {
                        rs.close();
                    }
                } catch (Exception e) {
                    txtItemDesc.setText("");
                    e.printStackTrace();
                }
            }
        });

        clearForm.setOnClickListener(view -> {
            Toast.makeText(this, "Clear Form", Toast.LENGTH_SHORT).show();
            nduDialog(this,
                    getResources().getString(R.string.update_item_code) + "?",
                    "Ini akan merefresh list dan semua data scan yang belum terimport akan hilang!",
                    true,
                    dialogIcon,
                    "Yes",
                    "Cancel",
                    (DialogInterface dialog, int which) -> {
                        if (which == BUTTON_POSITIVE) {
                            dialog.cancel();
                            openFilePicker();
                            //progressDialog.dismiss();
                        }
                        dialog.cancel();
                    });
        });

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

        switchWip.setOnCheckedChangeListener((buttonView, isChecked) -> {
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

    private void openFilePicker() {
//        Intent intentfile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intentfile.addCategory(Intent.CATEGORY_OPENABLE);
//        intentfile.setType("text/xml");
//        //https://stackoverflow.com/questions/35915602/selecting-a-specific-type-of-file-in-android
//        startActivityForResult(intentfile, PICKFILE_RESULT_CODE);

        if (permissionGranted()) {
//            SingleFilePickerDialog singleFilePickerDialog = new SingleFilePickerDialog(this,
//                    () -> Toast.makeText(EformProcessActivity.this, "Canceled!!", Toast.LENGTH_SHORT).show(),
//                    files -> {
//                        Toast.makeText(EformProcessActivity.this, files[0].getPath(), Toast.LENGTH_SHORT).show();
////                        editor.putString(XML_PATH, files[0].getPath());
////                        editor.apply();
//
//                        pullDataAsyncTask task = new pullDataAsyncTask();
//                        deleteAssetDatabase();
//                        task.execute();
//                    });
//            singleFilePickerDialog.show();
            pullDataAsyncTask task = new pullDataAsyncTask();
            deleteAssetDatabase();
            task.execute();
        } else {
            requestPermission();
        }
    }

    private void deleteAssetDatabase() {
        Log.d(TAG, "deleteAssetDatabase: true");
        db.dropTable();
    }

    private boolean permissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    private void runDexter() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(EformProcessActivity.this, "Storage Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(EformProcessActivity.this, "Storage Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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

                        String firebaseLastProduct = (String) snapEform.child(TXT_QCP_LAST_PRODUCT).getValue();
                        String firebaseChangeOver = (String) snapEform.child(TXT_QCP_CHANGE_OVER).getValue();
                        String firebaseMaterialFlushing = (String) snapEform.child(TXT_QCP_MATERIAL_FLUSHING).getValue();
                        String firebaseQtyFlushing = (String) snapEform.child(INT_QCP_QUANTITY_FLUSHING).getValue();


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
                        txtLastProduct.setText(firebaseLastProduct);
                        txtChangeOver.setText(firebaseChangeOver);
                        txtMaterialFlushing.setText(firebaseMaterialFlushing);
                        txtQtyFlushing.setText(firebaseQtyFlushing);

                        switchWip.setChecked(firebaseBitDraft == 1);
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
        qcProcess.child(BIT_ACTIVE_DRAFT_PACKING).setValue(1);

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
        intent.putExtra(INT_QCP_TOTAL_CHARGES, intTotalChargesValue);
        intent.putExtra(LIST_FROM_KEY, PROCESS);
        startActivity(intent);
    }

    /**
     * sub-class of AsyncTask
     */
    @SuppressLint("StaticFieldLeak")
    protected class pullDataAsyncTask extends AsyncTask<Context, Integer, String> {
        /*https://stackoverflow.com/questions/6450275/android-how-to-work-with-asynctasks-progressdialog*/
        private final ProgressDialog dialog = new ProgressDialog(EformProcessActivity.this);
        private int totalAsset = 0;

        // -- run intensive processes here
        // -- notice that the datatype of the first param in the class definition matches the param passed to this
        // method
        // -- and that the datatype of the last param in the class definition matches the return type of this method
        @Override
        protected String doInBackground(Context... params) {
            // -- on every iteration
            // -- runs a while loop that causes the thread to sleep for 50 milliseconds
            // -- publishes the progress - calls the onProgressUpdate handler defined below
            // -- and increments the counter variable i by one

            /*https://www.tutlane.com/tutorial/android/android-xml-parsing-using-sax-parser*/
            /*https://stackoverflow.com/questions/15967896/how-to-parse-xml-file-from-sdcard-in-android*/
            try {
                @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                /*Input from android asset folder*/
                //InputStream istream = getAssets().open("userdetails.xml");

                /*Input from mnt/sdcard*/
                //String pathFile = preferences.getString(XML_PATH, "mnt/sdcard/Asset/Asset.xml");
                File file = new File("mnt/sdcard/Smart QAP/List Products.xml");
                //File file = new File(Objects.requireNonNull(pathFile));
                InputStream inputStreamSd = new FileInputStream(file.getPath());
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputStreamSd);
                NodeList nList = doc.getElementsByTagName("product");
                HashMap<String, String> hashMap;
                totalAsset = nList.getLength();

                for (int i = 0; i < nList.getLength(); i++) {
                    if (nList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                        hashMap = new HashMap<>();
                        Element elm = (Element) nList.item(i);
                        hashMap.put(COLUMN_ITEM_CODE, getNodeValue(COLUMN_ITEM_CODE, elm));
                        hashMap.put(COLUMN_ITEM_DESC, getNodeValue(COLUMN_ITEM_DESC, elm));
                        hashMap.put(COLUMN_SINGKATAN, getNodeValue(COLUMN_SINGKATAN, elm));
                        hashMap.put(COLUMN_KODE_SACHET, getNodeValue(COLUMN_KODE_SACHET, elm));
                        hashMap.put(COLUMN_CAP_RASA, getNodeValue(COLUMN_CAP_RASA, elm));
                        hashMap.put(COLUMN_CAP_BOX, getNodeValue(COLUMN_CAP_BOX, elm));
                        hashMap.put(COLUMN_IS_KWG, getNodeValue(COLUMN_IS_KWG, elm));
                        hashMap.put(COLUMN_UMUR_PRODUK, getNodeValue(COLUMN_UMUR_PRODUK, elm));
                        hashMap.put(COLUMN_KODE_PABRIK, getNodeValue(COLUMN_KODE_PABRIK, elm));
                        hashMap.put(COLUMN_KET, getNodeValue(COLUMN_KET, elm));
                        hashMap.put(COLUMN_LOKAL_EXPORT, getNodeValue(COLUMN_LOKAL_EXPORT, elm));
                        hashMap.put(COLUMN_GRAMASI_PERPCS, getNodeValue(COLUMN_GRAMASI_PERPCS, elm));
                        hashMap.put(COLUMN_JUMLAH_DOOS_PERBOX, getNodeValue(COLUMN_JUMLAH_DOOS_PERBOX, elm));
                        hashMap.put(COLUMN_JUMLAH_PCS_PERBOX, getNodeValue(COLUMN_JUMLAH_PCS_PERBOX, elm));
                        hashMap.put(COLUMN_TANGGAL_PIC_PENGUPDATE, getNodeValue(COLUMN_TANGGAL_PIC_PENGUPDATE, elm));
                        hashMap.put(COLUMN_BARCODE, getNodeValue(COLUMN_BARCODE, elm));

                        arrayList.add(hashMap);
                        //scan get position
                        if (db.checkIsItemCodeInDB(hashMap.put(COLUMN_ITEM_CODE, getNodeValue(COLUMN_ITEM_CODE, elm)))) {
                            Log.d(TAG, "onReceive: Exist");
                            Log.d(TAG, "loadAssetList: ");
                        } else {
                            // Inserting record
                            Log.d(TAG, "onReceive: Data No Exist" + i);
                            db.inputDataFromDom(Objects.requireNonNull(hashMap));
                            publishProgress(i);
                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                return "No File";
                //displayExceptionMessage(e.getMessage());
            }
            return "COMPLETE!";
        }

        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute()");
            super.onPreExecute();
            this.dialog.setMessage("Importing data item code...");
            this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.dialog.setCancelable(false);
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.show();
        }

        // -- called from the publish progress
        // -- notice that the datatype of the second param gets passed to this method
        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                double valuesDb = Double.parseDouble(String.valueOf((values[0])));
                double totalAssetDb = Double.parseDouble(String.valueOf(totalAsset));
                double percenTage = (valuesDb / totalAssetDb) * 100;
                Log.d(TAG, "onCreate: " + percenTage);
                BigDecimal bd = new BigDecimal(percenTage).setScale(2, RoundingMode.HALF_EVEN);
                bd.doubleValue();
//                this.dialog.setMessage("Importing data asset " + (values[0]) + "/" + totalAsset + " (" + bd + "%)");
                this.dialog.setMessage("Importing data asset, Please wait!");
                this.dialog.setMax(totalAsset);
                this.dialog.setProgress((values[0]));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // -- called if the cancel button is pressed
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "onCancelled()");
            this.dialog.setMessage("Cancelled!");
        }

        // -- called as soon as doInBackground method completes
        // -- notice that the third param gets passed to this method
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "onPostExecute(): " + result);
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result.equals("COMPLETE!")) {
                this.dialog.setMessage(result);
                Toast.makeText(EformProcessActivity.this, "Import complete", Toast.LENGTH_SHORT).show();
            } else if (result.equals("No File")) {
                Toast.makeText(EformProcessActivity.this, "No Asset.xml file in the directory", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EformProcessActivity.this, "Import failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    protected String getNodeValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        Node node = nodeList.item(0);
        if (node != null) {
            if (node.hasChildNodes()) {
                Node child = node.getFirstChild();
                while (child != null) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    @SuppressLint("NonConstantResourceId")
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