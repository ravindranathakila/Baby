package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.Obj;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.jpa.RefreshSpec;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 1/23/11
 * Time: 10:26 PM
 */
public interface GeneralCRUDWall {

    /**
     *
     * @param whosWall
     * @param msgOwner__ The (usually non-living) entity which owns this wall.
     * It is usually something like a {@link ai.ilikeplaces.entities.PrivateEvent PrivateEvent}
     * @param requester
     * @param contentToBeAppended
     * @return
     */
    public Return<Wall> addEntryToWall(final HumanId whosWall, final HumanId msgOwner__, final Obj requester, final String contentToBeAppended);

    public Return<Wall> muteWall(final HumanId operator, final HumanId mutee__, final Obj requester);

    public Return<Wall> unmuteWall(final HumanId operator, final HumanId mutee__, final Obj requester);

    public Return<Wall> readWall(final HumanId whosWall, final Obj requester, RefreshSpec refreshSpec__);
}
