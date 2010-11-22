package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanCRUDPrivatePhotoLocal {

    final static public String NAME = HumanCRUDPrivatePhotoLocal.class.getSimpleName();

    public Return<PrivatePhoto> cPrivatePhoto(final String humanId, final String fileName, final String privatePhotoName, final String privatePhotoDescription, final String privatePhotoURLPath);

    public Return<PrivatePhoto> addPrivatePhotoAlbum(final long publicPhotoId, final long albumId);

    public Return<List<PrivatePhoto>> rPrivatePhoto(final String humanId);

    public Return<Boolean> dPrivatePhoto(final HumanId humanId, final long publicPhotoId);
}