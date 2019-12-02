package com.example.medicalapp.doc_pat_interaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.R;
import com.example.medicalapp.models.Patient;

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
    public void onBindViewHolder(@NonNull AdapterMyPatientsList.Holder holder, int position) {
        final Patient patient = patientList.get(holder.getAdapterPosition());
        holder.patientNamePlace.setText(patient.getPatientName());
        holder.patientAgePlace.setText(patient.getPatientAge());
        holder.patientRegisteredAtPlace.setText(patient.getPatientRegisteredAt());
        holder.patientPhonePlace.setText(patient.getPatientPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private CardView cardPatientList;
        private TextView patientNamePlace, patientAgePlace, patientRegisteredAtPlace, patientPhonePlace;

        public Holder(@NonNull View itemView) {
            super(itemView);

            cardPatientList = itemView.findViewById(R.id.cardPatientList);

            patientNamePlace = itemView.findViewById(R.id.patientNamePlace);
            patientAgePlace = itemView.findViewById(R.id.patientAgePlace);
            patientRegisteredAtPlace = itemView.findViewById(R.id.patientRegisteredAtPlace);
            patientPhonePlace = itemView.findViewById(R.id.patientPhonePlace);

        }
    }
}
