package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.MethodPreamble;
import ai.ilikeplaces.servlets.ServletLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.NoSuchEJBException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

/**
 * Initially this class was designed to help the nosuchejb exception bein thrown when the session
 * bound variable is attempted to be removed, but has already been discarded by the container.
 *
 * Since the role of this class is confusing, and also hard to remember, they have been enlisted below.
 *
 * 1. Mainly, to maintain the EJB session with the client(http) session.
 * 2. EJB can die or be disked out(passivated). HTTP session will be active due ot
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@FIXME(issue = "MANY",
        issues =
                {"This class can be broken in several. One for suppressing NoSuchEJBException, one for Notifications and one for httpsessionbinding. Kind of in the form of a pipeline. If this is Pipe and notification needy classes are A & B, then Pipe pipelines notifications between A and B.",
                        "i18n"})
final public class SessionBoundBadRefWrapper<T> implements HttpSessionBindingListener, Observer {

    final public T boundInstance;

    final private HttpSession bindingInstance;

    private boolean isAlive = false;

    @FIXME(issue = "You will have to verify what happens if a huge amount of session timeouts happen\n" +
            "This was made static to avoid a huge amount of references. i.e. Reference per user!")
    final static Logger logger = LoggerFactory.getLogger(SessionBoundBadRefWrapper.class.getName());


    public SessionBoundBadRefWrapper(final T theObjectWhichShouldBeBound__, final HttpSession theObjectWhichIsBoundTo__) {
        this.boundInstance = theObjectWhichShouldBeBound__;
        this.bindingInstance = theObjectWhichIsBoundTo__;
        registerAsLiveStatusObserver();
        this.isAlive = true;
    }

//    @FIXME(issue = "isAlive",
//            issues = {"As the caller did not register as an observer, he has no right to call isAlive so we return false anyway. But what happens then?",
//                    "Could an enum be used?"})
//    public SessionBoundBadRefWrapper(final T theObjectWhichShouldBeBound__, final HttpSession theObjectWhichIsBoundTo__) {
//        this.boundInstance = theObjectWhichShouldBeBound__;
//        this.bindingInstance = theObjectWhichIsBoundTo__;
//        this.isAlive = false;
//    }

    @MethodPreamble(description = "This return will be a new reference so no risk of caller modifying live state", authors = {"Ravindranth Akila"}, callBackModules = {})
    final public boolean isAlive() {
        return isAlive;
    }

    @Override
    @FIXME(issue = "This class should be a pipeline between the two objects expecting notification. Split split!!!")
    public void update(Observable o, Object dying) {
        final SmartLogger smartLogger = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, "Update received on observable", 60000, null, true);
        if (dying instanceof Boolean && (Boolean) dying) {
            this.isAlive = false;
            try {
                bindingInstance.removeAttribute(ServletLogin.HumanUser);//To be safe since ItsNat session doesn't die;)
                bindingInstance.invalidate();
                smartLogger.complete(Loggers.LEVEL.USER, "Session Invalidated Successfully.");
            } catch (final IllegalStateException e_) {
                smartLogger.complete("Session Invalidated FAILED!");
                Loggers.EXCEPTION.debug("HELLO, I JUST RECEIVED AN INVALIDATION CALL ON A ALREADY INVALIDATED SESSION. I CAUGHT AND IGNORED EXCEPTION.");
                //Already invalidated. So lets ignore this. This is the sole reason for this class.
            }
        } else {
            smartLogger.complete("Wrong Type");
        }
    }

    void registerAsLiveStatusObserver() {
        final Method m;
        try {
            m = boundInstance.getClass().getMethod("addObserver", new Class[]{Observer.class});
            try {
                m.invoke(boundInstance, this);
            } catch (final IllegalAccessException ex) {
                Loggers.EXCEPTION.error(null, ex);
            } catch (final IllegalArgumentException ex) {
                Loggers.EXCEPTION.error(null, ex);
            } catch (final InvocationTargetException ex) {
                Loggers.EXCEPTION.error(null, ex);
            }
        } catch (final NoSuchMethodException ex) {
            Loggers.EXCEPTION.info(null, ex);
        } catch (final SecurityException ex) {
            Loggers.EXCEPTION.info(null, ex);
        }
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        final Method m;
        try {
            m = boundInstance.getClass().getMethod("valueBound", new Class[]{HttpSessionBindingEvent.class});
            try {
                m.invoke(boundInstance, event);
            } catch (final IllegalAccessException ex) {
                Loggers.EXCEPTION.error(null, ex);
            } catch (final IllegalArgumentException ex) {
                Loggers.EXCEPTION.error(null, ex);
            } catch (final InvocationTargetException ex) {
                Loggers.EXCEPTION.error(null, ex);
            } catch (final NoSuchEJBException ex__) {
                /*This is the whole purpose of this wrapper. i.e. Ignore this exception*/
                /*If you have more specific exceptions which need logging, add them here*/
                Loggers.EXCEPTION.error("HELLO, NO SUCH EJB EXCEPTION JUST OCCURRED DUE TO A INVALIDATION OF REMOVED EJB.");
            }
        } catch (final NoSuchMethodException ex) {
            Loggers.EXCEPTION.error(null, ex);
        } catch (final SecurityException ex) {
            Loggers.EXCEPTION.error(null, ex);
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        final Method m;
        try {
            m = boundInstance.getClass().getMethod("valueUnbound", new Class[]{HttpSessionBindingEvent.class});
            try {
                m.invoke(boundInstance, event);
            } catch (final IllegalAccessException ex) {
                Loggers.EXCEPTION.error(null, ex);
            } catch (final IllegalArgumentException ex) {
                Loggers.EXCEPTION.error(null, ex);
            } catch (final InvocationTargetException ex) {
                /*This is the whole purpose of this wrapper. i.e. Ignore this exception*/
                /*If you have more specific exceptions which need logging, add them here*/
                Loggers.EXCEPTION.info("HELLO, NO SUCH EJB EXCEPTION JUST OCCURRED DUE TO A INVALIDATION OF REMOVED EJB.");
            } catch (final NoSuchEJBException ex__) {
                /**
                 * According to invoke specs of method "InvocationTargetException - if the underlying method throws an exception." which means
                 *  any exception will be wrapped. Hence this might not be a valid catch
                 */
                Loggers.EXCEPTION.info("HELLO, NO SUCH EJB EXCEPTION JUST OCCURRED DUE TO A INVALIDATION OF REMOVED EJB.");
            }
        } catch (final NoSuchMethodException ex) {
            Loggers.EXCEPTION.error(null, ex);
        } catch (final SecurityException ex) {
            Loggers.EXCEPTION.error(null, ex);
        }
    }
}
