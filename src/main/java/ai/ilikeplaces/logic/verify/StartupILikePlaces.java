package ai.ilikeplaces.logic.verify;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.logic.crud.HumanCRUDHumanLocal;
import ai.ilikeplaces.logic.verify.util.Verify;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.MethodParams;
import ai.ilikeplaces.util.MethodTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Startup
@Singleton
@NOTE(note = "USE s.o.p FOR LOGGING AS LOGGER MIGHT FAIL TOO!")
public class StartupILikePlaces implements StartupILikePlacesLocal {
//    @EJB
//    private HumanCRUDHumanLocal humanCRUDHumanLocal_;

    public StartupILikePlaces() {
    }

    @PostConstruct
    public void postContruct() {
        System.out.println("");
        System.out.println("********* ********* ********* ********* *********");
        System.out.println("********* START UP CHECK ILIKEPLACES");
        System.out.println("********* The Worlds PlaYGrounD!");
        System.out.println("********* ********* ********* ********* *********");
        System.out.println("********* (logging startup-only via S.O.P)");
        System.out.println("********* ********* ********* ********* *********");
        System.out.println("");
        System.out.println(RBGet.config.getString("bn"));
        System.out.println(RBGet.config.getString("codename"));
        System.out.println("");
        System.out.println("PLEASE NOTE GENERAL SETUP");
        System.out.println("1. Make sure openejb.log in shows no issues.");
        System.out.println("2. Configure datasource in openejb xml file.");
        System.out.println("3. Configure stateful and stateless in openejb xml file.");
        System.out.println("4. Configure cdn and filepath in Config.properties file.");
        System.out.println("5. Verify that classfiles also include .properties files in war.");
        System.out.println("6. Verify that derbyclient.lib is the same version of the derby you installed in the OS.");
        System.out.println("7. Verify that the first entry for location \"The Planet Earth\" was set");
        System.out.println("8. Configure tomcat from localhost to domain name, www.ilikeplaces.com");
/*        System.out.println("Disabling entity lifecycle listener logging");
        EntityLifeCycleListener.POST_ACTIONS.setObj(false);
        EntityLifeCycleListener.PRE_ACTIONS.setObj(false);
        MethodTimer.DO_LOG.setObj(false);
        MethodParams.DO_LOG.setObj(false);*/
        System.out.println("");
        logger.debug(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0004"), System.getProperty("java.class.path"));
        System.out.println("STARTING TO VERIFY CLASSES");
        System.out.println("");

//        try {
//            try {
//                System.out.println("VERIFICATION OF " + humanCRUDHumanLocal_.getClass().getSimpleName() + ":" +
//                        humanCRUDHumanLocal_.getClass().getMethod("verify", new Class[]{}).invoke(humanCRUDHumanLocal_, new Object[]{}));
//            } catch (IllegalAccessException e) {
//                logger.error("{}", e);
//            } catch (InvocationTargetException e) {
//                logger.error("{}", e);
//            } catch (NoSuchMethodException e) {
//                logger.error("{}", e);
//            }
//        } catch (final Exception e) {
//            e.printStackTrace(System.out);
//        }
//
//        @TODO(task = "LOAD FROM PROPERTIES FILE WHICH MAKES IT POSSIBLE TO DEPLOY ON ERRORS" +
//                "PROPERTIES FILE SHOULD BE EDITABLE, HENCE IN AN ACCESSIBLE PATH")
//        String[] verifyClasses = {RBGet.class.getName()};
//
//        for (final String verifyClass : verifyClasses) {
//            System.out.println("");
//            try {
//                try {
//                    try {
//                        final Method m = Class.forName(verifyClass).getMethod("verify", new Class[]{});
//                        if (Verify.class.isInstance(verifyClass)) {
//                            try {
//                                System.out.println("VERIFICATION OF " + verifyClass + ":" + m.invoke(Class.forName(verifyClass).newInstance(), new Object[]{}));
//                            } catch (final IllegalAccessException e) {
//                                e.printStackTrace(System.out);
//                            } catch (final InvocationTargetException e) {
//                                e.printStackTrace(System.out);
//                            } catch (final InstantiationException e) {
//                                e.printStackTrace(System.out);
//                            }
//                        } else {
//Assumed to be a static method, which cannot implement the interface
//
//                            try {
//                                System.out.println("VERIFICATION OF " + verifyClass + ":" + m.invoke(null, new Object[]{}));
//                            } catch (final IllegalAccessException e) {
//                                e.printStackTrace(System.out);
//                            } catch (final InvocationTargetException e) {
//                                e.printStackTrace(System.out);
//                            }
//                        }
//                    } catch (final NoSuchMethodException e) {
//                        e.printStackTrace(System.out);
//                    }
//                } catch (final ClassNotFoundException e) {
//                    e.printStackTrace(System.out);
//                }
//            } catch (final Exception e) {
//                e.printStackTrace(System.out);
//            }
//        }

        System.out.println("Check title, Check ads, Check location Search Auto Suggest or Suggest on feed.");
        System.out.println("");

        System.out.println(RBGet.config.getString("bn"));
        System.out.println(RBGet.config.getString("codename"));

        System.out.println("********* ********* ********* ********* *********");
        System.out.println("********* START UP CHECK DONE ILIKEPLACES");
        System.out.println("********* ********* ********* ********* *********");
        System.out.println("");
    }

    final static Logger logger = LoggerFactory.getLogger(StartupILikePlaces.class);
}
