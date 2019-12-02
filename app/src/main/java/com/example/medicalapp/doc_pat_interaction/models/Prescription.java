package com.example.medicalapp.doc_pat_interaction.models;

import java.util.List;

public class Prescription {

    String prescriptionDate, prescriptionDuration, prescriptionDetail;

    public Prescription() {
    }

    public Prescription(String prescriptionDate, String prescriptionDuration, String prescriptionDetail) {
        this.prescriptionDate = prescriptionDate;
        this.prescriptionDuration = prescriptionDuration;
        this.prescriptionDetail = prescriptionDetail;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public String getPrescriptionDuration() {
        return prescriptionDuration;
    }

    public String getPrescriptionDetail() {
        return prescriptionDetail;
    }
}
