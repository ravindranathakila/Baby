package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.exception.AbstractEjbApplicationRuntimeException;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.logic.crud.unit.*;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.*;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.security.blowfish.jbcrypt.BCrypt;
import ai.ilikeplaces.security.face.SingletonHashingFace;
import ai.ilikeplaces.util.*;
import net.sf.oval.exception.ConstraintsViolatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDHumansUnseen extends AbstractSLBCallbacks implements HumanCRUDHumansUnseenLocal {


    @EJB
    private CRUDHumansUnseenLocal crudHumansUnseenLocal_;


    public HumanCRUDHumansUnseen() {
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addEntry(final String humanId, final Long wallId) {
        crudHumansUnseenLocal_.addEntry(humanId, wallId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addEntry(final List<String> humanIds, final Long wallId) {
        crudHumansUnseenLocal_.addEntry(humanIds, wallId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeEntry(final String humanId, final Long wallId) {
           crudHumansUnseenLocal_.removeEntry(humanId,wallId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Set<Wall> readEntries(final String humanId) {
        return crudHumansUnseenLocal_.readEntries(humanId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HumansUnseen getHumansUnseen(final String humanId) {
        return crudHumansUnseenLocal_.getHumansUnseen(humanId);
    }
}
