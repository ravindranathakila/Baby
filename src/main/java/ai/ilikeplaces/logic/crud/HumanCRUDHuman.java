package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.logic.crud.unit.CHumanLocal;
import ai.ilikeplaces.logic.crud.unit.RHumanLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.security.blowfish.jbcrypt.BCrypt;
import ai.ilikeplaces.security.face.SingletonHashingFace;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.DBOffilne;
import ai.ilikeplaces.util.MethodParams;
import ai.ilikeplaces.util.MethodTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({DBOffilne.class, MethodTimer.class, MethodParams.class})
public class HumanCRUDHuman extends AbstractSLBCallbacks implements HumanCRUDHumanLocal {

    @EJB
    private RHumanLocal rHumanLocal_;

    @EJB
    private CHumanLocal cHumanLocal_;

    @EJB
    private SingletonHashingFace singletonHashingFace;

    public HumanCRUDHuman() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", HumanCRUDHuman.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Human doDirtyRHuman(final String humanId) {
        return rHumanLocal_.doDirtyRHuman(humanId);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void doNTxCHuman(final Human human) {
        cHumanLocal_.doNTxCHuman(human);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean doDirtyCheckHuman(final String humanId) {
        return rHumanLocal_.doDirtyCheckHuman(humanId);
    }

    /*BEGINNING OF PREPERATOR METHODS*/

    @TODO(task = "ADD HUMANSIDENTITY VALUES TAKE FROM THE SERVLETSIGNUP. CHANGE ALL NON_CRUDSERVICE CLASSES TO USE NON_TRANSACTIONAL AS REQUIRED")
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void doCHuman(final String humanId, final String password) throws IllegalAccessException {
        if (doDirtyCheckHuman(humanId)) {
            throw new IllegalAccessException(RBGet.logMsgs.getString("ai.ilikeplaces.logic.crud.HumanCRUDHuman.0001") + humanId);
        }
        final Human newUser = new Human();
        newUser.setHumanId(humanId);
        final HumansAuthentication ha = new HumansAuthentication();
        ha.setHumanId(newUser.getHumanId());
        ha.setHumanAuthenticationSalt(BCrypt.gensalt());
        ha.setHumanAuthenticationHash(singletonHashingFace.getHash(password, ha.getHumanAuthenticationSalt()));
        newUser.setHumansAuthentications(ha);
        newUser.setHumanAlive(true);

        final HumansIdentity hid = new HumansIdentity();
        hid.setHumanId(newUser.getHumanId());
        newUser.setHumansIdentity(hid);

        final HumansPrivateLocation hpl = new HumansPrivateLocation();
        hpl.setHumanId(newUser.getHumanId());
        newUser.setHumansPrivateLocation(hpl);

        final HumansPrivateEvent hpe = new HumansPrivateEvent();
        hpe.setHumanId(newUser.getHumanId());
        newUser.setHumansPrivateEvent(hpe);

        final HumansPrivatePhoto hprp = new HumansPrivatePhoto();
        hprp.setHumanId(newUser.getHumanId());
        newUser.setHumansPrivatePhoto(hprp);

        final HumansPublicPhoto hpup = new HumansPublicPhoto();
        hpup.setHumanId(newUser.getHumanId());
        newUser.setHumansPublicPhoto(hpup);
        doNTxCHuman(newUser);
    }


    /*END OF PREPERATOR METHODS*/

    final static Logger logger = LoggerFactory.getLogger(HumanCRUDHuman.class);
}
