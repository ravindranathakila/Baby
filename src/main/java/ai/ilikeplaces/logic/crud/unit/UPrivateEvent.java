package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc._expect_null;
import ai.ilikeplaces.doc._forget_null;
import ai.ilikeplaces.doc._ok;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.entities.etc.HumansFriend;
import ai.ilikeplaces.exception.*;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 15, 2010
 * Time: 12:26:35 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class UPrivateEvent extends AbstractSLBCallbacks implements UPrivateEventLocal {

    @EJB
    private CrudServiceLocal<PrivateEvent> privateEventCrudServiceLocal_;

    @EJB
    private RPrivateEventLocal rPrivateEventLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivateEvent> humansPrivateEventCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<Human> humanCrudServiceLocal_;

    @EJB
    private UHumansNetPeopleLocal uHumansNetPeopleLocal;

    private static final String UPDATE_EVENT_DATA_OF = "update event data of ";

    @_ok
    @Override
    public PrivateEvent doUPrivateEventData(final String humanId__, final long privateEventId__, final String privateEventName__, final String privateEventInfo__, final String privateEventStartDate__, final String privateEventEndDate__) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId__);
        if (rPrivateEventLocal_.doRPrivateEventIsOwner(humanId__, privateEventId__)) {
            throw new NoPrivilegesException(humanId__, UPDATE_EVENT_DATA_OF + privateEvent_.toString());
        }

        if (privateEventName__ != null) {
            privateEvent_.setPrivateEventName(privateEventName__);
        }
        if (privateEventInfo__ != null) {
            privateEvent_.setPrivateEventInfo(privateEventInfo__);
        }
        if (privateEventStartDate__ != null) {
            privateEvent_.setPrivateEventStartDate(privateEventStartDate__);
        }
        if (privateEventEndDate__ != null) {
            privateEvent_.setPrivateEventEndDate(privateEventEndDate__);
        }
        return privateEvent_;
    }


    @_ok
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateEvent doUPrivateEventAddOwner(final String adder, final long privateEventId__, final HumansFriend addeee) throws DBDishonourCheckedException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(adder, addeee.getHumanId())) {
            throw new NotFriendsException(adder, addeee.getHumanId());
        }

        @_expect_null
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId__);

        //also safe to keep adder scoped within
        checkAuthority:
        {
            @_expect_null
            final HumansPrivateEvent adderhumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, adder);

            if (!privateEvent_.getPrivateEventOwners().contains(adderhumansPrivateEvent)) {
                throw new NoPrivilegesException(adderhumansPrivateEvent.getHumanId(), "manage private event:" + privateEvent_.toString());
            }
        }


        wiringBothSides:
        {

            @_forget_null
            final HumansPrivateEvent adeehumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, addeee.getHumanId());

            if (!privateEvent_.getPrivateEventOwners().contains(adeehumansPrivateEvent)) {//Concurrent update safe
                privateEvent_.getPrivateEventOwners().add(adeehumansPrivateEvent);
            } else {
                throw DBDishonourCheckedException.ADDING_AN_EXISTING_VALUE;
            }
            if (!adeehumansPrivateEvent.getPrivateEventsOwned().contains(privateEvent_)) {//Concurrent update safe
                adeehumansPrivateEvent.getPrivateEventsOwned().add(privateEvent_);
            } else {
                throw DBDishonourCheckedException.ADDING_AN_EXISTING_VALUE;
            }
        }


        return privateEvent_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateEvent doUPrivateEventRemoveOwner(final String remover, final long privateEventId__, final HumansFriend removeee) throws DBDishonourCheckedException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(remover, removeee.getHumanId())) {
            throw new NotFriendsException(remover, removeee.getHumanId());
        }

        @_expect_null
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId__);

        //also safe to keep adder scoped within
        checkAuthority:
        {
            @_forget_null
            final HumansPrivateEvent removerhumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, remover);

            if (!privateEvent_.getPrivateEventOwners().contains(removerhumansPrivateEvent)) {
                throw new NoPrivilegesException(removerhumansPrivateEvent.getHumanId(), "manage private event:" + privateEvent_.toString());
            }
        }

        wiringBothSides:
        {
            @_forget_null
            final HumansPrivateEvent removeehumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, removeee.getHumanId());

            if (privateEvent_.getPrivateEventOwners().contains(removeehumansPrivateEvent)) {//Concurrent update safe
                privateEvent_.getPrivateEventOwners().remove(removeehumansPrivateEvent);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
            if (removeehumansPrivateEvent.getPrivateEventsOwned().contains(privateEvent_)) {//Concurrent update safe
                removeehumansPrivateEvent.getPrivateEventsOwned().remove(privateEvent_);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
        }


        return privateEvent_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateEvent doUPrivateEventAddViewer(final String adder, final long privateEventId__, final HumansFriend addeee) throws DBDishonourCheckedException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(adder, addeee.getHumanId())) {
            throw new NotFriendsException(adder, addeee.getHumanId());
        }

        @_expect_null
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId__);


        //also safe to keep adder scoped within
        checkAuthority:
        {
            @_forget_null
            final HumansPrivateEvent adderhumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, adder);

            if (!privateEvent_.getPrivateEventViewers().contains(adderhumansPrivateEvent)) {
                throw new NoPrivilegesException(adderhumansPrivateEvent.getHumanId(), "manage private event:" + privateEvent_.toString());
            }
        }


        wiringBothSides:
        {
            @_forget_null
            final HumansPrivateEvent adeehumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, addeee.getHumanId());

            if (!privateEvent_.getPrivateEventViewers().contains(adeehumansPrivateEvent)) {//Concurrent update safe
                privateEvent_.getPrivateEventViewers().add(adeehumansPrivateEvent);
            } else {
                throw DBDishonourCheckedException.ADDING_AN_EXISTING_VALUE;
            }
            if (!adeehumansPrivateEvent.getPrivateEventsViewed().contains(privateEvent_)) {//Concurrent update safe
                adeehumansPrivateEvent.getPrivateEventsViewed().add(privateEvent_);
            } else {
                throw DBDishonourCheckedException.ADDING_AN_EXISTING_VALUE;
            }
        }


        return privateEvent_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateEvent doUPrivateEventRemoveViewer(final String remover, final long privateEventId__, final HumansFriend removeee) throws DBDishonourCheckedException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(remover, removeee.getHumanId())) {
            throw new NotFriendsException(remover, removeee.getHumanId());
        }

        @_expect_null
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId__);

        //also safe to keep adder scoped within
        checkAuthority:
        {
            @_forget_null
            final HumansPrivateEvent removeehumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, remover);

            if (!privateEvent_.getPrivateEventOwners().contains(removeehumansPrivateEvent)) {
                throw new NoPrivilegesException(removeehumansPrivateEvent.getHumanId(), "manage private event:" + privateEvent_.toString());
            }
        }

        wiringBothSides:
        {
            @_forget_null
            final HumansPrivateEvent removerhumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, removeee.getHumanId());

            if (privateEvent_.getPrivateEventViewers().contains(removerhumansPrivateEvent)) {//Concurrent update safe
                privateEvent_.getPrivateEventViewers().remove(removerhumansPrivateEvent);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
            if (removerhumansPrivateEvent.getPrivateEventsViewed().contains(privateEvent_)) {//Concurrent update safe
                removerhumansPrivateEvent.getPrivateEventsViewed().remove(privateEvent_);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
        }


        return privateEvent_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateEvent doUPrivateEventAddInvite(final String adder, final long privateEventId__, final HumansFriend addeee) throws NoPrivilegesException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(adder, addeee.getHumanId())) {
            throw new NotFriendsException(adder, addeee.getHumanId());
        }

        @_expect_null
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId__);


        //also safe to keep adder scoped within
        checkAuthority:
        {
            @_forget_null
            final HumansPrivateEvent adderhumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, adder);

            if (!privateEvent_.getPrivateEventInvites().contains(adderhumansPrivateEvent)) {
                throw new NoPrivilegesException(adderhumansPrivateEvent.getHumanId(), "manage private event:" + privateEvent_.toString());
            }
        }


        wiringBothSides:
        {
            @_forget_null
            final HumansPrivateEvent adeehumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, addeee.getHumanId());

            if (!privateEvent_.getPrivateEventInvites().contains(adeehumansPrivateEvent)) {//Concurrent update safe
                privateEvent_.getPrivateEventInvites().add(adeehumansPrivateEvent);
            } else {
                throw new DBDishonourException("Adding an existing invite");
            }
            if (!adeehumansPrivateEvent.getPrivateEventsInvited().contains(privateEvent_)) {//Concurrent update safe
                adeehumansPrivateEvent.getPrivateEventsInvited().add(privateEvent_);
            } else {
                throw new DBDishonourException("Adding an existing event");
            }
        }


        return privateEvent_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateEvent doUPrivateEventRemoveInvite(final String remover, final long privateEventId__, final HumansFriend removeee) throws DBDishonourCheckedException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(remover, removeee.getHumanId())) {
            throw new NotFriendsException(remover, removeee.getHumanId());
        }

        @_expect_null
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId__);

        //also safe to keep adder scoped within
        checkAuthority:
        {
            @_forget_null
            final HumansPrivateEvent removeehumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, remover);

            if (!privateEvent_.getPrivateEventOwners().contains(removeehumansPrivateEvent)) {
                throw new NoPrivilegesException(removeehumansPrivateEvent.getHumanId(), "manage private event:" + privateEvent_.toString());
            }
        }

        wiringBothSides:
        {
            @_forget_null
            final HumansPrivateEvent removerhumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, removeee.getHumanId());

            if (privateEvent_.getPrivateEventInvites().contains(removerhumansPrivateEvent)) {//Concurrent update safe
                privateEvent_.getPrivateEventInvites().remove(removerhumansPrivateEvent);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
            if (removerhumansPrivateEvent.getPrivateEventsInvited().contains(privateEvent_)) {//Concurrent update safe
                removerhumansPrivateEvent.getPrivateEventsInvited().remove(privateEvent_);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
        }


        return privateEvent_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateEvent doUPrivateEventAddReject(final String adder, final long privateEventId__, final HumansFriend addeee) throws DBDishonourCheckedException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(adder, addeee.getHumanId())) {
            throw new NotFriendsException(adder, addeee.getHumanId());
        }

        @_expect_null
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId__);


        //also safe to keep adder scoped within
        checkAuthority:
        {
            @_forget_null
            final HumansPrivateEvent adderhumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, adder);

            if (!privateEvent_.getPrivateEventRejects().contains(adderhumansPrivateEvent)) {
                throw new NoPrivilegesException(adderhumansPrivateEvent.getHumanId(), "manage private event:" + privateEvent_.toString());
            }
        }


        wiringBothSides:
        {
            @_forget_null
            final HumansPrivateEvent adeehumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, addeee.getHumanId());

            if (!privateEvent_.getPrivateEventRejects().contains(adeehumansPrivateEvent)) {//Concurrent update safe
                privateEvent_.getPrivateEventRejects().add(adeehumansPrivateEvent);
            } else {
                throw DBDishonourCheckedException.ADDING_AN_EXISTING_VALUE;
            }
            if (!adeehumansPrivateEvent.getPrivateEventsRejected().contains(privateEvent_)) {//Concurrent update safe
                adeehumansPrivateEvent.getPrivateEventsRejected().add(privateEvent_);
            } else {
                throw DBDishonourCheckedException.ADDING_AN_EXISTING_VALUE;
            }
        }


        return privateEvent_;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivateEvent doUPrivateEventRemoveReject(final String remover, final long privateEventId__, final HumansFriend removeee) throws DBDishonourCheckedException {

        if (!uHumansNetPeopleLocal.doDirtyIsHumansNetPeople(remover, removeee.getHumanId())) {
            throw new NotFriendsException(remover, removeee.getHumanId());
        }

        @_expect_null
        final PrivateEvent privateEvent_ = privateEventCrudServiceLocal_.find(PrivateEvent.class, privateEventId__);

        //also safe to keep adder scoped within
        checkAuthority:
        {
            @_forget_null
            final HumansPrivateEvent removeehumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, remover);

            if (!privateEvent_.getPrivateEventOwners().contains(removeehumansPrivateEvent)) {
                throw new NoPrivilegesException(removeehumansPrivateEvent.getHumanId(), "manage private event:" + privateEvent_.toString());
            }
        }

        wiringBothSides:
        {
            @_forget_null
            final HumansPrivateEvent removerhumansPrivateEvent = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, removeee.getHumanId());

            if (privateEvent_.getPrivateEventRejects().contains(removerhumansPrivateEvent)) {//Concurrent update safe
                privateEvent_.getPrivateEventRejects().remove(removerhumansPrivateEvent);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
            if (removerhumansPrivateEvent.getPrivateEventsRejected().contains(privateEvent_)) {//Concurrent update safe
                removerhumansPrivateEvent.getPrivateEventsRejected().remove(privateEvent_);
            } else {
                throw DBDishonourCheckedException.REMOVING_A_NON_EXISTING_VALUE;
            }
        }


        return privateEvent_;
    }
}
