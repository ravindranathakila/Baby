package ai.ilikeplaces.logic.Listeners.widgets;

import ai.doc._doc;
import ai.doc._note;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.contactimports.ImportedContact;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AIEventListener;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.reaver.Return;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 2/17/12
 * Time: 6:47 PM
 */
@_doc(
        NOTE = {
                @_note("The term JUICE was coined up from Seth Godin's book Unleashing the Idea Virus."),
                @_note("The idea is to provide so much positive Juice into a HIVE which we believe can get infected by our idea virus."),
                @_note("We have a pitfall though. We have four sections, Talk, Tribes, Moments and Snaps. This needs 4 ideas(or do we?)"),
                @_note("Having four ideas might confuse the user."),
                @_note("This also gives us Mixed Fruit Juice rather than a fine blend of one:-/."),

                @_note("Excitement: http://www.psychologytoday.com/blog/learning-play/200912/the-nature-excitement"),
                @_note("Excitement (more depth): http://worldcupcollege.com/2010/06/the-psychology-of-excitement/")
        }
)
public class Juice extends AbstractWidgetListener<JuiceCriteria> {

    private static final String ENTER_A_VALID_EMAIL = "enter.a.valid.email";
    private static final String INVITED_ADDED_0 = "invited.added.0";
    private static final String COULD_NOT_INVITE_AND_ADD_TRY_AGAIN = "could.not.invite.and.add.try.again";
    private static final String MESSAGE_ADDING_SELF_AS_FRIEND = "message.adding.self.as.friend";
    private static final String IS_ALREADY_YOUR_FRIEND = "0.is.already.your.friend";
    private static final String READ_MORE = "read.more";

    public static enum JuiceIds implements WidgetIds {
        Juice_Content,

        Juice_Talk,
        Juice_Talk_Content,
        Juice_Tribe,
        Juice_Tribe_Content,
        Juice_Moment,
        Juice_Moment_Content,
        Juice_Snap,
        Juice_Snap_Content,

        Juice_Signup_Show,

        Juice_Email,
        Juice_Signup_Click,
        Juice_Noti,

    }


    /**
     * @param request__
     * @param appendToElement__
     */
    public Juice(final ItsNatServletRequest request__, final JuiceCriteria juiceCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.Juice, juiceCriteria, appendToElement__);
        super.registerUserNotifier(JuiceIds.Juice_Noti);
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
        super.registerForInputText(JuiceIds.Juice_Email, new AIEventListener<JuiceCriteria>(criteria) {
            /**
             * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
             *
             * @param evt fired from client
             */
            @Override
            protected void onFire(Event evt) {
                Email email__ = new Email(getTargetInputText(evt));
                if (email__.valid()) {
                    criteria.getInviteData().setEmail(email__);
                } else {
                    notifyUser("Sorry, but we think this email is invalid :-(");
                }
            }
        });

        super.registerForClick(JuiceIds.Juice_Signup_Click, new AIEventListener<JuiceCriteria>(criteria) {
            /**
             * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
             *
             * @param evt fired from client
             */
            @Override
            protected void onFire(Event evt) {
                notifyUser("Hold on please... We are signing you up!");
            }
        });
        super.registerForClick(JuiceIds.Juice_Signup_Click, new AIEventListener<JuiceCriteria>(criteria) {

            /**
             * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
             *
             * @param evt fired from client
             */
            @Override
            protected void onFire(Event evt) {
                final Email email = criteria.getInviteData().getEmail();

                if (email.valid()) {
                    if (!DB.getHumanCRUDHumanLocal(false).doDirtyCheckHuman(email.getObj()).returnValue()) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                final Return<Boolean> returnVal = ai.ilikeplaces.logic.Listeners.widgets.Bate.sendInviteToOfflineInvite(
                                        "I Like Places",
                                        new ImportedContact().setEmail(email.getObj()).setFullName(""));
                                if (!returnVal.valid() || !returnVal.returnValue()) {
                                    //we are being optimistic and assuming user entered the proper email.
                                    //This is because mail server check takes a lot of time
                                }
                            }
                        }).start();

                        $$sendJSStmt(JSCodeToSend.redirectPageWithURL(Controller.Page.Activate.getURL()));
                    } else {
                        $$sendJS(JSCodeToSend.redirectPageWithURL(Controller.Page.Profile.getURL()));
                    }
                }
            }
        });

        super.registerForClick(JuiceIds.Juice_Signup_Show, new AIEventListener<JuiceCriteria>(criteria) {
            //this is in place just to record analytics
        });
    }
}
