package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.crud.unit.CPrivateEventLocal;
import ai.ilikeplaces.logic.crud.unit.DPrivateEventLocal;
import ai.ilikeplaces.logic.crud.unit.RPrivateEventLocal;
import ai.ilikeplaces.logic.crud.unit.UPrivateEventLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 11:58:04 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class})
public class HumanCRUDPrivateEvent extends AbstractSLBCallbacks implements HumanCRUDPrivateEventLocal {

    @EJB
    private CPrivateEventLocal cPrivateEventLocal;

    @EJB
    private RPrivateEventLocal rPrivateEventLocal;

    @EJB
    private UPrivateEventLocal uPrivateEventLocal;

    @EJB
    private DPrivateEventLocal dPrivateEventLocal;
    private static final String CHECK_OWNERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL = "Check ownership of private event Successful!";
    private static final String CHECK_OWNERSHIP_OF_PRIVATE_EVENT_FAILED = "Check ownership of private event FAILED!";
    private static final String CHECK_VIEWERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL = "Check viewership of private event Successful!";
    private static final String CHECK_VIEWERSHIP_OF_PRIVATE_EVENT_FAILED = "Check viewership of private event FAILED!";


    @Override
    public Return<PrivateEvent> cPrivateEvent(final String humanId, final long privateLocationId, final String privateEventName, final String privateEventInfo, final String startDate, final String endDate) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(cPrivateEventLocal.doNTxCPrivateEvent(humanId, privateLocationId, privateEventName, privateEventInfo, startDate, endDate), "Save private event Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<PrivateEvent>(t, "Save private event FAILED!", true);
        }
        return r;

    }


    @Override
    public Return<PrivateEvent> uPrivateEventAddOwner(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal.doUPrivateEventAddOwner(humanId__.getObj(), privateEventId__, owner), "Update private event Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<PrivateEvent>(t, "Update private event FAILED!", true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveOwner(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal.doUPrivateEventRemoveOwner(humanId__.getObj(), privateEventId__, owner), "Update private event Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<PrivateEvent>(t, "Update private event FAILED!", true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventAddVisitor(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal.doUPrivateEventAddViewer(humanId__.getObj(), privateEventId__, owner), "Update private event Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<PrivateEvent>(t, "Update private event FAILED!", true);
        }
        return r;
    }

    @Override
    public Return<PrivateEvent> uPrivateEventRemoveVisitor(final HumanId humanId__, final long privateEventId__, final HumansFriend owner) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(uPrivateEventLocal.doUPrivateEventRemoveViewer(humanId__.getObj(), privateEventId__, owner), "Update private event Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<PrivateEvent>(t, "Update private event FAILED!", true);
        }
        return r;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<PrivateEvent> dirtyRPrivateEvent(final String humanId, final long privateEventId) {
        Return<PrivateEvent> r;
        try {
            r = new ReturnImpl<PrivateEvent>(rPrivateEventLocal.doDirtyRPrivateEvent(humanId, privateEventId), "View private event Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<PrivateEvent>(t, "View private event FAILED!", true);
        }
        return r;

    }

    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<Boolean> dirtyRPrivateEventIsOwner(final String humanId, final Long privateEventId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(rPrivateEventLocal.doDirtyRPrivateEventIsOwner(humanId, privateEventId), CHECK_OWNERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, CHECK_OWNERSHIP_OF_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<Boolean> dirtyRPrivateEventIsViewer(final String humanId, final Long privateEventId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(rPrivateEventLocal.doDirtyRPrivateEventIsViewer(humanId, privateEventId), CHECK_VIEWERSHIP_OF_PRIVATE_EVENT_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, CHECK_VIEWERSHIP_OF_PRIVATE_EVENT_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Boolean> dPrivateEvent(final String humanId, final long privateEventId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(dPrivateEventLocal.doNTxDPrivateEvent(humanId, privateEventId), "Delete private event Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "Delete private event FAILED!", true);
        }
        return r;

    }
}