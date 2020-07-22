package com.ndu.sanghiang.kners.smartqap.inline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ndu.sanghiang.kners.R;
import net.cachapa.expandablelayout.ExpandableLayout;
import java.util.Objects;

public class EformProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandableLayout expandableLayout0;
    private ExpandableLayout expandableLayout1;
    private ExpandableLayout expandableLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eform_process);
        expandableLayout0 = findViewById(R.id.expandable_layout_product_id);
        expandableLayout1 = findViewById(R.id.expandable_layout_change_over);
        expandableLayout2 = findViewById(R.id.expandable_layout_submit);

        Button clearForm = findViewById(R.id.button_clear_form);
        Button proceed = findViewById(R.id.button_proceed);

        Toolbar tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        //Menampilkan panah Back ←
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        expandableLayout0.setOnExpansionUpdateListener((expansionFraction, state) -> Log.d("ExpandableLayout0", "State: " + state));
        expandableLayout1.setOnExpansionUpdateListener((expansionFraction, state) -> Log.d("ExpandableLayout1", "State: " + state));
        expandableLayout1.setOnExpansionUpdateListener((expansionFraction, state) -> Log.d("ExpandableLayout2", "State: " + state));

        findViewById(R.id.expand_button_product_id).setOnClickListener(this);
        findViewById(R.id.expand_button_change_over).setOnClickListener(this);
        findViewById(R.id.expand_button_submit).setOnClickListener(this);

        expandableLayout0.expand();
        expandableLayout1.collapse();
        expandableLayout2.collapse();

        proceed.setOnClickListener(view -> {
            Toast.makeText(this, "Proceed", Toast.LENGTH_SHORT).show();
            goToEformLine();
        });
        clearForm.setOnClickListener(view -> Toast.makeText(this, "Clear Form", Toast.LENGTH_SHORT).show());
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
                expandableLayout2.collapse();
                break;
            case R.id.expand_button_change_over:
                expandableLayout1.expand();
                expandableLayout0.collapse();
                expandableLayout2.collapse();
                break;
            case R.id.expand_button_submit:
                expandableLayout2.expand();
                expandableLayout1.expand();
                expandableLayout0.expand();
                break;
            default:
                expandableLayout0.collapse();
                expandableLayout1.collapse();
                expandableLayout2.collapse();
                break;
        }
    }
}