package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.security.face.SingletonHashingRemote;
import ai.ilikeplaces.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.Interceptors;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Singleton
@NOTE(note = "MADE FINAL AS CONSTRUCTOR THROWS EXCEPTION TO PREVENT UNINITIALIZED VARIABLES. SUBCLASSING SINGLETON NO SENSE ANYWAY.")
@FIXME(issue = "non injected call should be verified if a user sends false")
@Startup
@Interceptors({MethodTimer.class})
final public class DB implements DBLocal, DBRemote {

    final static private Properties P_ = new Properties();
    static private Context Context_ = null;
    static private boolean OK_ = false;
    final static private ResourceBundle exceptionMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.ExceptionMsgs");
    final static private ResourceBundle config = ResourceBundle.getBundle("ai.ilikeplaces.rbs.Config");
    final static private String ICF = config.getString("oejb.LICF");
    final static Logger logger = LoggerFactory.getLogger(DB.class);

    public static final String NAMING_EXCEPTION = "SORRY! I ENCOUNTERED AN NAMING EXCEPTION WHILE DOING A CONTEXT OPERATION.";

    static {
        try {
            DB.P_.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
            DB.P_.put(Context.PROVIDER_URL, "http://127.0.0.1:8080/openejb/ejb");
            DB.Context_ = new InitialContext(P_);
            DB.OK_ = true;
        } catch (NamingException ex) {
            DB.OK_ = false;
            logger.error(NAMING_EXCEPTION, ex);
        }
    }

    static public void isOK() {
        if (!OK_) {
            throw new IllegalStateException(exceptionMsgs.getString("ai.ilikeplaces.logic.crud.DB.0001"));
        }
    }

    public DB() {
    }

    @Override
    public HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal() {
        isOK();
        HumanCRUDPublicPhotoLocal h = null;
        try {
            h = (HumanCRUDPublicPhotoLocal) Context_.lookup(HumanCRUDPublicPhotoLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPublicPhotoLocal) LogNull.logThrow();
    }

    public static HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal(final boolean nonInjected) {
        HumanCRUDPublicPhotoLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCRUDPublicPhotoLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPublicPhotoLocal) LogNull.logThrow();
    }

    @Override
    public HumanCRUDPrivatePhotoLocal getHumanCRUDPrivatePhotoLocal() {
        isOK();
        HumanCRUDPrivatePhotoLocal h = null;
        try {
            h = (HumanCRUDPrivatePhotoLocal) Context_.lookup(HumanCRUDPrivatePhotoLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPrivatePhotoLocal) LogNull.logThrow();
    }

    public static HumanCRUDPrivatePhotoLocal getHumanCRUDPrivatePhotoLocal(final boolean nonInjected) {
        HumanCRUDPrivatePhotoLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCRUDPrivatePhotoLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPrivatePhotoLocal) LogNull.logThrow();
    }

    @Override
    public HumanCRUDHumanLocal getHumanCRUDHumanLocal() {
        isOK();
        HumanCRUDHumanLocal h = null;
        try {
            h = (HumanCRUDHumanLocal) Context_.lookup(HumanCRUDHumanLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDHumanLocal) LogNull.logThrow();
    }

    public static HumanCRUDHumanLocal getHumanCRUDHumanLocal(final boolean nonInjected) {
        HumanCRUDHumanLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCRUDHumanLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDHumanLocal) LogNull.logThrow();

    }

    @Override
    public HumanCRUDLocationLocal getHumanCRUDLocationLocal() {
        isOK();
        HumanCRUDLocationLocal h = null;
        try {
            h = (HumanCRUDLocationLocal) Context_.lookup(HumanCRUDLocationLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDLocationLocal) LogNull.logThrow();
    }

    public static HumanCRUDLocationLocal getHumanCRUDLocationLocal(final boolean nonInjected) {
        HumanCRUDLocationLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCRUDLocationLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDLocationLocal) LogNull.logThrow();

    }

    @Override
    public HumanCRUDMapLocal getHumanCRUDMapLocal() {
        isOK();
        HumanCRUDMapLocal h = null;
        try {
            h = (HumanCRUDMapLocal) Context_.lookup(HumanCRUDMapLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDMapLocal) LogNull.logThrow();
    }

    public static HumanCRUDMapLocal getHumanCRUDMapLocal(final boolean nonInjected) {
        HumanCRUDMapLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCRUDMapLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDMapLocal) LogNull.logThrow();

    }

