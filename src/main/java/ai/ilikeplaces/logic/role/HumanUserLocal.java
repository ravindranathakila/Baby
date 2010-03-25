package ai.ilikeplaces.logic.role;

import ai.ilikeplaces.doc.License;

import javax.ejb.Local;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;
import java.util.Observer;


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
    public void setHumanUserId(String loggedOnUserId);

    /**
     * @param o
     */
    public void addObserver(Observer o);

    /**
     * @param o
     */
    public void deleteObserver(Observer o);
}
