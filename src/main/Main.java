package main;

import java.sql.SQLException;

import sql.DbConnection;

/** Main application class. */
public final class Main
{
    // TODO don't hard-code this, maybe an env file.
    private static final String URL = "jdbc:mysql://localhost:3306/SnackOverflow";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Main entry point.
     * @param args command line args (ignored)
     */
    public static void main(String[] args) throws SQLException
    {
        try (var database = DbConnection.connect(URL, USER, PASSWORD);
             var dialog = new Dialog())
        {
            dialog.open(database);
        }
    }
}