    @Override
    public HumanCRUDPrivateEventLocal getHumanCrudPrivateEventLocal() {
        isOK();
        HumanCRUDPrivateEventLocal h = null;
        try {
            h = (HumanCRUDPrivateEventLocal) Context_.lookup(HumanCRUDPrivateEventLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPrivateEventLocal) LogNull.logThrow();
    }

    public static HumanCRUDPrivateEventLocal getHumanCrudPrivateEventLocal(final boolean nonInjected) {
        HumanCRUDPrivateEventLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCrudPrivateEventLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPrivateEventLocal) LogNull.logThrow();
    }


    @Override
    public HumanCRUDPrivateLocationLocal getHumanCrudPrivateLocationLocal() {
        isOK();
        HumanCRUDPrivateLocationLocal h = null;
        try {
            h = (HumanCRUDPrivateLocationLocal) Context_.lookup(HumanCRUDPrivateLocationLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPrivateLocationLocal) LogNull.logThrow();

    }

    public static HumanCRUDPrivateLocationLocal getHumanCrudPrivateLocationLocal(final boolean nonInjected) {
        HumanCRUDPrivateLocationLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCrudPrivateLocationLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPrivateLocationLocal) LogNull.logThrow();

    }


    @Override
    public HumanCRUDWallLocal getHumanCrudWallLocal() {
        isOK();
        HumanCRUDWallLocal h = null;
        try {
            h = (HumanCRUDWallLocal) Context_.lookup(HumanCRUDWallLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDWallLocal) LogNull.logThrow();

    }

    public static HumanCRUDWallLocal getHumanCrudWallLocal(final boolean nonInjected) {
        HumanCRUDWallLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCrudWallLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDWallLocal) LogNull.logThrow();

    }

    @Override
    public SingletonHashingRemote getSingletonHashingFaceLocal() {
        isOK();
        SingletonHashingRemote h = null;
        try {
            h = (SingletonHashingRemote) Context_.lookup(SingletonHashingRemote.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (SingletonHashingRemote) LogNull.logThrow();

    }

    public static SingletonHashingRemote getSingletonHashingFaceLocal(final boolean nonInjected) {
        SingletonHashingRemote h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getSingletonHashingFaceLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (SingletonHashingRemote) LogNull.logThrow();

    }

    @Override
    public HumanUserLocal getHumanUserLocal() {
        isOK();
        HumanUserLocal h = null;
        try {
            h = (HumanUserLocal) Context_.lookup(HumanUserLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanUserLocal) LogNull.logThrow();

    }

    public static HumanUserLocal getHumanUserLocal(final boolean nonInjected) {
        HumanUserLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanUserLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanUserLocal) LogNull.logThrow();

    }


    @Override
    public HumanCRUDHumansUnseenLocal getHumanCRUDHumansUnseenLocal() {
        isOK();
        HumanCRUDHumansUnseenLocal h = null;
        try {
            h = (HumanCRUDHumansUnseenLocal) Context_.lookup(HumanCRUDHumansUnseenLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDHumansUnseenLocal) LogNull.logThrow();

    }

    public static HumanCRUDHumansUnseenLocal getHumanCRUDHumansUnseenLocal(final boolean nonInjected) {
        HumanCRUDHumansUnseenLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCRUDHumansUnseenLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDHumansUnseenLocal) LogNull.logThrow();

    }

    @Override
    public HumanCRUDTribeLocal getHumanCRUDTribeLocal() {
        isOK();
        HumanCRUDTribeLocal h = null;
        try {
            h = (HumanCRUDTribeLocal) Context_.lookup(HumanCRUDTribeLocal.NAME.replace("Local","Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDTribeLocal) LogNull.logThrow();

    }

    public static HumanCRUDTribeLocal getHumanCRUDTribeLocal(final boolean nonInjected) {
        HumanCRUDTribeLocal h = null;
        try {
            h = ((DBRemote) Context_.lookup(DBRemote.NAME)).getHumanCRUDTribeLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDTribeLocal) LogNull.logThrow();

    }

}
