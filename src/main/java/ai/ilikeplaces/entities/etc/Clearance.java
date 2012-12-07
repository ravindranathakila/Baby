package ai.ilikeplaces.entities.etc;

import ai.doc.License;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 21, 2010
 * Time: 8:35:05 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface Clearance {

    public Long getClearance();

    public void setClearance(final Long clearance);
}
