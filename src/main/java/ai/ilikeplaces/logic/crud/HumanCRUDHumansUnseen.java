package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.entities.HumansUnseen;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.crud.unit.CRUDHumansUnseenLocal;
import ai.ilikeplaces.util.*;
import ai.scribble.License;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({EntityManagerInjector.class, DBOffline.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
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
        crudHumansUnseenLocal_.removeEntry(humanId, wallId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Wall> readEntries(final String humanId) {
        return crudHumansUnseenLocal_.readEntries(humanId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HumansUnseen getHumansUnseen(final String humanId) {
        return crudHumansUnseenLocal_.doRHumansUnseenBadly(humanId);
    }
}
