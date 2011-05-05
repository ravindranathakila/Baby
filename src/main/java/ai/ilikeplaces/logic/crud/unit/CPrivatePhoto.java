package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansPrivatePhoto;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CPrivatePhoto implements CPrivatePhotoLocal {

    @EJB
    private CrudServiceLocal<PrivatePhoto> privatePhotoCrudServiceLocal_;
    @EJB
    private CrudServiceLocal<HumansPrivatePhoto> humansPrivatePhotoCrudServiceLocal_;

    public CPrivatePhoto() {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PrivatePhoto doCPrivatePhotoLocal(final String humanId, final PrivatePhoto privatePhoto) {

        final HumansPrivatePhoto humansPrivatePhoto =
                humansPrivatePhotoCrudServiceLocal_.find(HumansPrivatePhoto.class, humanId);

        privatePhoto.setHumansPrivatePhoto(humansPrivatePhoto);

        final PrivatePhoto managedPrivatePhoto = privatePhotoCrudServiceLocal_.create(privatePhoto);

        humansPrivatePhoto.getPrivatePhotos().add(managedPrivatePhoto);

        return managedPrivatePhoto;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PrivatePhoto doCPrivatePhotoLocal(final String humanId,
                                             String privatePhotoName,
                                             String fileName,
                                             String privatePhotoDescription,
                                             String privatePhotoURLPath
    ) {
        final PrivatePhoto privatePhoto = new PrivatePhoto().
                setPrivatePhotoNameR(privatePhotoName)
                .setPrivatePhotoFilePathR(fileName)
                .setPrivatePhotoDescriptionR(privatePhotoDescription)
                .setPrivatePhotoURLPathR(privatePhotoURLPath)
                .setPrivatePhotoWallR(new Wall().setWallRContent(""));

        final HumansPrivatePhoto humansPrivatePhoto =
                humansPrivatePhotoCrudServiceLocal_.find(HumansPrivatePhoto.class, humanId);

        privatePhoto.setHumansPrivatePhoto(humansPrivatePhoto);

        final PrivatePhoto managedPrivatePhoto = privatePhotoCrudServiceLocal_.create(privatePhoto);

        humansPrivatePhoto.getPrivatePhotos().add(managedPrivatePhoto);

        return managedPrivatePhoto;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PrivatePhoto doCPrivatePhotoLocal(final String humanId, final String privatePhotoURLPath) {
        final PrivatePhoto privatePhoto = new PrivatePhoto().
                setPrivatePhotoURLPathR(privatePhotoURLPath)
                .setPrivatePhotoWallR(new Wall().setWallRContent(""));

        final HumansPrivatePhoto humansPrivatePhoto =
                humansPrivatePhotoCrudServiceLocal_.find(HumansPrivatePhoto.class, humanId);

        privatePhoto.setHumansPrivatePhoto(humansPrivatePhoto);

        final PrivatePhoto managedPrivatePhoto = privatePhotoCrudServiceLocal_.create(privatePhoto);

        humansPrivatePhoto.getPrivatePhotos().add(managedPrivatePhoto);

        return managedPrivatePhoto;
    }

    final static Logger logger = LoggerFactory.getLogger(CPrivatePhoto.class);
}