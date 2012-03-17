package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.SEE;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.people.People;
import ai.ilikeplaces.logic.Listeners.widgets.people.PeopleCriteria;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventDelete;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventView;
import ai.ilikeplaces.logic.contactimports.ImportedContact;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLDocument;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventDelete.PrivateEventDeleteIds.privateEventDeleteNotice;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/20/11
 * Time: 8:50 PM
 */
public class TribeWidget extends AbstractWidgetListener<TribeWidgetCriteria> {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS STATIC --------------------------

    private static final String ENTER_A_VALID_EMAIL = "enter.a.valid.email";
    private static final String INVITED_ADDED_0 = "invited.added.0";
    private static final String COULD_NOT_INVITE_AND_ADD_TRY_AGAIN = "could.not.invite.and.add.try.again";
    private static final String MESSAGE_ADDING_SELF_AS_FRIEND = "message.adding.self.as.friend";
    private static final String IS_ALREADY_YOUR_FRIEND = "0.is.already.your.friend";
    private static final String READ_MORE = "read.more";
    private static final String INVITED_BUT_COULD_NOT_ADD_TO_TRIBE_TRY_AGAIN_FROM_YOUR_FRIEND_LIST = "invited.but.could.not.add.to.tribe.try.again.from.your.friend.list";

// -------------------------- ENUMERATIONS --------------------------

    public static enum TribeWidgetIds implements WidgetIds {
        tribeHomeWidget,
        tribeHomeName,
        tribeHomeInfo,
        tribeHomeNotice,
        tribeHomeWall,
        tribeHomeMembers,
        tribeHomeAlbum,
        tribeHomeInviteNoti,
        tribeHomeInviteEmail,
        tribeHomeInviteClick,
        tribeHomeDeleteButton
    }

    // --------------------------- CONSTRUCTORS ---------------------------
    public TribeWidget(final ItsNatServletRequest request__, final TribeWidgetCriteria tribeWidgetCriteria, final Element appendToElement__) {

        super(request__, Controller.Page.TribeHome, tribeWidgetCriteria, appendToElement__);
    }

// ------------------------ OVERRIDING METHODS ------------------------

