package com.example.medicalapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class CommonFunctionsClass {

    public static boolean CheckGooglePlayServices(Context context) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                try {
                    googleAPI.getErrorDialog((Activity) context, result,
                            0).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return false;
        }
        return true;
    }

}
