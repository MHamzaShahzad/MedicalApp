package com.example.medicalapp.doc_pat_interaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.R;
import com.example.medicalapp.controllers.MyFirebaseDatabase;
import com.example.medicalapp.models.User;
import com.google.firebase.auth.FirebaseUser;
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
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        getDoctorDetails(holder, doctorsList.get(holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView doctorNamePlace, doctorClinicHospitalNamePlace, doctorClinicHospitalAddressPlace;

        public Holder(@NonNull View itemView) {
            super(itemView);

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
