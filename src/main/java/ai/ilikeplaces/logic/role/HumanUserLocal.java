package ai.ilikeplaces.logic.role;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.DelegatedObservable;

import javax.ejb.Local;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface HumanUserLocal extends HttpSessionBindingListener, Serializable {

    final static public String NAME = HumanUserLocal.class.getSimpleName();

    /**
     * @return
     */
    public String getHumanUserId();

    /**
     * @param loggedOnUserId
     */
    public HumanUserLocal setHumanUserId(String loggedOnUserId);

}
