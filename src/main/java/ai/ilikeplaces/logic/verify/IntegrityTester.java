package ai.ilikeplaces.logic.verify;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.logic.crud.HumanCRUDHumanLocal;
import ai.ilikeplaces.logic.verify.util.Verify;
import ai.ilikeplaces.rbs.RBGet;
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
public class IntegrityTester implements StartupILikePlacesLocal {
    @EJB
    private HumanCRUDHumanLocal humanCRUDHumanLocal_;

    public IntegrityTester() {
    }

    @PostConstruct
    public void postContruct() {
    }

    final static Logger logger = LoggerFactory.getLogger(IntegrityTester.class);
}