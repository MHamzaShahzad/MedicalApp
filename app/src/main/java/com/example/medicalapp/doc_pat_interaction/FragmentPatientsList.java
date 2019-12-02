package com.example.medicalapp.doc_pat_interaction;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PatternMatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicalapp.R;
import com.example.medicalapp.controllers.MyFirebaseDatabase;
import com.example.medicalapp.models.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentPatientsList extends Fragment {

    private static final String TAG = FragmentPatientsList.class.getName();
    private Context context;
    private View view;

    private RecyclerView recyclerPatientsList;
    private AdapterMyPatientsList adapterMyPatientsList;
    private List<Patient> patientList;

    private FirebaseUser firebaseUser;

    private ValueEventListener myPatientsEventListener;

    public static FragmentPatientsList newInstance() {
        return new FragmentPatientsList();
    }

    private FragmentPatientsList() {
        // Required empty public constructor
        patientList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        adapterMyPatientsList = new AdapterMyPatientsList(context, patientList);
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_patients_list, container, false);

            recyclerPatientsList = view.findViewById(R.id.recyclerPatientsList);
            recyclerPatientsList.setHasFixedSize(true);
            recyclerPatientsList.setLayoutManager(new GridLayoutManager(context, 1));
            recyclerPatientsList.setAdapter(adapterMyPatientsList);

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPatientsList();
    }

    private void getPatientsList() {
        myPatientsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                patientList.clear();
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {

                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : snapshots) {

                        try {

                            Patient patient = snapshot.getValue(Patient.class);
                            if (patient != null)
                                patientList.add(patient);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
                adapterMyPatientsList.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        MyFirebaseDatabase.MY_PATIENTS_REFERENCE.child(firebaseUser.getPhoneNumber()).addValueEventListener(myPatientsEventListener);
    }

}
