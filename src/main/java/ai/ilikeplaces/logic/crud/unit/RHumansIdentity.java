package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.Url;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@NOTE(note = "SEE CRUDSERVICE WHERE TO SUPPORT READ AND DIRTY READ, THE TX TYPE IS SUPPORTS.")
@Stateless
public class RHumansIdentity extends AbstractSLBCallbacks implements RHumansIdentityLocal {

    @EJB
    private CrudServiceLocal<HumansIdentity> humansIdentityCrudServiceLocal_;
    @EJB
    private CrudServiceLocal<Url> urlCrudServiceLocal;

    public RHumansIdentity() {
    }


    final static Logger logger = LoggerFactory.getLogger(RHumansIdentity.class);


    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<HumansIdentity> doDirtyRHumansIdentitiesByEmails(final List<String> emails) {
        return humansIdentityCrudServiceLocal_.findWithNamedQuery(HumansIdentity.FindPaginatedHumansByEmails,
                QueryParameter.with(HumansIdentity.HumansIdentityEmails, emails).parameters());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String doDirtyProfilePhoto(final String humanId) throws DBDishonourCheckedException {
        return humansIdentityCrudServiceLocal_.findBadly(HumansIdentity.class, humanId).getHumansIdentityProfilePhoto();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW/*Since lazy loading*/)
    public String doDirtyPublicURL(final String humanId) throws DBDishonourCheckedException {
        return humansIdentityCrudServiceLocal_.findBadly(HumansIdentity.class, humanId).getUrl().getUrl();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HumansIdentity doDirtyRHumansIdentity(final String humanId) throws DBDishonourCheckedException {
        return humansIdentityCrudServiceLocal_.findBadly(HumansIdentity.class, humanId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HumansIdentity doRHumansIdentity(final String humanId) throws DBDishonourCheckedException {
        return humansIdentityCrudServiceLocal_.findBadly(HumansIdentity.class, humanId);
    }

    @Override
    public String doDirtyProfileFromURL(final String url) {
        final Url loadedUrl = urlCrudServiceLocal.find(Url.class, url);
        return loadedUrl == null ? null : loadedUrl.getMetadata();
    }

    @Override
    public String test(final String emails) {
        final String[] emailarr = emails.replace(" ", "").split(",");
        logger.debug("Emails:" + Arrays.toString(emailarr));
        String str = "";
        for (HumansIdentity hi : doDirtyRHumansIdentitiesByEmails(Arrays.asList(emailarr))) {
            str += hi.getHumanId();
        }
        return str;
    }
}
