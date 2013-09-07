package ai.baby.logic.crud;

import ai.baby.logic.role.HumanUserLocal;
import ai.baby.security.face.SingletonHashingRemote;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface DBLocal {

    final static public String NAME = DBLocal.class.getSimpleName();

    public HumanCRUDHumanLocal getHumanCRUDHumanLocal();

    public HumanCRUDWallLocal getHumanCrudWallLocal();

    public SingletonHashingRemote getSingletonHashingFaceLocal();

    public HumanUserLocal getHumanUserLocal();

    public HumanCRUDTribeLocal getHumanCRUDTribeLocal();

}
