package model;

import java.time.LocalDate;

public class Donor {
    private int donorId;
    private String name;
    private String phone;
    private String email;
    private String bloodGroup;
    private LocalDate dateOfBirth;
    private String address;
    private LocalDate lastDonationDate;

    public Donor() {}

    public Donor(String name, String phone, String email, String bloodGroup,
                 LocalDate dateOfBirth, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Donor(int donorId, String name, String phone, String email, String bloodGroup,
                 LocalDate dateOfBirth, String address, LocalDate lastDonationDate) {
        this.donorId = donorId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.lastDonationDate = lastDonationDate;
    }

    // Getters and Setters
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
