package com.example.medicalapp.controllers;

import com.example.medicalapp.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseDatabase {

    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference USERS_REFERENCE = database.getReference(Constants.STRING_USERS_REFERENCE);
    public static final DatabaseReference MY_DOCTORS_REFERENCE = database.getReference(Constants.STRING_MY_DOCTORS_REFERENCE);
    public static final DatabaseReference MY_PATIENTS_REFERENCE = database.getReference(Constants.STRING_MY_PATIENTS_REFERENCE);
    public static final DatabaseReference PRESCRIPTIONS_REFERENCE = database.getReference(Constants.STRING_PRESCRIPTIONS_REFERENCE);

}
