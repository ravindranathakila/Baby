package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.security.face.SingletonHashingRemote;
import ai.ilikeplaces.util.LogNull;
import ai.ilikeplaces.util.MethodTimer;
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
final public class DB implements DBLocal {

    final static private Properties REMOTE_PROPERTIES = new Properties();
    static private Context REMOTE_CONTEXT = null;

    final static private Properties LOCAL_PROPERTIES = new Properties();
    static private Context LOCAL_CONTEXT = null;


    static private boolean OK_ = false;
    final static private ResourceBundle exceptionMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.ExceptionMsgs");
    final static Logger logger = LoggerFactory.getLogger(DB.class);

    public static final String NAMING_EXCEPTION = "SORRY! I ENCOUNTERED AN NAMING EXCEPTION WHILE DOING A CONTEXT OPERATION.";

    static {
        try {
            DB.REMOTE_PROPERTIES.put(Context.INITIAL_CONTEXT_FACTORY, RBGet.globalConfig.getString("oejb.RICF"));
            DB.REMOTE_PROPERTIES.put(Context.PROVIDER_URL, RBGet.globalConfig.getString("RICF_LOCATION"));
            DB.REMOTE_CONTEXT = new InitialContext(REMOTE_PROPERTIES);
            DB.OK_ = true;
        } catch (NamingException ex) {
            DB.OK_ = false;
            logger.error(NAMING_EXCEPTION, ex);
        }
    }

    static {
        try {
            DB.LOCAL_PROPERTIES.put(Context.INITIAL_CONTEXT_FACTORY, RBGet.globalConfig.getString("oejb.LICF"));
            DB.LOCAL_CONTEXT = new InitialContext(LOCAL_PROPERTIES);
            DB.OK_ = true;
        } catch (NamingException ex) {
            DB.OK_ = false;
            logger.error(NAMING_EXCEPTION, ex);
        }
    }


    static public void isOK() {
        if (!OK_) {
            throw new IllegalStateException(exceptionMsgs.getString("ai.ilikeplaces.static_initialization_failure"));
        }
    }

    public DB() {
    }

    @Override
    public HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal() {
        isOK();
        HumanCRUDPublicPhotoLocal h = null;
        try {
            h = (HumanCRUDPublicPhotoLocal) REMOTE_CONTEXT.lookup(HumanCRUDPublicPhotoLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPublicPhotoLocal) LogNull.logThrow();
    }

    public static HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal(final boolean nonInjected) {
        HumanCRUDPublicPhotoLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCRUDPublicPhotoLocal();
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
            h = (HumanCRUDPrivatePhotoLocal) REMOTE_CONTEXT.lookup(HumanCRUDPrivatePhotoLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPrivatePhotoLocal) LogNull.logThrow();
    }

    public static HumanCRUDPrivatePhotoLocal getHumanCRUDPrivatePhotoLocal(final boolean nonInjected) {
        HumanCRUDPrivatePhotoLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCRUDPrivatePhotoLocal();
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
            h = (HumanCRUDHumanLocal) REMOTE_CONTEXT.lookup(HumanCRUDHumanLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDHumanLocal) LogNull.logThrow();
    }

    public static HumanCRUDHumanLocal getHumanCRUDHumanLocal(final boolean nonInjected) {
        HumanCRUDHumanLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCRUDHumanLocal();
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
            h = (HumanCRUDLocationLocal) REMOTE_CONTEXT.lookup(HumanCRUDLocationLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDLocationLocal) LogNull.logThrow();
    }

    public static HumanCRUDLocationLocal getHumanCRUDLocationLocal(final boolean nonInjected) {
        HumanCRUDLocationLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCRUDLocationLocal();
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
            h = (HumanCRUDMapLocal) REMOTE_CONTEXT.lookup(HumanCRUDMapLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDMapLocal) LogNull.logThrow();
    }

    public static HumanCRUDMapLocal getHumanCRUDMapLocal(final boolean nonInjected) {
        HumanCRUDMapLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCRUDMapLocal();
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
            h = (HumanCRUDPrivateEventLocal) REMOTE_CONTEXT.lookup(HumanCRUDPrivateEventLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPrivateEventLocal) LogNull.logThrow();
    }

    public static HumanCRUDPrivateEventLocal getHumanCrudPrivateEventLocal(final boolean nonInjected) {
        HumanCRUDPrivateEventLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCrudPrivateEventLocal();
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
            h = (HumanCRUDPrivateLocationLocal) REMOTE_CONTEXT.lookup(HumanCRUDPrivateLocationLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDPrivateLocationLocal) LogNull.logThrow();

    }

    public static HumanCRUDPrivateLocationLocal getHumanCrudPrivateLocationLocal(final boolean nonInjected) {
        HumanCRUDPrivateLocationLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCrudPrivateLocationLocal();
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
            h = (HumanCRUDWallLocal) REMOTE_CONTEXT.lookup(HumanCRUDWallLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDWallLocal) LogNull.logThrow();

    }

    public static HumanCRUDWallLocal getHumanCrudWallLocal(final boolean nonInjected) {
        HumanCRUDWallLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCrudWallLocal();
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
            h = (SingletonHashingRemote) REMOTE_CONTEXT.lookup(SingletonHashingRemote.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (SingletonHashingRemote) LogNull.logThrow();

    }

    public static SingletonHashingRemote getSingletonHashingFaceLocal(final boolean nonInjected) {
        SingletonHashingRemote h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getSingletonHashingFaceLocal();
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
            h = (HumanUserLocal) REMOTE_CONTEXT.lookup(HumanUserLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanUserLocal) LogNull.logThrow();

    }

    public static HumanUserLocal getHumanUserLocal(final boolean nonInjected) {
        HumanUserLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanUserLocal();
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
            h = (HumanCRUDHumansUnseenLocal) REMOTE_CONTEXT.lookup(HumanCRUDHumansUnseenLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDHumansUnseenLocal) LogNull.logThrow();

    }

    public static HumanCRUDHumansUnseenLocal getHumanCRUDHumansUnseenLocal(final boolean nonInjected) {
        HumanCRUDHumansUnseenLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCRUDHumansUnseenLocal();
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
            h = (HumanCRUDTribeLocal) REMOTE_CONTEXT.lookup(HumanCRUDTribeLocal.NAME.replace("Local", "Remote"));
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDTribeLocal) LogNull.logThrow();

    }

    public static HumanCRUDTribeLocal getHumanCRUDTribeLocal(final boolean nonInjected) {
        HumanCRUDTribeLocal h = null;
        try {
            h = ((DBLocal) LOCAL_CONTEXT.lookup(DBLocal.NAME)).getHumanCRUDTribeLocal();
        } catch (NamingException ex) {
            logger.error(NAMING_EXCEPTION, ex);
        }
        return h != null ? h : (HumanCRUDTribeLocal) LogNull.logThrow();

    }

}
