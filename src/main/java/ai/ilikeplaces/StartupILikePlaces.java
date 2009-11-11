package ai.ilikeplaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
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
        System.out.println("PLEASE NOTE GENERAL SETUP");
        System.out.println("1. Make sure openejb.log in shows no issues.");
        System.out.println("1. Configure datasource in openejb xml file.");
        System.out.println("2. Configure stateful and stateless in openejb xml file.");
        System.out.println("2. Configure cdn and filepath in Config.properties file.");
        System.out.println("3. Verify that classfiles also include .properties files in war.");


        System.out.println("********* ********* ********* ********* *********");
        System.out.println("********* START UP CHECK DONE ILIKEPLACES");
        System.out.println("********* ********* ********* ********* *********");

    }
    final static Logger logger = LoggerFactory.getLogger(StartupILikePlaces.class);
}
