package com.example.medicalapp.doc_pat_interaction.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.Constants;
import com.example.medicalapp.R;
import com.example.medicalapp.doc_pat_interaction.FragmentPrescribeMedicine;
import com.example.medicalapp.doc_pat_interaction.FragmentPrescriptionList;
import com.example.medicalapp.doc_pat_interaction.models.Patient;

import java.util.List;

public class AdapterMyPatientsList extends RecyclerView.Adapter<AdapterMyPatientsList.Holder> {

    private Context context;
    private List<Patient> patientList;

    public AdapterMyPatientsList(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public AdapterMyPatientsList.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_patients_list , null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterMyPatientsList.Holder holder, int position) {
        final Patient patient = patientList.get(holder.getAdapterPosition());
        holder.patientNamePlace.setText(patient.getPatientName());
        holder.patientAgePlace.setText(patient.getPatientAge());
        holder.patientRegisteredAtPlace.setText(patient.getPatientRegisteredAt());
        holder.patientPhonePlace.setText(patient.getPatientPhoneNumber());

        holder.btnViewPrescriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_PRESCRIPTION_SEEN_BY_DOCTOR, true);
                bundle.putString(Constants.PATIENT_ID, patientList.get(holder.getAdapterPosition()).getPatientPhoneNumber());
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.home, FragmentPrescriptionList.getInstance(bundle), Constants.TITLE_PRESCRIPTIONS).addToBackStack(Constants.TITLE_PRESCRIPTIONS).commit();

            }
        });

        holder.btnPrescribeMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PATIENT_ID, patientList.get(holder.getAdapterPosition()).getPatientPhoneNumber());
                FragmentPrescribeMedicine.getInstance(bundle).show(((FragmentActivity) context).getSupportFragmentManager(), Constants.TITLE_PRESCRIBE_MEDICINE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Button btnViewPrescriptions, btnPrescribeMedicine;
        private TextView patientNamePlace, patientAgePlace, patientRegisteredAtPlace, patientPhonePlace;

        public Holder(@NonNull View itemView) {
            super(itemView);

            btnPrescribeMedicine = itemView.findViewById(R.id.btnPrescribeMedicine);
            btnViewPrescriptions = itemView.findViewById(R.id.btnViewPrescriptions);

            patientNamePlace = itemView.findViewById(R.id.patientNamePlace);
            patientAgePlace = itemView.findViewById(R.id.patientAgePlace);
            patientRegisteredAtPlace = itemView.findViewById(R.id.patientRegisteredAtPlace);
            patientPhonePlace = itemView.findViewById(R.id.patientPhonePlace);

        }
    }
}
