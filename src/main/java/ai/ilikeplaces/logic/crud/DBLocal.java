package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import javax.ejb.Local;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface DBLocal {

    final static public String NAME = DBLocal.class.getSimpleName();

    public HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal();

}
