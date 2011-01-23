package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.verify.util.Verify;
import ai.ilikeplaces.util.Obj;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDWallLocal extends Verify, GeneralCRUDWall {

    final static public String NAME = HumanCRUDWallLocal.class.getSimpleName();
}