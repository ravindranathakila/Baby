package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 2:41:45 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDPrivateEventLocal {

    public static final String NAME = HumanCRUDPrivateEventLocal.class.getSimpleName();
    
    public Return<PrivateEvent> cPrivateEvent(final String humanId, final long privateLocationId, final String privateEventName, final String privateEventInfo, final String startDate, final String endDate);

    public Return<PrivateEvent> dirtyRPrivateEvent(final String humanId, final long privateEventId);

    public Return<Boolean> dirtyRPrivateEventIsOwner(final String humanId, final Long privateEventId);

    public Return<Boolean> dirtyRPrivateEventIsViewer(final String humanId, final Long privateEventId);

    public Return<PrivateEvent> uPrivateEventAddOwner(final ai.ilikeplaces.logic.validators.unit.HumanId humanId__, final long privateEventId__, final HumansFriend owner);

    public Return<PrivateEvent> uPrivateEventAddVisitor(final ai.ilikeplaces.logic.validators.unit.HumanId humanId__, final long privateEventId__, final HumansFriend owner);

    public Return<PrivateEvent> uPrivateEventRemoveOwner(final ai.ilikeplaces.logic.validators.unit.HumanId humanId__, final long privateEventId__, final HumansFriend owner);

    public Return<PrivateEvent> uPrivateEventRemoveVisitor(final ai.ilikeplaces.logic.validators.unit.HumanId humanId__, final long privateEventId__, final HumansFriend owner);

    public Return<Boolean> dPrivateEvent(final String humanId, final long privateEventId);
}
