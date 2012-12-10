package ai.ilikeplaces.logic.crud;

import ai.doc.License;
import ai.reaver.Return;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 1, 2010
 * Time: 11:03:15 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface HumanCRUDMapLocal {
    final static public String NAME = HumanCRUDMapLocal.class.getSimpleName();

    public Return createEntry(final String label, final String entry);
}
