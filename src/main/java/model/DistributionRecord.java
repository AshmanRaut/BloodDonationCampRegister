package model;

import java.time.LocalDate;

public class DistributionRecord {
    private int distributionId;
    private int eventId;
    private LocalDate distributionDate;
    private int totalUnitsAvailable;
    private int totalUnitsDistributed;
    private int totalVolumeDistributed;
    private String hospitalName;
    private String notes;

    public DistributionRecord() {}

    public DistributionRecord(int eventId, LocalDate distributionDate, int totalUnitsAvailable,
                              String hospitalName) {
        this.eventId = eventId;
        this.distributionDate = distributionDate;
        this.totalUnitsAvailable = totalUnitsAvailable;
        this.hospitalName = hospitalName;
        this.totalUnitsDistributed = 0;
        this.totalVolumeDistributed = 0;
    }

    public DistributionRecord(int distributionId, int eventId, LocalDate distributionDate,
                              int totalUnitsAvailable, int totalUnitsDistributed,
                              int totalVolumeDistributed, String hospitalName, String notes) {
        this.distributionId = distributionId;
        this.eventId = eventId;
        this.distributionDate = distributionDate;
        this.totalUnitsAvailable = totalUnitsAvailable;
        this.totalUnitsDistributed = totalUnitsDistributed;
        this.totalVolumeDistributed = totalVolumeDistributed;
        this.hospitalName = hospitalName;
        this.notes = notes;
    }

    // Getters and Setters
    public int getDistributionId() { return distributionId; }
    public void setDistributionId(int distributionId) { this.distributionId = distributionId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public LocalDate getDistributionDate() { return distributionDate; }
    public void setDistributionDate(LocalDate distributionDate) { this.distributionDate = distributionDate; }

    public int getTotalUnitsAvailable() { return totalUnitsAvailable; }
    public void setTotalUnitsAvailable(int totalUnitsAvailable) { this.totalUnitsAvailable = totalUnitsAvailable; }

    public int getTotalUnitsDistributed() { return totalUnitsDistributed; }
    public void setTotalUnitsDistributed(int totalUnitsDistributed) { this.totalUnitsDistributed = totalUnitsDistributed; }

    public int getTotalVolumeDistributed() { return totalVolumeDistributed; }
    public void setTotalVolumeDistributed(int totalVolumeDistributed) { this.totalVolumeDistributed = totalVolumeDistributed; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Distribution " + distributionId + " - Event " + eventId;
    }
}