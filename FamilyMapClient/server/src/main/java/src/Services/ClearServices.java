package src.Services;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.RequestResponse.ClearResponse;

import javax.xml.crypto.Data;

/**
 * services for clearing the tables in the family map database
 */
public class ClearServices {
        /**
         * cleans out all the tables in the db
         *
         * @throws src.DAO.DataAccessException
         *
         * @return RequestResponse.clearResponse
         */
        public ClearResponse clearEverything() throws DataAccessException {
            Database myDb = new Database();

            try {
                myDb.clearTables();
            } catch (DataAccessException e) {

                e.printStackTrace();
                ClearResponse clearResponse = new ClearResponse();
                clearResponse.setResponse(e.getMessage());
                return clearResponse;
            }

            return new ClearResponse();
        }


}
