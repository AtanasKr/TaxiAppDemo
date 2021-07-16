package com.example.taxiappdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return inflater.inflate(R.layout.fragment_register,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Spinner spinner = view.findViewById(R.id.spinner);
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Client");
        spinnerArray.add("Driver");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,spinnerArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }
}
