package ai.ilikeplaces.logic.crud;

import ai.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.logic.crud.unit.CRUDAlbumLocal;
import ai.ilikeplaces.logic.crud.unit.CRUDTribeLocal;
import ai.ilikeplaces.logic.crud.unit.CRUDWallLocal;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.logic.validators.unit.VTribeName;
import ai.ilikeplaces.logic.validators.unit.VTribeStory;
import ai.ilikeplaces.util.*;
import ai.util.HumanId;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 11:58:04 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({EntityManagerInjector.class, DBOffline.class, ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
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
    private static final String FETCH_TRIBE_SUCCESSFUL = "Fetch Tribe Successful!";
    private static final String FETCH_TRIBE_FAILED = "Fetch Tribe FAILED!";
    private static final String ADD_MEMBER_TO_TRIBE_SUCCESSFUL = "Add Member To Tribe Successful!";
    private static final String ADD_MEMBER_TO_TRIBE_FAILED = "Add Member To Tribe FAILED!";
    private static final String REMOVE_MEMBER_FROM_TRIBE_SUCCESSFUL = "Remove Member From Tribe Successful!";
    private static final String REMOVE_MEMBER_FROM_TRIBE_FAILED = "Remove Member From Tribe FAILED!";


    private static final String READ_ALBUM_SUCCESSFUL = "Read album Successful!";
    private static final String READ_ALBUM_FAILED = "Read album FAILED!";
    private static final String ADD_PHOTO_TO_ALBUM_SUCCESSFUL = "Add photo to album Successful!";
    private static final String ADD_PHOTO_TO_ALBUM_FAILED = "Add photo to album FAILED!";
    private static final String CREATE_TRIBE_FAILED = "Create Tribe FAILED!";
    private static final String CREATE_TRIBE_SUCCESSFUL = "Create Tribe Successful!";


    @EJB
    private CRUDTribeLocal crudTribeLocal_;

    @EJB
    private CRUDWallLocal crudWallLocal_;

    @EJB
    private CRUDAlbumLocal crudAlbumLocal_;

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
            final Tribe tribe = getTribe(whosWall__, (VLong) requester__);
            final Wall wall = crudWallLocal_
                    .doRWall(tribe.getTribeWall().getWallId(), refreshSpec__);

            if (wall.getWallMetadata() == null) {
                RecoveringFromAbsentMetadata:
                {
                    final String key = Wall.WallMetadataKey.TRIBE.toString();
                    final String value = "" + tribe.getTribeId();

                    crudWallLocal_.doUpdateMetadata(wall.getWallId(), key, value);
                }
            }

            r = new ReturnImpl<Wall>(wall, READ_WALL_SUCCESSFUL);
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
    public Return<Tribe> createTribe(final HumanId humanId, final VTribeName vTribeName, final VTribeStory vTribeStory) {
        Return<Tribe> r;
        try {
            r = new ReturnImpl<Tribe>(crudTribeLocal_.doNTxCTribe(humanId.getHumanId(), vTribeName.getObjectAsValid(), vTribeStory.getObjectAsValid()), CREATE_TRIBE_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Tribe>(t, CREATE_TRIBE_FAILED, true);
        }
        return r;
    }

    /**
     * @param humanId To be added to the given Tribe
     * @param tribeId The tribe to which to add the given user
     * @return The Tribe
     */
    @Override
    public Return<Tribe> addToTribe(final HumanId humanId, final VLong tribeId) {
        Return<Tribe> r;
        try {
            r = new ReturnImpl<Tribe>(crudTribeLocal_.addToTribe(humanId.getObjectAsValid(), tribeId.getObjectAsValid()), ADD_MEMBER_TO_TRIBE_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Tribe>(t, ADD_MEMBER_TO_TRIBE_FAILED, true);
        }
        return r;
    }

    /**
     * @param humanId used to check permissions
     * @param tribeId which to fetch
     * @return Tribe
     */
    @Override
    public Return<Tribe> getTribe(final HumanId humanId, final VLong tribeId, final boolean doRefresh) {
        Return<Tribe> r;
        try {
            r = new ReturnImpl<Tribe>(crudTribeLocal_.getTribe(tribeId.getObjectAsValid()).refresh(), FETCH_TRIBE_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Tribe>(t, FETCH_TRIBE_FAILED, true);
        }
        return r;
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
    public Return<Tribe> removeFromTribe(final HumanId humanId, final VLong tribeId) {
        Return<Tribe> r;
        try {
            r = new ReturnImpl<Tribe>(crudTribeLocal_.removeFromTribe(humanId.getObjectAsValid(), tribeId.getObjectAsValid()), REMOVE_MEMBER_FROM_TRIBE_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Tribe>(t, REMOVE_MEMBER_FROM_TRIBE_FAILED, true);
        }
        return r;
    }

    /**
     * @param humanId The humanId of whose to return all the Tribes she's member of
     * @return The Tribes the given user is a member of, unrefreshed
     */
    @Override
    public Set<Tribe> getHumansTribesAsSet(final HumanId humanId) {
        return crudTribeLocal_.getHumansTribesAsSet(humanId.getObjectAsValid());
    }

    /**
     * @param humanId The humanId of whose to return all the Tribes she's member of
     * @return The Tribes the given user is a member of, unrefreshed
     */
    @Override
    public List<Tribe> getHumansTribes(final HumanId humanId) {
        return crudTribeLocal_.getHumansTribes(humanId.getObjectAsValid());
    }

    public Return<Album> rTribeReadAlbum(final HumanId humanId, final VLong tribeId, final RefreshSpec refreshSpecInit) {
        Return<Album> r;
        try {
            r = new ReturnImpl<Album>(crudTribeLocal_.rTribeReadAlbum(humanId.getHumanId(), tribeId.getObjectAsValid(), refreshSpecInit), "Read Album SUCCESSFUL!");
        } catch (final Throwable t) {
            r = new ReturnImpl<Album>(t, "Read Album FAILED!", true);
        }
        return r;
    }

    @Override
    public Return<Album> uTribeAddEntryToAlbum(final HumanId operator__, final long tribeId__, final RefObj<String> cdnFileName) {
        Return<Album> r;
        try {
            r = new ReturnImpl<Album>(crudAlbumLocal_.doUAlbumOfTribeAddEntry(tribeId__, operator__.getObjectAsValid(), cdnFileName.getObjectAsValid()), ADD_PHOTO_TO_ALBUM_SUCCESSFUL);
        } catch (final Throwable t) {
            r = new ReturnImpl<Album>(t, ADD_PHOTO_TO_ALBUM_FAILED, true);
        }
        return r;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Return<List<Msg>> readWallLastEntries(final HumanId humanId, final Obj<Long> wallId, final Integer numberOfEntriesToFetch, final RefreshSpec refreshSpec__) {
        Return<List<Msg>> r;
        r = new ReturnImpl<List<Msg>>(crudWallLocal_.doRHumansWallLastEntries(wallId.getObj(), numberOfEntriesToFetch), READ_WALL_SUCCESSFUL);
        return r;

    }
}
