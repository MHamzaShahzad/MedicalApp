package com.example.medicalapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalapp.Constants;
import com.example.medicalapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void hideProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void moveToHome() {
        startActivity(new Intent(context, HomeDrawerActivity.class));
        finish();
    }

    private void stopAlarmFromNotification(){
        if (getIntent() != null){
            try {
                Ringtone ringtone = (Ringtone) getIntent().getParcelableExtra(Constants.STRING_EXTRA_NOTIFICATION_RINGTONE);
                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.stop();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            moveToHome();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgressDialog();
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                    moveToHome();
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
}
