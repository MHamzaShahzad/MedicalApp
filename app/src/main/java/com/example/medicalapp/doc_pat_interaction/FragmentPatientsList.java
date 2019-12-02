package com.example.medicalapp.doc_pat_interaction;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicalapp.R;
import com.example.medicalapp.controllers.MyFirebaseDatabase;
import com.example.medicalapp.doc_pat_interaction.adapters.AdapterMyPatientsList;
import com.example.medicalapp.doc_pat_interaction.models.Patient;
import com.example.medicalapp.interfaces.FragmentInteractionListenerInterface;
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
    private FragmentInteractionListenerInterface mListener;


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
        if (mListener != null)
            mListener.onFragmentInteraction(this.getTag());
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

    private void removeMyPatientsEventListener(){
        if (myPatientsEventListener != null)
            MyFirebaseDatabase.MY_PATIENTS_REFERENCE.child(firebaseUser.getPhoneNumber()).removeEventListener(myPatientsEventListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentInteractionListenerInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FragmentInteractionListenerInterface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        getPatientsList();

        if (mListener != null)
            mListener.onFragmentInteraction(this.getTag());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeMyPatientsEventListener();
    }
}
