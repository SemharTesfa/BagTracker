package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
    public static Connection Connector() {
        try {
            // Load the JDBC driver for SQLite
            Class.forName("org.sqlite.JDBC");

            // Establish a connection to the SQLite database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:quillqube.db");

            // Define SQL statement to create the "Projects" table if it doesn't exist
            String createProjectsTableSQL = "CREATE TABLE IF NOT EXISTS Projects (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "startDate TEXT, " +
                    "description TEXT" +
                    ");";

            String createTicketsTableSQL = "CREATE TABLE IF NOT EXISTS Tickets (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "project_name TEXT NOT NULL, " +
                    "title TEXT NOT NULL, " +
                    "description TEXT" +
                    ");";
            
            String createCommentTableSQL = "CREATE TABLE IF NOT EXISTS Comments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ticket_name TEXT NOT NULL, " +
                    "comment TEXT NOT NULL, " +
                    "date TEXT " +
                    ");";



            // Execute the SQL statements to create the tables
            conn.createStatement().execute(createProjectsTableSQL);
            conn.createStatement().execute(createTicketsTableSQL);
            conn.createStatement().execute(createCommentTableSQL);

            // Return the database connection
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Return null if the connection fails
        return null;
    }
}
