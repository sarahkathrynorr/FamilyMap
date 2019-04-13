package src.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by westenm on 2/4/19.
 */

public class Database {
    private Connection conn;

    public Database(){
        try {
            //clearTables();
            createTables();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    static {
        try {
            //This is how we set up the driver for our database
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions
    public Connection openConnection() throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The pathing assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.

    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                //This will commit the changes to the database
                conn.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables() throws DataAccessException {

        openConnection();

        try (Statement stmt = conn.createStatement()){
            //First lets open our connection to our database.

            //We pull out a statement from the connection we just established
            //Statements are the basis for our transactions in SQL
            //Format this string to be exactly like a sql create table command
            String sql = "CREATE TABLE if not exists \"User\" (\n" +
                    "\t\"Username\"\tTEXT NOT NULL PRIMARY KEY,\n" +
                    "\t\"Password\"\tTEXT NOT NULL,\n" +
                    "\t\"Email\"\tTEXT NOT NULL,\n" +
                    "\t\"FirstName\"\tTEXT NOT NULL,\n" +
                    "\t\"LastName\"\tTEXT NOT NULL,\n" +
                    "\t\"Gender\"\tTEXT NOT NULL,\n" +
                    "\t\"PersonID\"\tTEXT NOT NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE if not exists \"Person\" (\n" +
                    "\t\"PersonID\"\tTEXT NOT NULL UNIQUE,\n" +
                    "\t\"Descendant\"\tTEXT NOT NULL,\n" +
                    "\t\"FirstName\"\tTEXT NOT NULL,\n" +
                    "\t\"LastName\"\tTEXT NOT NULL,\n" +
                    "\t\"Gender\"\tTEXT NOT NULL,\n" +
                    "\t\"Mother\"\tTEXT,\n" +
                    "\t\"Father\"\tTEXT,\n" +
                    "\t\"Spouse\"\tTEXT\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE if not exists \"Events\" (\n" +
                    "\t\"EventId\"\tTEXT NOT NULL PRIMARY KEY UNIQUE,\n" +
                    "\t\"Descendant\"\tTEXT NOT NULL,\n" +
                    "\t\"PersonID\"\tTEXT NOT NULL,\n" +
                    "\t\"Latitude\"\tTEXT NOT NULL,\n" +
                    "\t\"Longitude\"\tTEXT NOT NULL,\n" +
                    "\t\"Country\"\tTEXT NOT NULL,\n" +
                    "\t\"City\"\tTEXT NOT NULL,\n" +
                    "\t\"EventType\"\tTEXT NOT NULL,\n" +
                    "\t\"Year\" INTEGER NOT NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE if not exists \"AuthToken\" (\n" +
                    "\t\"Auth_token\" TEXT UNIQUE,\n" +
                    "\t\"Username\" TEXT,\n" +
                    "\t\"PersonID\" TEXT,\n" +
                    "\t\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                    ");";

            stmt.executeUpdate(sql);
            //if we got here without any problems we successfully created the table and can commit
            closeConnection(true);
        } catch (DataAccessException e) {
            //if our table creation caused an error, we can just not commit the changes that did happen
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            //if our table creation caused an error, we can just not commit the changes that did happen
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while creating tables");
        }


    }

    public void clearTables() throws DataAccessException
    {
        openConnection();

        try (Statement stmt = conn.createStatement()){
            String sql = "drop table if exists \"User\";\n" +
                    "drop table if exists \"Person\";\n" +
                    "drop table if exists \"Events\";\n" +
                    "drop table if exists \"AuthToken\";";
            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
