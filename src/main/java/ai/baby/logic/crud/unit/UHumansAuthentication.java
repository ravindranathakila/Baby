package ai.baby.logic.crud.unit;

import ai.baby.util.AbstractSLBCallbacks;
import ai.baby.util.blowfish.jbcrypt.BCrypt;
import ai.baby.util.jpa.CrudServiceLocal;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.baby.security.face.SingletonHashingRemote;
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

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_note(note = "SEE CRUDSERVICE WHERE TO SUPPORT READ AND DIRTY READ, THE TX TYPE IS SUPPORTS.")
@Stateless
public class UHumansAuthentication extends AbstractSLBCallbacks implements UHumansAuthenticationLocal {


    @EJB
    private CrudServiceLocal<HumansAuthentication> humansAuthenticationCrudServiceLocal_;

    @EJB
    private SingletonHashingRemote singletonHashingRemote_;

    public UHumansAuthentication() {
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean doUHumansAuthentication(final String humanId, final String currentPassword, final String newPassword) {
        boolean returnVal = false;
        final HumansAuthentication ha = humansAuthenticationCrudServiceLocal_.find(HumansAuthentication.class, humanId);
        if (ha.getHumanAuthenticationHash().equals(singletonHashingRemote_.getHash(currentPassword,
                ha.getHumanAuthenticationSalt()))) {

            ha.setHumanAuthenticationSalt(BCrypt.gensalt());
            ha.setHumanAuthenticationHash(singletonHashingRemote_.getHash(newPassword, ha.getHumanAuthenticationSalt()));
            returnVal = true;
        } else {
            throw new IllegalAccessError("Sorry! The given current password does not match!");
        }
        return returnVal;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean doUHumansAuthentication(final String humanId, final String newPassword) {
        final HumansAuthentication ha = humansAuthenticationCrudServiceLocal_.find(HumansAuthentication.class, humanId);

        ha.setHumanAuthenticationSalt(BCrypt.gensalt());
        ha.setHumanAuthenticationHash(singletonHashingRemote_.getHash(newPassword, ha.getHumanAuthenticationSalt()));

        return true;
    }

    final static Logger logger = LoggerFactory.getLogger(UHumansAuthentication.class);
}