    /**
     * Use this only in conjunction with {@link #AbstractWidgetListener(org.itsnat.core.ItsNatServletRequest, ai.ilikeplaces.servlets.Controller.Page, Object, org.w3c.dom.Element)}
     * GENERIC constructor.
     *
     * @param tribeWidgetCriteria
     */
    @Override
    protected void init(final TribeWidgetCriteria tribeWidgetCriteria) {

        criteria.setTribe(DB.getHumanCRUDTribeLocal(false).getTribe(criteria.getHumanId(), criteria.getTribeId(), true).returnValueBadly());

        SetTribeName:
        {
           $$(TribeWidgetIds.tribeHomeName).setTextContent(criteria.getTribe().getTribeName());
        }

        setTribeStory:
        {
            $$(TribeWidgetIds.tribeHomeInfo).setTextContent(criteria.getTribe().getTribeStory());
        }


        UCUpdateUserLocation:
        {
            super.getHumanUserFromRequest(request).storeAndUpdateWith(
                    HumanUserLocal.STORE_KEY.USER_LOCATION_TYPE,
                    Wall.wallTypeTribe
            );
            super.getHumanUserFromRequest(request).storeAndUpdateWith(
                    HumanUserLocal.STORE_KEY.USER_LOCATION_DETAILS,
                    criteria.getTribeId()
            );
        }

        new WallWidgetTribe(request, new WallWidgetTribeCriteria().setHumanId(criteria.getHumanId()).setTribeId(criteria.getTribeId().getObj()), $$(TribeWidgetIds.tribeHomeWall));
        new AlbumManagerTribe(request, $$(TribeWidgetIds.tribeHomeAlbum), criteria.getHumanId(), criteria.getTribe());
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {

        Delete:
        {


            super.registerForClick($$(TribeWidgetIds.tribeHomeDeleteButton), new AIEventListener<TribeWidgetCriteria>(criteria) {

                final Obj<Boolean> alertedUserWithConfirm = (Obj<Boolean>) new Obj<Boolean>().setObjAsValid(false);

                /**
                 * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                 *
                 * @param evt_ fired from client
                 */
                @Override
                protected void onFire(Event evt_) {
                    if (!alertedUserWithConfirm.getObj()) {
                        alertedUserWithConfirm.setObjAsValid(true);
                        $$(evt_).setAttribute(MarkupTag.IMG.src(),
                                $$(evt_).getAttribute(MarkupTag.IMG.src()).replace("JDelete.png", "JConfirm.png")
                        );
                        //$$(evt_).setTextContent("Confirm Delete!");
                    } else {
                        alertedUserWithConfirm.setObjAsValid(false); //needed because chrome or itsnat seems to be resending the event.

                        final Return<Tribe> r = DB.getHumanCRUDTribeLocal(false).removeFromTribe(criteria.getHumanId(), criteria.getTribeId());
                        if (r.valid()) {
                            remove(evt_.getTarget(), EventType.CLICK, this, false);
                            $$sendJS(
                                    JSCodeToSend.redirectPageWithURL(Controller.Page.Tribes.getURL())
                            );
                            //clear($$(privateEventDeleteNotice));
                        } else {
                            $$(privateEventDeleteNotice).setTextContent(r.returnMsg());
                        }
                    }


                }

            }
            );
        }

        AddRemoveVisitors:
        {
            final HumansNetPeople user = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(criteria.getHumanId());

            new MemberHandler<HumansFriend, List<HumansFriend>, Return<Tribe>>(
                    request, $$(TribeWidgetIds.tribeHomeMembers),
                    user,
                    user.getHumansNetPeoples(),
                    criteria.getTribe().getTribeMembers(),
                    new Save<Return<Tribe>>() {
                        final long mytribeId = criteria.getTribe().getTribeId();

                        @Override
                        public Return<Tribe> save(final HumanId humanId, final HumansFriend humansFriend) {

                            return inviteToTribe(humanId, humansFriend, criteria.getTribeId());
                        }
                    },
                    new Save<Return<Tribe>>() {
                        final long mytribeId = criteria.getTribe().getTribeId();


                        @Override
                        public Return<Tribe> save(final HumanId humanId, final HumansFriend humansFriend) {

                            final Return<Tribe> returnVal = DB.getHumanCRUDTribeLocal(false).removeFromTribe(new HumanId(humansFriend.getHumanId()), new VLong(mytribeId));

                            if (returnVal.returnStatus() == 0) {
                                SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                        humanId.getObj(),
                                        ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                new HumanId(humanId.getHumanId()),
                                                humansFriend.getHumanId(),
                                                ai.ilikeplaces.logic.Listeners.widgets.UserProperty.SENDER_NAME + " has removed you as member of tribe " + returnVal.returnValue().getTribeName()
                                        ));
                            }
                            return returnVal;
                        }
                    }
            );
        }

