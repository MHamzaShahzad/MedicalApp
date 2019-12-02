package com.example.medicalapp.models;

public class User {

    String userId, accountType, patientName, patientAge, patientCity, doctorName, doctorClinicHospitalName,
    doctorClinicHospitalAddress, doctorCNIC;

    public User() {
    }

    public User(String userId, String accountType, String patientName, String patientAge, String patientCity) {
        this.userId = userId;
        this.accountType = accountType;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientCity = patientCity;
    }

    public User(String userId, String accountType, String doctorName, String doctorClinicHospitalName, String doctorClinicHospitalAddress, String doctorCNIC) {
        this.userId = userId;
        this.accountType = accountType;
        this.doctorName = doctorName;
        this.doctorClinicHospitalName = doctorClinicHospitalName;
        this.doctorClinicHospitalAddress = doctorClinicHospitalAddress;
        this.doctorCNIC = doctorCNIC;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public String getPatientCity() {
        return patientCity;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorClinicHospitalName() {
        return doctorClinicHospitalName;
    }

    public String getDoctorClinicHospitalAddress() {
        return doctorClinicHospitalAddress;
    }

    public String getDoctorCNIC() {
        return doctorCNIC;
    }
}
