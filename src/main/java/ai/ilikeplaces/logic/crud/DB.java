package ai.ilikeplaces.logic.crud;

import java.util.logging.Level;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.util.*;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Singleton
@NOTE(note = "MADE FINAL AS CONSTRUCTOR THROWS EXCEPTION TO PREVENT UNINITIALIZED VARIABLES. SUBCLASSING SINGLETON NO SENSE ANYWAY.")
@Startup
final public class DB implements DBLocal {

    final static private Properties P_ = new Properties();
    static private Context Context_ = null;
    static private boolean OK_ = false;

    static {
        try {
            DB.P_.put(Context.INITIAL_CONTEXT_FACTORY, DB.ICF);
            DB.Context_ = new InitialContext(P_);
            DB.OK_ = true;
        } catch (NamingException ex) {
            DB.OK_ = false;
            java.util.logging.Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    final static private String ICF = "org.apache.openejb.client.LocalInitialContextFactory";

    final static public void isOK() {
        if (!OK_) {
            throw new IllegalStateException("SORRY! INITIALIZATION OF STATICS OF THIS CLASS HAVE FAILED. HENCE I PREVENTED ACCESS.");
        }
    }

    public DB() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", DB.class, this.hashCode());
    }
    final static Logger logger = LoggerFactory.getLogger(DB.class);

    @Override
    public HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal() {
        isOK();
        HumanCRUDPublicPhotoLocal h = null;
        try {
            h = (HumanCRUDPublicPhotoLocal) Context_.lookup(HumanCRUDPublicPhotoLocal.NAME);
        } catch (NamingException ex) {
            logger.error("{}", ex);
        }
        return h != null ? h : (HumanCRUDPublicPhotoLocal) LogNull.logThrow();
    }

    public static HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal(final boolean nonInjected) {
        isOK();
        HumanCRUDPublicPhotoLocal h = null;
        try {
            h = ((DBLocal) Context_.lookup(DBLocal.NAME)).getHumanCRUDPublicPhotoLocal();
        } catch (NamingException ex) {
            logger.error("{}", ex);
        }
        return h != null ? h : (HumanCRUDPublicPhotoLocal) LogNull.logThrow();
    }
}
