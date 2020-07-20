package com.ndu.sanghiang.kners.projecttrackerfi.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.ndu.sanghiang.kners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PenanggulanganFragment extends Fragment {


    private TextInputEditText inputEditTextPenanggulangan1;
    private TextInputEditText inputEditTextPenanggulangan2;
    private TextInputEditText inputEditTextPenanggulangan3;
    private TextInputEditText inputEditTextPenanggulangan4;
    private TextInputEditText inputEditTextPenanggulangan5;

    private TextInputEditText inputEditTextPenanggulanganProblem1;
    private TextInputEditText inputEditTextPenanggulanganProblem2;
    private TextInputEditText inputEditTextPenanggulanganProblem3;
    private TextInputEditText inputEditTextPenanggulanganProblem4;
    private TextInputEditText inputEditTextPenanggulanganProblem5;

    private TextInputEditText inputEditTextPenanggulanganDate1;
    private TextInputEditText inputEditTextPenanggulanganDate2;
    private TextInputEditText inputEditTextPenanggulanganDate3;
    private TextInputEditText inputEditTextPenanggulanganDate4;
    private TextInputEditText inputEditTextPenanggulanganDate5;

    private TextInputEditText inputEditTextPenanggulanganPic1;
    private TextInputEditText inputEditTextPenanggulanganPic2;
    private TextInputEditText inputEditTextPenanggulanganPic3;
    private TextInputEditText inputEditTextPenanggulanganPic4;
    private TextInputEditText inputEditTextPenanggulanganPic5;

    private TextView textViewPenanggulanganProgress1;
    private TextView textViewPenanggulanganProgress2;
    private TextView textViewPenanggulanganProgress3;
    private TextView textViewPenanggulanganProgress4;
    private TextView textViewPenanggulanganProgress5;

    public PenanggulanganFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_penanggulangan, container, false);

        inputEditTextPenanggulangan1 = view.findViewById(R.id.penanggulangan1);
        inputEditTextPenanggulangan2 = view.findViewById(R.id.penanggulangan2);
        inputEditTextPenanggulangan3 = view.findViewById(R.id.penanggulangan3);
        inputEditTextPenanggulangan4 = view.findViewById(R.id.penanggulangan4);
        inputEditTextPenanggulangan5 = view.findViewById(R.id.penanggulangan5);
        
        inputEditTextPenanggulanganProblem1 = view.findViewById(R.id.penanggulangan_problem1);
        inputEditTextPenanggulanganProblem2 = view.findViewById(R.id.penanggulangan_problem2);
        inputEditTextPenanggulanganProblem3 = view.findViewById(R.id.penanggulangan_problem3);
        inputEditTextPenanggulanganProblem4 = view.findViewById(R.id.penanggulangan_problem4);
        inputEditTextPenanggulanganProblem5 = view.findViewById(R.id.penanggulangan_problem5);
        
        inputEditTextPenanggulanganDate1 = view.findViewById(R.id.penanggulangan_date1);
        inputEditTextPenanggulanganDate2 = view.findViewById(R.id.penanggulangan_date2);
        inputEditTextPenanggulanganDate3 = view.findViewById(R.id.penanggulangan_date3);
        inputEditTextPenanggulanganDate4 = view.findViewById(R.id.penanggulangan_date4);
        inputEditTextPenanggulanganDate5 = view.findViewById(R.id.penanggulangan_date5);

        inputEditTextPenanggulanganPic1 = view.findViewById(R.id.penanggulangan_pic1);
        inputEditTextPenanggulanganPic2 = view.findViewById(R.id.penanggulangan_pic2);
        inputEditTextPenanggulanganPic3 = view.findViewById(R.id.penanggulangan_pic3);
        inputEditTextPenanggulanganPic4 = view.findViewById(R.id.penanggulangan_pic4);
        inputEditTextPenanggulanganPic5 = view.findViewById(R.id.penanggulangan_pic5);

        textViewPenanggulanganProgress1 = view.findViewById(R.id.progress1);
        textViewPenanggulanganProgress2 = view.findViewById(R.id.progress2);
        textViewPenanggulanganProgress3 = view.findViewById(R.id.progress3);
        textViewPenanggulanganProgress4 = view.findViewById(R.id.progress4);
        textViewPenanggulanganProgress5 = view.findViewById(R.id.progress5);

        //material status
        textViewPenanggulanganProgress1.setText("");
        textViewPenanggulanganProgress1.setOnClickListener(v -> {
            if (textViewPenanggulanganProgress1.getText().toString().equals("")){
                textViewPenanggulanganProgress1.setText("In Progress");
                textViewPenanggulanganProgress1.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewPenanggulanganProgress1.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewPenanggulanganProgress1.getText().toString().equals("In Progress")){
                textViewPenanggulanganProgress1.setText("Done");
                textViewPenanggulanganProgress1.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewPenanggulanganProgress1.getText().toString().equals("Done")){
                textViewPenanggulanganProgress1.setText("");
                textViewPenanggulanganProgress1.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress1.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });

        textViewPenanggulanganProgress2.setText("");
        textViewPenanggulanganProgress2.setOnClickListener(v -> {
            if (textViewPenanggulanganProgress2.getText().toString().equals("")){
                textViewPenanggulanganProgress2.setText("In Progress");
                textViewPenanggulanganProgress2.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewPenanggulanganProgress2.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewPenanggulanganProgress2.getText().toString().equals("In Progress")){
                textViewPenanggulanganProgress2.setText("Done");
                textViewPenanggulanganProgress2.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewPenanggulanganProgress2.getText().toString().equals("Done")){
                textViewPenanggulanganProgress2.setText("");
                textViewPenanggulanganProgress2.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress2.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });

        textViewPenanggulanganProgress3.setText("");
        textViewPenanggulanganProgress3.setOnClickListener(v -> {
            if (textViewPenanggulanganProgress3.getText().toString().equals("")){
                textViewPenanggulanganProgress3.setText("In Progress");
                textViewPenanggulanganProgress3.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewPenanggulanganProgress3.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewPenanggulanganProgress3.getText().toString().equals("In Progress")){
                textViewPenanggulanganProgress3.setText("Done");
                textViewPenanggulanganProgress3.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewPenanggulanganProgress3.getText().toString().equals("Done")){
                textViewPenanggulanganProgress3.setText("");
                textViewPenanggulanganProgress3.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress3.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });

        textViewPenanggulanganProgress4.setText("");
        textViewPenanggulanganProgress4.setOnClickListener(v -> {
            if (textViewPenanggulanganProgress4.getText().toString().equals("")){
                textViewPenanggulanganProgress4.setText("In Progress");
                textViewPenanggulanganProgress4.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewPenanggulanganProgress4.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewPenanggulanganProgress4.getText().toString().equals("In Progress")){
                textViewPenanggulanganProgress4.setText("Done");
                textViewPenanggulanganProgress4.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewPenanggulanganProgress4.getText().toString().equals("Done")){
                textViewPenanggulanganProgress4.setText("");
                textViewPenanggulanganProgress4.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress4.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });

        textViewPenanggulanganProgress5.setText("");
        textViewPenanggulanganProgress5.setOnClickListener(v -> {
            if (textViewPenanggulanganProgress5.getText().toString().equals("")){
                textViewPenanggulanganProgress5.setText("In Progress");
                textViewPenanggulanganProgress5.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewPenanggulanganProgress5.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewPenanggulanganProgress5.getText().toString().equals("In Progress")){
                textViewPenanggulanganProgress5.setText("Done");
                textViewPenanggulanganProgress5.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewPenanggulanganProgress5.getText().toString().equals("Done")){
                textViewPenanggulanganProgress5.setText("");
                textViewPenanggulanganProgress5.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewPenanggulanganProgress5.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });
        
        return view;
    }

}
