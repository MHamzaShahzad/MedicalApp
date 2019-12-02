package com.example.medicalapp.doc_pat_interaction;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicalapp.Constants;
import com.example.medicalapp.R;
import com.example.medicalapp.controllers.MyFirebaseDatabase;
import com.example.medicalapp.doc_pat_interaction.adapters.AdapterPrescriptionList;
import com.example.medicalapp.doc_pat_interaction.models.Prescription;
import com.example.medicalapp.interfaces.FragmentInteractionListenerInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPrescriptionList extends Fragment {

    private static final String TAG = FragmentPrescriptionList.class.getName();
    private Context context;
    private View view;

    private RecyclerView recyclerPrescriptionList;
    private AdapterPrescriptionList adapterPrescriptionList;
    private List<Prescription> prescriptionList;

    private Bundle arguments;
    private FirebaseUser firebaseUser;

    private ValueEventListener prescriptionEventListener;

    private FragmentInteractionListenerInterface mListener;


    public static FragmentPrescriptionList getInstance(Bundle arguments){
        return new FragmentPrescriptionList(arguments);
    }

    private FragmentPrescriptionList(Bundle arguments) {
        // Required empty public constructor
        this.arguments = arguments;
        prescriptionList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mListener != null)
            mListener.onFragmentInteraction(this.getTag());
        context = container.getContext();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        adapterPrescriptionList = new AdapterPrescriptionList(context, prescriptionList);
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_prescription_list, container, false);

            initRecyclerView();
            initPrescriptionListener();

        }
        return view;
    }

    private void initRecyclerView(){
        recyclerPrescriptionList = view.findViewById(R.id.recyclerPrescriptionList);
        recyclerPrescriptionList.setHasFixedSize(true);
        recyclerPrescriptionList.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerPrescriptionList.setAdapter(adapterPrescriptionList);
    }

    private void initPrescriptionListener(){
        prescriptionEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prescriptionList.clear();
                Log.e(TAG, "onDataChange: " + dataSnapshot );
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null){

                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : snapshots){

                        try {

                            Prescription prescription = snapshot.getValue(Prescription.class);
                            if (prescription != null)
                                prescriptionList.add(prescription);


                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }
                adapterPrescriptionList.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void loadMyPrescriptionsList(){

        MyFirebaseDatabase.PRESCRIPTIONS_REFERENCE.child(firebaseUser.getPhoneNumber()).child(arguments.getString(Constants.DOCTOR_ID)).addListenerForSingleValueEvent(prescriptionEventListener);

    }

    private void loadPatientPrescriptionList(){

        MyFirebaseDatabase.PRESCRIPTIONS_REFERENCE.child(arguments.getString(Constants.PATIENT_ID)).child(firebaseUser.getPhoneNumber()).addListenerForSingleValueEvent(prescriptionEventListener);
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

        if (arguments != null){

            if (arguments.getBoolean(Constants.IS_PRESCRIPTION_SEEN_BY_DOCTOR))
                loadPatientPrescriptionList();
            else
                loadMyPrescriptionsList();
        }

        if (mListener != null)
            mListener.onFragmentInteraction(this.getTag());
    }

}
