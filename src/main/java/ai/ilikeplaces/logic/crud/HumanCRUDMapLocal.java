package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.util.Return;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 1, 2010
 * Time: 11:03:15 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface HumanCRUDMapLocal {
    final static public String NAME = HumanCRUDMapLocal.class.getSimpleName();

    public Return createEntry(final String label, final String entry);
}
