package com.example.medicalapp.doc_pat_interaction.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.R;
import com.example.medicalapp.doc_pat_interaction.models.Prescription;

import java.util.List;

public class AdapterPrescriptionList extends RecyclerView.Adapter<AdapterPrescriptionList.Holder> {

    private Context context;
    private List<Prescription> prescriptionList;

    public AdapterPrescriptionList(Context context, List<Prescription> prescriptionList) {
        this.context = context;
        this.prescriptionList = prescriptionList;
    }

    @NonNull
    @Override
    public AdapterPrescriptionList.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_prescription_card, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPrescriptionList.Holder holder, int position) {

        final Prescription prescription = prescriptionList.get(holder.getAdapterPosition());

        holder.textPrescribedAt.setText(prescription.getPrescriptionDate());
        holder.textPrescriptionDuration.setText(prescription.getPrescriptionDuration());
        holder.textPrescriptionDetail.setText(prescription.getPrescriptionDetail());

    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView textPrescribedAt, textPrescriptionDuration, textPrescriptionDetail;

        public Holder(@NonNull View itemView) {
            super(itemView);

            textPrescribedAt = itemView.findViewById(R.id.textPrescribedAt);
            textPrescriptionDuration = itemView.findViewById(R.id.textPrescriptionDuration);
            textPrescriptionDetail = itemView.findViewById(R.id.textPrescriptionDetail);

        }
    }
}
