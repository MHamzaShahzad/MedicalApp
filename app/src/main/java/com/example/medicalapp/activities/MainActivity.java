package com.example.medicalapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalapp.CommonFunctionsClass;
import com.example.medicalapp.Constants;
import com.example.medicalapp.R;
import com.example.medicalapp.controllers.MyFirebaseDatabase;
import com.example.medicalapp.models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();
    private Context context;
    private static final int RC_SIGN_IN = 1;
    List<AuthUI.IdpConfig> providers;

    private Button btnSignIn, btnSignUp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        initLayoutWidgets();
        initProgressDialog();
        initProviders();
        stopAlarmFromNotification();

        showProgressDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initLayoutWidgets() {

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);


        initClickListeners();
    }

    private void initClickListeners() {
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void initProviders() {
        // Choose authentication providers

        providers = Collections.singletonList(
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        /*providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );*/


    }

    private void showSignInOptions() {

        showProgressDialog();
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN);

    }

    private void showProgressDialog() {
        try {
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToHome() {
        startActivity(new Intent(context, HomeDrawerActivity.class));
        finish();
    }

    private void stopAlarmFromNotification() {
        if (getIntent() != null) {
            try {
                if (getIntent().getBooleanExtra(Constants.STRING_EXTRA_NOTIFICATION_STOP_RINGTONE, false))
                    sendBroadcast(new Intent(Constants.STRING_EXTRA_STOP_RINGTONE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            ifAccountTypeMentioned(user);
        else
            hideProgressDialog();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgressDialog();
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                    ifAccountTypeMentioned(user);
            } else {
                if (response != null && response.getError() != null) {
                    Log.e(TAG, "onActivityResult: Error" + response.getError().getErrorCode());
                    Toast.makeText(context, response.getError().getErrorCode(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch ((int) view.getId()) {
            case R.id.btnSignIn:
                showSignInOptions();
                break;
            case R.id.btnSignUp:
                showSignInOptions();
                break;
        }
    }

    private void ifAccountTypeMentioned(final FirebaseUser user){
        MyFirebaseDatabase.USERS_REFERENCE.child(user.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null)
                    moveToHome();
                else
                    showDialogForAccountCreatedAs(user);
                hideProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }

    private void showDialogForAccountCreatedAs(final FirebaseUser user){

        final RadioButton accountTypeDoctor, accountTypePatient;
        final RadioGroup radioGroupAccountType;
        final Button btnSubmit;
        final LinearLayout layout_doctor_details, layout_patient_details;
        final EditText inputDoctorFullName, inputDoctorClinicOrHospitalName, inputDoctorClinicOrHospitalAddress, inputDoctorCNIC,
                inputPatientName, inputPatientAge, inputPatientCity;

        View view = LayoutInflater.from(context).inflate(R.layout.layout_account_created_as , null);

        accountTypeDoctor = view.findViewById(R.id.accountTypeDoctor);
        accountTypePatient = view.findViewById(R.id.accountTypePatient);

        inputDoctorFullName = view.findViewById(R.id.inputDoctorFullName);
        inputDoctorClinicOrHospitalName = view.findViewById(R.id.inputDoctorClinicOrHospitalName);
        inputDoctorClinicOrHospitalAddress = view.findViewById(R.id.inputDoctorClinicOrHospitalAddress);
        inputDoctorCNIC = view.findViewById(R.id.inputDoctorCNIC);
        inputPatientName = view.findViewById(R.id.inputPatientName);
        inputPatientAge = view.findViewById(R.id.inputPatientAge);
        inputPatientCity = view.findViewById(R.id.inputPatientCity);

        radioGroupAccountType = view.findViewById(R.id.radioGroupAccountType);

        layout_doctor_details = view.findViewById(R.id.layout_doctor_details);
        layout_patient_details = view.findViewById(R.id.layout_patient_details);

        btnSubmit = view.findViewById(R.id.btnSubmit);


        radioGroupAccountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (radioGroup.getCheckedRadioButtonId() == accountTypeDoctor.getId()) {
                    layout_doctor_details.setVisibility(View.VISIBLE);
                    layout_patient_details.setVisibility(View.GONE);
                }

                if (radioGroup.getCheckedRadioButtonId() == accountTypePatient.getId()) {
                    layout_patient_details.setVisibility(View.VISIBLE);
                    layout_doctor_details.setVisibility(View.GONE);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User newUser = null;
                if (accountTypeDoctor.isChecked())
                    newUser = new User(
                      user.getUid(),
                      Constants.ACCOUNT_TYPE_DOCTOR,
                      inputDoctorFullName.getText().toString().trim(),
                      inputDoctorClinicOrHospitalName.getText().toString().trim(),
                      inputDoctorClinicOrHospitalAddress.getText().toString().trim(),
                      inputDoctorCNIC.getText().toString().trim()
                    );

                if (accountTypePatient.isChecked())
                    newUser = new User(
                      user.getUid(),
                      Constants.ACCOUNT_TYPE_PATIENT,
                      inputPatientName.getText().toString().trim(),
                      inputPatientAge.getText().toString().trim(),
                      inputPatientCity.getText().toString().trim()
                    );

                MyFirebaseDatabase.USERS_REFERENCE.child(user.getPhoneNumber()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            moveToHome();
                    }
                });
            }
        });

        new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create().show();
    }

}
