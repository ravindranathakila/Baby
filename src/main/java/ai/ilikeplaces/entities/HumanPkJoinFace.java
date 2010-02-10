package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 12, 2010
 * Time: 9:08:06 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface HumanPkJoinFace extends HumanIdFace {

    public Human getHuman();

    public void setHuman(final Human human);
}
