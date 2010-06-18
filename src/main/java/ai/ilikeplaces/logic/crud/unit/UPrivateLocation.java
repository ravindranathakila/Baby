package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBDishonourException;
import ai.ilikeplaces.exception.NoPrivilegesException;
import ai.ilikeplaces.exception.NotFriendsException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

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
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateLocation doUPrivateLocationAddOwner(final String adder, final long privateLocationId__, final HumansFriend addee) throws NoPrivilegesException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(adder, addee.getHumanId())) {
            throw new NotFriendsException(adder, addee.getHumanId());
        }

        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);

        final HumansPrivateLocation adeehumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, addee.getHumanId());

        //also safe to keep adder scoped within
        checkAuthority:
        {
            final HumansPrivateLocation adderhumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, adder);

            if (!privateLocation_.getPrivateLocationOwners().contains(adderhumansPrivateLocation)) {
                throw new NoPrivilegesException(adderhumansPrivateLocation.getHumanId(), "manage private location:" + privateLocation_.toString());
            }
        }


        wiringBothSides:
        {
            if (!privateLocation_.getPrivateLocationOwners().contains(adeehumansPrivateLocation)) {//Concurrent update safe
                privateLocation_.getPrivateLocationOwners().add(adeehumansPrivateLocation);
            } else {
                throw new DBDishonourException("Adding an existing owner");
            }
            if (!adeehumansPrivateLocation.getPrivateLocationsOwned().contains(privateLocation_)) {//Concurrent update safe
                adeehumansPrivateLocation.getPrivateLocationsOwned().add(privateLocation_);
            } else {
                throw new DBDishonourException("Adding an existing location");
            }
        }


        return privateLocation_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateLocation doUPrivateLocationRemoveOwner(final String remover, final long privateLocationId__, final HumansFriend removee) throws NoPrivilegesException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(remover, removee.getHumanId())) {
            throw new NotFriendsException(remover, removee.getHumanId());
        }

        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);

        //also safe to keep adder scoped within
        checkAuthority:
        {
            final HumansPrivateLocation removerhumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, remover);

            if (!privateLocation_.getPrivateLocationOwners().contains(removerhumansPrivateLocation)) {
                throw new NoPrivilegesException(removerhumansPrivateLocation.getHumanId(), "manage private location:" + privateLocation_.toString());
            }
        }

        wiringBothSides:
        {
            final HumansPrivateLocation removeehumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, removee.getHumanId());

            if (privateLocation_.getPrivateLocationOwners().contains(removeehumansPrivateLocation)) {//Concurrent update safe
                privateLocation_.getPrivateLocationOwners().remove(removeehumansPrivateLocation);
            } else {
                throw new DBDishonourException("Removing a non existing owner");
            }
            if (removeehumansPrivateLocation.getPrivateLocationsOwned().contains(privateLocation_)) {//Concurrent update safe
                removeehumansPrivateLocation.getPrivateLocationsOwned().remove(privateLocation_);
            } else {
                throw new DBDishonourException("Removing a non existing location");
            }
        }


        return privateLocation_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateLocation doUPrivateLocationAddViewer(final String adder, final long privateLocationId__, final HumansFriend addee) throws NoPrivilegesException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(adder, addee.getHumanId())) {
            throw new NotFriendsException(adder, addee.getHumanId());
        }

        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);


        //also safe to keep adder scoped within
        checkAuthority:
        {
            final HumansPrivateLocation adderhumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, adder);

            if (!privateLocation_.getPrivateLocationViewers().contains(adderhumansPrivateLocation)) {
                throw new NoPrivilegesException(adderhumansPrivateLocation.getHumanId(), "manage private location:" + privateLocation_.toString());
            }
        }


        wiringBothSides:
        {
            final HumansPrivateLocation adeehumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, addee.getHumanId());

            if (!privateLocation_.getPrivateLocationViewers().contains(adeehumansPrivateLocation)) {//Concurrent update safe
                privateLocation_.getPrivateLocationViewers().add(adeehumansPrivateLocation);
            } else {
                throw new DBDishonourException("Adding an existing viewer");
            }
            if (!adeehumansPrivateLocation.getPrivateLocationsViewed().contains(privateLocation_)) {//Concurrent update safe
                adeehumansPrivateLocation.getPrivateLocationsViewed().add(privateLocation_);
            } else {
                throw new DBDishonourException("Adding an existing location");
            }
        }


        return privateLocation_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateLocation doUPrivateLocationRemoveViewer(final String remover, final long privateLocationId__, final HumansFriend removee) throws DBDishonourCheckedException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(remover, removee.getHumanId())) {
            throw new NotFriendsException(remover, removee.getHumanId());
        }

        final PrivateLocation privateLocation_ = privateLocationCrudServiceLocal_.find(PrivateLocation.class, privateLocationId__);

        //also safe to keep adder scoped within
        checkAuthority:
        {
            final HumansPrivateLocation removeehumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, remover);

            if (!privateLocation_.getPrivateLocationOwners().contains(removeehumansPrivateLocation)) {
                throw new NoPrivilegesException(removeehumansPrivateLocation.getHumanId(), "manage private location:" + privateLocation_.toString());
            }
        }

        wiringBothSides:
        {
            final HumansPrivateLocation removerhumansPrivateLocation = humansPrivateLocationCrudServiceLocal_.find(HumansPrivateLocation.class, removee.getHumanId());

            if (privateLocation_.getPrivateLocationViewers().contains(removerhumansPrivateLocation)) {//Concurrent update safe
                privateLocation_.getPrivateLocationViewers().remove(removerhumansPrivateLocation);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
            if (removerhumansPrivateLocation.getPrivateLocationsViewed().contains(privateLocation_)) {//Concurrent update safe
                removerhumansPrivateLocation.getPrivateLocationsViewed().remove(privateLocation_);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
        }


        return privateLocation_;
    }

}
