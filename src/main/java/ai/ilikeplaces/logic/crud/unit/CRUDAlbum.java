package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.entities.etc.RefreshException;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CRUDAlbum extends AbstractSLBCallbacks implements CRUDAlbumLocal {

    @EJB
    private CrudServiceLocal<Album> albumCrudServiceLocal_;

    //@EJB
    //private CrudServiceLocal<PrivatePhoto> privatePhotoCrudServiceLocal_;

    @EJB
    private CPrivatePhotoLocal cPrivatePhotoLocal_;

    @EJB
    private RPrivateEventLocal rPrivateEventLocal_;

    @EJB
    private CRUDTribeLocal crudTribeLocal_;

    @EJB
    private RHumanLocal rHumanLocal_;


    final static private RefreshSpec REFRESH_SPEC_INIT = new RefreshSpec("albumPhotos");


    public CRUDAlbum() {
    }


    final static Logger logger = LoggerFactory.getLogger(CRUDAlbum.class);

    /**
     * Method for use with Albums where the album upload mode is shown to a privateevent owner who does direct uploads
     * to the album of the privateevent, instead of individually uploading them and assigning them to the album.
     * <p/>
     * <p/>
     * Adding a photo to an album actually consists of 2 steps which in the case, are done as 1.
     * <p/>
     * <p/>
     * Usually, a use is supposed to upload photos and then allocate it to an album.
     * 1. Upload photo.
     * 2. Assign it to album
     * <p/>
     * <p/>
     * Therefore,
     * <p/>
     * <p/>
     * 1. Add the photo as belonging to the user. i.e. HumansPrivatePhoto.
     * <p/>
     * 2. Add the photo to the album. This is a two sided wiring.
     *
     * @param privateEventId
     * @param humanId
     * @param photoUrl
     * @return
     * @throws DBDishonourCheckedException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public Album doUAlbumOfPrivateEventAddEntry(final long privateEventId, final String humanId, final String photoUrl) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {


        /**
         * Adding photo to users
         */
        final PrivatePhoto managedPrivatePhoto__ = cPrivatePhotoLocal_.doCPrivatePhotoLocal(humanId, photoUrl);

//        final HumansPrivatePhoto MhumansPrivatePhoto__ = rHumanLocal_.doRHuman(humanId).getHumansPrivatePhoto();
//        final List<PrivatePhoto> MprivatePhotos__ = MhumansPrivatePhoto__.getPrivatePhotos();//Eager
//
//        WiringBothSides:
//        {
//            MprivatePhotos__.add(managedPrivatePhoto__);
//            managedPrivatePhoto__.setHumansPrivatePhoto(MhumansPrivatePhoto__);
//        }

        ////////////////////////////////////////////////////////

        final long UalbumId__ = rPrivateEventLocal_.doRPrivateEventAsOwner(humanId, privateEventId).getPrivateEventAlbum().getAlbumId();
        final Album Malbum__ = albumCrudServiceLocal_.findBadly(Album.class, UalbumId__);
        WiringBothSides:
        {
            Malbum__.getAlbumPhotos().add(managedPrivatePhoto__);
            managedPrivatePhoto__.getAlbums().add(Malbum__);
        }
        return Malbum__;

    }


    /**
     * @param humanId
     * @param privateEventId by which to fetch the album
     * @param eager
     * @return
     * @throws DBDishonourCheckedException
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Deprecated
    public Album doRAlbumByPrivateEvent(final String humanId, final long privateEventId, final boolean eager) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        return eager ?
                rPrivateEventLocal_.doRPrivateEventAsSystem(privateEventId, false).getPrivateEventAlbum().refresh() :
                rPrivateEventLocal_.doRPrivateEventAsSystem(privateEventId, false).getPrivateEventAlbum();
    }

    /**
     * @param humanId
     * @param privateEventId by which to fetch the album
     * @param refreshSpec
     * @return
     * @throws DBDishonourCheckedException
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Album doRAlbumByPrivateEvent(final String humanId, final long privateEventId, final RefreshSpec refreshSpec) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        try {
            return rPrivateEventLocal_.doRPrivateEventAsSystem(privateEventId, false).getPrivateEventAlbum().refresh(refreshSpec);
        } catch (final RefreshException e) {
            throw new DBFetchDataException(e);
        }
    }


    /**
     * Method for use with Albums where the album upload mode is shown to a privateevent owner who does direct uploads
     * to the album of the privateevent, instead of individually uploading them and assigning them to the album.
     * <p/>
     * <p/>
     * Adding a photo to an album actually consists of 2 steps which in the case, are done as 1.
     * <p/>
     * <p/>
     * Usually, a use is supposed to upload photos and then allocate it to an album.
     * 1. Upload photo.
     * 2. Assign it to album
     * <p/>
     * <p/>
     * Therefore,
     * <p/>
     * <p/>
     * 1. Add the photo as belonging to the user. i.e. HumansPrivatePhoto.
     * <p/>
     * 2. Add the photo to the album. This is a two sided wiring.
     *
     * @param tribeId
     * @param humanId
     * @param photoUrl
     * @return
     * @throws DBDishonourCheckedException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public Album doUAlbumOfTribeAddEntry(final long tribeId, final String humanId, final String photoUrl) throws DBDishonourCheckedException, DBFetchDataException, RefreshException {


        /**
         * Adding photo to users
         */
        final PrivatePhoto managedPrivatePhoto__ = cPrivatePhotoLocal_.doCPrivatePhotoLocal(humanId, photoUrl);


        final Album Malbum__ = crudTribeLocal_.rTribeReadAlbum(humanId, tribeId, REFRESH_SPEC_INIT);
        WiringBothSides:
        {
            Malbum__.getAlbumPhotos().add(managedPrivatePhoto__);
            managedPrivatePhoto__.getAlbums().add(Malbum__);
        }
        return Malbum__;

    }


    /**
     * @param humanId
     * @param tribeId     by which to fetch the album
     * @param refreshSpec
     * @return
     * @throws DBDishonourCheckedException
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Album doRAlbumByTribe(final String humanId, final long tribeId, final RefreshSpec refreshSpec) throws DBDishonourCheckedException, DBFetchDataException {
        try {
            return crudTribeLocal_.rTribeReadAlbum(humanId, tribeId, REFRESH_SPEC_INIT);
        } catch (final RefreshException e) {
            throw new DBFetchDataException(e);
        }
    }
}
