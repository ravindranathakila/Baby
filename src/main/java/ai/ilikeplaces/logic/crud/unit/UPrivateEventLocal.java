package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateEvent;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 12, 2010
 * Time: 10:31:21 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UPrivateEventLocal {

    public PrivateEvent doUPrivateEventData(final String humanId__, final long privateEventId__, final String privateEventName__, final String privateEventInfo__, final String privateEventStartDate__, final String privateEventE);

    public PrivateEvent doUPrivateEventAddOwner(final String humanId__, final long privateEventId__, final HumansFriend privateEventOwner__);

    public PrivateEvent doUPrivateEventRemoveOwner(final String humanId__, final long privateEventId__, final HumansFriend privateEventOwner__);

    public PrivateEvent doUPrivateEventAddViewer(final String humanId__, final long privateEventId__, final HumansFriend privateEventViewer__);

    public PrivateEvent doUPrivateEventRemoveViewer(final String humanId__, final long privateEventId__, final HumansFriend privateEventViewer__);

    public PrivateEvent doUPrivateEventAddInvite(final String humanId__, final long privateEventId__, final HumansFriend privateEventInvite__);

    public PrivateEvent doUPrivateEventRemoveInvite(final String humanId__, final long privateEventId__, final HumansFriend privateEventInvite__);

    public PrivateEvent doUPrivateEventAddReject(final String humanId__, final long privateEventId__, final HumansFriend privateEventReject__);

    public PrivateEvent doUPrivateEventRemoveReject(final String humanId__, final long privateEventId__, final HumansFriend privateEventReject__);

}