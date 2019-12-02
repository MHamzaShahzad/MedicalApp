package com.example.medicalapp.models;

public class Patient {

    String patientPhoneNumber, doctorPhoneNumber, patientName, patientAge, patientHealthProfile, patientRegisteredAt;

    public Patient() {
    }

    public Patient(String patientPhoneNumber, String doctorPhoneNumber, String patientName, String patientAge, String patientHealthProfile, String patientRegisteredAt) {
        this.patientAge = patientAge;
        this.patientPhoneNumber = patientPhoneNumber;
        this.doctorPhoneNumber = doctorPhoneNumber;
        this.patientName = patientName;
        this.patientHealthProfile = patientHealthProfile;
        this.patientRegisteredAt = patientRegisteredAt;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public String getPatientPhoneNumber() {
        return patientPhoneNumber;
    }

    public String getDoctorPhoneNumber() {
        return doctorPhoneNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientHealthProfile() {
        return patientHealthProfile;
    }

    public String getPatientRegisteredAt() {
        return patientRegisteredAt;
    }
}
