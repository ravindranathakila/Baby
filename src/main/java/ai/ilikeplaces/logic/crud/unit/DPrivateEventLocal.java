package ai.ilikeplaces.logic.crud.unit;

import ai.doc.License;
import ai.ilikeplaces.exception.NoPrivilegesException;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 12, 2010
 * Time: 10:31:21 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface DPrivateEventLocal {

    /**
     * Removes the {@link ai.ilikeplaces.entities.HumansPrivateEvent caller} from {@link ai.ilikeplaces.entities.PrivateEvent event} if is not the last {@link ai.ilikeplaces.entities.HumansPrivateEvent owner}.
     * Does not delete the {@link ai.ilikeplaces.entities.PrivateEvent event}.
     * <br/>
     * <br/>
     * Deletes the {@link ai.ilikeplaces.entities.PrivateEvent event} if the caller is last {@link ai.ilikeplaces.entities.HumansPrivateEvent Owner} .
     * <br/>
     * <br/>
     * Removes the {@link ai.ilikeplaces.entities.HumansPrivateEvent caller} from {@link ai.ilikeplaces.entities.PrivateEvent event}.
     * <br/>
     *
     * @param humanId
     * @param privateEventId
     * @return
     */
    public boolean doNTxDPrivateEvent(final String humanId, final long privateEventId) throws NoPrivilegesException;

}
