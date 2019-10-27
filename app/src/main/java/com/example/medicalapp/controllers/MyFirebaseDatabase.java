package com.example.medicalapp.controllers;

import com.example.medicalapp.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseDatabase {

    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference USERS_REFERENCE = database.getReference(Constants.STRING_USERS_REFERENCE);
    public static final DatabaseReference DOCTORS_REFERENCE = database.getReference(Constants.STRING_DOCTORS_REFERENCE);
    public static final DatabaseReference INVENTORY_REFERENCE = database.getReference(Constants.STRING_INVENTORY_REFERENCE);

}
