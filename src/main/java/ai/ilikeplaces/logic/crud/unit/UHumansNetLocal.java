package ai.ilikeplaces.logic.crud.unit;

import ai.doc.License;
import ai.util.HumanId;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UHumansNetLocal {
    public void doNTxUDisplayName(final HumanId humanId, String displayName);
}
