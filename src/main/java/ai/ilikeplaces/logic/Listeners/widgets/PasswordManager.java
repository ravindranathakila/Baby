package ai.ilikeplaces.logic.Listeners.widgets;

import ai.doc.License;
import ai.doc._ok;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.Obj;
import ai.reaver.HumanId;
import ai.reaver.Return;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import javax.servlet.http.HttpSession;

import static ai.ilikeplaces.servlets.Controller.Page.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_ok
abstract public class PasswordManager extends AbstractWidgetListener {
    public final static String YIKES_SOMETHING_WENT_WRONG = RBGet.gui().getString("YIKES_SOMETHING_WENT_WRONG");
// ------------------------------ FIELDS ------------------------------

    HumanId humanId = null;
    Obj<Boolean> current;
    Obj<Boolean> newSet;
    Obj<Boolean> newConfirmSet;

    Password currentPass;
    Password newPass;
    Password newConfirmPass;

    HttpSession httpSession;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     * @param humanId__
     * @param httpSession__
     */
    public PasswordManager(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId__, final HttpSession httpSession__) {
        super(request__, Page.PasswordChange, appendToElement__, humanId__, httpSession__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.current = new Obj<Boolean>(false);
        this.newSet = new Obj<Boolean>(false);
        this.newConfirmSet = new Obj<Boolean>(false);

        this.currentPass = new Password(null);
        this.newPass = new Password(null);
        this.newConfirmPass = new Password(null);


        this.humanId = (HumanId) initArgs[0];
        this.httpSession = (HttpSession) initArgs[1];

        $$(ProfilePasswordNotice).setTextContent("Change password for " + humanId.getObj());

        displayBlock($$(ProfilePasswordWidget));
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        itsNatHTMLDocument__.addEventListener((EventTarget) $$(ProfilePasswordCurrent), EventType.BLUR.toString(), new EventListener() {
            private HumanId myhumanId = humanId;
            private Obj<Boolean> mycurrent = current;

            @Override
            public void handleEvent(final Event evt_) {
                final Password profilePasswordCurrentString = new Password($$(evt_).getAttribute(MarkupTag.TEXTAREA.value()));
                if (profilePasswordCurrentString.validate() != 0) {
                    mycurrent.setObj(false);
                    $$(ProfilePasswordNotice).setTextContent(profilePasswordCurrentString.getViolationAsString());
                } else {
                    if (ServletLogin.isCorrectPassword(myhumanId, profilePasswordCurrentString.getObj())) {
                        mycurrent.setObj(true);
                        currentPass.setObjAsValid(profilePasswordCurrentString.getObj());
                        $$(ProfilePasswordNotice).setTextContent("Correct!");
                    } else {
                        mycurrent.setObj(false);
                        $$(ProfilePasswordNotice).setTextContent("Sorry! Password Wrong.");
                    }
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(ProfilePasswordNew), EventType.BLUR.toString(), new EventListener() {
            private HumanId myhumanId = humanId;
            private Obj<Boolean> mycurrent = current;
            private Obj<Boolean> mynewSet = newSet;
            private Obj<Boolean> mynewConfirmSet = newConfirmSet;

            @Override
            public void handleEvent(final Event evt_) {
                final Password profilePasswordNew = new Password($$(evt_).getAttribute(MarkupTag.TEXTAREA.value()));
                if (profilePasswordNew.validate() != 0) {
                    mynewSet.setObj(false);
                    $$(ProfilePasswordNotice).setTextContent(profilePasswordNew.getViolationAsString());
                } else {
                    mynewSet.setObj(true);
                    if (mycurrent.getObj()) {
                        newPass.setObjAsValid(profilePasswordNew.getObj());
                        $$(ProfilePasswordNotice).setTextContent("New Password Valid!");
                    } else {
                        $$(ProfilePasswordNotice).setTextContent("Sorry! You have to provide the current password first.");
                    }
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(ProfilePasswordNewConfirm), EventType.BLUR.toString(), new EventListener() {
            private HumanId myhumanId = humanId;
            private Obj<Boolean> mycurrent = current;
            private Obj<Boolean> mynewSet = newSet;
            private Obj<Boolean> mynewConfirmSet = newConfirmSet;

            private Password mynewPass = newPass;
            private Password mynewConfirmPass = newConfirmPass;

            @Override
            public void handleEvent(final Event evt_) {
                final Password profilePasswordNewConfirm = new Password($$(evt_).getAttribute(MarkupTag.TEXTAREA.value()));
                if (profilePasswordNewConfirm.validate() != 0) {
                    mynewConfirmSet.setObj(false);
                    $$(ProfilePasswordNotice).setTextContent(profilePasswordNewConfirm.getViolationAsString());
                } else {
                    mynewConfirmSet.setObj(true);
                    if (mycurrent.getObj()) {
                        if (mynewSet.getObj() && mynewPass.getObj().equals(profilePasswordNewConfirm.getObj())) {
                            mynewConfirmPass.setObjAsValid(profilePasswordNewConfirm.getObj());
                            $$(ProfilePasswordNotice).setTextContent("Confirmation Correct!");
                        } else {
                            mynewConfirmSet.setObj(false);
                            $$(ProfilePasswordNotice).setTextContent("Sorry! Confirmation Wrong.");
                        }
                    } else {
                        $$(ProfilePasswordNotice).setTextContent("Sorry! You have to provide the current password first.");
                    }
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(ProfilePasswordSave), EventType.CLICK.toString(), new EventListener() {
            private HumanId myhumanId = humanId;
            private Obj<Boolean> mycurrent = current;
            private Obj<Boolean> mynewSet = newSet;
            private Obj<Boolean> mynewConfirmSet = newConfirmSet;

            private Password mycurrentPass = currentPass;
            private Password mynewPass = newPass;
            private Password mynewConfirmPass = newConfirmPass;

            private HttpSession myhttpSession = httpSession;

            @Override
            public void handleEvent(final Event evt_) {
                if (!mycurrent.getObj()) {
                    $$(ProfilePasswordNotice).setTextContent("Sorry! You have to re-enter the current password.");
                } else if (!mynewSet.getObj()) {
                    $$(ProfilePasswordNotice).setTextContent("Sorry! You have to re-enter new password.");
                } else if (!mynewConfirmSet.getObj()) {
                    $$(ProfilePasswordNotice).setTextContent("Sorry! You have to re-enter the confirmation.");
                } else {
                    if (!mynewPass.getObjectAsValid().equals(mynewConfirmPass.getObjectAsValid())) {
                        $$(ProfilePasswordNotice).setTextContent("Sorry! Your new password and its' confirmation do not match.");
                    } else {
                        final Return<Boolean> r = ServletLogin.changePassword(myhttpSession, myhumanId, mycurrentPass, mynewPass);
                        if (r.returnStatus() == 0) {
                            if (r.returnValue()) {
                                $$(ProfilePasswordNotice).setTextContent("Password Updated Successfully!");
                                remove(evt_.getTarget(), EventType.CLICK, this);
                            } else {
                                $$(ProfilePasswordNotice).setTextContent(YIKES_SOMETHING_WENT_WRONG);
                            }
                        } else {
                            $$(ProfilePasswordNotice).setTextContent(YIKES_SOMETHING_WENT_WRONG);
                        }
                    }
                }
            }
        }, false);
    }
}
