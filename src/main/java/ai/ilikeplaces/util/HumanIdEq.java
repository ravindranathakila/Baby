package ai.ilikeplaces.util;

import ai.ilikeplaces.entities.etc.HumanEquals;
import ai.scribble.License;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 22, 2010
 * Time: 2:16:43 AM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class HumanIdEq extends HumanEquals {

    final String humanId;

    public HumanIdEq(final String humanId) {
        this.humanId = humanId;
    }

    @Override
    public String getHumanId() {
        return humanId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (getClass() == o.getClass()) {
            final HumanIdEq that = (HumanIdEq) o;
            return (!(this.getHumanId() == null || that.getHumanId() == null)) && this.getHumanId().equals(that.getHumanId());
        } else {
            return matchHumanId(o);
        }
    }
}
