package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.util.LogNull;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.logging.Level;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Singleton
@NOTE(note = "MADE FINAL AS CONSTRUCTOR THROWS EXCEPTION TO PREVENT UNINITIALIZED VARIABLES. SUBCLASSING SINGLETON NO SENSE ANYWAY.")
@FIXME(issue = "NON INJECTED CALL SHOULD BE VERIFIED IF A USER SENDS FALSE")
@Startup
final public class DB implements DBLocal {

    final static private Properties P_ = new Properties();
    static private Context Context_ = null;
    static private boolean OK_ = false;
    final static private ResourceBundle exceptionMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.ExceptionMsgs");
    final static private ResourceBundle config = ResourceBundle.getBundle("ai.ilikeplaces.rbs.Config");
    final static private String ICF = config.getString("oejb.LICF");
    final static Logger logger = LoggerFactory.getLogger(DB.class);

    static {
        try {
            DB.P_.put(Context.INITIAL_CONTEXT_FACTORY, DB.ICF);
            DB.Context_ = new InitialContext(P_);
            DB.OK_ = true;
        } catch (NamingException ex) {
            DB.OK_ = false;
            logger.error("{}", ex);
        }
    }

    static public void isOK() {
        if (!OK_) {
            throw new IllegalStateException(exceptionMsgs.getString("ai.ilikeplaces.logic.crud.DB.0001"));
        }
    }

    public DB() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", DB.class, this.hashCode());
    }

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

    @Override
    public HumanCRUDHumanLocal getHumanCRUDHumanLocal() {
        isOK();
        HumanCRUDHumanLocal h = null;
        try {
            h = (HumanCRUDHumanLocal) Context_.lookup(HumanCRUDHumanLocal.NAME);
        } catch (NamingException ex) {
            logger.error("{}", ex);
        }
        return h != null ? h : (HumanCRUDHumanLocal) LogNull.logThrow();
    }

    public static HumanCRUDHumanLocal getHumanCRUDHumanLocal(final boolean nonInjected) {
        isOK();
        HumanCRUDHumanLocal h = null;
        try {
            h = ((DBLocal) Context_.lookup(DBLocal.NAME)).getHumanCRUDHumanLocal();
        } catch (NamingException ex) {
            logger.error("{}", ex);
        }
        return h != null ? h : (HumanCRUDHumanLocal) LogNull.logThrow();

    }

    @Override
    public HumanCRUDLocationLocal getHumanCRUDLocationLocal() {
        isOK();
        HumanCRUDLocationLocal h = null;
        try {
            h = (HumanCRUDLocationLocal) Context_.lookup(HumanCRUDLocationLocal.NAME);
        } catch (NamingException ex) {
            logger.error("{}", ex);
        }
        return h != null ? h : (HumanCRUDLocationLocal) LogNull.logThrow();
    }

    public static HumanCRUDLocationLocal getHumanCRUDLocationLocal(final boolean nonInjected) {
        isOK();
        HumanCRUDLocationLocal h = null;
        try {
            h = ((DBLocal) Context_.lookup(DBLocal.NAME)).getHumanCRUDLocationLocal();
        } catch (NamingException ex) {
            logger.error("{}", ex);
        }
        return h != null ? h : (HumanCRUDLocationLocal) LogNull.logThrow();

    }
}