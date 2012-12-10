package ai.ilikeplaces.logic.verify;

import ai.doc.License;
import ai.doc._note;
import ai.ilikeplaces.entities.etc.EntityLifeCycleListener;
import ai.ilikeplaces.entities.etc.FriendUtil;
import ai.ilikeplaces.exception.DBException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MethodParams;
import ai.ilikeplaces.util.MethodTimer;
import ai.ilikeplaces.util.Return;
import ai.ilikeplaces.ygp.impl.ClientFactory;
import ai.util.HumanId;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Startup
@Singleton
@_note(note = "USE s.o.p FOR LOGGING AS LOGGER MIGHT FAIL TOO!")
public class StartupILikePlaces implements StartupILikePlacesLocal {
//    @EJB
//    private HumanCRUDHumanLocal humanCRUDHumanLocal_;

    public ClientFactory ygdClientFactory;

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
        System.out.println(RBGet.globalConfig.getString("bn"));
        System.out.println(RBGet.globalConfig.getString("codename"));
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

        EntityLifeCycleListener.POST_ACTIONS = false;
        EntityLifeCycleListener.PRE_ACTIONS = false;

        MethodTimer.DO_LOG.setObj(Loggers.DEBUG.isDebugEnabled());
        MethodParams.DO_LOG.setObj(Loggers.DEBUG.isDebugEnabled());


/*        System.out.println("Disabling entity lifecycle listener logging");
        EntityLifeCycleListener.POST_ACTIONS.setObj(false);
        EntityLifeCycleListener.PRE_ACTIONS.setObj(false);
        MethodTimer.DO_LOG.setObj(false);
        MethodParams.DO_LOG.setObj(false);*/
        System.out.println("");
//        logger.debug(RBGet.logMsgs.getString("ai.ilikeplaces.servlets.Controller.0004"), System.getProperty("java.class.path"));
//        System.out.println("STARTING TO VERIFY CLASSES");
//        System.out.println("");

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

        System.out.println("");
        System.out.println("SETTING PERSISTENCE LOGGING");

        EntityLifeCycleListener.PRE_ACTIONS = Loggers.DEBUG.isDebugEnabled();
        EntityLifeCycleListener.POST_ACTIONS = Loggers.DEBUG.isDebugEnabled();

        MethodTimer.DO_LOG.setObjAsValid(Boolean.getBoolean(RBGet.globalConfig.getString("PROFILE_METHODS")));

        System.out.println("DONE SETTING PERSISTENCE LOGGING");

        System.out.println(RBGet.globalConfig.getString("bn"));
        System.out.println(RBGet.globalConfig.getString("codename"));

        System.out.println("********* ********* ********* ********* *********");
        System.out.println("********* START UP CHECK DONE ILIKEPLACES");
        System.out.println("********* ********* ********* ********* *********");
        System.out.println("");

        System.out.println("Registering Entity Hooks");

        FriendUtil.APPROACH_FOR_CHECKING_HUMANS_FRIEND = new FriendUtil.CheckHumanApproach() {
            @Override
            public Return<Boolean> check(final HumanId me, final HumanId other) {
                final Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doDirtyIsHumansNetPeople(me, other);
                if (r.returnStatus() != 0) {
                    throw new DBException(r.returnError());
                }
                return r;
            }
        };
        System.out.println("Done registering Entity Hooks");

    }
}
