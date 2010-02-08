package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;

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
    
    public Return<PrivateEvent> cPrivateEvent(final String humanId, final String privateEventName, final String privateEventInfo);

    public Return<PrivateEvent> rPrivateEvent(final String humanId, final long privateEventId);

    public Return<Boolean> dPrivateEvent(final String humanId, final long privateEventId);
}
