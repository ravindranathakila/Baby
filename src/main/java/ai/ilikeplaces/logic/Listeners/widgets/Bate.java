package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import net.sf.oval.Validator;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.text.MessageFormat;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class Bate extends AbstractWidgetListener {
    private Email email;
    private Password password;

    /**
     * @param request__
     * @param appendToElement__
     * @param initArgs
     */
    public Bate(final ItsNatServletRequest request__, final Element appendToElement__, final Object... initArgs) {
        super(request__, Page.Bate, appendToElement__, initArgs);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        email = new Email("");
        password = new Password("");
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        UCSignup:
        {
            itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.BateSignupEmail), EventType.BLUR.toString(), new EventListener() {

                final Validator v = new Validator();
                final Email myemail = email;

                @Override
                public void handleEvent(final Event evt_) {
                    myemail.setObj(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value()));
                    if (myemail.validate(v) == 0) {
                        $$(Controller.Page.BateSignupNotifications).setTextContent("Email Valid!");
                    } else {
                        $$(Controller.Page.BateSignupNotifications).setTextContent("Email INVALID!");
                    }
                }
            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
            itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.BateSignupPassword), EventType.BLUR.toString(), new EventListener() {

                final Validator v = new Validator();
                final Password mypassword = password;

                @Override
                public void handleEvent(final Event evt_) {
                    mypassword.setObj(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.INPUT.value()));
                    if (mypassword.validate(v) == 0) {
                        $$(Controller.Page.BateSignupNotifications).setTextContent("Password Valid!");
                    } else {
                        $$(Controller.Page.BateSignupNotifications).setTextContent("Password INVALID!");
                    }
                }
            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

            itsNatHTMLDocument_.addEventListener((EventTarget) $$(Controller.Page.BateSignupButton), EventType.CLICK.toString(), new EventListener() {

                final Validator v = new Validator();
                final Email myemail = email;
                final Password mypassword = password;

                @Override
                public void handleEvent(final Event evt_) {
                    if (myemail.validate(v) == 0 && mypassword.validate(v) == 0) {
                        if (!DB.getHumanCRUDHumanLocal(true).doDirtyCheckHuman(myemail.getObj()).returnValue()) {
                            try {

                                final Return<Boolean> humanCreateReturn = DB.getHumanCRUDHumanLocal(true).doCHuman(
                                        new HumanId().setObjAsValid(email.getObj()),
                                        mypassword,
                                        myemail);

                                if (humanCreateReturn.returnValue()) {

                                    final String activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                            .append(ServletLogin.Username, myemail.getObj(), true)
                                            .append(ServletLogin.Password,
                                                    DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myemail.getObj()))
                                                            .returnValue()
                                                            .getHumanAuthenticationHash())
                                            .get();

                                    final String mail = MessageFormat.format(RBGet.gui().getString("SIGNUP_BODY"), RBGet.globalConfig.getString("noti_mail"))
                                            .replace("activationURL", "<a href='" +
                                                    activationURL + "' >" + activationURL + "</a>");
                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                            myemail.getObj(),
                                            RBGet.gui().getString("SIGNUP_HEADER"),
                                            mail);
                                    $$sendJSStmt(JSCodeToSend.redirectPageWithURL(Controller.Page.Activate.getURL()));
                                }

                            } catch (DBDishonourCheckedException e) {
                                $$(Controller.Page.BateSignupNotifications).setTextContent("Email was taken meanwhile!:(");
                            }
                        } else {
                            $$(Controller.Page.BateSignupNotifications).setTextContent("This email is TAKEN!:(");
                        }
                    } else {
                        //Ignored as the individual validators would've reported the error by now
                    }

                }
            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
        }
    }


}
