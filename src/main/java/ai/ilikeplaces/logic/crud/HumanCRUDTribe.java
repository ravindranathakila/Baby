package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.exception.AbstractEjbApplicationException;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.logic.crud.unit.CRUDHumansWallLocal;
import ai.ilikeplaces.logic.crud.unit.CRUDWallLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.jpa.RefreshSpec;

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
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class, RuntimeExceptionWrapper.class})
public class HumanCRUDTribe extends AbstractSLBCallbacks implements HumanCRUDTribeLocal {

    /**
     * Creates a Tribe and adds this user as a Tribe member of it
     *
     * @param humanId To be added to the Tribe
     * @return The created tribe
     */
    @Override
    public Tribe createTribe(HumanId humanId) {
        return null;  
    }

    /**
     * @param humanId To be added to the given Tribe
     * @param tribeId The tribe to which to add the given user
     * @return The Tribe
     */
    @Override
    public Tribe addToTribe(HumanId humanId, VLong tribeId) {
        return null;  
    }

    /**
     * @param humanId To be removed from the given Tribe
     * @param tribeId The tribe from which to remove the given user, and <b>if last member, removes the tribe too.</b>
     * @return The Tribe
     */
    @Override
    public Tribe removeFromTribe(HumanId humanId, VLong tribeId) {
        return null;  
    }

    /**
     * @param humanId The humanId of whose to return all the Tribes she's member of
     * @return The Tribes the given user is a member of
     */
    @Override
    public Set<Tribe> getHumansTribes(HumanId humanId) {
        return null;  
    }

    /**
     * @param whosWall
     * @param msgOwner__          The (usually non-living) entity which owns this wall.
     *                            It is usually something like a {@link ai.ilikeplaces.entities.PrivateEvent PrivateEvent}
     * @param requester
     * @param contentToBeAppended
     * @return
     */
    @Override
    public Return<Wall> addEntryToWall(HumanId whosWall, HumanId msgOwner__, Obj requester, String contentToBeAppended) {
        return null;  
    }

    @Override
    public Return<Wall> muteWall(HumanId operator, HumanId mutee__, Obj requester) {
        return null;  
    }

    @Override
    public Return<Wall> unmuteWall(HumanId operator, HumanId mutee__, Obj requester) {
        return null;  
    }

    @Override
    public Return<Wall> readWall(HumanId whosWall, Obj requester, RefreshSpec refreshSpec__) {
        return null;  
    }
}