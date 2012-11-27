package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;

import javax.persistence.Transient;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Feb 10, 2010
 * Time: 6:39:28 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface HumansFriend extends HumanIdFace {

    public String getHumanId();

    @Transient
    public String getDisplayName();

    @Transient
    public boolean ifFriend(final String friendsHumanId);

    @Transient
    public boolean notFriend(final String friendsHumanId);

}
