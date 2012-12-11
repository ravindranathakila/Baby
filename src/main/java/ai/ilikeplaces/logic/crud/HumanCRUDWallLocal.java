package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.logic.verify.util.Verify;
import ai.ilikeplaces.util.Obj;
import ai.reaver.HumanId;
import ai.reaver.Return;
import ai.scribble.License;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Remote
public interface HumanCRUDWallLocal extends Verify, GeneralCRUDWall {

    final static public String NAME = HumanCRUDWallLocal.class.getSimpleName();

    public Return<List<Msg>> readWallLastEntries(final HumanId whosWall, final Obj wallOwnerId__, final Integer numberOfEntriesToFetch, final RefreshSpec refreshSpec__);

    public Return<Long> readWallId(final HumanId whosWall, final Obj wallOwnerId__);
}
