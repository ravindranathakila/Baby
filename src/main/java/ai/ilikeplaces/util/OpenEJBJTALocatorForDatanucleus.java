//package ai.ilikeplaces.util;
//
//import org.apache.openejb.OpenEJB;
//import org.datanucleus.ClassLoaderResolver;
//import org.datanucleus.jta.TransactionManagerLocator;
//
//import javax.transaction.TransactionManager;
//
///**
// * Created by IntelliJ IDEA.
// * User: ravindranathakila
// * Date: 9/29/12
// * Time: 9:27 PM
// */
//public class OpenEJBJTALocatorForDatanucleus implements TransactionManagerLocator {
//
//    public static final String FILL = "###################################################################";
//    public static final String OPENEJB_TRANSACTION_MANAGER_TAKENUP = "OpenEJBJTALocatorForDatanucleus is being called by Datanucleus to obtain OpenEJB's transaction manager";
//
//    @Override
//    public TransactionManager getTransactionManager(final ClassLoaderResolver classLoaderResolver) {
//        Loggers.DEBUG.debug(FILL);
//        Loggers.DEBUG.debug(OPENEJB_TRANSACTION_MANAGER_TAKENUP);
//        Loggers.DEBUG.debug(FILL);
//        return OpenEJB.getTransactionManager();
//    }
//}
