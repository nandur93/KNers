package com.ndu.sanghiang.kners.projecttrackerfi.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ndu.sanghiang.kners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StandarisasiFragment extends Fragment {


    private TextView textViewStandarisasiStatus;

    public StandarisasiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standarisasi, container, false);
        textViewStandarisasiStatus = view.findViewById(R.id.standarisasi_status);

        //standarisasi status
        textViewStandarisasiStatus.setText("");
        textViewStandarisasiStatus.setOnClickListener(v -> {
            if (textViewStandarisasiStatus.getText().toString().equals("")){
                textViewStandarisasiStatus.setText("NOK");
                textViewStandarisasiStatus.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewStandarisasiStatus.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewStandarisasiStatus.getText().toString().equals("NOK")){
                textViewStandarisasiStatus.setText("OK");
                textViewStandarisasiStatus.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewStandarisasiStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewStandarisasiStatus.getText().toString().equals("OK")){
                textViewStandarisasiStatus.setText("");
                textViewStandarisasiStatus.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewStandarisasiStatus.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });
        
        return view;
    }

}
