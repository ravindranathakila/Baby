package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.autoplay.AutoplayControls;
import ai.ilikeplaces.logic.Listeners.widgets.autoplay.AutoplayControlsCriteria;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.logic.validators.unit.SimpleString;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpSession;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class SignInOn extends AbstractWidgetListener {
// ------------------------------ FIELDS ------------------------------

    private static final String TRUE = "true";

    private static final String ISSIGNUP = "issignup";

    private HumanId username = null;
    private Password password = null;

    private SimpleString dbHash = null;
    private SimpleString dbSalt = null;
    private Obj<Boolean> userOk = null;
    private Obj<Boolean> existButNotActive = null;

// -------------------------- ENUMERATIONS --------------------------

    public static enum SignInOnIds implements WidgetIds {
        signinon_login,
        signinon_logon,
        signinonUsername,
        signinonPassword,
        signinonSubmit,
        signinonNotice,
        signinon_autoplay,
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param request__
     * @param appendToElement__
     * @param humanId
     * @param request
     */
    public SignInOn(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId, final ServletRequest request) {
        super(request__, Page.SignInOn, appendToElement__, humanId);
    }

// ------------------------ CANONICAL METHODS ------------------------

    // ------------------------ OVERRIDING METHODS ------------------------
    @WARNING(warning = "During work ont this class, from time to time you'll want to make it a WEB 2.0 login." +
            "However, remember that having a login on a separate servlet is better for security.  HTTPS support." +
            "Also, note that we have a policy of redirecting the user to the refer. " +
            "You will also have to make sure both online and offline modes are available.")
    @Override
    protected void init(final Object... initArgs) {
        registerUserNotifier($$(SignInOnIds.signinonNotice));

        username = (HumanId) initArgs[0];
        password = new Password();
        dbHash = new SimpleString("");
        dbSalt = new SimpleString("");
        userOk = new Obj<Boolean>(false);
        existButNotActive = new Obj<Boolean>(false);

        if (username.validate() == 0) {
            UCShowHideWidgets:
            {
                $$displayBlock($$(SignInOnIds.signinon_logon));
                $$displayNone($$(SignInOnIds.signinon_login));
            }

            UCAutoplay:
            {
                new AutoplayControls(request, new AutoplayControlsCriteria().setHumanId(username), $$(SignInOnIds.signinon_autoplay));
            }
        } else {
            UCShowHIdeWIdgets:
            {
                $$displayBlock($$(SignInOnIds.signinon_login));
                $$displayNone($$(SignInOnIds.signinon_logon));
            }
            UCShowPleaseLoginMessage:
            {
                $$(SignInOnIds.signinonNotice).setTextContent("Login!");
            }
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(SignInOnIds.signinonUsername), EventType.BLUR.toString(), new EventListener() {
            private HumanId myusername = username;
            private SimpleString mydbHash = dbHash;
            private SimpleString mydbSalt = dbSalt;
            private Obj<Boolean> myuserOk = userOk;
            private Obj<Boolean> myexistButNotActive = existButNotActive;

            @Override
            public void handleEvent(final Event evt_) {
                myusername.setObj($$(evt_).getAttribute(MarkupTag.INPUT.value()));

                if (myusername.validate() == 0) {
                    Human existingUser = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(myusername.getObjectAsValid());
                    if (existingUser != null && existingUser.getHumanAlive()) {/*Ok user name valid but now we check for password*/
                        final HumansAuthentication humansAuthentication = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(myusername).returnValue();
                        mydbHash.setObj(humansAuthentication.getHumanAuthenticationHash());
                        mydbSalt.setObj(humansAuthentication.getHumanAuthenticationSalt());
                        myuserOk.setObj(true);
                    } else if (existingUser != null && !existingUser.getHumanAlive()) {/*Ok password wrong or not activated. What do we do with this guy? First lets make his session object null*/
                        myuserOk.setObj(false);
                        myexistButNotActive.setObj(true);
                        $$sendJS(JSCodeToSend.redirectPageWithURL("/page/_profile"));
                        notifyUser("Please activate your account.");
                    }
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(SignInOnIds.signinonPassword), EventType.BLUR.toString(), new EventListener() {
            private Password mypassword = password;

            @Override
            public void handleEvent(final Event evt_) {
                mypassword.setObj($$(evt_).getAttribute(MarkupTag.INPUT.value()));
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));


//        Lat time we checked, this did not work!
//        super.registerForClick($$(Page.signinonSubmit), new EventListener() {
//            @Override
//            public void handleEvent(Event evt) {
//                notifyUser("Trying to log you in...");
//            }
//        });


        final EventListener loginListener = new EventListener() {
            private HumanId myusername = username;
            private Password mypassword = password;
            private SimpleString mydbHash = dbHash;
            private SimpleString mydbSalt = dbSalt;
            private Obj<Boolean> myuserOk = userOk;
            private Obj<Boolean> myexistButNotActive = existButNotActive;


            @Override
            public void handleEvent(final Event evt_) {
                final HttpSession userSession_ = ((ItsNatHttpSession) request.getItsNatSession()).getHttpSession();

                if (myusername.validate() == 0 && mypassword.validate() == 0) {
                    if (userSession_.getAttribute(HumanUserLocal.NAME) == null) {
                        /*Ok the session does not have the bean, initialize it with the user with email id and password*/
                        if (myuserOk.getObj()) {/*Ok user name valid but now we check for password*/
                            if (mydbHash.getObj().equals(DB.getSingletonHashingFaceLocal(true).getHash(mypassword.getObjectAsValid(), mydbSalt.getObj()))) {
                                final HumanUserLocal humanUserLocal = DB.getHumanUserLocal(true);

                                humanUserLocal.setHumanUserId(myusername.getObjectAsValid());

                                userSession_.setAttribute(HumanUserLocal.NAME, (new SessionBoundBadRefWrapper<HumanUserLocal>(humanUserLocal, userSession_)));

                                notifyUser("Logging you in...");

                                $$sendJS(JSCodeToSend.refreshPageIn(0));
                            } else {/*Ok password wrong or not activated. What do we do with this guy? First lets make his session object null*/
                                notifyUser("Ha ha wrong password!");
                            }
                        } else {/*There is no such user. Ask if he forgot username or whether to create a new account :)*/
                            if (myexistButNotActive.getObj()) {
                                $$sendJS(JSCodeToSend.redirectPageWithURL("/page/_profile"));
                                notifyUser("Please activate your account.");
                            } else {
                                notifyUser(myusername.getObj() + " is not a user of this website");
                            }
                        }
                    } else {
                        //We just ignore since the form is not visible and this cannot happen, or a hacker is trying something ;)
                    }
                } else {
                    notifyUser("Login failed! Retry or Click Settings");
                }
            }
        };

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(SignInOnIds.signinonPassword), EventType.BLUR.toString(), loginListener, false);
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(SignInOnIds.signinonSubmit), EventType.CLICK.toString(), loginListener, false);
    }
}