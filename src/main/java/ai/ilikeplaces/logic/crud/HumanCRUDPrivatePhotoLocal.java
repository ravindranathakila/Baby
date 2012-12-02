package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.entities.RefreshSpec;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.Obj;
import ai.ilikeplaces.util.Return;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Remote
public interface HumanCRUDPrivatePhotoLocal extends GeneralCRUDWall {

    final static public String NAME = HumanCRUDPrivatePhotoLocal.class.getSimpleName();

    public Return<PrivatePhoto> cPrivatePhoto(final String humanId, final String fileName, final String privatePhotoName, final String privatePhotoDescription, final String privatePhotoURLPath);

    public Return<List<PrivatePhoto>> rPrivatePhotos(final String humanId);

    public Return<PrivatePhoto> rPrivatePhoto(final HumanId humanId, final Obj<Long> privatePhotoId, RefreshSpec refreshSpec);

    public Return<Boolean> dPrivatePhoto(final HumanId humanId, final long publicPhotoId);
}
