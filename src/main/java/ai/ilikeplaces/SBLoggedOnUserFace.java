package ai.ilikeplaces;

import javax.ejb.Local;
import java.util.Observer;
import javax.servlet.http.HttpSessionBindingEvent;


/**
 *
 * @author Ravindranath Akila
 */
@Local
public interface SBLoggedOnUserFace extends javax.servlet.http.HttpSessionBindingListener {

    /**
     *
     * @return
     */
    public String getLoggedOnUserId();

    /**
     *
     * @param loggedOnUserId
     */
    public void setLoggedOnUserId(String loggedOnUserId);

    /**
     *
     * @param o
     */
    public void addObserver(Observer o);

    /**
     *
     * @param o
     */
    public void deleteObserver(Observer o);
}
