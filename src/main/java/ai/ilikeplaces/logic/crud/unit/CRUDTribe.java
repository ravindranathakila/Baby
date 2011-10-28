package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.HumansTribe;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.jpa.CrudService;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;

import javax.ejb.*;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/27/11
 * Time: 7:33 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CRUDTribe implements CRUDTribeLocal {
// ------------------------------ FIELDS ------------------------------

    @EJB
    private CrudServiceLocal<Tribe> tribeCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansTribe> humansTribeCrudServiceLocal__;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface CRUDTribeLocal ---------------------

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Tribe doNTxCTribe(final String humanId, final String tribeName, final String tribeStory) {
        final Tribe managedTribe = tribeCrudServiceLocal_.create(
                new Tribe()
                        .setTribeNameR(tribeName)
                        .setTribeStoryR(tribeStory)
                        .setTribeWallR(new Wall())
                        .setTribeAlbumR(new Album()));

        final HumansTribe managedHumansTribe = getHumansTribe(humanId);

        WiringBothSidesOfTheRelationship:
        {
            managedTribe.getTribeMembers().add(managedHumansTribe);
            managedHumansTribe.getTribes().add(managedTribe);
        }

        return managedTribe;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HumansTribe getHumansTribe(String humanId) {
        return humansTribeCrudServiceLocal__.find(HumansTribe.class, humanId);
    }

    /**
     * @param humanId To be added to the given Tribe
     * @param tribeId The tribe to which to add the given user
     * @return The Tribe
     */
    @Override
    public Tribe addToTribe(final String humanId,final Long tribeId) {
        final Tribe managedTribe = getTribe(tribeId);

        final HumansTribe managedHumansTribe = getHumansTribe(humanId);

        WiringBothSidesOfTheRelationship:
        {
            managedTribe.getTribeMembers().add(managedHumansTribe);
            managedHumansTribe.getTribes().add(managedTribe);
        }

        return managedTribe;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Tribe getTribe(final Long tribeId) {
        return tribeCrudServiceLocal_.find(Tribe.class, tribeId);
    }

    /**
     * @param humanId To be removed from the given Tribe
     * @param tribeId The tribe from which to remove the given user, and <b>if last member, removes the tribe too.</b>
     * @return The Tribe
     */
    @Override
    public Tribe removeFromTribe(final String humanId, final Long tribeId) {
        final Tribe managedTribe = getTribe(tribeId);

        final HumansTribe managedHumansTribe = getHumansTribe(humanId);

        WiringBothSidesOfTheRelationship:
        {
            final boolean isMember = managedTribe.getTribeMembers().contains(managedHumansTribe);
            if (managedTribe.getTribeMembers().size() == 1 && isMember) {
                managedTribe.getTribeMembers().remove(managedHumansTribe);
                managedHumansTribe.getTribes().remove(managedTribe);
                tribeCrudServiceLocal_.delete(Tribe.class, tribeId);
            } else if (isMember) {
                managedTribe.getTribeMembers().remove(managedHumansTribe);
                managedHumansTribe.getTribes().remove(managedTribe);
            }
        }

        return managedTribe;
    }

    /**
     * @param humanId The humanId of whose to return all the Tribes she's member of
     * @return The Tribes the given user is a member of
     */
    @Override
    public Set<Tribe> getHumansTribes(final String humanId) {
        final HumansTribe humansTribe = getHumansTribe(humanId);

        humansTribe.getTribes().size();

        return humansTribe.getTribes();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Tribe doRTribe(final Long tribeId) {
        return tribeCrudServiceLocal_.find(Tribe.class, tribeId);
    }
}
