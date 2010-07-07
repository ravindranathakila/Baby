package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.logic.crud.unit.CPrivateLocationLocal;
import ai.ilikeplaces.logic.crud.unit.DPrivateLocationLocal;
import ai.ilikeplaces.logic.crud.unit.RPrivateLocationLocal;
import ai.ilikeplaces.logic.crud.unit.UPrivateLocationLocal;
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
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDPrivateLocation extends AbstractSLBCallbacks implements HumanCRUDPrivateLocationLocal {

    @EJB
    private CPrivateLocationLocal cPrivateLocationLocal;

    @EJB
    private RPrivateLocationLocal rPrivateLocationLocal;

    @EJB
    private UPrivateLocationLocal uPrivateLocationLocal;

    @EJB
    private DPrivateLocationLocal dPrivateLocationLocal;
    private static final String READ_PRIVATE_LOCATION_SUCCESSFUL = "Read private location Successful!";
    private static final String READ_PRIVATE_LOCATION_FAILED = "Read private location FAILED!";
    private static final String CHECK_OWNERSHIP_OF_PRIVATE_LOCATION_SUCCESSFUL = "Check ownership of private location Successful!";
    private static final String CHECK_OWNERSHIP_OF_PRIVATE_LOCATION_FAILED = "Check ownership of private location FAILED!";
    private static final String CHECK_VIEWERSHIP_OF_PRIVATE_LOCATION_SUCCESSFUL = "Check viewership of private location Successful!";
    private static final String CHECK_VIEWERSHIP_OF_PRIVATE_LOCATION_FAILED = "Check viewership of private location FAILED!";
    private static final String DELETE_PRIVATE_LOCATION_SUCCESSFUL = "Delete private location Successful!";
    private static final String DELETE_PRIVATE_LOCATION_FAILED = "Delete private location FAILED!";
    private static final String SAVE_PRIVATE_LOCATION_SUCCESSFUL = "Save private location Successful!";
    private static final String SAVE_PRIVATE_LOCATION_FAILED = "Save private location FAILED!";
    private static final String UPDATE_PRIVATE_LOCATION_SUCCESSFUL = "Update private location Successful!";
    private static final String UPDATE_PRIVATE_LOCATION_FAILED = "Update private location FAILED!";


    @Override
    public Return<PrivateLocation> cPrivateLocation(final RefObj<String> humanId, final String privateLocationName, final String privateLocationInfo){
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(cPrivateLocationLocal.doNTxCPrivateLocation(humanId.getObjectAsValid(), privateLocationName, privateLocationInfo), SAVE_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateLocation>(t, UPDATE_PRIVATE_LOCATION_FAILED, true);
        }
        return r;
    }


    @Override
    public Return<PrivateLocation> uPrivateLocationAddOwner(final RefObj<String> humanId__, final long privateLocationId__, final HumansFriend owner) {
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(uPrivateLocationLocal.doUPrivateLocationAddOwner(humanId__.getObjectAsValid(), privateLocationId__, owner), UPDATE_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateLocation>(t, UPDATE_PRIVATE_LOCATION_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateLocation> uPrivateLocationRemoveOwner(final RefObj<String> humanId__, final long privateLocationId__, final HumansFriend owner) {
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(uPrivateLocationLocal.doUPrivateLocationRemoveOwner(humanId__.getObjectAsValid(), privateLocationId__, owner), UPDATE_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateLocation>(t, UPDATE_PRIVATE_LOCATION_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateLocation> uPrivateLocationAddVisitor(final RefObj<String> humanId__, final long privateLocationId__, final HumansFriend owner) {
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(uPrivateLocationLocal.doUPrivateLocationAddViewer(humanId__.getObjectAsValid(), privateLocationId__, owner), UPDATE_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateLocation>(t, UPDATE_PRIVATE_LOCATION_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<PrivateLocation> uPrivateLocationRemoveVisitor(final RefObj<String> humanId__, final long privateLocationId__, final HumansFriend owner) {
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(uPrivateLocationLocal.doUPrivateLocationRemoveViewer(humanId__.getObjectAsValid(), privateLocationId__, owner), UPDATE_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateLocation>(t, UPDATE_PRIVATE_LOCATION_FAILED, true);
        }
        return r;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<PrivateLocation> dirtyRPrivateLocationAsAny(final RefObj<String> humanId, final long privateLocationId) {
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(rPrivateLocationLocal.doRPrivateLocationAsAny(humanId.getObjectAsValid(), privateLocationId), READ_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateLocation>(t, READ_PRIVATE_LOCATION_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<PrivateLocation> dirtyRPrivateLocationAsOwner(final RefObj<String> humanId, final long privateLocationId) {
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(rPrivateLocationLocal.doRPrivateLocationAsOwner(humanId.getObjectAsValid(), privateLocationId), READ_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateLocation>(t, READ_PRIVATE_LOCATION_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<PrivateLocation> dirtyRPrivateLocationAsViewer(final RefObj<String> humanId, final long privateLocationId) {
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(rPrivateLocationLocal.doRPrivateLocationAsViewer(humanId.getObjectAsValid(), privateLocationId), READ_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<PrivateLocation>(t, READ_PRIVATE_LOCATION_FAILED, true);
        }
        return r;

    }

    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<Boolean> dirtyRPrivateLocationIsOwner(final RefObj<String> humanId, final Long privateLocationId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(rPrivateLocationLocal.doRPrivateLocationIsOwner(humanId.getObjectAsValid(), privateLocationId), CHECK_OWNERSHIP_OF_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Boolean>(t, CHECK_OWNERSHIP_OF_PRIVATE_LOCATION_FAILED, true);
        }
        return r;
    }

    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Return<Boolean> dirtyRPrivateLocationIsViewer(final RefObj<String> humanId, final Long privateLocationId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(rPrivateLocationLocal.doRPrivateLocationIsViewer(humanId.getObjectAsValid(), privateLocationId), CHECK_VIEWERSHIP_OF_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Boolean>(t, CHECK_VIEWERSHIP_OF_PRIVATE_LOCATION_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Boolean> dPrivateLocation(final RefObj<String> humanId, final long privateLocationId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(dPrivateLocationLocal.doNTxDPrivateLocation(humanId.getObjectAsValid(), privateLocationId), DELETE_PRIVATE_LOCATION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Boolean>(t, CHECK_VIEWERSHIP_OF_PRIVATE_LOCATION_FAILED, true);
        }
        return r;
    }
}
