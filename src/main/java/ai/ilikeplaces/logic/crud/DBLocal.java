package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface DBLocal {

    final static public String NAME = DBLocal.class.getSimpleName();

    public HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal();

    public HumanCRUDPrivatePhotoLocal getHumanCRUDPrivatePhotoLocal();

    public HumanCRUDLocationLocal getHumanCRUDLocationLocal();

    public HumanCRUDHumanLocal getHumanCRUDHumanLocal();

    public HumanCRUDMapLocal getHumanCRUDMapLocal();

    public HumanCRUDPrivateEventLocal getHumanCrudPrivateEventLocal();

    public HumanCRUDPrivateLocationLocal getHumanCrudPrivateLocationLocal();

    public HumanCRUDWallLocal getHumanCrudWallLocal();

}
