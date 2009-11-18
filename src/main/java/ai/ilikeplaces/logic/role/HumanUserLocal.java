package ai.ilikeplaces.logic.role;

import javax.ejb.Local;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Observer;


/**
 * @author Ravindranath Akila
 */
@Local
public interface HumanUserLocal extends HttpSessionBindingListener {

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
