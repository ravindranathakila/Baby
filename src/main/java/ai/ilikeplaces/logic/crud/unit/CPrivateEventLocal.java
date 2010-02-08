package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateEvent;

import javax.ejb.Local;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 12:07:04 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CPrivateEventLocal {

    public PrivateEvent doNTxCPrivateEvent(final String humanId,
                                           final long privateLocationId,
                                           final String eventName,
                                           final String eventInfo,
                                           final Date startDate,
                                           final Date endDate);

}
