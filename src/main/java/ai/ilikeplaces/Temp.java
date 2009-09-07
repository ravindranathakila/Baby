package ai.ilikeplaces;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import ai.ilikeplaces.entities.*;

/**
 *
 * @author Ravindranath Akila
 */
@Stateless
public class Temp implements TempLocal {
    @EJB
    private CrudServiceLocal<Location> csl;
    
    public CrudServiceLocal temp(){
        return csl;
    }
}
