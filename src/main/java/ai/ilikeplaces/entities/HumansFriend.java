package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;

import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Feb 10, 2010
 * Time: 6:39:28 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface HumansFriend extends HumanIdFace {

    public String getHumanId();

    public Human getHuman();

    @Transient
    public String getDisplayName();

    @Transient
    public boolean isFriend(final String friendsHumanId);

    @Transient
    public boolean notFriend(final String friendsHumanId);

}
