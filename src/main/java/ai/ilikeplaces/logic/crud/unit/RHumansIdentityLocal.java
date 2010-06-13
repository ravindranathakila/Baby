package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.util.RefObj;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RHumansIdentityLocal {

    public List<HumansIdentity> doDirtyRHumansIdentitiesByEmails(final List<String> emails);

    public String doDirtyProfilePhoto(final String humanId);

    public String doDirtyPublicURL(final String humanId);

    public HumansIdentity doDirtyRHumansIdentity(final String humanId);

    public String doDirtyProfileFromURL(final String url);    

    public String test(final String emails);
}