package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
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
@NOTE(note = "SEE CRUDSERVICE WHERE TO SUPPORT READ AND DIRTY READ, THE TX TYPE IS SUPPORTS.")
@Stateless
public class RHumansPrivateLocation extends AbstractSLBCallbacks implements RHumansPrivateLocationLocal {


    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal_;

    public RHumansPrivateLocation() {
        logger.debug(RBGet.logMsgs.getString("common.Constructor.Init"), RHumansPrivateLocation.class, this.hashCode());
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HumansPrivateLocation doDirtyRHumansPrivateLocation(String humanId) {
        return humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId);
    }

    final static Logger logger = LoggerFactory.getLogger(RHumansPrivateLocation.class);
}