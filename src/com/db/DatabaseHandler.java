package com.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handler class for the database
 */
public class DatabaseHandler {
    private static String connectionUrl;
    private static Connection con;
    private static Statement stmt;

    /**
     * Attempts to establish a connection to the database.
     * 
     * @param serverName   - Server Instance Name
     * @param port         - Connection Port
     * @param databaseName - Database Name
     * @param user         - Database Username
     * @param password     - Database Password
     * @throws SQLException
     */
    public static void connect(String serverName, String port, String databaseName, String user, String password)
            throws SQLException {
        if (!isClosed())
            return; // If is already connected

        connectionUrl = "jdbc:sqlserver://" + serverName + ":" + port + ";databaseName=" + databaseName + ";user="
                + user + ";password=" + password + ";" + "trustServerCertificate=true";

        con = DriverManager.getConnection(connectionUrl);
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    /**
     * Releases the connection to the database
     * 
     * @return "false" if the database is already closed, otherwise "true"
     * @throws SQLException
     */
    public static boolean disconnect() throws SQLException {
        if (!isClosed()) {
            con.close();
            return true;
        }
        return false;
    }

    /**
     * Executes a SELECT type query
     * 
     * @param Query - SQL Query to be execute
     * @return Results of the query
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed Statement,
     *                      the given SQL statement produces anything other than a
     *                      single ResultSet object,
     *                      the method is called on a PreparedStatement or
     *                      CallableStatement
     */
    public static ResultSet executeSelect(String Query) throws SQLException {
        // Prevents to execute injected SQL
        if (Query.contains("--") || Query.contains("/*"))
            return null;
        ResultSet rs = stmt.executeQuery(Query);
        return rs;
    }

    /**
     * Execute an INSERT, DELETE, UPDATE type query
     * 
     * @param query - SQL Query to be execute
     * @return Number of affected rows
     * @throws SQLException if a database access error occurs, this method is called
     *                      on a closed Statement,
     *                      the given SQL statement produces a ResultSet object,
     *                      the method is called on a PreparedStatement or
     *                      CallableStatement
     */
    public static int executeOperation(String Query) throws SQLException {
        // Prevents to execute injected SQL
        if (Query.contains("--") || Query.contains("/*"))
            return -1;
        return stmt.executeUpdate(Query);
    }

    /**
     * Executes a Stored Procedure with/without arguments
     * 
     * @param procedureName - Name of the Stored Procedure
     * @param args          - SP Parameters (if necessary)
     * @return - ResultSet of the execution
     * @throws SQLException if a database access error occurs. Also can be caused if
     *                      the Stored Procedure doens't returns a ResultSet (SP
     *                      that updates the DB)
     */
    public static ResultSet executeSP_W_ResultSet(String procedureName, String... args) throws SQLException {
        StringBuilder spString = new StringBuilder();
        spString.append("{call ").append(procedureName);
        if (args != null && args.length > 0) {
            spString.append("(");
            for (int i = 0; i < args.length; i++) {
                spString.append("?").append(", ");
            }
            spString.deleteCharAt(spString.length() - 2).append(")");
        }
        spString.append("}");

        CallableStatement spCall = con.prepareCall(spString.toString());

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                try {
                    spCall.setInt(i + 1, Integer.parseInt(args[i]));
                    continue;
                } catch (NumberFormatException e) {
                    // If the argument is not a number
                    // TODO: handle exception
                }
                spCall.setString(i, args[i]);
            }
        }

        return spCall.executeQuery();
    }

    /**
     * Executes a Stored Procedure with/without arguments for updating the Database
     * (changes the data)
     * 
     * @param procedureName - Name of the Stored Procedure
     * @param args          - SP Parameters (if necessary)
     * @return - Number of affected rows
     * @throws SQLException if a database access error occurs. Also can be caused if
     *                      the Stored Procedure returns a ResultSet (SP
     *                      that fetch data)
     */
    public static int executeSP_Update(String procedureName, String... args) throws SQLException {
        StringBuilder spString = new StringBuilder();
        spString.append("{call ").append(procedureName);
        if (args != null && args.length > 0) {
            spString.append("(");
            for (int i = 0; i < args.length; i++) {
                spString.append("?").append(", ");
            }
            spString.deleteCharAt(spString.length() - 2).append(")");
        }
        spString.append("}");

        CallableStatement spCall = con.prepareCall(spString.toString());

        for (int i = 0; i < args.length; i++) {
            try {
                spCall.setInt(i, Integer.parseInt(args[i]));
                continue;
            } catch (NumberFormatException e) {
                // TODO: handle exception
            }
            spCall.setString(i, args[i]);
        }

        return spCall.executeUpdate();
    }

    /**
     * Checks if the conection is closed
     * 
     * @return "true" if is closed, otherwise "false"
     * @throws SQLException if a database access error occurs
     */
    public static boolean isClosed() throws SQLException {
        return con != null ? con.isClosed() : true;
    }
}
