package dao;

import config.DatabaseConfig;
import model.BloodUnit;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BloodUnitDAO {

    public void addBloodUnit(BloodUnit unit) throws SQLException {
        String sql = "INSERT INTO BloodUnit (donor_id, event_id, blood_group, volume_ml, collection_date, expiry_date, tested, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, unit.getDonorId());
            pstmt.setInt(2, unit.getEventId());
            pstmt.setString(3, unit.getBloodGroup());
            pstmt.setInt(4, unit.getVolumeMl());
            pstmt.setDate(5, Date.valueOf(unit.getCollectionDate()));
            pstmt.setDate(6, Date.valueOf(unit.getExpiryDate()));
            pstmt.setBoolean(7, unit.isTested());
            pstmt.setString(8, unit.getStatus());
            pstmt.executeUpdate();
        }
    }

    public BloodUnit getBloodUnitById(int unitId) throws SQLException {
        String sql = "SELECT * FROM BloodUnit WHERE unit_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, unitId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractBloodUnit(rs);
            }
        }
        return null;
    }

    public List<BloodUnit> getAllBloodUnits() throws SQLException {
        List<BloodUnit> units = new ArrayList<>();
        String sql = "SELECT * FROM BloodUnit ORDER BY collection_date DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                units.add(extractBloodUnit(rs));
            }
        }
        return units;
    }

    public List<BloodUnit> getBloodUnitsByEvent(int eventId) throws SQLException {
        List<BloodUnit> units = new ArrayList<>();
        String sql = "SELECT * FROM BloodUnit WHERE event_id = ? ORDER BY collection_date DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                units.add(extractBloodUnit(rs));
            }
        }
        return units;
    }

    public List<BloodUnit> getBloodUnitsByDonor(int donorId) throws SQLException {
        List<BloodUnit> units = new ArrayList<>();
        String sql = "SELECT * FROM BloodUnit WHERE donor_id = ? ORDER BY collection_date DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, donorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                units.add(extractBloodUnit(rs));
            }
        }
        return units;
    }

    public List<BloodUnit> getAvailableUnits() throws SQLException {
        List<BloodUnit> units = new ArrayList<>();
        String sql = "SELECT * FROM BloodUnit WHERE status = 'Available' AND expiry_date >= CURDATE() AND tested = TRUE ORDER BY expiry_date ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                units.add(extractBloodUnit(rs));
            }
        }
        return units;
    }

    public void updateBloodUnit(BloodUnit unit) throws SQLException {
        String sql = "UPDATE BloodUnit SET donor_id = ?, event_id = ?, blood_group = ?, volume_ml = ?, collection_date = ?, expiry_date = ?, tested = ?, status = ? WHERE unit_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, unit.getDonorId());
            pstmt.setInt(2, unit.getEventId());
            pstmt.setString(3, unit.getBloodGroup());
            pstmt.setInt(4, unit.getVolumeMl());
            pstmt.setDate(5, Date.valueOf(unit.getCollectionDate()));
            pstmt.setDate(6, Date.valueOf(unit.getExpiryDate()));
            pstmt.setBoolean(7, unit.isTested());
            pstmt.setString(8, unit.getStatus());
            pstmt.setInt(9, unit.getUnitId());
            pstmt.executeUpdate();
        }
    }

    public void deleteBloodUnit(int unitId) throws SQLException {
        String sql = "DELETE FROM BloodUnit WHERE unit_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, unitId);
            pstmt.executeUpdate();
        }
    }

    private BloodUnit extractBloodUnit(ResultSet rs) throws SQLException {
        return new BloodUnit(
                rs.getInt("unit_id"),
                rs.getInt("donor_id"),
                rs.getInt("event_id"),
                rs.getString("blood_group"),
                rs.getInt("volume_ml"),
                rs.getDate("collection_date").toLocalDate(),
                rs.getDate("expiry_date").toLocalDate(),
                rs.getBoolean("tested"),
                rs.getString("status")
        );
    }
}
