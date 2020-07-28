package com.ndu.sanghiang.kners.smartqap.inline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ndu.sanghiang.kners.R;

import net.cachapa.expandablelayout.ExpandableLayout;

public class QcInlineFragment extends Fragment implements View.OnClickListener {

    private ExpandableLayout expandableLayout0;
    private ExpandableLayout expandableLayout1;
    private ExpandableLayout expandableLayout2;

    private Button createNew;
    private Button continueLast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qc_inline, container, false);

        expandableLayout0 = rootView.findViewById(R.id.expandable_layout_0);
        expandableLayout1 = rootView.findViewById(R.id.expandable_layout_1);
        expandableLayout2 = rootView.findViewById(R.id.expandable_layout_2);

        createNew = rootView.findViewById(R.id.button_create_new);
        continueLast = rootView.findViewById(R.id.button_continue_last);

        expandableLayout0.setOnExpansionUpdateListener((expansionFraction, state) -> Log.d("ExpandableLayout0", "State: " + state));
        expandableLayout1.setOnExpansionUpdateListener((expansionFraction, state) -> Log.d("ExpandableLayout1", "State: " + state));
        expandableLayout1.setOnExpansionUpdateListener((expansionFraction, state) -> Log.d("ExpandableLayout2", "State: " + state));

        rootView.findViewById(R.id.expand_button_0).setOnClickListener(this);
        rootView.findViewById(R.id.expand_button_1).setOnClickListener(this);
        rootView.findViewById(R.id.expand_button_2).setOnClickListener(this);

        expandableLayout0.expand();
        expandableLayout1.collapse();
        expandableLayout2.collapse();

        createNew.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Create New", Toast.LENGTH_SHORT).show();
            goToEform();
        });
        continueLast.setOnClickListener(view -> {
            goToLastRecord();
            Toast.makeText(getContext(), "Continue Last", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    private void goToEform() {
        Intent goToEform = new
                Intent(getActivity(), EformProcessActivity.class);
        startActivity(goToEform);
    }

    private void goToLastRecord() {
        Intent goToLastRecord = new
                Intent(getActivity(), LastSavedActivity.class);
        startActivity(goToLastRecord);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expand_button_0:
                expandableLayout0.expand();
                expandableLayout1.collapse();
                expandableLayout2.collapse();
                break;
            case R.id.expand_button_1:
                expandableLayout1.expand();
                expandableLayout0.collapse();
                expandableLayout2.collapse();
                break;
            case R.id.expand_button_2:
                expandableLayout2.expand();
                expandableLayout1.collapse();
                expandableLayout0.collapse();
                break;
            default:
                expandableLayout0.collapse();
                expandableLayout1.collapse();
                expandableLayout2.collapse();
                break;
        }
    }
}