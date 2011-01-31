package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivatePhoto;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */


@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CPrivatePhotoLocal {

    @Deprecated
    public PrivatePhoto doCPrivatePhotoLocal(final String humanId,
                                             final PrivatePhoto privatePhoto);

    public PrivatePhoto doCPrivatePhotoLocal(final String humanId, final String privatePhotoURLPath);

    public PrivatePhoto doCPrivatePhotoLocal(final String humanId,
                                             final String privatePhotoName,
                                             final String fileName,
                                             final String privatePhotoDescription,
                                             final String privatePhotoURLPath
    );
}