        AddRemoveVisitorsByEmail:
        {
            super.registerForInputText($$(TribeWidgetIds.tribeHomeInviteEmail),
                    new AIEventListener<TribeWidgetCriteria>(criteria) {
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
                                $$(TribeWidgetIds.tribeHomeInviteNoti).setTextContent(RBGet.gui().getString(ENTER_A_VALID_EMAIL));
                            }
                        }
                    }
            );

            super.registerForClick($$(TribeWidgetIds.tribeHomeInviteClick),
                    new AIEventListener<TribeWidgetCriteria>(criteria) {
                        /**
                         * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
                         *
                         * @param evt fired from client
                         */
                        @Override
                        protected void onFire(Event evt) {

                            final Email email = criteria.getInviteData().getEmail();

                            if (email.valid()) {
                                boolean isValidDbUser = false;

                                if (!DB.getHumanCRUDHumanLocal(false).doDirtyCheckHuman(email.getObj()).returnValue()) {
                                    final Return<Boolean> returnVal = ai.ilikeplaces.logic.Listeners.widgets.Bate.sendInviteToOfflineInvite(
                                            UserProperty.HUMANS_IDENTITY_CACHE.get(criteria.getHumanId().getHumanId(), "").getHuman().getDisplayName(),
                                            new ImportedContact().setEmail(email.getObj()).setFullName(""));

                                    isValidDbUser = returnVal.valid() && returnVal.returnValue();
                                } else {
                                    isValidDbUser = true;
                                }

                                if (isValidDbUser) {
                                    if (!DB.getHumanCRUDHumanLocal(false).doDirtyIsHumansNetPeople(criteria.getHumanId(), new HumanId(email.getObj())).returnValue()) {
                                        if (!criteria.getHumanId().getHumanId().equals(email.getObj())) {
                                            Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doNTxAddHumansNetPeople(criteria.getHumanId(), new HumanId(email.getObj()));
                                            if (r.valid()) {
                                                final Return<Tribe> tribeReturn = inviteToTribe(
                                                        criteria.getHumanId(),
                                                        DB.getHumanCRUDHumanLocal(false).doDirtyRHuman(new Obj<String>(email.getObj())),
                                                        criteria.getTribeId()
                                                );

                                                if (tribeReturn.valid()) {
                                                    $$(TribeWidgetIds.tribeHomeInviteNoti).setTextContent(MessageFormat.format(RBGet.gui().getString(INVITED_ADDED_0), email));
                                                    $$(TribeWidgetIds.tribeHomeInviteEmail).setAttribute(MarkupTag.INPUT.value(), "");
                                                } else {
                                                    $$(TribeWidgetIds.tribeHomeInviteNoti).setTextContent(RBGet.gui().getString(INVITED_BUT_COULD_NOT_ADD_TO_TRIBE_TRY_AGAIN_FROM_YOUR_FRIEND_LIST));
                                                }
                                            } else {
                                                $$(TribeWidgetIds.tribeHomeInviteNoti).setTextContent(RBGet.gui().getString(COULD_NOT_INVITE_AND_ADD_TRY_AGAIN));
                                            }
                                        } else {
                                            $$(TribeWidgetIds.tribeHomeInviteNoti).setTextContent(RBGet.gui().getString(MESSAGE_ADDING_SELF_AS_FRIEND));
                                        }
                                    } else {
                                        $$(TribeWidgetIds.tribeHomeInviteNoti).setTextContent(
                                                MessageFormat.format(RBGet.gui().getString(IS_ALREADY_YOUR_FRIEND), UserProperty.HUMANS_IDENTITY_CACHE.get(email.getObj(), "").getHuman().getDisplayName()));
                                    }
                                } else {
                                    $$(TribeWidgetIds.tribeHomeInviteNoti).setTextContent(RBGet.gui().getString(COULD_NOT_INVITE_AND_ADD_TRY_AGAIN));
                                }
                            }
                        }
                    });
        }

        UCFiltering:
        {
            final ArrayList<HumansTribe> humansTribes = new ArrayList<HumansTribe>(criteria.getTribe().getTribeMembers());
            final boolean remove = humansTribes.remove(criteria.getHumanId());

            @SEE(seeClasses = {
                    WallWidgetHumansWall.class,
                    PrivateEventDelete.class,
                    PrivateEventView.class,
                    Tribe.class
            })
            final People people = new People(request, new PeopleCriteria().setPeople((List<HumanIdFace>) (List<?>) humansTribes), $(Controller.Page.Skeleton_left_column));
        }
    }

    private Return<Tribe> inviteToTribe(final HumanId humanId, final HumansFriend humansFriend, final VLong tribeId) {

        final Return<Tribe> returnVal = DB.getHumanCRUDTribeLocal(false).addToTribe(new HumanId(humansFriend.getHumanId()), tribeId);

        if (returnVal.returnStatus() == 0) {
            SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                    humanId.getObj(),
                    UserProperty.getUserPropertyHtmlFor(
                            new HumanId(humanId.getHumanId()),
                            humansFriend.getHumanId(),
                            ElementComposer.compose($$(MarkupTag.A))
                                    .$ElementSetHref("#")
                                    .$ElementSetText("I added you as a member of tribe " + returnVal.returnValue().getTribeName())
                                    .get()
                    ));
        }
        return returnVal;
    }
}
