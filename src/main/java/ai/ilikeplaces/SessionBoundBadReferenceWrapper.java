package ai.ilikeplaces;

import ai.ilikeplaces.doc.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSessionBindingEvent;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;
import javax.ejb.NoSuchEJBException;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@FIXME(issue = "This class can be broken in several. One for suppressing NoSuchEJBException, one for Notifications and one for httpsessionbinding.\n" +
"Kind of in the form of a pipeline. If this is Pipe and notification needy classes are A & B, then Pipe pipelines notifications between A and B.")
final public class SessionBoundBadReferenceWrapper<T> implements HttpSessionBindingListener, Observer {

    final public T boundInstance;
    final private HttpSession bindingInstance;
    @FIXME(issue = "You will have to verify what happens if a huge amount of session timeouts happen\n" +
    "This was made static to avoid a huge amount of references. i.e. Reference per user!")
    final static Logger logger = Logger.getLogger(SessionBoundBadReferenceWrapper.class.getName());
    private boolean isAlive = false;

    public SessionBoundBadReferenceWrapper(final T theObjectWhichShouldBeBound__, final HttpSession theObjectWhichIsBindedTo__, final boolean implementsObservable) {
        this.boundInstance = theObjectWhichShouldBeBound__;
        this.bindingInstance = theObjectWhichIsBindedTo__;
        if (implementsObservable) {
            registerAsLiveStatusObserver();
            this.isAlive = true;
        }
    }

    @FIXME(issue = "isAlive",
    issues = {"As the caller did not register as an observer, he has no right to call isalive so we return false anyway. But what happens then?",
        "Could an enum be used?"})
    public SessionBoundBadReferenceWrapper(final T theObjectWhichShouldBeBound__, final HttpSession theObjectWhichIsBindedTo__) {
        this.boundInstance = theObjectWhichShouldBeBound__;
        this.bindingInstance = theObjectWhichIsBindedTo__;
        this.isAlive = false;
    }

    @MethodPreamble(description = "This return will be a new reference so no risk of caller modifying live state", authors = {"Ravindranth Akila"}, callBackModules = {})
    final public boolean isAlive() {
        return isAlive;
    }

    @Override
    @FIXME(issue = "This class should be a pipeline between the two objects expecting notification. Split split!!!")
    public void update(Observable o, Object dying) {
        if (dying instanceof Boolean && (Boolean) dying == true) {
            this.isAlive = false;
            try{
                ((HttpSession) bindingInstance).invalidate();
            } catch (final IllegalStateException e_){
                logger.info("HELLO, I JUST RECIEVED AN INVALIDATION CALL ON A ALREADY INVALIDATED SESSION. I CAUGHT AND IGNORED EXCEPTION.");
                //Already invalidated. So lets ignore this.
            }
        } else {
            logger.info(dying.toString());
        }
    }

    void registerAsLiveStatusObserver() {
        Method m;
        try {
            m = boundInstance.getClass().getMethod("addObserver", new Class[]{Observer.class});
            try {
                m.invoke(boundInstance, this);
            } catch (final IllegalAccessException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (final IllegalArgumentException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (final InvocationTargetException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } catch (final NoSuchMethodException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (final SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        Method m;
        try {
            m = boundInstance.getClass().getMethod("valueBound", new Class[]{HttpSessionBindingEvent.class});
            try {
                m.invoke(boundInstance, event);
            } catch (final IllegalAccessException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (final IllegalArgumentException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (final InvocationTargetException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (final NoSuchEJBException ex__) {
                /*This is the whole purpose of this wrapper. i.e. Ignore this exception*/
                /*If you have more specific exceptions which need logging, add them here*/
                logger.info("HELLO! NO SUCH EJB EXCEPTION JUST OCCURED DUE TO A INVALIDATION OF REMOVED EJB.");
            }
        } catch (final NoSuchMethodException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (final SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        Method m;
        try {
            m = boundInstance.getClass().getMethod("valueUnbound", new Class[]{HttpSessionBindingEvent.class});
            try {
                m.invoke(boundInstance, event);
            } catch (final IllegalAccessException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (final IllegalArgumentException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (final InvocationTargetException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (final NoSuchEJBException ex__) {
                /*This is the whole purpose of this wrapper. i.e. Ignore this exception*/
                /*If you have more specific exceptions which need logging, add them here*/
                logger.info("HELLO! NO SUCH EJB EXCEPTION JUST OCCURED DUE TO A INVALIDATION OF REMOVED EJB.");
            }
        } catch (final NoSuchMethodException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (final SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
