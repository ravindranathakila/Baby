package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.logic.validators.unit.VTribeName;
import ai.ilikeplaces.logic.validators.unit.VTribeStory;
import ai.ilikeplaces.util.Obj;
import ai.ilikeplaces.util.RefObj;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.jpa.RefreshSpec;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDTribeLocal extends GeneralCRUDWall {

    final static public String NAME = HumanCRUDTribeLocal.class.getSimpleName();

    /**
     * Creates a Tribe and adds this user as a Tribe member of it
     *
     * @param humanId To be added to the Tribe
     * @return The created tribe
     */
    public Return<Tribe> createTribe(final HumanId humanId, final VTribeName vTribeName, final VTribeStory vTribeStory);

    /**
     * @param humanId To be added to the given Tribe
     * @param tribeId The tribe to which to add the given user
     * @return The Tribe
     */
    public Return<Tribe> addToTribe(final HumanId humanId, final VLong tribeId);

    /**
     * @param humanId used to check permissions
     * @param tribeId which to fetch
     * @return Tribe
     */
    public Tribe getTribe(final HumanId humanId, final VLong tribeId);

    /**
     * @param humanId   used to check permissions
     * @param tribeId   which to fetch
     * @param doRefresh whether to return a fully refreshed tribe
     * @return Tribe
     */
    public Return<Tribe> getTribe(final HumanId humanId, final VLong tribeId, final boolean doRefresh);

    /**
     * @param humanId To be removed from the given Tribe
     * @param tribeId The tribe from which to remove the given user, and <b>if last member, removes the tribe too.</b>
     * @return The Tribe
     */
    public Return<Tribe> removeFromTribe(final HumanId humanId, final VLong tribeId);

    /**
     * @param humanId The humanId of whose to return all the Tribes she's member of
     * @return The Tribes the given user is a member of
     */
    public Set<Tribe> getHumansTribes(final HumanId humanId);


    /**
     * @param humanId
     * @param tribeId
     * @param refreshSpecInit
     * @return
     */
    public Return<Album> rTribeReadAlbum(final HumanId humanId, final VLong tribeId, final RefreshSpec refreshSpecInit);

    /**
     * @param operator__
     * @param tribeId__
     * @param cdnFileName
     * @return
     */
    public Return<Album> uTribeAddEntryToAlbum(final HumanId operator__, final long tribeId__, final RefObj<String> cdnFileName);



    /**
     * @param wallId
     * @param numberOfEntriesToFetch
     * @return
     */
    public Return<List<Msg>> readWallLastEntries(final HumanId humanId, final Obj<Long> wallId, final Integer numberOfEntriesToFetch, final RefreshSpec refreshSpec__);

}