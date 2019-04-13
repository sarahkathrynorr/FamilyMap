package src.DAO;
/**
 * Created by westenm on 2/8/19.
 */

public class DataAccessException extends Exception {
    public DataAccessException(String message)
    {
        super(message);
    }

    DataAccessException()
    {
        super();
    }
}
