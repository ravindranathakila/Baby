package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 12:07:04 AM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UPrivateEventLocal {

    public Return<PrivateEvent> doNTxCPrivateEvent(final String humanId, final String eventName, final String eventInfo, final Date startDate, final Date endDate);

    PrivateEvent doUPrivateEventAddOwner(String obj, long privateEventId__, HumansFriend owner);

    PrivateEvent doUPrivateEventRemoveOwner(String obj, long privateEventId__, HumansFriend owner);

    PrivateEvent doUPrivateEventAddViewer(String obj, long privateEventId__, HumansFriend owner);

    PrivateEvent doUPrivateEventRemoveViewer(String obj, long privateEventId__, HumansFriend owner);
}