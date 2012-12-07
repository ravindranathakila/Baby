package ai.ilikeplaces.logic.crud;

import ai.doc.License;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.security.face.SingletonHashingRemote;

import javax.ejb.Remote;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Remote
public interface DBRemote {

    final static public String NAME = DBRemote.class.getSimpleName();

    public HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal();

    public HumanCRUDPrivatePhotoLocal getHumanCRUDPrivatePhotoLocal();

    public HumanCRUDLocationLocal getHumanCRUDLocationLocal();

    public HumanCRUDHumanLocal getHumanCRUDHumanLocal();

    public HumanCRUDMapLocal getHumanCRUDMapLocal();

    public HumanCRUDPrivateEventLocal getHumanCrudPrivateEventLocal();

    public HumanCRUDPrivateLocationLocal getHumanCrudPrivateLocationLocal();

    public HumanCRUDWallLocal getHumanCrudWallLocal();

    public SingletonHashingRemote getSingletonHashingFaceLocal();

    public HumanUserLocal getHumanUserLocal();

    public HumanCRUDHumansUnseenLocal getHumanCRUDHumansUnseenLocal();

    public HumanCRUDTribeLocal getHumanCRUDTribeLocal();

}
