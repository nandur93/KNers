package com.ndu.sanghiang.kners.projecttrackerfi.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.ndu.sanghiang.kners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EvaluasiHasilFragment extends Fragment {

    //quality
    private TextInputEditText inputEditTextQualityBefore;
    private TextInputEditText inputEditTextQualityAfter;
    //cost
    private TextInputEditText inputEditTextCostBefore;
    private TextInputEditText inputEditTextCostAfter;
    //delivery
    private TextInputEditText inputEditTextDeliveryBefore;
    private TextInputEditText inputEditTextDeliveryAfter;
    //safety
    private TextInputEditText inputEditTextSafetyBefore;
    private TextInputEditText inputEditTextSafetyAfter;
    //moral
    private TextInputEditText inputEditTextMoralBefore;
    private TextInputEditText inputEditTextMoralAfter;
    //productivity
    private TextInputEditText inputEditTextProductivityBefore;
    private TextInputEditText inputEditTextProductivityAfter;
    //environment
    private TextInputEditText inputEditTextEnvironmentBefore;
    private TextInputEditText inputEditTextEnvironmentAfter;

    public EvaluasiHasilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluasi_hasil, container, false);

        inputEditTextQualityBefore = view.findViewById(R.id.evaluasi_quality_before);
        inputEditTextQualityAfter = view.findViewById(R.id.evaluasi_quality_after);

        inputEditTextCostBefore = view.findViewById(R.id.evaluasi_cost_before);
        inputEditTextCostAfter = view.findViewById(R.id.evaluasi_cost_after);

        inputEditTextDeliveryBefore = view.findViewById(R.id.evaluasi_delivery_before);
        inputEditTextDeliveryAfter = view.findViewById(R.id.evaluasi_delivery_after);

        inputEditTextSafetyBefore = view.findViewById(R.id.evaluasi_safety_before);
        inputEditTextSafetyAfter = view.findViewById(R.id.evaluasi_safety_after);

        inputEditTextMoralBefore = view.findViewById(R.id.evaluasi_moral_before);
        inputEditTextMoralAfter = view.findViewById(R.id.evaluasi_moral_after);

        inputEditTextProductivityBefore = view.findViewById(R.id.evaluasi_productivity_before);
        inputEditTextProductivityAfter = view.findViewById(R.id.evaluasi_productivity_after);

        inputEditTextEnvironmentBefore = view.findViewById(R.id.evaluasi_environment_before);
        inputEditTextEnvironmentAfter = view.findViewById(R.id.evaluasi_environment_after);

        return view;
    }

}
