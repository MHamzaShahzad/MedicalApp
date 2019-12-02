package com.example.medicalapp.doc_pat_interaction;


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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentMyDoctors extends Fragment {

    private static final String TAG = FragmentMyDoctors.class.getName();
    private Context context;
    private View view;

    private RecyclerView recyclerMyDoctorsList;
    private AdapterMyDoctorsList adapterMyDoctorsList;

    private ValueEventListener doctorsEventListener;
    private List<String> myDoctorsList;

    private FirebaseUser firebaseUser;

    public static FragmentMyDoctors newInstance(){
        return new FragmentMyDoctors();
    }

    private FragmentMyDoctors() {
        // Required empty public constructor
        myDoctorsList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        adapterMyDoctorsList = new AdapterMyDoctorsList(context, myDoctorsList);
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_doctors, container, false);

            initRecyclerView();
            loadMyDoctorsList();
        }
        return view;
    }

    private void initRecyclerView(){
        recyclerMyDoctorsList = view.findViewById(R.id.recyclerMyDoctorsList);
        recyclerMyDoctorsList.setHasFixedSize(true);
        recyclerMyDoctorsList.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerMyDoctorsList.setAdapter(adapterMyDoctorsList);
    }

    private void loadMyDoctorsList(){
        doctorsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myDoctorsList.clear();
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null){

                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : snapshots){
                        myDoctorsList.add( (String) snapshot.getValue());
                    }

                }
                adapterMyDoctorsList.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        MyFirebaseDatabase.MY_DOCTORS_REFERENCE.child(firebaseUser.getPhoneNumber()).addValueEventListener(doctorsEventListener);
    }

    private void removeDoctorsEventListener(){
        if (doctorsEventListener != null)
            MyFirebaseDatabase.MY_DOCTORS_REFERENCE.child(firebaseUser.getPhoneNumber()).removeEventListener(doctorsEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeDoctorsEventListener();
    }
}
