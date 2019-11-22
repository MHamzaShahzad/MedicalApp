package com.example.medicalapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentContactUs extends DialogFragment {

    private static final String TAG = FragmentContactUs.class.getName();
    private Context context;
    private View view;

    public FragmentContactUs() {
        // Required empty public constructor
    }

    public static FragmentContactUs newInstance() {
        Log.d(TAG, "newInstance: ");
        return new FragmentContactUs();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = ((FragmentActivity) context).getLayoutInflater().inflate(R.layout.fragment_contact_us, null);
        initDialogWidgets(view);
        builder.setView(view);
        
        return builder.create();
    }

    private void initDialogWidgets(View view) {


    }


}
