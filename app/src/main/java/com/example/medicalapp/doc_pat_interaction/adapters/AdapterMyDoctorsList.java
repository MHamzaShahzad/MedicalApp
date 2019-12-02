package com.example.medicalapp.doc_pat_interaction.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.Constants;
import com.example.medicalapp.R;
import com.example.medicalapp.controllers.MyFirebaseDatabase;
import com.example.medicalapp.doc_pat_interaction.FragmentPrescriptionList;
import com.example.medicalapp.doc_pat_interaction.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterMyDoctorsList extends RecyclerView.Adapter<AdapterMyDoctorsList.Holder> {

    private Context context;
    private List<String> doctorsList;

    public AdapterMyDoctorsList(Context context, List<String> doctorsList) {
        this.context = context;
        this.doctorsList = doctorsList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doctors_list , null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {


        getDoctorDetails(holder, doctorsList.get(holder.getAdapterPosition()));
        holder.cardDoctorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_PRESCRIPTION_SEEN_BY_DOCTOR, false);
                bundle.putString(Constants.DOCTOR_ID, doctorsList.get(holder.getAdapterPosition()));
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.home, FragmentPrescriptionList.getInstance(bundle), Constants.TITLE_PRESCRIPTIONS).addToBackStack(Constants.TITLE_PRESCRIPTIONS).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private CardView cardDoctorList;
        private TextView doctorNamePlace, doctorClinicHospitalNamePlace, doctorClinicHospitalAddressPlace;

        public Holder(@NonNull View itemView) {
            super(itemView);

            cardDoctorList = itemView.findViewById(R.id.cardDoctorList);
            doctorNamePlace = itemView.findViewById(R.id.doctorNamePlace);
            doctorClinicHospitalNamePlace = itemView.findViewById(R.id.doctorClinicHospitalNamePlace);
            doctorClinicHospitalAddressPlace = itemView.findViewById(R.id.doctorClinicHospitalAddressPlace);

        }
    }

    private void getDoctorDetails(final Holder holder, String doctorId){
        MyFirebaseDatabase.USERS_REFERENCE.child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null){

                    try{

                        User user = dataSnapshot.getValue(User.class);
                        if (user != null){
                            holder.doctorNamePlace.setText(user.getDoctorName());
                            holder.doctorClinicHospitalNamePlace.setText(user.getDoctorClinicHospitalName());
                            holder.doctorClinicHospitalAddressPlace.setText(user.getDoctorClinicHospitalAddress());
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
