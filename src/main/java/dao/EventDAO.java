package dao;

import config.DatabaseConfig;
import model.DonationEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public void addEvent(DonationEvent event) throws SQLException {
        String sql = "INSERT INTO DonationEvent (event_name, event_date, location, organizer, target_units) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getEventName());
            pstmt.setDate(2, Date.valueOf(event.getEventDate()));
            pstmt.setString(3, event.getLocation());
            pstmt.setString(4, event.getOrganizer());
            pstmt.setInt(5, event.getTargetUnits());
            pstmt.executeUpdate();
        }
    }

    public DonationEvent getEventById(int eventId) throws SQLException {
        String sql = "SELECT * FROM DonationEvent WHERE event_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractEvent(rs);
            }
        }
        return null;
    }

    public List<DonationEvent> getAllEvents() throws SQLException {
        List<DonationEvent> events = new ArrayList<>();
        String sql = "SELECT * FROM DonationEvent ORDER BY event_date DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(extractEvent(rs));
            }
        }
        return events;
    }

    public void updateEvent(DonationEvent event) throws SQLException {
        String sql = "UPDATE DonationEvent SET event_name = ?, event_date = ?, location = ?, organizer = ?, target_units = ? WHERE event_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getEventName());
            pstmt.setDate(2, Date.valueOf(event.getEventDate()));
            pstmt.setString(3, event.getLocation());
            pstmt.setString(4, event.getOrganizer());
            pstmt.setInt(5, event.getTargetUnits());
            pstmt.setInt(6, event.getEventId());
            pstmt.executeUpdate();
        }
    }

    public void deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM DonationEvent WHERE event_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.executeUpdate();
        }
    }

    private DonationEvent extractEvent(ResultSet rs) throws SQLException {
        return new DonationEvent(
                rs.getInt("event_id"),
                rs.getString("event_name"),
                rs.getDate("event_date").toLocalDate(),
                rs.getString("location"),
                rs.getString("organizer"),
                rs.getInt("target_units")
        );
    }
}
