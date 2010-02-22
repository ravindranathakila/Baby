package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateLocation;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 12:20:48 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RPrivateLocationLocal {

    public PrivateLocation doDirtyRPrivateLocation(final String humanId, final Long privateLocationId);

    public boolean doDirtyRPrivateLocationIsOwner(final String humanId, final Long privateLocationId);

    public boolean doDirtyRPrivateLocationIsViewer(final String humanId, final Long privateLocationId);

    public PrivateLocation doRPrivateLocationAsViewer(final String humanId, final Long privateLocationId);

    public PrivateLocation doRPrivateLocationAsOwner(final String humanId, final Long privateLocationId);
}
