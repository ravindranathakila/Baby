package ai.ilikeplaces.logic.Listeners.widgets;

import ai.doc.License;
import ai.doc._note;
import ai.doc._ok;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.ForgotPasswordCode;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_ok
@_note(note = "Password is mailed to the user. However, if another obtains it, according to current logic(2010-05-27) " +
        "if a hacker obtains this info, he still does not have access to this widgets local copy of that code. " +
        "Hence, he has to get the code and also forge the users http session on the server, which is very hard.")
abstract public class ForgotPasswordManager extends AbstractWidgetListener {
// ------------------------------ FIELDS ------------------------------

    HumanId humanId;
    ForgotPasswordCode code;
    Email email;
    Obj<Boolean> emailset;
    Obj<Boolean> codeCorrect;
    Obj<Boolean> newSet;

    Password newPass;

    HttpSession httpSession;

    public static enum ForgotPasswordManagerIds implements WidgetIds {
        ProfileForgotPasswordWidget,
        ProfileForgotPasswordNotice,
        ProfileForgotPasswordEmailAddress,
        ProfileForgotPasswordCodeMail,
        ProfileForgotPasswordEmailedCode,
        ProfileForgotPasswordNew,
        ProfileForgotPasswordSave,
        ProfileForgotPasswordEmailAddressSection,
        ProfileForgotPasswordEmailRecoverySection,
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param request__
     * @param appendToElement__
     * @param httpSession__
     */
    public ForgotPasswordManager(final ItsNatServletRequest request__, final Element appendToElement__, final HttpSession httpSession__) {
        super(request__, Page.ForgotPasswordChange, appendToElement__, httpSession__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        humanId = new HumanId("");//faking so that later listener can set it

        this.code = new ForgotPasswordCode(null);
        this.email = new Email(null);
        this.emailset = new Obj<Boolean>(false);
        this.codeCorrect = new Obj<Boolean>(false);
        this.newSet = new Obj<Boolean>(false);

        this.newPass = new Password(null);


        this.httpSession = (HttpSession) initArgs[0];

        displayBlock($$(ForgotPasswordManagerIds.ProfileForgotPasswordWidget));
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        itsNatHTMLDocument__.addEventListener((EventTarget) $$(ForgotPasswordManagerIds.ProfileForgotPasswordEmailAddress), EventType.BLUR.toString(), new EventListener() {
            private Email myemail = email;
            private Obj<Boolean> myemailset = emailset;

            @Override
            public void handleEvent(final Event evt_) {
                final Email tempemail = new Email($$(evt_).getAttribute(MarkupTag.TEXTAREA.value()));
                if (tempemail.validate() == 0) {
                    myemail.setObj(tempemail.getObj());
                    myemailset.setObj(true);
                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Okay that appears to be a proper email address.");
                } else {
                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Duh! enter a proper email address!");
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(ForgotPasswordManagerIds.ProfileForgotPasswordCodeMail), EventType.CLICK.toString(), new EventListener() {
            private Email myemail = email;
            private Obj<Boolean> myemailset = emailset;
            private HumanId myhumanId = humanId;

            @Override
            public void handleEvent(final Event evt_) {
                if (myemailset.getObj()) {
                    final Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doDirtyCheckHuman(myemail.getObj());
                    if (r.returnStatus() == 0) {
                        if (r.returnValue()) {
                            final Return<HumansAuthentication> rha = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(new HumanId(myemail.getObj()));
                            if (rha.returnStatus() == 0) {
                                code.setObj(UUID.randomUUID().toString());
                                final Return<Boolean> mailsent = SendMail.getSendMailLocal().sendAsSimpleText(myemail.getObj(),
                                        "I Like Places code(paste on website now)",
                                        code.getObj());
                                if (mailsent.returnStatus() == 0 && mailsent.returnValue()) {
                                    myhumanId.setObj(myemail.getObj());
                                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("We've emailed you a code. Paste it here. Don't leave this page.");
                                    $$displayNone($$(ForgotPasswordManagerIds.ProfileForgotPasswordEmailAddressSection));
                                    $$displayBlock($$(ForgotPasswordManagerIds.ProfileForgotPasswordEmailRecoverySection));
                                    remove(evt_.getCurrentTarget(), EventType.CLICK, this);
                                } else {
                                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent(PasswordManager.YIKES_SOMETHING_WENT_WRONG);
                                }
                            } else {
                                $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent(PasswordManager.YIKES_SOMETHING_WENT_WRONG);
                            }
                        } else {
                            $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Oh! " + myemail.getObj() + " hasn't signed up. Check for spellings once again please.");
                        }
                    } else {
                        $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent(PasswordManager.YIKES_SOMETHING_WENT_WRONG);
                    }
                } else {
                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Enter a proper email address to send the code to!");
                }
            }
        }, false);


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(ForgotPasswordManagerIds.ProfileForgotPasswordEmailedCode), EventType.BLUR.toString(), new EventListener() {
            private Email myemail = email;
            private ForgotPasswordCode mycode = code;
            private Obj<Boolean> mycodeCorrect = codeCorrect;

            @Override
            public void handleEvent(final Event evt_) {
                final ForgotPasswordCode usersValue = new ForgotPasswordCode($$(evt_).getAttribute(MarkupTag.TEXTAREA.value()));
                if (usersValue.validate() == 0) {
                    if (usersValue.getObj().equals(mycode.getObj()) || usersValue.getObj().trim().equals(mycode.getObj())) {
                        mycodeCorrect.setObj(true);
                        $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Good, that is the code we emailed you. Now select a new password.");
                    } else {
                        mycodeCorrect.setObj(false);//defensive as this value is set above
                        $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Oh! That isn't the code we emailed you. Try again");
                        //$$displayBlock($$(ForgotPasswordManagerIds.ProfileForgotPasswordEmailAddressSection));
                        //$$displayNone($$(ForgotPasswordManagerIds.ProfileForgotPasswordEmailRecoverySection));
                    }
                } else {
                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent(usersValue.getViolationAsString());
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(ForgotPasswordManagerIds.ProfileForgotPasswordNew), EventType.BLUR.toString(), new EventListener() {
            private HumanId myhumanId = humanId;
            private Obj<Boolean> mycodeCorrect = codeCorrect;
            private Password mynewPass = newPass;

            private Obj<Boolean> mynewSet = newSet;

            @Override
            public void handleEvent(final Event evt_) {
                final Password profilePasswordNew = new Password($$(evt_).getAttribute(MarkupTag.TEXTAREA.value()));
                if (profilePasswordNew.validate() != 0) {
                    mynewSet.setObj(false);
                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent(profilePasswordNew.getViolationAsString());
                } else {
                    mynewSet.setObj(true);
                    if (mycodeCorrect.getObj()) {
                        mynewPass.setObj(profilePasswordNew.getObj());
                        $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("New Password Valid!");
                    } else {
                        $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Sorry! You have to provide the code first.");
                    }
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(ForgotPasswordManagerIds.ProfileForgotPasswordSave), EventType.CLICK.toString(), new EventListener() {
            private HumanId myhumanId = humanId;
            private Obj<Boolean> mycodeCorrect = codeCorrect;
            private Obj<Boolean> mynewSet = newSet;

            private Password mynewPass = newPass;

            private HttpSession myhttpSession = httpSession;

            @Override
            public void handleEvent(final Event evt_) {
                if (!codeCorrect.getObj()) {
                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Sorry! You have to enter the code.");
                } else if (!mynewSet.getObj()) {
                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Sorry! You have to enter new password.");
                } else {
                    final Return<Boolean> r = ServletLogin.changePassword(myhumanId, mynewPass);
                    if (r.returnStatus() == 0) {
                        if (r.returnValue()) {
                            $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Password Updated Successfully! Please login.");
                            $$displayNone($$(ForgotPasswordManagerIds.ProfileForgotPasswordEmailRecoverySection));

                            AttemptToUnlockAccountForPeopleWhoFailedToDoSoWithEmail:
                            {
                                final Return<Boolean> rAct = DB.getHumanCRUDHumanLocal(true).doUActivateHuman(new HumanId(myhumanId.getHumanId()).getSelfAsValid());
                                if (rAct.returnStatus() == 0 && rAct.returnValue()) {
                                    //Please DO NOT CHANGE THIS unless you know what you are doing!
                                } else {
                                    $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Password Updated Successfully But Something Went Wrong!");
                                }
                            }
                        } else {
                            $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Sorry! something went wrong.");
                        }
                    } else {
                        $$(ForgotPasswordManagerIds.ProfileForgotPasswordNotice).setTextContent("Sorry! something went wrong.");
                    }
                }
            }
        }, false);
    }
}
