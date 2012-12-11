package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.logic.crud.unit.CRUDHumansWallLocal;
import ai.ilikeplaces.logic.crud.unit.CRUDWallLocal;
import ai.ilikeplaces.util.*;
import ai.reaver.Return;
import ai.reaver.ReturnImpl;
import ai.scribble.License;
import ai.scribble.WARNING;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 11:58:04 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({EntityManagerInjector.class, DBOffline.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDWall extends AbstractSLBCallbacks implements HumanCRUDWallLocal {


    private static final String READ_WALL_ID_SUCCESSFUL = "Read Wall Id Successful!";
    private static final String READ_WALL_ID_FAILED = "Read Wall Id FAILED!";
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Deprecated
    public Return<Wall> readWall(final HumanId whosWall, final Obj requester, final RefreshSpec refreshSpec__) {
        Return<Wall> r;
        r = new ReturnImpl<Wall>(crudHumansWallLocal_.doRHumansWallRefreshed(whosWall.getObj()).getWall(), READ_WALL_SUCCESSFUL);
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Return<List<Msg>> readWallLastEntries(final HumanId whosWall, final Obj requester, final Integer numberOfEntriesToFetch, final RefreshSpec refreshSpec__) {
        Return<List<Msg>> r;
        r = new ReturnImpl<List<Msg>>(crudHumansWallLocal_.doRHumansWallLastEntries(whosWall.getObj(), numberOfEntriesToFetch), READ_WALL_SUCCESSFUL);
        return r;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Long> readWallId(final HumanId whosWall, final Obj requester) {
        Return<Long> r;
        try {
            r = new ReturnImpl<Long>(crudHumansWallLocal_.doDirtyRHumansWallID(whosWall.getObj()), READ_WALL_ID_SUCCESSFUL);
        } catch (DBDishonourCheckedException e) {
            r = new ReturnImpl<Long>(e, READ_WALL_ID_FAILED, true);
        }
        return r;

    }

    @WARNING(warning = "Please not that the underlaying rHumansWall method assumes transaction scope REQUIRED, which is used here.")
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Return<Wall> addEntryToWall(final HumanId whosWall, final HumanId msgOwner__, Obj wallReference__, final String contentToBeAppended) {
        final Wall wall = crudHumansWallLocal_.doRHumansWallRefreshed(whosWall.getObj()).getWall();

//        wall.getWallMsgs().size();//refreshing
//        wall.getWallMsgs().add(new Msg()
//                .setMsgContentR(contentToBeAppended)
//                .setMsgTypeR(Msg.msgTypeHUMAN)
//                .setMsgMetadataR(msgOwner__.getObj()));

        crudHumansWallLocal_.doUHumansWallMsgs(whosWall.getObj(), (new Msg()
                .setMsgContentR(contentToBeAppended)
                .setMsgTypeR(Msg.msgTypeHUMAN)
                .setMsgMetadataR(msgOwner__.getObj())));

        return new ReturnImpl<Wall>(wall, UPDATE_WALL_SUCCESSFUL);

    }

    @Override
    public Return<Wall> muteWall(final HumanId operator__, final HumanId mutee, Obj wallReference) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doUAddMuteEntry(crudHumansWallLocal_.doDirtyRHumansWallID(operator__.getHumanId()),
                            mutee.getObj()), UPDATE_WALL_SUBSCRIPTION_SUCCESSFUL);
        } catch (final AbstractEjbApplicationException t) {
            r = new ReturnImpl<Wall>(t, UPDATE_WALL_SUBSCRIPTION_FALIED, true);
        }
        return r;


    }

    @Override
    public Return<Wall> unmuteWall(final HumanId operator__, final HumanId mutee, Obj wallReference) {

        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doURemoveMuteEntry(crudHumansWallLocal_.doDirtyRHumansWallID(operator__.getHumanId()),
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
