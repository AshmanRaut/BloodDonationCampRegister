package dao;

import config.DatabaseConfig;
import model.DistributionRecord;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DistributionDAO {

    public void addDistributionRecord(DistributionRecord record) throws SQLException {
        String sql = "INSERT INTO DistributionRecord (event_id, distribution_date, total_units_available, hospital_name, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getEventId());
            pstmt.setDate(2, Date.valueOf(record.getDistributionDate()));
            pstmt.setInt(3, record.getTotalUnitsAvailable());
            pstmt.setString(4, record.getHospitalName());
            pstmt.setString(5, record.getNotes());
            pstmt.executeUpdate();
        }
    }

    public DistributionRecord getRecordById(int distributionId) throws SQLException {
        String sql = "SELECT * FROM DistributionRecord WHERE distribution_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, distributionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractRecord(rs);
            }
        }
        return null;
    }

    public DistributionRecord getRecordByEventId(int eventId) throws SQLException {
        String sql = "SELECT * FROM DistributionRecord WHERE event_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractRecord(rs);
            }
        }
        return null;
    }

    public List<DistributionRecord> getAllRecords() throws SQLException {
        List<DistributionRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM DistributionRecord ORDER BY distribution_date DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(extractRecord(rs));
            }
        }
        return records;
    }

    public void updateDistributionRecord(DistributionRecord record) throws SQLException {
        String sql = "UPDATE DistributionRecord SET distribution_date = ?, total_units_available = ?, total_units_distributed = ?, total_volume_distributed = ?, hospital_name = ?, notes = ? WHERE distribution_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(record.getDistributionDate()));
            pstmt.setInt(2, record.getTotalUnitsAvailable());
            pstmt.setInt(3, record.getTotalUnitsDistributed());
            pstmt.setInt(4, record.getTotalVolumeDistributed());
            pstmt.setString(5, record.getHospitalName());
            pstmt.setString(6, record.getNotes());
            pstmt.setInt(7, record.getDistributionId());
            pstmt.executeUpdate();
        }
    }

    public void deleteDistributionRecord(int distributionId) throws SQLException {
        String sql = "DELETE FROM DistributionRecord WHERE distribution_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, distributionId);
            pstmt.executeUpdate();
        }
    }

    private DistributionRecord extractRecord(ResultSet rs) throws SQLException {
        return new DistributionRecord(
                rs.getInt("distribution_id"),
                rs.getInt("event_id"),
                rs.getDate("distribution_date").toLocalDate(),
                rs.getInt("total_units_available"),
                rs.getInt("total_units_distributed"),
                rs.getInt("total_volume_distributed"),
                rs.getString("hospital_name"),
                rs.getString("notes")
        );
    }
}
