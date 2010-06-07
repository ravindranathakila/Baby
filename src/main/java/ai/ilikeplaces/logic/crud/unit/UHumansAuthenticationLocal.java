package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UHumansAuthenticationLocal {

    public boolean doUHumansAuthentication(final String humanId, final String currentPassword, final String newPassword);

    public boolean doUHumansAuthentication(final String humanId, final String newPassword);
}