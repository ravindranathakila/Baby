package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.Obj;
import ai.ilikeplaces.util.RefObj;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.jpa.RefreshSpec;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 2:41:45 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDPrivateEventLocal extends GeneralCRUDWall {

    public static final String NAME = HumanCRUDPrivateEventLocal.class.getSimpleName();

    /**
     * @param humanId
     * @param privateLocationId
     * @param privateEventName
     * @param privateEventInfo
     * @param startDate
     * @param endDate
     * @return
     */
    public Return<PrivateEvent> cPrivateEvent(final String humanId, final long privateLocationId, final String privateEventName, final String privateEventInfo, final String startDate, final String endDate);

    /**
     * @param humanId
     * @param privateEventId
     * @return
     */
    public Return<PrivateEvent> dirtyRPrivateEventAsAny(final String humanId, final long privateEventId);

    /**
     * @param humanId
     * @param privateEventId
     * @return
     */
    public Return<PrivateEvent> dirtyRPrivateEventInfoAsAny(final String humanId, final long privateEventId);

    /**
     * @param humanId
     * @param privateEventId
     * @return
     */
    public Return<Boolean> dirtyRPrivateEventIsOwner(final HumanId humanId, final Long privateEventId);

    /**
     * @param humanId
     * @param privateEventId
     * @return
     */
    public Return<Boolean> dirtyRPrivateEventIsViewer(final HumanId humanId, final Long privateEventId);

    /**
     * @param friend__
     * @param privateEventId__
     * @param owner
     * @return
     */
    public Return<PrivateEvent> uPrivateEventAddOwner(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    /**
     * @param owner__
     * @param privateEventId__
     * @param friend__
     * @param privateLocationId__
     * @return
     */
    public Return<PrivateEvent> uPrivateEventAddOwnerWithPrivateLocationCheck(final HumanId owner__, final long privateEventId__, final HumansFriend friend__, final long privateLocationId__);

    /**
     * @param friend__
     * @param privateEventId__
     * @param owner
     * @return
     */
    public Return<PrivateEvent> uPrivateEventRemoveOwner(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    /**
     * @param friend__
     * @param privateEventId__
     * @param owner
     * @return
     */
    public Return<PrivateEvent> uPrivateEventAddVisitor(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    /**
     * @param owner__
     * @param privateEventId__
     * @param friend__
     * @param privateLocationId__
     * @return
     */
    public Return<PrivateEvent> uPrivateEventAddVisitorWithPrivateLocationCheck(final HumanId owner__, final long privateEventId__, final HumansFriend friend__, final long privateLocationId__);

    /**
     * @param friend__
     * @param privateEventId__
     * @param owner
     * @return
     */
    public Return<PrivateEvent> uPrivateEventRemoveVisitor(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    /**
     * @param friend__
     * @param privateEventId__
     * @param owner
     * @return
     */
    public Return<PrivateEvent> uPrivateEventAddInvite(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    /**
     * @param owner__
     * @param privateEventId__
     * @param friend__
     * @param privateLocationId__
     * @return
     */
    public Return<PrivateEvent> uPrivateEventAddInviteWithPrivateLocationCheck(final HumanId owner__, final long privateEventId__, final HumansFriend friend__, final long privateLocationId__);

    /**
     * @param friend__
     * @param privateEventId__
     * @param owner
     * @return
     */
    public Return<PrivateEvent> uPrivateEventRemoveInvite(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    /**
     * @param friend__
     * @param privateEventId__
     * @param owner
     * @return
     */
    public Return<PrivateEvent> uPrivateEventAddReject(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    /**
     * @param friend__
     * @param privateEventId__
     * @param owner
     * @return
     */
    public Return<PrivateEvent> uPrivateEventRemoveReject(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    /**
     * @param operator__
     * @param privateEventId__
     * @return
     */
    public Return<Boolean> dPrivateEvent(final HumanId operator__, final long privateEventId__);


    /**
     * @param operator__
     * @param privateEventId__
     * @return
     */
    @Deprecated
    public Return<Album> rPrivateEventReadAlbum(final HumanId operator__, final long privateEventId__);

    /**
     *
     * @param operator__
     * @param privateEventId__
     * @param refreshSpec
     * @return
     */
    public Return<Album> rPrivateEventReadAlbum(final HumanId operator__, final long privateEventId__, final RefreshSpec refreshSpec);

    /**
     * @param operator__
     * @param privateEventId__
     * @param cdnFileName
     * @return
     */
    public Return<Album> uPrivateEventAddEntryToAlbum(final HumanId operator__, final long privateEventId__, final RefObj<String> cdnFileName);

    /**
     * @param operator__
     * @param privateEventId__
     * @return
     */
    public Return<Album> uPrivateEventRemoveEntryFromAlbum(final HumanId operator__, final long privateEventId__);


    /**
     * @param operator__    operator__
     * @param latitudeSouth horizontal bottom of bounding box
     * @param latitudeNorth horizontal up of bounding box
     * @param longitudeWest vertical left of bounding box
     * @param longitudeEast vertical right of bounding box
     * @return the list of private events inside a specific bounding box
     */
    public Return<List<PrivateEvent>> doRPrivateEventsByBounds(final HumanId operator__, final double latitudeSouth, final double latitudeNorth, final double longitudeWest, final double longitudeEast);

    /**
     * @param latitudeSouth horizontal bottom of bounding box
     * @param latitudeNorth horizontal up of bounding box
     * @param longitudeWest vertical left of bounding box
     * @param longitudeEast vertical right of bounding box
     * @return the list of private events inside a specific bounding box
     */
    public Return<List<PrivateEvent>> doRPrivateEventsByBoundsAsSystem(final double latitudeSouth, final double latitudeNorth, final double longitudeWest, final double longitudeEast);



    /**
     * @param wallId
     * @param numberOfEntriesToFetch
     * @return
     */
    public Return<List<Msg>> readWallLastEntries(final HumanId humanId, final Obj<Long> wallId, final Integer numberOfEntriesToFetch, final RefreshSpec refreshSpec__);

    /**
     *
     * @param humanId
     * @return
     */
    public Return<List<PrivateEvent>> doDirtyRPrivateEventsOfHuman(final HumanId humanId);

}
