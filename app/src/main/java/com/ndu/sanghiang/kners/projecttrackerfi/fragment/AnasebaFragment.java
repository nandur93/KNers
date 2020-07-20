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
public class AnasebaFragment extends Fragment {

    //material
    public static TextInputEditText inputAnasebaMaterialWhy1;
    public static TextInputEditText inputAnasebaMaterialWhy2;
    public static TextInputEditText inputAnasebaMaterialWhy3;
    public static TextInputEditText inputAnasebaMaterialWhy4;
    public static TextInputEditText inputAnasebaMaterialWhy5;
    //machine
    public static TextInputEditText inputAnasebaMachineWhy1;
    public static TextInputEditText inputAnasebaMachineWhy2;
    public static TextInputEditText inputAnasebaMachineWhy3;
    public static TextInputEditText inputAnasebaMachineWhy4;
    public static TextInputEditText inputAnasebaMachineWhy5;
    //method
    public static TextInputEditText inputAnasebaMethodWhy1;
    public static TextInputEditText inputAnasebaMethodWhy2;
    public static TextInputEditText inputAnasebaMethodWhy3;
    public static TextInputEditText inputAnasebaMethodWhy4;
    public static TextInputEditText inputAnasebaMethodWhy5;
    //man
    public static TextInputEditText inputAnasebaManWhy1;
    public static TextInputEditText inputAnasebaManWhy2;
    public static TextInputEditText inputAnasebaManWhy3;
    public static TextInputEditText inputAnasebaManWhy4;
    public static TextInputEditText inputAnasebaManWhy5;
    //environment
    public static TextInputEditText inputAnasebaEnvironmentWhy1;
    public static TextInputEditText inputAnasebaEnvironmentWhy2;
    public static TextInputEditText inputAnasebaEnvironmentWhy3;
    public static TextInputEditText inputAnasebaEnvironmentWhy4;
    public static TextInputEditText inputAnasebaEnvironmentWhy5;

    public AnasebaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_anaseba, container, false);
        //material
        inputAnasebaMaterialWhy1 = view.findViewById(R.id.material_why1);
        inputAnasebaMaterialWhy2 = view.findViewById(R.id.material_why2);
        inputAnasebaMaterialWhy3 = view.findViewById(R.id.material_why3);
        inputAnasebaMaterialWhy4 = view.findViewById(R.id.material_why4);
        inputAnasebaMaterialWhy5 = view.findViewById(R.id.material_why5);
        //machine
        inputAnasebaMachineWhy1 = view.findViewById(R.id.machine_why1);
        inputAnasebaMachineWhy2 = view.findViewById(R.id.machine_why2);
        inputAnasebaMachineWhy3 = view.findViewById(R.id.machine_why3);
        inputAnasebaMachineWhy4 = view.findViewById(R.id.machine_why4);
        inputAnasebaMachineWhy5 = view.findViewById(R.id.machine_why5);
        //method
        inputAnasebaMethodWhy1 = view.findViewById(R.id.method_why1);
        inputAnasebaMethodWhy2 = view.findViewById(R.id.method_why2);
        inputAnasebaMethodWhy3 = view.findViewById(R.id.method_why3);
        inputAnasebaMethodWhy4 = view.findViewById(R.id.method_why4);
        inputAnasebaMethodWhy5 = view.findViewById(R.id.method_why5);
        //man
        inputAnasebaManWhy1 = view.findViewById(R.id.man_why1);
        inputAnasebaManWhy2 = view.findViewById(R.id.man_why2);
        inputAnasebaManWhy3 = view.findViewById(R.id.man_why3);
        inputAnasebaManWhy4 = view.findViewById(R.id.man_why4);
        inputAnasebaManWhy5 = view.findViewById(R.id.man_why5);
        //environment
        inputAnasebaEnvironmentWhy1 = view.findViewById(R.id.environment_why1);
        inputAnasebaEnvironmentWhy2 = view.findViewById(R.id.environment_why2);
        inputAnasebaEnvironmentWhy3 = view.findViewById(R.id.environment_why3);
        inputAnasebaEnvironmentWhy4 = view.findViewById(R.id.environment_why4);
        inputAnasebaEnvironmentWhy5 = view.findViewById(R.id.environment_why5);
        //
        
        
        
        return view;
    }

}
