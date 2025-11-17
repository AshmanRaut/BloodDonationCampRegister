package dao;

import config.DatabaseConfig;
import model.Donor;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DonorDAO {

    public void addDonor(Donor donor) throws SQLException {
        String sql = "INSERT INTO Donor (name, phone, email, blood_group, date_of_birth, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, donor.getName());
            pstmt.setString(2, donor.getPhone());
            pstmt.setString(3, donor.getEmail());
            pstmt.setString(4, donor.getBloodGroup());
            pstmt.setDate(5, Date.valueOf(donor.getDateOfBirth()));
            pstmt.setString(6, donor.getAddress());
            pstmt.executeUpdate();
        }
    }

    public Donor getDonorById(int donorId) throws SQLException {
        String sql = "SELECT * FROM Donor WHERE donor_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, donorId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractDonor(rs);
            }
        }
        return null;
    }

    public List<Donor> getAllDonors() throws SQLException {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT * FROM Donor ORDER BY name";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                donors.add(extractDonor(rs));
            }
        }
        return donors;
    }

    public List<Donor> getDonorsByBloodGroup(String bloodGroup) throws SQLException {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT * FROM Donor WHERE blood_group = ? ORDER BY name";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bloodGroup);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                donors.add(extractDonor(rs));
            }
        }
        return donors;
    }

    public void updateDonor(Donor donor) throws SQLException {
        String sql = "UPDATE Donor SET name = ?, phone = ?, email = ?, blood_group = ?, date_of_birth = ?, address = ?, last_donation_date = ? WHERE donor_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, donor.getName());
            pstmt.setString(2, donor.getPhone());
            pstmt.setString(3, donor.getEmail());
            pstmt.setString(4, donor.getBloodGroup());
            pstmt.setDate(5, Date.valueOf(donor.getDateOfBirth()));
            pstmt.setString(6, donor.getAddress());
            pstmt.setDate(7, donor.getLastDonationDate() != null ? Date.valueOf(donor.getLastDonationDate()) : null);
            pstmt.setInt(8, donor.getDonorId());
            pstmt.executeUpdate();
        }
    }

    public void deleteDonor(int donorId) throws SQLException {
        String sql = "DELETE FROM Donor WHERE donor_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, donorId);
            pstmt.executeUpdate();
        }
    }

    private Donor extractDonor(ResultSet rs) throws SQLException {
        return new Donor(
                rs.getInt("donor_id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("blood_group"),
                rs.getDate("date_of_birth").toLocalDate(),
                rs.getString("address"),
                rs.getDate("last_donation_date") != null ? rs.getDate("last_donation_date").toLocalDate() : null
        );
    }
}
