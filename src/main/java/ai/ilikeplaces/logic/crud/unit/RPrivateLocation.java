package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.exception.NoPrivilegesException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 18, 2010
 * Time: 10:45:35 PM
 */


@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class RPrivateLocation extends AbstractSLBCallbacks implements RPrivateLocationLocal {

    @EJB
    private CrudServiceLocal<PrivateLocation> privateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<Human> humanCrudServiceLocal_;

    private static final String VIEW_PRIVATE_LOCATION = "view private location:";

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    @Override
    public PrivateLocation doRPrivateLocationAsAny(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId).refresh();
        //final HumansPrivateLocation humansPrivateLocation_ = humansPrivateLocationCrudServiceLocal_.findBadly(HumansPrivateLocation.class, humanId);
        final Human human = humanCrudServiceLocal_.findBadly(Human.class, humanId);

        securityChecks:
        {
            //This old check was taking up too much time. Nevertheless, left here in case the new approach introduces bugs
            //Also note that there is an unwanted two way check here.
            //Note that the not clause is not clear too
//            if (!(privateLocation_.getPrivateLocationOwners().contains(humansPrivateLocation_)
//                    || humansPrivateLocation_.getPrivateLocationsViewed().contains(privateLocation_))) {
//                throw new NoPrivilegesException(humanId, VIEW_PRIVATE_LOCATION + privateLocation_.toString());
//            }
            /**
             * Should be either an owner of visitor
             */
            if (!(privateLocation_.getPrivateLocationViewers().contains(human) || privateLocation_.getPrivateLocationOwners().contains(human))) {
                throw new NoPrivilegesException(humanId, VIEW_PRIVATE_LOCATION + privateLocation_.toString());
            }
        }
        return privateLocation_;
    }


    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public PrivateLocation doRPrivateLocationAsSystem(final Long privateLocationId, final boolean eager) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        return eager ? privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId).refresh() :
                privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId);
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    @Override
    public boolean doRPrivateLocationIsOwner(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
//        return privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId).getPrivateLocationOwners()
//                .contains(humansPrivateLocationCrudServiceLocal_.findBadly(HumansPrivateLocation.class, humanId));
        return privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId).refresh().getPrivateLocationOwners()
                .contains(humanCrudServiceLocal_.findBadly(Human.class, humanId));
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    @Override
    public boolean doRPrivateLocationIsViewer(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
//        return privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId).getPrivateLocationViewers()
//                .contains(humansPrivateLocationCrudServiceLocal_.findBadly(HumansPrivateLocation.class, humanId));
        return privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId).refresh().getPrivateLocationViewers()
                .contains(humanCrudServiceLocal_.findBadly(Human.class, humanId));
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    @Override
    public boolean doRPrivateLocationIsViewerOrOwner(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        final PrivateLocation pl = privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId).refresh();
        return pl.getPrivateLocationViewers()
                .contains(humanCrudServiceLocal_.findBadly(Human.class, humanId)) ||
                pl.getPrivateLocationOwners()
                        .contains(humanCrudServiceLocal_.findBadly(Human.class, humanId));
    }

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public PrivateLocation doRPrivateLocationAsViewer(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId).refresh();
        // final HumansPrivateLocation humansPrivateLocation_ = humansPrivateLocationCrudServiceLocal_.findBadly(HumansPrivateLocation.class, humanId);
        final Human human = humanCrudServiceLocal_.findBadly(Human.class, humanId);

        securityChecks:
        {
//            if (!(humansPrivateLocation_.getPrivateLocationsViewed().contains(privateLocation_))) {
//                throw new NoPrivilegesException(RBGet.expMsgs.getString("ai.ilikeplaces.logic.crud.unit.RPrivateLocation.0001"));
//            }
            if (!privateLocation_.getPrivateLocationViewers().contains(human)) {
                throw new NoPrivilegesException(humanId, VIEW_PRIVATE_LOCATION + privateLocation_.toString());
            }
        }

        return privateLocation_;
    }

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public PrivateLocation doRPrivateLocationAsOwner(final String humanId, final Long privateLocationId) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.findBadly(PrivateLocation.class, privateLocationId).refresh();
//        final HumansPrivateLocation humansPrivateLocation_ = humansPrivateLocationCrudServiceLocal_.findBadly(HumansPrivateLocation.class, humanId);
        final Human human = humanCrudServiceLocal_.findBadly(Human.class, humanId);

        securityChecks:
        {
//            if (!(privateLocation_.getPrivateLocationOwners().contains(humansPrivateLocation_))) {
//                throw new NoPrivilegesException(RBGet.expMsgs.getString("ai.ilikeplaces.logic.crud.unit.RPrivateLocation.0002"));
//            }
            if (!privateLocation_.getPrivateLocationOwners().contains(human)) {
                throw new NoPrivilegesException(humanId, VIEW_PRIVATE_LOCATION + privateLocation_.toString());
            }
        }

        return privateLocation_;
    }

    @Override
    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public List<PrivateLocation> doRPrivateLocationsAsGlobal(
            final Double PrivateLocationLatitudeSouth,
            final Double PrivateLocationLatitudeNorth,
            final Double PrivateLocationLongitudeWest,
            final Double PrivateLocationLongitudeEast) {

        return privateLocationCrudServiceLocal_
                .findWithNamedQuery(PrivateLocation.FindAllPrivateLocationsByBounds,
                        QueryParameter
                                .newInstance()
                                .add(PrivateLocation.PrivateLocationLatitudeSouth, PrivateLocationLatitudeSouth)
                                .add(PrivateLocation.PrivateLocationLatitudeNorth, PrivateLocationLatitudeNorth)
                                .add(PrivateLocation.PrivateLocationLongitudeWest, PrivateLocationLongitudeWest)
                                .add(PrivateLocation.PrivateLocationLongitudeEast, PrivateLocationLongitudeEast)
                                .parameters());
    }
}


//select * from ilp.privatelocation where (privatelocationlatitude BETWEEN 40 AND 50) AND (privatelocationlongitude between -75 and -7)
