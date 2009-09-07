package ai.ilikeplaces;

import javax.ejb.*;

/**
 *
 * @author Ravindranath Akila
 */
@Local
public interface TempLocal {
    public CrudServiceLocal temp();

}
