package com.example.medicalapp.inventory;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicalapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMedicineInventoryList extends Fragment {

    private static final String TAG = FragmentMedicineInventoryList.class.getName();
    private Context context;
    private View view;

    private RecyclerView recyclerInventoryMedicine;
    private AdapterMedicineInventory adapterMedicineInventory;
    private List<MedInventoryModel> medInventoryModelList;

    private MedInventoryDatabaseManagement medInventoryDatabaseManagement;

    public FragmentMedicineInventoryList() {
        // Required empty public constructor
        medInventoryModelList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        // Inflate the layout for this fragment
        if (view == null) {

            medInventoryModelList = new ArrayList<>();
            medInventoryDatabaseManagement = new MedInventoryDatabaseManagement(context);


            view = inflater.inflate(R.layout.fragment_medicine_inventory_list, container, false);

            recyclerInventoryMedicine = view.findViewById(R.id.recyclerInventoryMedicine);
            recyclerInventoryMedicine.setHasFixedSize(true);
            recyclerInventoryMedicine.setLayoutManager(new GridLayoutManager(context, 1));
            adapterMedicineInventory = new AdapterMedicineInventory(context, medInventoryModelList);
            recyclerInventoryMedicine.setAdapter(adapterMedicineInventory);

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadInventoryMedicines();
    }

    private void loadInventoryMedicines(){
        Log.e(TAG, "loadInventoryMedicines: " + medInventoryModelList.size() );
        medInventoryModelList.clear();
        medInventoryModelList.addAll(medInventoryDatabaseManagement.retrieveMedicineFromInventory());
        Log.e(TAG, "loadInventoryMedicines: " + medInventoryModelList.size() );
        adapterMedicineInventory.notifyDataSetChanged();

    }

}
