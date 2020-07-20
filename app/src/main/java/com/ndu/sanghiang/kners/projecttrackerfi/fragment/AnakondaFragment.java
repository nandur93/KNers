package com.ndu.sanghiang.kners.projecttrackerfi.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;
import com.ndu.sanghiang.kners.R;

import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.ENVIRONMENT_STATUS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.ENVIRONMENT_WAH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.ENVIRONMENT_WSBH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.MACHINE_STATUS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.MACHINE_WAH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.MACHINE_WSBH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.MAN_STATUS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.MAN_WAH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.MAN_WSBH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.MATERIAL_STATUS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.MATERIAL_WAH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.MATERIAL_WSBH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.METHOD_STATUS;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.METHOD_WAH;
import static com.ndu.sanghiang.kners.projecttrackerfi.fragment.FirebaseChildKeys.METHOD_WSBH;


public class AnakondaFragment extends Fragment {
    //Edittext
    public static TextInputEditText inputEditTextMaterialWSBH;
    public static TextInputEditText inputEditTextMaterialWAH;
    public static TextInputEditText inputEditTextMachineWSBH;
    public static TextInputEditText inputEditTextMachineWAH;
    public static TextInputEditText inputEditTextMethodWSBH;
    public static TextInputEditText inputEditTextMethodWAH;
    public static TextInputEditText inputEditTextManWSBH;
    public static TextInputEditText inputEditTextManWAH;
    public static TextInputEditText inputEditTextEnvironmentWSBH;
    public static TextInputEditText inputEditTextEnvironmentWAH;

    //Textview Status
    private TextView textViewStatusMaterial;
    private TextView textViewStatusMachine;
    private TextView textViewStatusMethod;
    private TextView textViewStatusMan;
    private TextView textViewStatusEnvironment;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anakonda, container, false);
        //Edittext
        inputEditTextMaterialWSBH = view.findViewById(R.id.edittext_material_wsbh);
        inputEditTextMaterialWAH = view.findViewById(R.id.edittext_material_wah);
        inputEditTextMachineWSBH = view.findViewById(R.id.edittext_machine_wsbh);
        inputEditTextMachineWAH = view.findViewById(R.id.edittext_machine_wah);
        inputEditTextMethodWSBH = view.findViewById(R.id.edittext_method_wsbh);
        inputEditTextMethodWAH = view.findViewById(R.id.edittext_method_wah);
        inputEditTextManWSBH = view.findViewById(R.id.edittext_man_wsbh);
        inputEditTextManWAH = view.findViewById(R.id.edittext_man_wah);
        inputEditTextEnvironmentWSBH = view.findViewById(R.id.edittext_environment_wsbh);
        inputEditTextEnvironmentWAH = view.findViewById(R.id.edittext_environment_wah);
        //Textview
        textViewStatusMaterial = view.findViewById(R.id.textview_status_material);
        textViewStatusMachine = view.findViewById(R.id.textview_status_machine);
        textViewStatusMethod = view.findViewById(R.id.textview_status_method);
        textViewStatusMan = view.findViewById(R.id.textview_status_man);
        textViewStatusEnvironment = view.findViewById(R.id.textview_status_environment);

        //logic start here
        //init status text
        textViewStatusMaterial.setText("NOK");
        textViewStatusMachine.setText("NOK");
        textViewStatusMethod.setText("NOK");
        textViewStatusMan.setText("NOK");
        textViewStatusEnvironment.setText("NOK");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //material status
        textViewStatusMaterial.setOnClickListener(v -> {
            if (textViewStatusMaterial.getText().toString().equals("NOK")){
                textViewStatusMaterial.setText("OK");
                textViewStatusMaterial.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewStatusMaterial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewStatusMaterial.getText().toString().equals("OK")){
                textViewStatusMaterial.setText("NOK");
                textViewStatusMaterial.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewStatusMaterial.setBackgroundColor(getResources().getColor(R.color.colorError));
            }
        });
        //machine status
        textViewStatusMachine.setOnClickListener(v -> {
            if (textViewStatusMachine.getText().toString().equals("NOK")){
                textViewStatusMachine.setText("OK");
                textViewStatusMachine.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewStatusMachine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewStatusMachine.getText().toString().equals("OK")){
                textViewStatusMachine.setText("NOK");
                textViewStatusMachine.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewStatusMachine.setBackgroundColor(getResources().getColor(R.color.colorError));
            }
        });
        //methode status
        textViewStatusMethod.setOnClickListener(v -> {
            if (textViewStatusMethod.getText().toString().equals("NOK")){
                textViewStatusMethod.setText("OK");
                textViewStatusMethod.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewStatusMethod.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewStatusMethod.getText().toString().equals("OK")){
                textViewStatusMethod.setText("NOK");
                textViewStatusMethod.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewStatusMethod.setBackgroundColor(getResources().getColor(R.color.colorError));
            }
        });
        //man status
        textViewStatusMan.setOnClickListener(v -> {
            if (textViewStatusMan.getText().toString().equals("NOK")){
                textViewStatusMan.setText("OK");
                textViewStatusMan.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewStatusMan.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewStatusMan.getText().toString().equals("OK")){
                textViewStatusMan.setText("NOK");
                textViewStatusMan.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewStatusMan.setBackgroundColor(getResources().getColor(R.color.colorError));
            }
        });
        //environment status
        textViewStatusEnvironment.setOnClickListener(v -> {
            if (textViewStatusEnvironment.getText().toString().equals("NOK")){
                textViewStatusEnvironment.setText("OK");
                textViewStatusEnvironment.setTextColor(getResources().getColor(R.color.navigationBarColor));
                textViewStatusEnvironment.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }else if(textViewStatusEnvironment.getText().toString().equals("OK")){
                textViewStatusEnvironment.setText("NOK");
                textViewStatusEnvironment.setTextColor(getResources().getColor(R.color.textColorPrimary));
                textViewStatusEnvironment.setBackgroundColor(getResources().getColor(R.color.colorError));
            }
        });
        
        //Load data dari sharedPref
        String finalMaterialWSBH = sharedPrefs.getString(MATERIAL_WSBH,"");
        String finalMaterialWAH = sharedPrefs.getString(MATERIAL_WAH,"");
        String finalMaterialStatus = sharedPrefs.getString(MATERIAL_STATUS,"");

        String finalMachineWSBH = sharedPrefs.getString(MACHINE_WSBH,"");
        String finalMachineWAH = sharedPrefs.getString(MACHINE_WAH,"");
        String finalMachineStatus = sharedPrefs.getString(MACHINE_STATUS,"");

        String finalMethodWSBH = sharedPrefs.getString(METHOD_WSBH,"");
        String finalMethodWAH = sharedPrefs.getString(METHOD_WAH,"");
        String finalMethodStatus = sharedPrefs.getString(METHOD_STATUS,"");
        
        String finalManWSBH = sharedPrefs.getString(MAN_WSBH,"");
        String finalManWAH = sharedPrefs.getString(MAN_WAH,"");
        String finalManStatus = sharedPrefs.getString(MAN_STATUS,"");

        String finalEnvironmentWSBH = sharedPrefs.getString(ENVIRONMENT_WSBH,"");
        String finalEnvironmentWAH = sharedPrefs.getString(ENVIRONMENT_WAH,"");
        String finalEnvironmentStatus = sharedPrefs.getString(ENVIRONMENT_STATUS,"");
        

        return view;
    }
}