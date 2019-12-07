package com.example.medicalapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonFunctionsClass {

    private static final String TAG = CommonFunctionsClass.class.getName();

    public static void subscribeToTopic(final Context context, final String topic, final boolean isHidden){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (isHidden)
                        Log.d(TAG, "onComplete: subscription to " + topic + " successful!" );
                    else
                        Toast.makeText(context, "Subscription to "+ topic + " successful.", Toast.LENGTH_LONG).show();
                }else {
                    if (isHidden)
                        Log.d(TAG, "onComplete: can't subscribe successfully to " + topic );
                    else
                        Toast.makeText(context, "Subscription to "+ topic + " failed.", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public static void unSubscribeFromTopic(final Context context, final String topic, final boolean isHidden){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (isHidden)
                        Log.d(TAG, "onComplete: un-subscribed from " + topic + " successful!" );
                    else
                        Toast.makeText(context, "Un-Subscribed from "+ topic + " successful.", Toast.LENGTH_LONG).show();
                }else {
                    if (isHidden)
                        Log.d(TAG, "onComplete: can't un-subscribe successfully from " + topic );
                    else
                        Toast.makeText(context, "Un-Subscribed from "+ topic + " failed.", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

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

    public static boolean isOutdated(String dueDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        Date strDate = null;
        try {
            strDate = sdf.parse(dueDate);
            if (strDate != null)
                if (System.currentTimeMillis() > strDate.getTime()) {
                    return true;
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void clearFragmentBackStack(FragmentManager fragmentManager){
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++)
            fragmentManager.popBackStack();
    }

}
