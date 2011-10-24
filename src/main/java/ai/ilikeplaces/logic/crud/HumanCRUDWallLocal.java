package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.verify.util.Verify;
import ai.ilikeplaces.util.Obj;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.util.jpa.RefreshSpec;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDWallLocal extends Verify, GeneralCRUDWall {

    final static public String NAME = HumanCRUDWallLocal.class.getSimpleName();

    public Return<List<Msg>> readWallLastEntries(final HumanId whosWall, final Obj wallOwnerId__, final Integer numberOfEntriesToFetch, final RefreshSpec refreshSpec__);

    public Return<Long> readWallId(final HumanId whosWall, final Obj wallOwnerId__);
}