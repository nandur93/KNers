package com.ndu.sanghiang.kners.projecttrackerfi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ndu.sanghiang.kners.R;

public class TemaFragment extends Fragment implements View.OnClickListener {

    View view;
    Button buttonSubmit;
    EditText editTextThemeInput;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tema, container, false);
        buttonSubmit = view.findViewById(R.id.button_submit);
        editTextThemeInput = view.findViewById(R.id.editTextTema);
        buttonSubmit.setOnClickListener(this);
        return view;


    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(),editTextThemeInput.getText().toString(),Toast.LENGTH_SHORT).show();
        //do what you want to do when button is clicked
    }
}
