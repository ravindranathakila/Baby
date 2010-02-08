package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RHumansIdentityLocal {

    public List<HumansIdentity> doDirtyRHumansIdentitiesByEmails(final List<String> emails);

    public String test(final String emails);
}