package ai.baby.logic.crud.unit;

import ai.ilikeplaces.entities.etc.HumanId;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UHumansNetLocal {
    public void doNTxUDisplayName(final HumanId humanId, String displayName);
}
