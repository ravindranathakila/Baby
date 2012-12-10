package ai.ilikeplaces.logic.crud;

import ai.doc.License;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.util.Return;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Remote
public interface HumanCRUDPublicPhotoLocal {

    final static public String NAME = HumanCRUDPublicPhotoLocal.class.getSimpleName();

    public Return<PublicPhoto> cPublicPhoto(final String humanId, final long locationId, final String fileName, final String publicPhotoName, final String publicPhotoDescription, final String publicPhotoURLPath, final int retries);

    public Return<List<PublicPhoto>> rPublicPhoto(final String humanId);

    public boolean uPublicPhotoDescription(final String humanId, final long publicPhotoId, final String publicPhotoDescription);

    public boolean dPublicPhoto(final String humanId, final long publicPhotoId);
}
