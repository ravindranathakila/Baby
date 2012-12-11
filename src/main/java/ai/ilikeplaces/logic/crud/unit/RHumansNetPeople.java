package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.scribble.License;
import ai.scribble._note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_note(note = "SEE CRUDSERVICE WHERE TO SUPPORT READ AND DIRTY READ, THE TX TYPE IS SUPPORTS.")
@Stateless
public class RHumansNetPeople extends AbstractSLBCallbacks implements RHumansNetPeopleLocal {

    @EJB
    private CrudServiceLocal<HumansNetPeople> crudServiceLocal_;

    public RHumansNetPeople() {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HumansNetPeople doDirtyRHumansNetPeople(final String humanId) {
        return crudServiceLocal_.find(HumansNetPeople.class, humanId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HumansNetPeople doRHumansNetPeople(final String humanId) {
        return crudServiceLocal_.find(HumansNetPeople.class, humanId);
    }

    @Override
    public List<HumansNetPeople> doDirtyRHumansBefriends(final String humanId) {
        return crudServiceLocal_.findWithNamedQuery(HumansNetPeople.FindHumansNetPeoplesWhoHaveMeAsAFriend,
                QueryParameter.newInstance().add(HumansNetPeople.humanIdCOL, humanId).parameters());
    }


    final static Logger logger = LoggerFactory.getLogger(RHumansNetPeople.class);
}
