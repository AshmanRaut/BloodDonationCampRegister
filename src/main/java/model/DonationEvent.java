package model;

import java.time.LocalDate;

public class DonationEvent {
    private int eventId;
    private String eventName;
    private LocalDate eventDate;
    private String location;
    private String organizer;
    private int targetUnits;

    public DonationEvent() {}

    public DonationEvent(String eventName, LocalDate eventDate, String location,
                         String organizer, int targetUnits) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.organizer = organizer;
        this.targetUnits = targetUnits;
    }

    public DonationEvent(int eventId, String eventName, LocalDate eventDate, String location,
                         String organizer, int targetUnits) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.organizer = organizer;
        this.targetUnits = targetUnits;
    }

    // Getters and Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }

    public int getTargetUnits() { return targetUnits; }
    public void setTargetUnits(int targetUnits) { this.targetUnits = targetUnits; }

    @Override
    public String toString() {
        return eventName + " (" + eventDate + ")";
    }
}
