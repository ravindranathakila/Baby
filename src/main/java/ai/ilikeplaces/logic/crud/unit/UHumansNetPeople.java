package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;
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
public class UHumansNetPeople extends AbstractSLBCallbacks implements UHumansNetPeopleLocal {

    @EJB
    private CrudServiceLocal<HumansNetPeople> crudServiceLocal_;

    @EJB
    private RHumansNetPeopleLocal rHumansNetPeopleLocal_;

    public UHumansNetPeople() {
        logger.debug(RBGet.logMsgs.getString("common.Constructor.Init"), UHumansNetPeople.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @WARNING(warning = "ADD will not work if you remove transaction as entities will be unmanaged.")
    public boolean doNTxAddHumansNetPeople(final String adderHumanId, final String addeeHumanId) {
        final HumansNetPeople adder = rHumansNetPeopleLocal_.doRHumansNetPeople(adderHumanId);
        final HumansNetPeople addee = rHumansNetPeopleLocal_.doRHumansNetPeople(addeeHumanId);

        if (!adder.getHumansNetPeoples().contains(addee)) {
            adder.getHumansNetPeoples().add(addee);
        } else {
            throw new IllegalArgumentException("SORRY! THIS SHOULD NOT HAPPEN. YOU ARE ADDING AN EXISTING FRIEND.");
        }
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @WARNING(warning = "REMOVE will not work if you remove transaction as entities will be unmanaged.")
    public boolean doNTxRemoveHumansNetPeople(final String adderHumanId, final String addeeHumanId) {
        final HumansNetPeople adder = rHumansNetPeopleLocal_.doRHumansNetPeople(adderHumanId);
        final HumansNetPeople addee = rHumansNetPeopleLocal_.doRHumansNetPeople(addeeHumanId);

        if (adder.getHumansNetPeoples().contains(addee)) {
            adder.getHumansNetPeoples().remove(addee);
        } else {
            throw new IllegalArgumentException("SORRY! THIS SHOULD NOT HAPPEN. YOU ARE REMOVING A NON-EXISTING FRIEND.");
        }
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean doNTxIsHumansNetPeople(final String adderHumanId, final String addeeHumanId) {
        final HumansNetPeople adder = rHumansNetPeopleLocal_.doRHumansNetPeople(adderHumanId);
        final HumansNetPeople addee = rHumansNetPeopleLocal_.doRHumansNetPeople(addeeHumanId);
        Loggers.DEBUG.debug("Is friend:"+adder.getHumansNetPeoples().contains(addee));
        return adder.getHumansNetPeoples().contains(addee);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean doDirtyIsHumansNetPeople(final String adderHumanId, final String addeeHumanId) {
        final HumansNetPeople adder = rHumansNetPeopleLocal_.doRHumansNetPeople(adderHumanId);
        final HumansNetPeople addee = rHumansNetPeopleLocal_.doRHumansNetPeople(addeeHumanId);
        Loggers.DEBUG.debug("Is friend:"+adder.getHumansNetPeoples().contains(addee));
        return adder.getHumansNetPeoples().contains(addee);
    }

    final static Logger logger = LoggerFactory.getLogger(UHumansNetPeople.class);

}