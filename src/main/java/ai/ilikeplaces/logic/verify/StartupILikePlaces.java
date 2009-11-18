package ai.ilikeplaces.logic.verify;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.logic.verify.util.Verify;
import ai.ilikeplaces.rbs.RBGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
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

    public StartupILikePlaces() {
    }

    @PostConstruct
    public void postContruct() {

        System.out.println("********* ********* ********* ********* *********");
        System.out.println("********* START UP CHECK ILIKEPLACES");
        System.out.println("********* ********* ********* ********* *********");
        System.out.println("");
        System.out.println("PLACEHOLDER ID:11");
        System.out.println("PLEASE NOTE GENERAL SETUP");
        System.out.println("1. Make sure openejb.log in shows no issues.");
        System.out.println("1. Configure datasource in openejb xml file.");
        System.out.println("2. Configure stateful and stateless in openejb xml file.");
        System.out.println("2. Configure cdn and filepath in Config.properties file.");
        System.out.println("3. Verify that classfiles also include .properties files in war.");
        System.out.println("");
        System.out.println("STARTING TO VERIFY CLASSES");
        System.out.println("");

        @TODO(task = "LOAD FROM PROPERTIES FILE WHICH MAKES IT POSSIBLE TO DEPLOY ON ERRORS" +
                "PROPERTIES FILE SHOULD BE EDITABLE, HENCE IN AN ACCESSIBLE PATH")
        String[] verifyClasses = {RBGet.class.getName()};

        for (final String verifyClass : verifyClasses) {
            System.out.println("");
            try {
                try {
                    final Method m = Class.forName(verifyClass).getMethod("verify", new Class[]{});
                    if (Verify.class.isInstance(verifyClass)) {
                        try {
                            System.out.println(m.invoke(Class.forName(verifyClass).newInstance(), new Object[]{}));
                        } catch (final IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (final InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        } catch (final InstantiationException e) {
                            e.printStackTrace(System.out);
                        }
                    } else {
                        try {
                            System.out.println(m.invoke(null, new Object[]{}));
                        } catch (final IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (final InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace(System.out);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace(System.out);
            }
        }


        System.out.println("********* ********* ********* ********* *********");
        System.out.println("********* START UP CHECK DONE ILIKEPLACES");
        System.out.println("********* ********* ********* ********* *********");

    }

    final static Logger logger = LoggerFactory.getLogger(StartupILikePlaces.class);
}
