package ai.ilikeplaces.entities.etc;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Human;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 12, 2010
 * Time: 9:08:06 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface HumanPkJoinFace extends HumanIdFace, HumanFace {

    @Override
    public Human getHuman();

    public void setHuman(final Human human);
}
