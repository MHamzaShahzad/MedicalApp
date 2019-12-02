package com.example.medicalapp.doc_pat_interaction;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicalapp.Constants;
import com.example.medicalapp.R;
import com.example.medicalapp.controllers.MyFirebaseDatabase;
import com.example.medicalapp.doc_pat_interaction.models.Prescription;
import com.example.medicalapp.reminder.Class.Alert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPrescribeMedicine extends DialogFragment {

    private static final String TAG = FragmentPrescribeMedicine.class.getName();
    private Context context;
    private View view;

    private EditText textPrescriptionDetail, textPrescriptionDuration;
    private Button btnSubmitPrescription;

    private Bundle arguments;

    private FirebaseUser firebaseUser;

    public static FragmentPrescribeMedicine getInstance(Bundle arguments) {
        return new FragmentPrescribeMedicine(arguments);
    }

    private FragmentPrescribeMedicine(Bundle arguments) {
        // Required empty public constructor
        this.arguments = arguments;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        context = getContext();

        view = LayoutInflater.from(context).inflate(R.layout.fragment_prescribe_medicine, null);

        initLayoutWidgets(view);

        return new AlertDialog.Builder(context)
                .setView(view).create();
    }

    private void initLayoutWidgets(View view) {
        textPrescriptionDetail = view.findViewById(R.id.textPrescriptionDetail);
        textPrescriptionDuration = view.findViewById(R.id.textPrescriptionDuration);
        btnSubmitPrescription = view.findViewById(R.id.btnSubmitPrescription);

        setBtnSubmitPrescription();
    }

    private void setBtnSubmitPrescription() {
        btnSubmitPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String date = new SimpleDateFormat("dd MM yyyy hh:mm a", Locale.ENGLISH).format(Calendar.getInstance().getTime());
                MyFirebaseDatabase.PRESCRIPTIONS_REFERENCE.child(arguments.getString(Constants.PATIENT_ID)).child(firebaseUser.getPhoneNumber()).child(date).setValue(buildPrescriptionInstance(date)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(view, "Prescription Uploaded Successfully!", Snackbar.LENGTH_LONG).show();
                            FragmentPrescribeMedicine.this.dismiss();
                        } else
                            Snackbar.make(view, "Prescription Uploading Failed!", Snackbar.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    private Prescription buildPrescriptionInstance(String date) {
        return new Prescription(
                date,
                textPrescriptionDuration.getText().toString().trim(),
                textPrescriptionDetail.getText().toString().trim()
        );
    }

}
