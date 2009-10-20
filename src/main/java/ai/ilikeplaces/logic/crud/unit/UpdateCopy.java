package ai.ilikeplaces.logic.crud.unit;

import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import java.lang.reflect.Field;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class UpdateCopy<T> {

    final static Logger logger = LoggerFactory.getLogger(UpdateCopy.class);

    public boolean update(final T copyToThis, final T copyFromThis, final Field field) {
        boolean returnVal = false;
        try {
            field.set(copyToThis, field.get(copyFromThis));
            returnVal = true;
        } catch (IllegalArgumentException ex) {
            returnVal = false;
            logger.error("{}", ex);
            throw new IllegalArgumentException(ex);
        } catch (IllegalAccessException ex) {
            returnVal = false;
            logger.error("{}", ex);
            throw new IllegalArgumentException(ex);
        }
        return returnVal;
    }
}
