package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.exception.NoPrivilegesException;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 12, 2010
 * Time: 10:31:21 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface DPrivateEventLocal {

    public boolean doNTxDPrivateEvent(final String humanId, final long privateEventId) throws NoPrivilegesException;

}