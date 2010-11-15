package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.logic.crud.unit.CRUDHumansWallLocal;
import ai.ilikeplaces.logic.crud.unit.CRUDWallLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 11:58:04 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDWall extends AbstractSLBCallbacks implements HumanCRUDWallLocal {


    @WARNING(warning = "Try not to use CRUDWallLocal because CRUDHumansWallLocal suffices. Pay special attention to performance issues here. Walls are bulky")
    @EJB
    private CRUDHumansWallLocal crudHumansWallLocal_;

    @WARNING(warning = "Try not to use CRUDWallLocal because CRUDHumansWallLocal suffices. Pay special attention to performance issues here. Walls are bulky")
    @EJB
    private CRUDWallLocal crudWallLocal_;

    public static final String READ_WALL_SUCCESSFUL = "Read Wall Successful!";
    public static final String READ_WALL_FAILED = "Read Wall FAILED!";
    public static final String UPDATE_WALL_SUCCESSFUL = "Update Wall Successful!";
    public static final String UPDATE_WALL_FAILED = "Update wall FAILED!";
    private static final String UPDATE_WALL_SUBSCRIPTION_SUCCESSFUL = "Update Wall Subscription Successful!";
    private static final String UPDATE_WALL_SUBSCRIPTION_FALIED = "Update Wall Subscription FALIED!";


    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Wall> dirtyRWall(final HumanId humanId) {
        Return<Wall> r;
        r = new ReturnImpl<Wall>(crudHumansWallLocal_.dirtyRHumansWall(humanId.getObj()).getWall(), READ_WALL_SUCCESSFUL);
        return r;

    }

    @WARNING(warning = "Please not that the underlaying rHumansWall method assumes transaction scope REQUIRED, which is used here.")
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Wall> uNTxAddEntryToWall(final HumanId humanId__, final HumanId msgOwner__, final String contentToBeAppended) {
        Return<Wall> r;
        try {
            final Wall wall = crudHumansWallLocal_.rHumansWall(humanId__.getObj()).getWall();

            wall.getWallMsgs().add(new Msg()
                    .setMsgContentR(contentToBeAppended)
                    .setMsgTypeR(Msg.msgTypeHUMAN)
                    .setMsgMetadataR(msgOwner__.getObj()));

            r = new ReturnImpl<Wall>(wall, UPDATE_WALL_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_WALL_FAILED, true);
        }
        return r;

    }

    @Override
    public Return<Wall> uWallAddMuteEntryToWall(final HumanId operator__, final HumanId mutee) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doNTxUAddMuteEntry(crudHumansWallLocal_.dirtyRHumansWallID(operator__.getHumanId()),
                                           mutee.getObj()), UPDATE_WALL_SUBSCRIPTION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_WALL_SUBSCRIPTION_FALIED, true);
        }
        return r;


    }

    @Override
    public Return<Wall> uWallRemoveMuteEntryToWall(final HumanId operator__, final HumanId mutee) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doNTxURemoveMuteEntry(crudHumansWallLocal_.dirtyRHumansWallID(operator__.getHumanId()),
                                           mutee.getObj()), UPDATE_WALL_SUBSCRIPTION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_WALL_SUBSCRIPTION_FALIED, true);
        }
        return r;


    }


    @Override
    public String verify() {
        throw ExceptionCache.METHOD_NOT_IMPLEMENTED;
    }
}