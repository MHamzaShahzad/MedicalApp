package com.example.medicalapp.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.medicalapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MedInventoryMainActivity extends AppCompatActivity {

    public static FloatingActionButton addMedFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_inventory);
        addMedFAB = findViewById(R.id.fab_add_new_med);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inventory, new FragmentMedicineInventoryList()).commit();

        setAddMedFAB();
    }

    private void setAddMedFAB(){
        addMedFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inventory, new FragmentAddMedInInventory()).addToBackStack(null).commit();
            }
        });
    }

}
