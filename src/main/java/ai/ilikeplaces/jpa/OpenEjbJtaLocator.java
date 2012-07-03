package ai.ilikeplaces.jpa;

import ai.ilikeplaces.doc.DOCUMENTATION;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.util.LogNull;
import ai.ilikeplaces.util.Loggers;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.jta.TransactionManagerLocator;

import javax.transaction.TransactionManager;

import org.apache.openejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/2/12
 * Time: 9:42 PM
 */
@Deprecated
@DOCUMENTATION(
        NOTE = @NOTE("At the time of this writing, the Datanuclues calls this method but fails to detect any presence of the transaction. " +
                "Added this class in for others to find and pursue further. " +
                "See http://www.datanucleus.org/servlet/forum/viewthread_thread,7191_lastpage,yes#35839" +
                "")
)
public class OpenEjbJtaLocator implements TransactionManagerLocator {

    public static final boolean DEBUG_ENABLED = Loggers.DEBUG.isDebugEnabled();

    @Override
    public TransactionManager getTransactionManager(ClassLoaderResolver classLoaderResolver) {
        if (DEBUG_ENABLED) {
            Loggers.debug("Attempting to return OpenEJB javax.transaction.TransactionManager Manager to Datanuclues.");
        }

        final TransactionManager transactionManager = OpenEJB.getTransactionManager();

        if (DEBUG_ENABLED) {
            LogNull.logThrow(transactionManager, "OpenEJB javax.transaction.TransactionManager IS NULL.");
            Loggers.debug("Returning javax.transaction.TransactionManager to Datanuclues. The javax.transaction.TransactionManager is NOT null.");
        }


        return transactionManager;
    }
}
