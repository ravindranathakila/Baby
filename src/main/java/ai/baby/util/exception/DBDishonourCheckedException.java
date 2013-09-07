package ai.baby.util.exception;

import ai.scribble.License;

import javax.ejb.ApplicationException;

/**
 * Throw this exception if the DB is being dishonored by callers
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@ApplicationException(rollback = true)
public class DBDishonourCheckedException extends AbstractEjbApplicationException {

    final static private String MSG = "SORRY! I ENCOUNTERED AN OPERATION WHICH LOGICALLY DISHONORS THE STATE OF THE DB. THE SOURCES SHOULD REFLECT THIS WHERE THE EXCEPTION WAS THROWN. SEE BELOW FOR MORE DETAILS.\n";
    final static public DBDishonourCheckedException QUERYING_AFFIRMATIVELY_A_NON_EXISTING_ENTITY = new DBDishonourCheckedException("QUERYING AFFIRMATIVELY A NON-EXISTENT ENTITY. TRY NOT TO QUERY AN ENTITY WHICH YOU ARE NOT SURE EXISTS IN THE DATABASE, IN THIS MANNER. OR YOU CAN CATCH THIS EXCEPTION AND ACT ACCORDINGLY.");

    final static public DBDishonourCheckedException ADDING_AN_EXISTING_VALUE = new DBDishonourCheckedException("Adding an existing record to database.");
    final static public DBDishonourCheckedException REMOVING_A_NON_EXISTING_VALUE = new DBDishonourCheckedException("Removing a non existing record from database.");

    /**
     * @param message
     */
    public DBDishonourCheckedException(final String message) {
        super(MSG + message);
    }
}
