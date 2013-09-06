package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.util.exception.DBDishonourException;
import ai.ilikeplaces.util.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;
import ai.scribble.License;
import ai.scribble.WARNING;
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
public class UHumansNetPeople extends AbstractSLBCallbacks implements UHumansNetPeopleLocal {
// ------------------------------ FIELDS ------------------------------

    final static Logger logger = LoggerFactory.getLogger(UHumansNetPeople.class);


    @EJB
    private RHumansNetPeopleLocal rHumansNetPeopleLocal_;

    @EJB
    private CrudServiceLocal<HumansNetPeople> crudServiceLocal_;

// --------------------------- CONSTRUCTORS ---------------------------

    public UHumansNetPeople() {
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface UHumansNetPeopleLocal ---------------------

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @WARNING(warning = "ADD will not work if you remove transaction as entities will be unmanaged.")
    public boolean doNTxAddHumansNetPeople(final String adderHumanId, final String addeeHumanId) {
        if (adderHumanId.equals(addeeHumanId)) {
            throw new DBDishonourException("SORRY! ADDING SELF IS NOT SUPPORTED!");
        }
        final HumansNetPeople adder = rHumansNetPeopleLocal_.doRHumansNetPeople(adderHumanId);
        final HumansNetPeople addee = rHumansNetPeopleLocal_.doRHumansNetPeople(addeeHumanId);

        if (!adder.getHumansNetPeoples().contains(addee)) {
            adder.getHumansNetPeoples().add(addee);
        } else {
            throw DBDishonourException.ADDING_AN_EXISTING_VALUE;
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
            throw DBDishonourException.REMOVING_A_NON_EXISTING_VALUE;
        }
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean doNTxIsHumansNetPeople(final String checker, final String checkeee) {
        final HumansNetPeople cker = rHumansNetPeopleLocal_.doRHumansNetPeople(checker);
        final HumansNetPeople ckeee = rHumansNetPeopleLocal_.doRHumansNetPeople(checkeee);
        final boolean contains = cker.getHumansNetPeoples().contains(ckeee);
        Loggers.DEBUG.debug("Is friend:" + contains);
        return contains;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean doDirtyIsHumansNetPeople(final String adderHumanId, final String addeeHumanId) {
        final HumansNetPeople adder = rHumansNetPeopleLocal_.doRHumansNetPeople(adderHumanId);
        final HumansNetPeople addee = rHumansNetPeopleLocal_.doRHumansNetPeople(addeeHumanId);
        final boolean contains = adder.getHumansNetPeoples().contains(addee);
        Loggers.DEBUG.debug("Is friend:" + contains);
        return contains;
    }
}
