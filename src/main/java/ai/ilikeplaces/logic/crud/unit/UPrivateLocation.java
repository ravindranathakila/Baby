package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.exception.NoPrivilegesException;
import ai.ilikeplaces.exception.NotFriendsException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 15, 2010
 * Time: 12:26:35 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class UPrivateLocation extends AbstractSLBCallbacks implements UPrivateLocationLocal {

    @EJB
    private CrudServiceLocal<PrivateLocation> privateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<Human> humanCrudServiceLocal_;

    @EJB
    private UHumansNetPeopleLocal uHumansNetPeopleLocal;

    @Override
    public PrivateLocation doUPrivateLocationData(final String humanId__, final String privateLocationId__, final String privateLocationName__, final String privateLocationInfo__) {
        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);
        if (privateLocationName__ != null) {
            privateLocation_.setPrivateLocationName(privateLocationName__);
        }
        if (privateLocationInfo__ != null) {
            privateLocation_.setPrivateLocationInfo(privateLocationInfo__);
        }
        return privateLocation_;
    }


    @Override
    @TODO(task = "Verify owner")
    @NOTE(note = "Make loop run even upon failures. Action should complete upon failures")
    public PrivateLocation doUPrivateLocationAddOwners(final String humanId__, final long privateLocationId__, final List<String> privateLocationOwners__) {
        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);
        final List<HumansPrivateLocation> list = new ArrayList<HumansPrivateLocation>();

        for (final String humanId : privateLocationOwners__) {
            try {
                list.add(humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId));
            } catch (final Exception e) {
                logger.error("SORRY! I ENCOUNTERED AN ERROR DURING LIST MANIPULATION", e);
            }
        }

        for (final HumansPrivateLocation humansPrivateLocation : list) {
            try {
                privateLocation_.getPrivateLocationOwners().add(humansPrivateLocation);
            } catch (final Exception e) {
                logger.error("SORRY! I ENCOUNTERED AN ERROR DURING LIST MANIPULATION", e);
            }
        }

        return privateLocation_;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateLocation doUPrivateLocationAddOwner(final String adder, final long privateLocationId__, final HumansFriend addee) {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(adder, addee.getHumanId())) {
            throw new NotFriendsException(adder, addee.getHumanId());
        }

        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);

        final HumansPrivateLocation adeehumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, addee.getHumanId());

        final HumansPrivateLocation adderhumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, adder);

        if (!privateLocation_.getPrivateLocationOwners().contains(adderhumansPrivateLocation)) {
            throw new NoPrivilegesException(adderhumansPrivateLocation.getHumanId(), "manage private location:" + privateLocation_.toString());
        }


        wiringBothSides:
        {
            privateLocation_.getPrivateLocationOwners().add(adeehumansPrivateLocation);
            adeehumansPrivateLocation.getPrivateLocationsOwned().add(privateLocation_);
        }


        return privateLocation_;
    }

    @Override
    @TODO(task = "Verify owner")
    @NOTE(note = "Make loop run even upon failures. Action should complete upon failures")
    public PrivateLocation doUPrivateLocationRemoveOwners(final String humanId__, final String privateLocationId__, final List<String> privateLocationOwners__) {
        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);
        final List<HumansPrivateLocation> list = new ArrayList<HumansPrivateLocation>();

        for (final String humanId : privateLocationOwners__) {
            try {
                list.add(humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId));
            } catch (final Exception e) {
                logger.error("SORRY! I ENCOUNTERED AN ERROR DURING LIST MANIPULATION", e);
            }
        }

        for (final HumansPrivateLocation humansPrivateLocation : list) {
            try {
                privateLocation_.getPrivateLocationOwners().remove(humansPrivateLocation);
            } catch (final Exception e) {
                logger.error("SORRY! I ENCOUNTERED AN ERROR DURING LIST MANIPULATION", e);
            }
        }
        return privateLocation_;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateLocation doUPrivateLocationRemoveOwner(final String adder, final long privateLocationId__, final HumansFriend addee) {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(adder, addee.getHumanId())) {
            throw new NotFriendsException(adder, addee.getHumanId());
        }

        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);

        final HumansPrivateLocation adeehumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, addee.getHumanId());

        final HumansPrivateLocation adderhumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, adder);

        if (!privateLocation_.getPrivateLocationOwners().contains(adderhumansPrivateLocation)) {
            throw new NoPrivilegesException(adderhumansPrivateLocation.getHumanId(), "manage private location:" + privateLocation_.toString());
        }


        wiringBothSides:
        {
            privateLocation_.getPrivateLocationOwners().remove(adeehumansPrivateLocation);
            adeehumansPrivateLocation.getPrivateLocationsOwned().remove(privateLocation_);
        }


        return privateLocation_;
    }

    @Override
    @NOTE(note = "Make loop run even upon failures. Action should complete upon failures")
    public PrivateLocation doUPrivateLocationAddVisitors(final String humanId__, final String privateLocationId__, final List<String> privateLocationVisitors__) {

        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);

        for (final String humanId : privateLocationVisitors__) {
            try {
                humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId).getPrivateLocationsViewed().add(privateLocation_);
            } catch (final Exception e) {
                logger.error("SORRY! I ENCOUNTERED AN ERROR DURING LIST MANIPULATION", e);
            }
        }

        return privateLocation_;
    }

    @Override
    public PrivateLocation doUPrivateLocationAddVisitor(String humanId__, String privateLocationId__, HumansFriend privateLocationVisitor__) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    @NOTE(note = "Make loop run even upon failures. Action should complete upon failures")
    public PrivateLocation doUPrivateLocationRemoveVisitors(final String humanId__, final String privateLocationId__, final List<String> privateLocationVisitors__) {

        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);
        for (final String humanId : privateLocationVisitors__) {
            try {
                humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, humanId).getPrivateLocationsViewed().remove(privateLocation_);
            } catch (final Exception e) {
                logger.error("SORRY! I ENCOUNTERED AN ERROR DURING LIST MANIPULATION", e);
            }
        }


        return privateLocation_;
    }

    @Override
    public PrivateLocation doUPrivateLocationRemoveVisitor(String humanId__, String privateLocationId__, HumansFriend privateLocationVisitor__) {
        throw new UnsupportedOperationException("TODO");
    }
}
