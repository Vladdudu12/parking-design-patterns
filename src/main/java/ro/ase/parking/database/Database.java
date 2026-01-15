package ro.ase.parking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String JDBC_URL = "jdbc:h2:./data/parkingdb;AUTO_SERVER=TRUE";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "";

    private static Connection connection;

    private Database() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
        }
        return connection;
    }

    public static void init() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        // USERS
        stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                     "id BIGINT GENERATED ALWAYS AS IDENTITY, " +
                     "name VARCHAR(255), " +
                     "email VARCHAR(255));");

        stmt.execute("INSERT INTO users (name, email) VALUES ('Alex', 'alex@mail.com');");
        stmt.execute("INSERT INTO users (name, email) VALUES ('Maria', 'maria@mail.com');");

        // PARKING SPOTS
        stmt.execute("CREATE TABLE IF NOT EXISTS parking_spots (" +
                     "id BIGINT GENERATED ALWAYS AS IDENTITY, " +
                     "price_per_hour DOUBLE, " +
                     "status VARCHAR(50), " +
                     "current_user_id BIGINT);");

        stmt.execute("INSERT INTO parking_spots (price_per_hour, status, current_user_id) VALUES (10, 'LIBER', NULL);");
        stmt.execute("INSERT INTO parking_spots (price_per_hour, status, current_user_id) VALUES (12, 'LIBER', NULL);");
        stmt.execute("INSERT INTO parking_spots (price_per_hour, status, current_user_id) VALUES (8, 'LIBER', NULL);");
        stmt.execute("INSERT INTO parking_spots (price_per_hour, status, current_user_id) VALUES (15, 'LIBER', NULL);");
        stmt.execute("INSERT INTO parking_spots (price_per_hour, status, current_user_id) VALUES (9, 'LIBER', NULL);");

        // RESERVATIONS
        stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                     "id BIGINT GENERATED ALWAYS AS IDENTITY, " +
                     "user_id BIGINT, " +
                     "spot_id BIGINT, " +
                     "start_time TIMESTAMP, " +
                     "end_time TIMESTAMP);");

        // PARKING SESSIONS
        stmt.execute("CREATE TABLE IF NOT EXISTS parking_sessions (" +
                     "id BIGINT GENERATED ALWAYS AS IDENTITY, " +
                     "spot_id BIGINT, " +
                     "start_time TIMESTAMP, " +
                     "end_time TIMESTAMP);");
    }
}
