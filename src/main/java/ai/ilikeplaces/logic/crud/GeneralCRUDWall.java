package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.Obj;
import ai.ilikeplaces.util.Return;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 1/23/11
 * Time: 10:26 PM
 */
public interface GeneralCRUDWall {
    public Return<Wall> addEntryToWall(final HumanId operator, final HumanId msgOwner__, final Obj wallOwnerId__, final String contentToBeAppended);

    public Return<Wall> muteWall(final HumanId operator, final HumanId mutee__, final Obj wallOwnerId__);

    public Return<Wall> unmuteWall(final HumanId operator, final HumanId mutee__, final Obj wallOwnerId__);

    public Return<Wall> readWall(final HumanId operator__, final Obj wallOwnerId__);
}
