package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.logic.contactimports.ImportedContact;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AIEventListener;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.MarkupTag;
import ai.reaver.Return;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLDocument;

import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 3/23/12
 * Time: 9:53 PM
 */
public class AdaptableSignup extends AbstractWidgetListener<AdaptableSignupCriteria> {

    private static final String ENTER_A_VALID_EMAIL = "enter.a.valid.email";
    private static final String INVITED_ADDED_0 = "invited.added.0";
    private static final String COULD_NOT_INVITE_AND_ADD_TRY_AGAIN = "could.not.invite.and.add.try.again";
    private static final String MESSAGE_ADDING_SELF_AS_FRIEND = "message.adding.self.as.friend";
    private static final String IS_ALREADY_YOUR_FRIEND = "0.is.already.your.friend";
    private static final String READ_MORE = "read.more";

    public static enum AdaptableSignupIds implements WidgetIds {
        AdaptableSignupWidget,
        AdaptableSignupNoti,
        AdaptableSignupEmail,
        AdaptableSignupSignup,
        AdaptableSignupTitle,
    }

    /**
     * @param request__
     * @param appendToElement__
     */
    public AdaptableSignup(final ItsNatServletRequest request__, final AdaptableSignupCriteria adaptableSignupCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.AdaptableSignup, adaptableSignupCriteria, appendToElement__);
    }

    @Override
    protected void init(final AdaptableSignupCriteria adaptableSignupCriteria) {
        registerUserNotifier(AdaptableSignupIds.AdaptableSignupNoti);
        $$(AdaptableSignupIds.AdaptableSignupTitle).setTextContent(criteria.getWidgetTitle());
    }

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the constructor
     *
     * @param itsNatHTMLDocument_
     * @param hTMLDocument_
     */
    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

        super.registerForInputText(AdaptableSignupIds.AdaptableSignupEmail,
                new AIEventListener<AdaptableSignupCriteria>(criteria) {
                    /**
                     * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                     *
                     * @param evt fired from client
                     */
                    @Override
                    protected void onFire(Event evt) {

                        final Email email = new Email($$(evt).getAttribute(MarkupTag.INPUT.value()));
                        if (email.valid()) {
                            criteria.getInviteData().setEmail(email);
                        } else {
                            notifyUser(RBGet.gui().getString(ENTER_A_VALID_EMAIL));
                        }
                    }
                }
        );

        super.registerForClick(AdaptableSignupIds.AdaptableSignupSignup,
                new AIEventListener<AdaptableSignupCriteria>(criteria) {
                    /**
                     * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                     *
                     * @param evt fired from client
                     */
                    @Override
                    protected void onFire(Event evt) {

                        final Email email = criteria.getInviteData().getEmail();

                        if (email.valid()) {

                            final HumanId invitee = new HumanId(email.getObj());
                            if (!DB.getHumanCRUDHumanLocal(false).doDirtyIsHumansNetPeople(criteria.getHumanId(), invitee).returnValue()) {
                                if (!criteria.getHumanId().getHumanId().equals(email.getObj())) {
                                    Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doNTxAddHumansNetPeople(criteria.getHumanId(), invitee);
                                    if (r.valid()) {
                                        $$(AdaptableSignupIds.AdaptableSignupNoti).setTextContent(MessageFormat.format(RBGet.gui().getString(INVITED_ADDED_0), email));
                                        $$(AdaptableSignupIds.AdaptableSignupEmail).setAttribute(MarkupTag.INPUT.value(), "");

                                        final String notification = criteria.getAdaptableSignupCallback().afterInvite(invitee);
                                        notifyUser(notification);
                                        $$sendJS(criteria.getAdaptableSignupCallback().jsToSend(invitee));

                                    } else {
                                        notifyUser(RBGet.gui().getString(COULD_NOT_INVITE_AND_ADD_TRY_AGAIN));
                                    }
                                } else {
                                    notifyUser(RBGet.gui().getString(MESSAGE_ADDING_SELF_AS_FRIEND));
                                }
                            } else {
                                notifyUser(MessageFormat.format(RBGet.gui().getString(IS_ALREADY_YOUR_FRIEND), UserProperty.HUMANS_IDENTITY_CACHE.get(email.getObj(), "").getHuman().getDisplayName()));
                            }
                        }
                    }
                });
    }

}

