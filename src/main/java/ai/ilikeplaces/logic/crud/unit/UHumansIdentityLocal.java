package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.util.RefObj;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UHumansIdentityLocal {
    public HumansIdentity doUHumansProfilePhoto(final String humanId, final String url);

    public HumansIdentity doUHumansPublicURL(final String humanId, final String url);
}