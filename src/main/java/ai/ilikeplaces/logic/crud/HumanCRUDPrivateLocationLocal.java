package ai.ilikeplaces.logic.crud;

import ai.doc.License;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.entities.etc.HumansFriend;
import ai.ilikeplaces.logic.validators.unit.VDouble;
import ai.ilikeplaces.util.RefObj;
import ai.ilikeplaces.util.Return;

import javax.ejb.Remote;


/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 2:41:13 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Remote
public interface HumanCRUDPrivateLocationLocal {
    final static public String NAME = HumanCRUDPrivateLocationLocal.class.getSimpleName();

//    @Deprecated
//    public Return<PrivateLocation> cPrivateLocation(final RefObj<String> humanId, final String privateLocationName, final String privateLocationInfo);

    public Return<PrivateLocation> cPrivateLocation(final RefObj<String> humanId, final RefObj<String> privateLocationName, final RefObj<String> privateLocationInfo, final VDouble latitude, final VDouble longitude);

    public Return<PrivateLocation> dirtyRPrivateLocationAsAny(final RefObj<String> humanId, final long privateLocationId);

    public Return<PrivateLocation> dirtyRPrivateLocationAsOwner(final RefObj<String> humanId, final long privateLocationId);

    public Return<PrivateLocation> dirtyRPrivateLocationAsViewer(final RefObj<String> humanId, final long privateLocationId);

    public Return<Boolean> dirtyRPrivateLocationIsOwner(final RefObj<String> humanId, final Long privateLocationId);

    public Return<Boolean> dirtyRPrivateLocationIsViewer(final RefObj<String> humanId, final Long privateLocationId);

    public Return<PrivateLocation> uPrivateLocationAddOwner(final RefObj<String> humanId__, final long privateLocationId__, final HumansFriend owner);

    public Return<PrivateLocation> uPrivateLocationAddVisitor(final RefObj<String> humanId__, final long privateLocationId__, final HumansFriend owner);

    public Return<PrivateLocation> uPrivateLocationRemoveOwner(final RefObj<String> humanId__, final long privateLocationId__, final HumansFriend owner);

    public Return<PrivateLocation> uPrivateLocationRemoveVisitor(final RefObj<String> humanId__, final long privateLocationId__, final HumansFriend owner);

    public Return<Boolean> dPrivateLocation(final RefObj<String> humanId, final long privateLocationId);
}
