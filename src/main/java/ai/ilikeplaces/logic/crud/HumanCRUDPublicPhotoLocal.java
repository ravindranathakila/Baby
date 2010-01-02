package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDPublicPhotoLocal {

    final static public String NAME = HumanCRUDPublicPhotoLocal.class.getSimpleName();

    public boolean doHumanCPublicPhoto(final String humanId, final long locationId, final String fileName, final String publicPhotoName, final String publicPhotoDescription, final String publicPhotoURLPath, final int retries);

    public Return<List<PublicPhoto>> doHumanRPublicPhoto(final String humanId);

    public boolean doHumanUPublicPhotoDescription(final String humanId, final long publicPhotoId, final String publicPhotoDescription);

    public boolean doHumanDPublicPhoto(final String humanId, final long publicPhotoId);
}
