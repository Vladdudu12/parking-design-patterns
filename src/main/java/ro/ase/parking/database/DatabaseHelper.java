package ro.ase.parking.database;

import ro.ase.parking.model.ParkingSession;
import ro.ase.parking.model.ParkingSpot;
import ro.ase.parking.model.Reservation;
import ro.ase.parking.model.User;
import ro.ase.parking.state.AvailableState;
import ro.ase.parking.state.OccupiedState;
import ro.ase.parking.state.ReservedState;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public static User findUserById(long id) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id=?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"));
        }
        return null;
    }

    public static ParkingSpot findSpotById(long id) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM parking_spots WHERE id=?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ParkingSpot spot = new ParkingSpot(
                    rs.getLong("id"),
                    rs.getDouble("price_per_hour")
            );
            spot.setState(switch(rs.getString("status")) {
                case "LIBER" -> new AvailableState();
                case "REZERVAT" -> new ReservedState();
                case "OCUPAT" -> new OccupiedState();
                default -> new AvailableState();
            });

            long userId = rs.getLong("current_user_id");
            if (!rs.wasNull()) {
                spot.setCurrentUser(findUserById(userId));
            }

            ps = conn.prepareStatement("SELECT * FROM reservations WHERE spot_id=? ORDER BY end_time DESC LIMIT 1");
            ps.setLong(1, id);
            ResultSet resRs = ps.executeQuery();
            if(resRs.next()) {
                Reservation reservation = new Reservation(
                        resRs.getLong("id"),
                        findUserById(resRs.getLong("user_id")),
                        resRs.getTimestamp("start_time").toLocalDateTime(),
                        resRs.getTimestamp("end_time").toLocalDateTime()
                );
                spot.setReservation(reservation);
            }

            ps = conn.prepareStatement("SELECT * from parking_sessions where spot_id=? LIMIT 1");
            ps.setLong(1, id);
            ResultSet sesRs = ps.executeQuery();
            if (sesRs.next()) {
                ParkingSession parkingSession = new ParkingSession(sesRs.getLong("id") ,sesRs.getTimestamp("start_time").toLocalDateTime());
                spot.setSession(parkingSession);
            }

            return spot;
        }
        return null;
    }

    public static List<ParkingSpot> getAllSpots() throws SQLException {
        List<ParkingSpot> spots = new ArrayList<>();
        Connection conn = Database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM parking_spots");
        while(rs.next()) {
            ParkingSpot spot = new ParkingSpot(
                    rs.getLong("id"),
                    rs.getDouble("price_per_hour")
            );
            spot.setState(switch(rs.getString("status")) {
                case "LIBER" -> new AvailableState();
                case "REZERVAT" -> new ReservedState();
                case "OCUPAT" -> new OccupiedState();
                default -> new AvailableState();
            });
            long userId = rs.getLong("current_user_id");
            if (!rs.wasNull()) {
                spot.setCurrentUser(findUserById(userId));
            }
            spots.add(spot);
        }
        return spots;
    }

    public static void updateSpot(ParkingSpot spot) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE parking_spots SET status=?, current_user_id=? WHERE id=?"
        );
        ps.setString(1, spot.getStatus());
        ps.setObject(2, spot.getCurrentUser() != null ? spot.getCurrentUser().getId() : null);
        ps.setLong(3, spot.getId());
        ps.executeUpdate();

        if (spot.getSession() != null) {
            insertSession(spot, spot.getSession());
        }
    }

    public static void insertReservation(Reservation reservation, ParkingSpot spot) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO reservations (user_id, spot_id, start_time, end_time) VALUES (?, ?, ?, ?)"
        );
        ps.setLong(1, reservation.getUser().getId());
        ps.setLong(2, spot.getId());
        ps.setTimestamp(3, Timestamp.valueOf(reservation.getStart()));
        ps.setTimestamp(4, Timestamp.valueOf(reservation.getEnd()));
        ps.executeUpdate();
    }

    public static void insertSession(ParkingSpot spot, ParkingSession session) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO parking_sessions (spot_id, start_time, end_time) VALUES (?, ?, ?)"
        );
        ps.setLong(1, spot.getId());
        ps.setTimestamp(2, Timestamp.valueOf(session.getStart()));
        ps.setTimestamp(3, session.getEnd() != null ? Timestamp.valueOf(session.getEnd()) : null);
        ps.executeUpdate();
    }
}

