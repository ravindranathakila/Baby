package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.security.blowfish.jbcrypt.BCrypt;
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
public class RHumansAuthentication extends AbstractSLBCallbacks implements RHumansAuthenticationLocal {


    @EJB
    private CrudServiceLocal<HumansAuthentication> humansAuthenticationCrudServiceLocal_;

    public RHumansAuthentication() {
        logger.debug(RBGet.logMsgs.getString("common.Constructor.Init"), RHumansAuthentication.class, this.hashCode());
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HumansAuthentication doDirtyRHumansAuthentication(String humanId) {
        return humansAuthenticationCrudServiceLocal_.find(HumansAuthentication.class, humanId);
    }

    final static Logger logger = LoggerFactory.getLogger(RHumansAuthentication.class);
}