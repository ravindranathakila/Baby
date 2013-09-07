package ai.baby.logic.crud.unit;

import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.baby.util.exception.DBDishonourCheckedException;
import ai.baby.util.exception.DBFetchDataException;
import ai.baby.util.jpa.CrudServiceLocal;
import ai.baby.util.AbstractSLBCallbacks;
import ai.scribble.License;
import ai.scribble._note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_note(note = "SEE CRUDSERVICE WHERE TO SUPPORT READ AND DIRTY READ, THE TX TYPE IS SUPPORTS.")
@Stateless
public class RHumansPrivateLocation extends AbstractSLBCallbacks implements RHumansPrivateLocationLocal {


    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal_;

    public RHumansPrivateLocation() {
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public HumansPrivateLocation doNTxRHumansPrivateLocation(String humanId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        final HumansPrivateLocation hpl = humansPrivateLocationCrudServiceLocal_.findBadly(HumansPrivateLocation.class, humanId).refresh();
        return hpl;
    }

    final static Logger logger = LoggerFactory.getLogger(RHumansPrivateLocation.class);
}
