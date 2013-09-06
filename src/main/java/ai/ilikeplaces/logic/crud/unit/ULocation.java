package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.LongMsg;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.util.exception.DBFetchDataException;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.scribble.License;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/19/10
 * Time: 9:32 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class ULocation extends AbstractSLBCallbacks implements ULocationLocal {

    @EJB
    private RLocationLocal rLocationLocal_;


    public ULocation() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Location doULocationLatLng(long locationId, final double latitude, final double longitude) {
        final Location managedLocation = rLocationLocal_.doRLocation(locationId);
        managedLocation.setLocationGeo1(String.valueOf(latitude));
        managedLocation.setLocationGeo2(String.valueOf(longitude));
        return managedLocation;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Location doULocationPosts(final long locationId, final Map<String, String> posts, final RefreshSpec refreshSpec) throws DBFetchDataException {
        final Location managedLocation = rLocationLocal_.doRLocation(locationId, refreshSpec);
        final Map<String, LongMsg> dbPostsMap = new HashMap<String, LongMsg>();
        for (final LongMsg dbPost : managedLocation.getLongMsgs()) {
            dbPostsMap.put(dbPost.getLongMsgMetadata().split("\\|")[0], dbPost);
        }

        for (final String postMeta : posts.keySet()) {
            final String postMetaId = postMeta.split("\\|")[0];
            if (dbPostsMap.containsKey(postMetaId)) {//Update Post if exists
                dbPostsMap.get(postMetaId).setLongMsgContentR(posts.get(postMeta)).setLongMsgMetadataR(postMeta);
            } else {//Add as new post
                managedLocation.getLongMsgs().add(new LongMsg().setLongMsgContentR(posts.get(postMeta)).setLongMsgMetadataR(postMeta));
            }
        }
        return managedLocation;
    }
}
