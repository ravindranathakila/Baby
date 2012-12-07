package ai.ilikeplaces.logic.crud.unit;

import ai.doc.License;
import ai.ilikeplaces.entities.Human;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RHumanLocal {

    /**
     * TransactionAttributeType.REQUIRED
     *
     * @param humandId
     * @return
     */

    public Human doRHuman(String humandId);

    public Human doNTxRHuman(String humandId);

    public Human doDirtyRHuman(String humanId);

    public boolean doDirtyCheckHuman(String humanId);
}
