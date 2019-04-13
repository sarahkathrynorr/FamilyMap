package src.DAO;

import src.Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Persons Database Access Object -
 * Here new people are added to the world
 * and removed from the world (RIP)
 * and information on the person can be updated
 */

public class PersonDatabaseAccess {
    private Connection conn;

    public PersonDatabaseAccess(Connection conn)
    {
        this.conn = conn;
    }


    /**
     * Add a new person (row) to the persons table
     *
     * - we love new babies here
     * calls the updatePerson method
     * @param personDetails - object body that contains:
     *  personId - numeric Id of person's row to be changed
     *  firstName - if this is null the first name is not changed
     *  lastName - if this is null the last name is not changed
     *  gender - this really shouldn't be changed in the first place
     *  username - descendant of the person added
     *  father - numeric value for id of father, who is the daddy
     *  mother - numeric value for the id of mother, who is ur ma
     *  spouse - numeric value for the id of their sweetheart)
     *
     * @throws DataAccessException
     *
     * @return Object body of person just added
     */
    public boolean addPerson(Person personDetails) throws DataAccessException {
        if (!personDetails.getGender().toUpperCase().equals("M") && !personDetails.getGender().toUpperCase().equals("F")) {
            throw new DataAccessException("Person gender must be M or F");
        }
        else {
            boolean commit = true;
            //We can structure our string to be similar to a sql command, but if we insert question
            //marks we can change them later with help from the statement
            String sql = "INSERT INTO Person (PersonID, Descendant, FirstName, LastName, " +
                    "Gender, Father, Mother, Spouse) VALUES(?,?,?,?,?,?,?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                //Using the statements built-in set(type) functions we can pick the question mark we want
                //to fill in and give it a proper value. The first argument corresponds to the first
                //question mark found in our sql String
                stmt.setString(1, personDetails.getPersonId());
                stmt.setString(2, personDetails.getDescendant());
                stmt.setString(3, personDetails.getFirstName());
                stmt.setString(4, personDetails.getLastName());
                stmt.setString(5, personDetails.getGender());
                stmt.setString(6, personDetails.getFather());
                stmt.setString(7, personDetails.getMother());
                stmt.setString(8, personDetails.getSpouse());

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                throw new DataAccessException("Error inserting new Person into the database");
            }

            return commit;
        }
    }

     /**
     * Retrieves everything the from a row in the person table
     *
     * @param personId - numeric Id of the person's row to get
     *
     * - if the personId does not exists then null is returned
     * @throws DataAccessException
     *
     * @return personObj - an object with all the parts of the person all nicely put together
     *
     */
    public Person getPerson(String personId, String descendant) throws DataAccessException {
        Person person = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE PersonID = ?" +
                "AND Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personId);
            stmt.setString(2, descendant);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("Descendant"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("Father"),
                        rs.getString("Mother"), rs.getString("Spouse"));
                return person;
            }
            else {
                throw new DataAccessException("Person not found in database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new DataAccessException("Error encountered while finding person");
        }
    }

    /**
     * Retrieves all the data from the entire table of persons
     *
     * @param descendant - the username of the currentUser
     *
     * @return personsArray - an array a personObjects associated with the user
     *
     * @throws DataAccessException
     */
    public ArrayList<Person> getAllPersons(String descendant) throws DataAccessException {
        Person person = null;
        ArrayList<Person> persons = new ArrayList<Person>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, descendant);
            rs = stmt.executeQuery();
            if (rs.next()) {
                do {
                    person = new Person(rs.getString("PersonID"), rs.getString("Descendant"),
                            rs.getString("FirstName"), rs.getString("LastName"),
                            rs.getString("Gender"), rs.getString("Father"),
                            rs.getString("Mother"), rs.getString("Spouse"));
                    persons.add(person);
                } while (rs.next());
                return persons;
            }
            else if (!rs.next()) {
                throw new DataAccessException("No people found in Database to return");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new DataAccessException("Error encountered while finding persons related to " + descendant);
        }
        return null;
    }

    /**
     * Removes all the persons from the table
     *
     * @throws DataAccessException
     *
     * @return boolean success value
     */
    public boolean removeAllPersons(String descendant) throws DataAccessException {
        String sql = "DELETE FROM Person WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, descendant);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new DataAccessException("Error encountered while removing persons");
        }

    }
}