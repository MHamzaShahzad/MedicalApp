package com.example.medicalapp.reminder.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.medicalapp.R;
import com.example.medicalapp.reminder.fragment.MedicineDashboardFragment;


public class MedicinePreviewActivity extends AppCompatActivity {
    LinearLayout fragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_preview);
        fragmentLayout = findViewById(R.id.MedicinePreviewFragmentLayout);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MedicineDashboardFragment fragment = new MedicineDashboardFragment();
        transaction.replace(R.id.MedicinePreviewFragmentLayout, fragment).commit();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void clickBack(View view) {
        this.finish();
    }
}
