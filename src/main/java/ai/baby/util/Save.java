package ai.baby.util;

import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.entities.etc.HumansFriend;
import ai.scribble.License;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Feb 11, 2010
 * Time: 1:15:02 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface Save<RETURN_TYPE> {
    public RETURN_TYPE save(final HumanId humansId, final HumansFriend humansFriend);
}
