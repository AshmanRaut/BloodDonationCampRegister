package model;

import java.time.LocalDate;

public class BloodUnit {
    private int unitId;
    private int donorId;
    private int eventId;
    private String bloodGroup;
    private int volumeMl;
    private LocalDate collectionDate;
    private LocalDate expiryDate;
    private boolean tested;
    private String status;

    public BloodUnit() {}

    public BloodUnit(int donorId, int eventId, String bloodGroup, int volumeMl,
                     LocalDate collectionDate, LocalDate expiryDate, boolean tested) {
        this.donorId = donorId;
        this.eventId = eventId;
        this.bloodGroup = bloodGroup;
        this.volumeMl = volumeMl;
        this.collectionDate = collectionDate;
        this.expiryDate = expiryDate;
        this.tested = tested;
        this.status = "Available";
    }

    public BloodUnit(int unitId, int donorId, int eventId, String bloodGroup, int volumeMl,
                     LocalDate collectionDate, LocalDate expiryDate, boolean tested, String status) {
        this.unitId = unitId;
        this.donorId = donorId;
        this.eventId = eventId;
        this.bloodGroup = bloodGroup;
        this.volumeMl = volumeMl;
        this.collectionDate = collectionDate;
        this.expiryDate = expiryDate;
        this.tested = tested;
        this.status = status;
    }

    // Getters and Setters
    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }

    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public int getVolumeMl() { return volumeMl; }
    public void setVolumeMl(int volumeMl) { this.volumeMl = volumeMl; }

    public LocalDate getCollectionDate() { return collectionDate; }
    public void setCollectionDate(LocalDate collectionDate) { this.collectionDate = collectionDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public boolean isTested() { return tested; }
    public void setTested(boolean tested) { this.tested = tested; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Unit " + unitId + " - " + bloodGroup + " (" + status + ")";
    }
}
