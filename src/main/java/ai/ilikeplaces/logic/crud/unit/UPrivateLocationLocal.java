package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.NoPrivilegesException;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 12, 2010
 * Time: 10:31:21 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UPrivateLocationLocal {

    public PrivateLocation doUPrivateLocationData(final String humanId__, final String privateLocationId__, final String privateLocationName__, final String privateLocationInfo__);

    public PrivateLocation doUPrivateLocationAddOwner(final String humanId__, final long privateLocationId__, final HumansFriend privateLocationOwner__) throws NoPrivilegesException;

    public PrivateLocation doUPrivateLocationRemoveOwner(final String humanId__, final long privateLocationId__, final HumansFriend privateLocationOwner__) throws NoPrivilegesException;

    public PrivateLocation doUPrivateLocationAddViewer(final String humanId__, final long privateLocationId__, final HumansFriend privateLocationVisitor__) throws NoPrivilegesException;

    public PrivateLocation doUPrivateLocationRemoveViewer(final String humanId__, final long privateLocationId__, final HumansFriend privateLocationVisitor__) throws DBDishonourCheckedException;

}