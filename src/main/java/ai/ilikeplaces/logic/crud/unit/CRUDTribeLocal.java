package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansTribe;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;

import javax.ejb.Local;
import java.util.Set;

/**
 * /**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDTribeLocal {
// -------------------------- OTHER METHODS --------------------------

    /**
     * @param humanId To be added to the given Tribe
     * @param tribeId The tribe to which to add the given user
     * @return The Tribe
     */
    public Tribe addToTribe(final String humanId, final Long tribeId);

    /**
     * Creates a Tribe and adds this user as a Tribe member of it
     *
     * @param humanId    To be added to the Tribe
     * @param tribeName  Tribe Name
     * @param tribeStory Tribe Story
     * @return The created tribe
     */
    public Tribe doNTxCTribe(final String humanId, final String tribeName, final String tribeStory);


    /**
     * @param tribeId Tribe Id by which to fetch the tribe
     * @return The tribe who's tribe id is tribeId
     */
    public Tribe doRTribe(final Long tribeId);

    /**
     *
     * @param humanId
     * @return
     */
    public HumansTribe getHumansTribe(final String humanId);

    /**
     * @param humanId The humanId of whose to return all the Tribes she's member of
     * @return The Tribes the given user is a member of
     */
    public Set<Tribe> getHumansTribes(final String humanId);

    /**
     *
     * @param tribeId
     * @return
     */
    public Tribe getTribe(final Long tribeId);

    /**
     * @param humanId To be removed from the given Tribe
     * @param tribeId The tribe from which to remove the given user, and <b>if last member, removes the tribe too.</b>
     * @return The Tribe
     */
    public Tribe removeFromTribe(final String humanId, final Long tribeId);    
}
