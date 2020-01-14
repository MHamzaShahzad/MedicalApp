package com.example.medicalapp.inventory;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.medicalapp.Constants;
import com.example.medicalapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddMedInInventory extends Fragment implements View.OnClickListener {

    private static final String TAG = FragmentAddMedInInventory.class.getName();
    private Context context;
    private View view;

    private EditText medName, medExpiryDate, medUserPerDay, medStock, medDetail;
    private Spinner spinnerMedType;
    private Button btnIncrement, btnDecrement, btnSubmitMed;

    private String[] medTypesList = {"Tablet", "Capsule", "Syrup", "Injection"};
    private DatePickerDialog dialog;

    private MedInventoryDatabaseManagement medInventoryDatabaseManagement;

    public FragmentAddMedInInventory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        medInventoryDatabaseManagement = new MedInventoryDatabaseManagement(context);
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_med_in_inventory, container, false);

            findViewsById();
            MedInventoryMainActivity.addMedFAB.hide();
        }
        return view;
    }

    private void findViewsById() {
        medName = view.findViewById(R.id.medName);
        medExpiryDate = view.findViewById(R.id.medExpiryDate);
        medUserPerDay = view.findViewById(R.id.medUserPerDay);
        medStock = view.findViewById(R.id.medStock);
        medDetail = view.findViewById(R.id.medDetail);
        spinnerMedType = view.findViewById(R.id.spinnerMedType);

        btnDecrement = view.findViewById(R.id.btnDecrement);
        btnIncrement = view.findViewById(R.id.btnIncrement);
        btnSubmitMed = view.findViewById(R.id.btnSubmitMed);

        initMedTypesSpinner();
        initDatePickerDialog();

        setClickListeners();

        getMedFromArguments();

    }

    private void setClickListeners() {
        btnIncrement.setOnClickListener(this);
        btnDecrement.setOnClickListener(this);
        btnSubmitMed.setOnClickListener(this);
        medExpiryDate.setOnClickListener(this);
    }

    private void getMedFromArguments() {
        Bundle bundleArguments = getArguments();
        if (bundleArguments != null) {

            try {

                MedInventoryModel model = (MedInventoryModel) bundleArguments.getSerializable(Constants.MED_INVENTORY_OBJ);
                if (model != null) {

                    medName.setText(model.getMedName());
                    medDetail.setText(model.getMedDetail());
                    medStock.setText( "" + model.getMedStock());
                    medUserPerDay.setText( "" + model.getMedPerDay());
                    medExpiryDate.setText(model.getMedExpiryDate());

                    for (int i = 0; i < medTypesList.length; i++)
                        if (medTypesList[i].equals(model.getMedType()))
                            spinnerMedType.setSelection(i);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void initMedTypesSpinner() {
        spinnerMedType.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, medTypesList));
    }

    private void initDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(
                context,
                mDateSetListener,
                year, month, day
        );
    }

    private void showDatePickerDialog() {
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            datePicker.animate();
            Log.e(TAG, "onDateSet: " + getStringDay(day) + " " + getMonthString(month) + " " + year);
            medExpiryDate.setText(getStringDay(day) + " " + getMonthString(month) + " " + year);
        }
    };

    private String getStringDay(int day) {
        if (day < 9)
            return "0" + day;
        else
            return String.valueOf(day);
    }

    private String getMonthString(int month) {
        String monthName = null;
        switch (month) {
            case 0:
                monthName = "Jan";
                break;
            case 1:
                monthName = "Feb";
                break;
            case 2:
                monthName = "Mar";
                break;
            case 3:
                monthName = "Apr";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "Jun";
                break;
            case 6:
                monthName = "Jul";
                break;
            case 7:
                monthName = "Aug";
                break;
            case 8:
                monthName = "Sep";
                break;
            case 9:
                monthName = "Oct";
                break;
            case 10:
                monthName = "Nov";
                break;
            case 11:
                monthName = "Dec";
                break;
        }
        return monthName;
    }

    private MedInventoryModel buildMedInstance() {

            return new MedInventoryModel(
                    medName.getText().toString().trim(),
                    medDetail.getText().toString().trim(),
                    new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()),
                    medExpiryDate.getText().toString().trim(),
                    spinnerMedType.getSelectedItem().toString(),
                    Integer.valueOf(medStock.getText().toString().trim()),
                    Integer.valueOf(medUserPerDay.getText().toString().trim()),
                    0
            );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MedInventoryMainActivity.addMedFAB.show();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.btnIncrement:
                if (!TextUtils.isEmpty(medUserPerDay.getText())) {
                    int previousValue = Integer.valueOf(medUserPerDay.getText().toString().trim());
                    previousValue++;
                    medUserPerDay.setText("" + previousValue);
                }
                break;

            case R.id.btnDecrement:
                if (!TextUtils.isEmpty(medUserPerDay.getText())) {
                    int previousValue = Integer.valueOf(medUserPerDay.getText().toString().trim());
                    previousValue--;
                    medUserPerDay.setText("" + previousValue);
                }
                break;

            case R.id.btnSubmitMed:

                if (TextUtils.isEmpty(medName.getText())){
                    medName.setError("Field is required");
                }else if (TextUtils.isEmpty(medDetail.getText())){
                    medDetail.setError("Field is required");

                }else if (TextUtils.isEmpty(medExpiryDate.getText())){
                    medExpiryDate.setError("Field is required");

                }else if (TextUtils.isEmpty(medStock.getText())){
                    medStock.setError("Field is required");

                }else if (TextUtils.isEmpty(medUserPerDay.getText())){
                    medUserPerDay.setError("Field is required");

                }else {

                    boolean result = medInventoryDatabaseManagement.addMedicineDetails(MedInventoryDatabaseHelper.TABLE_MEDICINE_DETAILS, buildMedInstance());
                    if (result)
                        ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
                    else
                        Snackbar.make(view, "Data could't be added!", Snackbar.LENGTH_LONG).show();

                }

                break;

            case R.id.medExpiryDate:
                showDatePickerDialog();
                break;

            default:
                Log.e(TAG, "onClick: EVENT_NOT_DEFINED ");
        }
    }
}
