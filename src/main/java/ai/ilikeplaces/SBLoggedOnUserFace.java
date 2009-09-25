package ai.ilikeplaces;

import javax.ejb.Local;

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
}
