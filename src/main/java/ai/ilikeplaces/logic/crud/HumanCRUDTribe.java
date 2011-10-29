package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.crud.unit.CRUDTribeLocal;
import ai.ilikeplaces.logic.crud.unit.CRUDWallLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.logic.validators.unit.VTribeName;
import ai.ilikeplaces.logic.validators.unit.VTribeStory;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.jpa.RefreshSpec;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 11:58:04 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDTribe extends AbstractSLBCallbacks implements HumanCRUDTribeLocal {
// ------------------------------ FIELDS ------------------------------

    private static final String WRITE_WALL_SUCCESSFUL = "Write Wall Successful!";
    private static final String WRITE_WALL_FAILED = "Write Wall FAILED!";
    private static final String MUTE_WALL_SUCCESSFUL = "Mute Wall Successful!";
    private static final String MUTE_WALL_FAILED = "Mute Wall FAILED!";
    private static final String UNMUTE_WALL_SUCCESSFUL = "Unmute Wall Successful!";
    private static final String UNMUTE_WALL_FAILED = "Unmute Wall FAILED!";
    private static final String READ_WALL_SUCCESSFUL = "Read Wall Successful!";
    private static final String READ_WALL_FAILED = "Read Wall FAILED!";

    private static final RefreshSpec REFRESH_SPEC = new RefreshSpec("privatePhotoWall");


    @EJB
    private CRUDTribeLocal crudTribeLocal_;

    @EJB
    private CRUDWallLocal crudWallLocal_;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface GeneralCRUDWall ---------------------


    @Override
    public Return<Wall> addEntryToWall(final HumanId whosWall__, final HumanId msgOwner__, final Obj wallReference__, final String contentToBeAppended) {
        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doUAddEntry((getTribe(msgOwner__, (VLong) wallReference__).getTribeWall().getWallId()),
                            msgOwner__.getObj(),
                            contentToBeAppended), WRITE_WALL_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Wall>(t, WRITE_WALL_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Wall> muteWall(final HumanId operator__, final HumanId mutee, final Obj wallReference__) {
        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doUAddMuteEntry((getTribe(operator__, (VLong) wallReference__).getTribeWall().getWallId()),
                            mutee.getObj()), MUTE_WALL_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Wall>(t, MUTE_WALL_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Wall> unmuteWall(final HumanId operator__, final HumanId mutee, final Obj wallReference__) {
        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doURemoveMuteEntry((getTribe(operator__, (VLong) wallReference__).getTribeWall().getWallId()),
                            mutee.getObj()), UNMUTE_WALL_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Wall>(t, UNMUTE_WALL_FAILED, true);
        }
        return r;
    }

    @Override
    public Return<Wall> readWall(final HumanId whosWall__, final Obj requester__, RefreshSpec refreshSpec__) {
        Return<Wall> r;
        try {
            r = new ReturnImpl<Wall>(crudWallLocal_
                    .doRWall(getTribe(whosWall__, (VLong) requester__).getTribeWall().getWallId(), refreshSpec__), READ_WALL_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Wall>(t, READ_WALL_FAILED, true);
        }
        return r;
    }

// --------------------- Interface HumanCRUDTribeLocal ---------------------

    /**
     * Creates a Tribe and adds this user as a Tribe member of it
     *
     * @param humanId To be added to the Tribe
     * @return The created tribe
     */
    @Override
    public Tribe createTribe(final HumanId humanId, final VTribeName vTribeName, final VTribeStory vTribeStory) {
        return crudTribeLocal_.doNTxCTribe(humanId.getHumanId(), vTribeName.getObjectAsValid(), vTribeStory.getObjectAsValid());
    }

    /**
     * @param humanId To be added to the given Tribe
     * @param tribeId The tribe to which to add the given user
     * @return The Tribe
     */
    @Override
    public Tribe addToTribe(final HumanId humanId, final VLong tribeId) {
        return crudTribeLocal_.addToTribe(humanId.getObjectAsValid(), tribeId.getObjectAsValid());
    }

    /**
     * @param humanId used to check permissions
     * @param tribeId which to fetch
     * @return Tribe
     */
    @Override
    public Tribe getTribe(final HumanId humanId, final VLong tribeId) {
        return crudTribeLocal_.getTribe(tribeId.getObjectAsValid());
    }

    /**
     * @param humanId To be removed from the given Tribe
     * @param tribeId The tribe from which to remove the given user, and <b>if last member, removes the tribe too.</b>
     * @return The Tribe
     */
    @Override
    public Tribe removeFromTribe(final HumanId humanId, final VLong tribeId) {
        return crudTribeLocal_.removeFromTribe(humanId.getObjectAsValid(), tribeId.getObjectAsValid());
    }

    /**
     * @param humanId The humanId of whose to return all the Tribes she's member of
     * @return The Tribes the given user is a member of
     */
    @Override
    public Set<Tribe> getHumansTribes(final HumanId humanId) {
        return crudTribeLocal_.getHumansTribes(humanId.getObjectAsValid());
    }
}