package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.RefObj;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 2:41:45 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDPrivateEventLocal {

    public static final String NAME = HumanCRUDPrivateEventLocal.class.getSimpleName();

    public Return<PrivateEvent> cPrivateEvent(final String humanId, final long privateLocationId, final String privateEventName, final String privateEventInfo, final String startDate, final String endDate);

    public Return<PrivateEvent> dirtyRPrivateEventAsAny(final String humanId, final long privateEventId);


    public Return<Boolean> dirtyRPrivateEventIsOwner(final HumanId humanId, final Long privateEventId);

    public Return<Boolean> dirtyRPrivateEventIsViewer(final HumanId humanId, final Long privateEventId);


    public Return<PrivateEvent> uPrivateEventAddOwner(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    public Return<PrivateEvent> uPrivateEventAddOwnerWithPrivateLocationCheck(final HumanId owner__, final long privateEventId__, final HumansFriend friend__, final long privateLocationId__);

    public Return<PrivateEvent> uPrivateEventRemoveOwner(final HumanId friend__, final long privateEventId__, final HumansFriend owner);


    public Return<PrivateEvent> uPrivateEventAddVisitor(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    public Return<PrivateEvent> uPrivateEventAddVisitorWithPrivateLocationCheck(final HumanId owner__, final long privateEventId__, final HumansFriend friend__, final long privateLocationId__);

    public Return<PrivateEvent> uPrivateEventRemoveVisitor(final HumanId friend__, final long privateEventId__, final HumansFriend owner);


    public Return<PrivateEvent> uPrivateEventAddInvite(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    public Return<PrivateEvent> uPrivateEventAddInviteWithPrivateLocationCheck(final HumanId owner__, final long privateEventId__, final HumansFriend friend__, final long privateLocationId__);

    public Return<PrivateEvent> uPrivateEventRemoveInvite(final HumanId friend__, final long privateEventId__, final HumansFriend owner);


    public Return<PrivateEvent> uPrivateEventAddReject(final HumanId friend__, final long privateEventId__, final HumansFriend owner);

    public Return<PrivateEvent> uPrivateEventRemoveReject(final HumanId friend__, final long privateEventId__, final HumansFriend owner);


    public Return<Wall> uPrivateEventAddToWall(final HumanId operator, final long privateEventId__, final String contentToBeAppended);

    public Return<Wall> uPrivateEventAddEntryToWall(final HumanId operator, final HumanId msgOwner__, final long privateEventId__, final String contentToBeAppended);

    public Return<Wall> uPrivateEventClearWall(final HumanId operator__, final long privateEventId__);

    public Return<Wall> rPrivateEventReadWall(final HumanId operator__, final long privateEventId__);


    public Return<Boolean> dPrivateEvent(final HumanId operator__, final long privateEventId__);


    public Return<Album> rPrivateEventReadAlbum(final HumanId operator__, final long privateEventId__);

    public Return<Album> uPrivateEventAddEntryToAlbum(final HumanId operator__, final long privateEventId__, final RefObj<String> cdnFileName);

    public Return<Album> uPrivateEventRemoveEntryFromAlbum(final HumanId operator__, final long privateEventId__);


    /**
     * @param latitudeSouth horizontal bottom of bounding box
     * @param latitudeNorth horizontal up of bounding box
     * @param longitudeWest vertical left of bounding box
     * @param longitudeEast vertical right of bounding box
     * @return the list of private events inside a specific bounding box
     */
    public Return<List<PrivateEvent>> doRPrivateEventsByBounds(final HumanId operator__, final double latitudeSouth, final double latitudeNorth, final double longitudeWest, final double longitudeEast);
}
