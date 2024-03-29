package com.example.medicalapp.doc_pat_interaction;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.medicalapp.R;
import com.example.medicalapp.controllers.MyFirebaseDatabase;
import com.example.medicalapp.controllers.SendPushNotificationFirebase;
import com.example.medicalapp.doc_pat_interaction.models.Patient;
import com.example.medicalapp.doc_pat_interaction.models.User;
import com.example.medicalapp.interfaces.FragmentInteractionListenerInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class FragmentRegisterPatient extends Fragment {

    private static final String TAG = FragmentRegisterPatient.class.getName();
    private Context context;
    private View view;

    private CountryCodePicker ccp;
    private EditText inputPhoneNumber, patientName, patientAge, patientHealthProfile;
    private Button btnLoadDetails, btnRegisterPatient;
    private LinearLayout layout_enter_patient_details;

    private FirebaseUser firebaseUser;

    private FragmentInteractionListenerInterface mListener;


    public static FragmentRegisterPatient newInstance() {
        return new FragmentRegisterPatient();
    }

    private FragmentRegisterPatient() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mListener != null)
            mListener.onFragmentInteraction(this.getTag());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        context = container.getContext();
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_register_patient, container, false);


            findViewsById();
            setBtnLoadDetails();
        }
        return view;
    }

    private void findViewsById() {

        ccp = view.findViewById(R.id.ccp);

        layout_enter_patient_details = view.findViewById(R.id.layout_enter_patient_details);

        inputPhoneNumber = view.findViewById(R.id.inputPhoneNumber);
        patientName = view.findViewById(R.id.patientName);
        patientAge = view.findViewById(R.id.patientAge);
        patientHealthProfile = view.findViewById(R.id.patientHealthProfile);

        btnLoadDetails = view.findViewById(R.id.btnLoadDetails);
        btnRegisterPatient = view.findViewById(R.id.btnRegisterPatient);


        ccp.registerCarrierNumberEditText(inputPhoneNumber);
    }

    private void setBtnLoadDetails() {
        btnLoadDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(inputPhoneNumber.getText())) {
                    inputPhoneNumber.setError("Enter Phone Number");
                } else if (!TextUtils.isEmpty(inputPhoneNumber.getText()) && inputPhoneNumber.length() != 10) {
                    inputPhoneNumber.setError("Enter Valid Phone Number");
                } else {
                    MyFirebaseDatabase.USERS_REFERENCE.child(ccp.getFullNumberWithPlus()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                                layout_enter_patient_details.setVisibility(View.VISIBLE);
                                try {

                                    User user = dataSnapshot.getValue(User.class);
                                    if (user != null) {
                                        patientName.setText(user.getPatientName());
                                        patientAge.setText(user.getPatientAge());

                                        setBtnRegisterPatient();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else
                                Snackbar.make(view, "No user exists with this number!", Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void setBtnRegisterPatient() {
        btnRegisterPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MyFirebaseDatabase.MY_PATIENTS_REFERENCE.child(firebaseUser.getPhoneNumber()).child(ccp.getFullNumberWithPlus()).setValue(buildPatientObject()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            MyFirebaseDatabase.MY_DOCTORS_REFERENCE.child(ccp.getFullNumberWithPlus()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {

                                        if (!isDoctorAlreadyExists(dataSnapshot))
                                            addNewDoctor();
                                        else {
                                            Snackbar.make(view, "Successful!", Snackbar.LENGTH_LONG).show();
                                            ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
                                        }


                                    } else
                                        addNewDoctor();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else
                            Snackbar.make(view, "Can't register, please try again!", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean isDoctorAlreadyExists(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
        for (DataSnapshot snapshot : snapshots) {
            if (snapshot.getValue() != null && snapshot.getValue().equals(firebaseUser.getPhoneNumber()))
                return true;
        }
        return false;
    }

    private void addNewDoctor() {
        MyFirebaseDatabase.MY_DOCTORS_REFERENCE.child(ccp.getFullNumberWithPlus()).push().setValue(firebaseUser.getPhoneNumber()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SendPushNotificationFirebase.buildAndSendNotification(
                        context,
                        ccp.getFullNumber().replace("+",""),
                        "Registered By Doctor",
                        "You have been added into a doctors circle, now this doctor can prescribe you medicine, you can check this into your doctors list."
                );
                Snackbar.make(view, "Patient successfully registered!", Snackbar.LENGTH_LONG).show();
                ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
            }
        });
    }

    private Patient buildPatientObject() {
        return new Patient(
                ccp.getFullNumberWithPlus(),
                firebaseUser.getPhoneNumber(),
                patientName.getText().toString().trim(),
                patientAge.getText().toString().trim(),
                patientHealthProfile.getText().toString().trim(),
                new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime())
        );
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
        if (mListener != null)
            mListener.onFragmentInteraction(this.getTag());
    }

}
