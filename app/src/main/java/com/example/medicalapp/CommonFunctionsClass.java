package com.example.medicalapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class CommonFunctionsClass {

    public static void sendSMS(Context context, String phoneNumber) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", phoneNumber);
        smsIntent.putExtra("sms_body", "Body of Message");
        ((Activity) context).startActivity(smsIntent);
    }

    public static void makeCall(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        // Send phone number to intent as data
        intent.setData(Uri.parse("tel:" + phoneNumber));
        // Start the dialer app activity with number
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ((Activity) context).startActivity(intent);
    }

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

    public static void clearFragmentBackStack(FragmentManager fragmentManager){
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++)
            fragmentManager.popBackStack();
    }

}
