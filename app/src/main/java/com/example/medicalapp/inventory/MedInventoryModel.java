package com.example.medicalapp.inventory;

import java.io.Serializable;

public class MedInventoryModel implements Serializable {

    String medName, medDetail, medAddedAt, medExpiryDate, medType;
    int mId, medStock, medPerDay, isNotifiedAboutExpiry;

    public MedInventoryModel() {
    }

    public MedInventoryModel( String medName, String medDetail, String medAddedAt, String medExpiryDate, String medType, int medStock, int medPerDay, int isNotifiedAboutExpiry) {
        this.medName = medName;
        this.medDetail = medDetail;
        this.medAddedAt = medAddedAt;
        this.medExpiryDate = medExpiryDate;
        this.medType = medType;
        this.medStock = medStock;
        this.medPerDay = medPerDay;
        this.isNotifiedAboutExpiry = isNotifiedAboutExpiry;
    }

    public MedInventoryModel(int mId, String medName, String medDetail, String medAddedAt, String medExpiryDate, String medType, int medStock, int medPerDay, int isNotifiedAboutExpiry) {
        this.mId = mId;
        this.medName = medName;
        this.medDetail = medDetail;
        this.medAddedAt = medAddedAt;
        this.medExpiryDate = medExpiryDate;
        this.medType = medType;
        this.medStock = medStock;
        this.medPerDay = medPerDay;
        this.isNotifiedAboutExpiry = isNotifiedAboutExpiry;
    }

    public int getmId() {
        return mId;
    }

    public String getMedName() {
        return medName;
    }

    public String getMedDetail() {
        return medDetail;
    }

    public String getMedAddedAt() {
        return medAddedAt;
    }

    public String getMedExpiryDate() {
        return medExpiryDate;
    }

    public String getMedType() {
        return medType;
    }

    public int getMedStock() {
        return medStock;
    }

    public int getMedPerDay() {
        return medPerDay;
    }

    public int getIsNotifiedAboutExpiry() {
        return isNotifiedAboutExpiry;
    }
}
