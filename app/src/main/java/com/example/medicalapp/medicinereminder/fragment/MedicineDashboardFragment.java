package com.example.medicalapp.medicinereminder.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.R;
import com.example.medicalapp.medicinereminder.Class.Medicine;
import com.example.medicalapp.medicinereminder.adapter.MedicineDashboardRecyclerViewAdapter;
import com.example.medicalapp.medicinereminder.database.MedicineManagementDatabase;

import java.util.ArrayList;


public class MedicineDashboardFragment extends Fragment {

    RecyclerView medicinePreviewRv;

    public MedicineDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        medicinePreviewRv = view.findViewById(R.id.MedicinePreviewRV);
        MedicineManagementDatabase obj = new MedicineManagementDatabase(getContext());
        ArrayList<Medicine> medicines = obj.retriveAllMedicineInfo();
        LinearLayoutManager LayoutManagaer = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        medicinePreviewRv.setLayoutManager(LayoutManagaer);
        MedicineDashboardRecyclerViewAdapter adapter = new MedicineDashboardRecyclerViewAdapter(getContext(), medicines);
        medicinePreviewRv.setAdapter(adapter);
        return view;
    }

}
