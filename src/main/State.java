package main;

import java.sql.SQLException;

import sql.DbConnection;

/**
 * Symbolizes a state ("menu") for interacting with the user.
 */
public interface State {
    /**
     * Outputs a text to be displayed to the user.
     * 
     * @return the text to be displayed
     * @throws SQLException 
     */
    public String prompt();
    
    /**
     * Handles user input given in the form of a string.
     * 
     * @param input the user input
     * @param con connection to the database
     * @return a new state
     */
    public StateResult handle(String input, DbConnection con);
}
