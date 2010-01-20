package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.crud.unit.CPrivateLocationLocal;
import ai.ilikeplaces.logic.crud.unit.DPrivateLocationLocal;
import ai.ilikeplaces.logic.crud.unit.RPrivateLocationLocal;
import ai.ilikeplaces.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 11:58:04 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({MethodTimer.class, MethodParams.class})
public class HumanCRUDPrivateLocation extends AbstractSLBCallbacks implements HumanCRUDPrivateLocationLocal {

    @EJB
    private CPrivateLocationLocal cPrivateLocationLocal;

    @EJB
    private RPrivateLocationLocal rPrivateLocationLocal;

    @EJB
    private DPrivateLocationLocal dPrivateLocationLocal;

    @Override
    public Return<PrivateLocation> cPrivateLocation(final String humanId, final String privateLocationName, final String privateLocationInfo) {
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(cPrivateLocationLocal.doNTxCPrivateLocation(humanId, privateLocationName, privateLocationInfo), "Save private location Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<PrivateLocation>(t, "Save private location FAILED!", true);
        }
        return r;

    }

    @Override
    public Return<PrivateLocation> rPrivateLocation(final String humanId, final long privateLocationId) {
        Return<PrivateLocation> r;
        try {
            r = new ReturnImpl<PrivateLocation>(rPrivateLocationLocal.doDirtyRPrivateEvent(humanId, privateLocationId), "View private location Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<PrivateLocation>(t, "View private location FAILED!", true);
        }
        return r;

    }

    @Override
    public Return<Boolean> dPrivateLocation(final String humanId, final long privateLocationId) {
        Return<Boolean> r;
        try {
            r = new ReturnImpl<Boolean>(dPrivateLocationLocal.doNTxDPrivateLocation(humanId, privateLocationId), "Delete private location Successful!");
        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "Delete private location FAILED!", true);
        }
        return r;

    }
}
