package ai.baby.logic.crud;

import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.baby.util.Obj;
import ai.reaver.Return;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 1/23/11
 * Time: 10:26 PM
 */
public interface GeneralCRUDWall {

    /**
     * @param whosWall
     * @param msgOwner__          The (usually non-living) entity which owns this wall.
     *                            It is usually something like a {@link ai.ilikeplaces.entities.PrivateEvent PrivateEvent}
     * @param wallReference
     * @param contentToBeAppended
     * @return
     */
    public Return<Wall> addEntryToWall(final HumanId whosWall, final HumanId msgOwner__, final Obj wallReference, final String contentToBeAppended);

    public Return<Wall> muteWall(final HumanId operator, final HumanId mutee__, final Obj wallReference);

    public Return<Wall> unmuteWall(final HumanId operator, final HumanId mutee__, final Obj wallReference);

    public Return<Wall> readWall(final HumanId whosWall, final Obj requester, RefreshSpec refreshSpec__);
}
