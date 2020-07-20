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
public class RencanaFragment extends Fragment {


    public static TextInputEditText inputEditTextRencana1;
    public static TextInputEditText inputEditTextRencana2;
    public static TextInputEditText inputEditTextRencana3;
    public static TextInputEditText inputEditTextRencana4;
    public static TextInputEditText inputEditTextRencana5;
    
    private TextView textViewRencanaProgress1;
    private TextView textViewRencanaProgress2;
    private TextView textViewRencanaProgress3;
    private TextView textViewRencanaProgress4;
    private TextView textViewRencanaProgress5;

    public RencanaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rencana, container, false);
        
        inputEditTextRencana1 = view.findViewById(R.id.edittext_akar_penyebab1);
        inputEditTextRencana2 = view.findViewById(R.id.edittext_akar_penyebab2);
        inputEditTextRencana3 = view.findViewById(R.id.edittext_akar_penyebab3);
        inputEditTextRencana4 = view.findViewById(R.id.edittext_akar_penyebab4);
        inputEditTextRencana5 = view.findViewById(R.id.edittext_akar_penyebab5);
        
        textViewRencanaProgress1 = view.findViewById(R.id.textview_progress_penyebab1);
        textViewRencanaProgress2 = view.findViewById(R.id.textview_progress_penyebab2);
        textViewRencanaProgress3 = view.findViewById(R.id.textview_progress_penyebab3);
        textViewRencanaProgress4 = view.findViewById(R.id.textview_progress_penyebab4);
        textViewRencanaProgress5 = view.findViewById(R.id.textview_progress_penyebab5);

        //material status
        textViewRencanaProgress1.setText("");
        textViewRencanaProgress1.setOnClickListener(v -> {
            if (textViewRencanaProgress1.getText().toString().equals("")){
                textViewRencanaProgress1.setText("In Progress");
                textViewRencanaProgress1.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewRencanaProgress1.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewRencanaProgress1.getText().toString().equals("In Progress")){
                textViewRencanaProgress1.setText("Done");
                textViewRencanaProgress1.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewRencanaProgress1.getText().toString().equals("Done")){
                textViewRencanaProgress1.setText("");
                textViewRencanaProgress1.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress1.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });

        //material status
        textViewRencanaProgress2.setText("");
        textViewRencanaProgress2.setOnClickListener(v -> {
            if (textViewRencanaProgress2.getText().toString().equals("")){
                textViewRencanaProgress2.setText("In Progress");
                textViewRencanaProgress2.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewRencanaProgress2.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewRencanaProgress2.getText().toString().equals("In Progress")){
                textViewRencanaProgress2.setText("Done");
                textViewRencanaProgress2.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewRencanaProgress2.getText().toString().equals("Done")){
                textViewRencanaProgress2.setText("");
                textViewRencanaProgress2.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress2.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });

        //material status
        textViewRencanaProgress3.setText("");
        textViewRencanaProgress3.setOnClickListener(v -> {
            if (textViewRencanaProgress3.getText().toString().equals("")){
                textViewRencanaProgress3.setText("In Progress");
                textViewRencanaProgress3.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewRencanaProgress3.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewRencanaProgress3.getText().toString().equals("In Progress")){
                textViewRencanaProgress3.setText("Done");
                textViewRencanaProgress3.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewRencanaProgress3.getText().toString().equals("Done")){
                textViewRencanaProgress3.setText("");
                textViewRencanaProgress3.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress3.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });

        //material status
        textViewRencanaProgress4.setText("");
        textViewRencanaProgress4.setOnClickListener(v -> {
            if (textViewRencanaProgress4.getText().toString().equals("")){
                textViewRencanaProgress4.setText("In Progress");
                textViewRencanaProgress4.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewRencanaProgress4.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewRencanaProgress4.getText().toString().equals("In Progress")){
                textViewRencanaProgress4.setText("Done");
                textViewRencanaProgress4.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewRencanaProgress4.getText().toString().equals("Done")){
                textViewRencanaProgress4.setText("");
                textViewRencanaProgress4.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress4.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });

        //material status
        textViewRencanaProgress5.setText("");
        textViewRencanaProgress5.setOnClickListener(v -> {
            if (textViewRencanaProgress5.getText().toString().equals("")){
                textViewRencanaProgress5.setText("In Progress");
                textViewRencanaProgress5.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewRencanaProgress5.setBackgroundColor(getResources().getColor(R.color.colorError));
            }else if(textViewRencanaProgress5.getText().toString().equals("In Progress")){
                textViewRencanaProgress5.setText("Done");
                textViewRencanaProgress5.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewRencanaProgress5.getText().toString().equals("Done")){
                textViewRencanaProgress5.setText("");
                textViewRencanaProgress5.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewRencanaProgress5.setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
            }
        });


        return view;
    }

}
