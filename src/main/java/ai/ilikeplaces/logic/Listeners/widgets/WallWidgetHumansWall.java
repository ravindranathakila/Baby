package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.entities.etc.HumanIdFace;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.people.People;
import ai.ilikeplaces.logic.Listeners.widgets.people.PeopleCriteria;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventDelete;
import ai.ilikeplaces.logic.Listeners.widgets.privateevent.PrivateEventView;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.cache.SmartCache;
import ai.ilikeplaces.util.cache.SmartCache3;
import ai.reaver.Return;
import ai.scribble.License;
import ai.scribble._fix;
import ai.scribble._see;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jun 17, 2010
 * Time: 12:17:36 AM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class WallWidgetHumansWall extends WallWidget {

    private static final String WALL_SUBIT_FROM_EMAIL = "ai/ilikeplaces/widgets/WallSubmitFromEmail.xhtml";
    private static final RefreshSpec REFRESH_SPEC = new RefreshSpec("wallMutes");
    private static final RefreshSpec REFRESH_SPEC_EMPTY = new RefreshSpec();

    final static public SmartCache<Pair<String, String>, Long> HUMANS_WALL_ID = new SmartCache<Pair<String, String>, Long>(new SmartCache.RecoverWith<Pair<String, String>, Long>() {
        @Override
        public Long getValue(final Pair<String, String> current_friend) {
            return DB.getHumanCrudWallLocal(false).readWallId(new HumanId(current_friend.getValue()), new Obj<String>(current_friend.getKey())).returnValueBadly();
        }
    });

    final static public SmartCache3<String, Msg, String> LAST_WALL_ENTRY = new SmartCache3<String, Msg, String>(
            new SmartCache3.RecoverWith<String, Msg, String>() {

                @Override
                public Msg getValue(final String whosWall, final String visitor) {
                    final List<Msg> msgs = DB.getHumanCrudWallLocal(false).readWallLastEntries(
                            new HumanId(whosWall),
                            new Obj<String>(visitor),
                            1,
                            new RefreshSpec()).returnValue();
                    return msgs.size() != 0 ? msgs.get(0) : null;//Well the comment that was here before was false!!! We all make mistakes!
                }
            }
    );

    private static final String TALK_AT_DOWN_TOWN_ER_0_S = "talk.at.down.town.er.0.s";


    HumanId requestedProfile;
    HumanId currUserAsVisitor;

    public WallWidgetHumansWall(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId requestedProfile, final HumanId currUserAsVisitor) {
        super(request__, appendToElement__, requestedProfile, currUserAsVisitor);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.requestedProfile = ((HumanId) initArgs[0]).getSelfAsValid();
        this.currUserAsVisitor = ((HumanId) initArgs[1]).getSelfAsValid();

        final HumansIdentity currUserAsVisitorHI = UserProperty.HUMANS_IDENTITY_CACHE.get((currUserAsVisitor.getHumanId()), "");
        final HumansIdentity requestedProfileHI = UserProperty.HUMANS_IDENTITY_CACHE.get((requestedProfile.getHumanId()), "");

        super.setWallProfileName(currUserAsVisitorHI.getHuman().getDisplayName());
        super.setWallProfilePhoto(UserProperty.formatProfilePhotoUrl(currUserAsVisitorHI.getHumansIdentityProfilePhoto()));
        super.setWallTitle(MessageFormat.format(RBGet.gui().getString(TALK_AT_DOWN_TOWN_ER_0_S), requestedProfileHI.getHuman().getDisplayName()));

        final int friendCount = ((List<HumansNetPeople>) $$getHumanUserFromRequest(request).cache(requestedProfile.getHumanId(), DownTownFlow.FRIENDS)).size();

        UCAddFriends:
        if (requestedProfile.equals(currUserAsVisitor)) {//Accessing own profile
            UCInviteFriendsIfNoFriends:
            {
                if (friendCount < 2) {

                    $$displayNone(WallWidgetIds.wallWidget);

                    String title = "You need at least 2 followers to unlock your talk page";

                    //https://upload.wikimedia.org/wikipedia/commons/8/8d/Ambox_padlock_red.svg
                    new Info(request,
                            new InfoCriteria()
                                    .setImage("/images/What_is_exciting_lately.png"),
                            $$(WallWidgetIds.wallGame));

                    new Info(request,
                            new InfoCriteria()
                                    .setImage("/images/What_is_exciting_lately_Talk.png"),
                            $$(WallWidgetIds.wallGame));

                    new Info(request,
                            new InfoCriteria()
                                    .setTitle(title)
                                    .setImage("/images/Locked.png"),
                            $$(WallWidgetIds.wallGame)) {
                        /**
                         * Use this only in conjunction with
                         * GENERIC constructor.
                         *
                         * @param infoCriteria
                         */
                        @Override
                        protected void init(InfoCriteria infoCriteria) {
                            UCSetFriendAddWidget:
                            {
                                String addFollowerTitle = "";

                                if (friendCount == 0) {
                                    addFollowerTitle = "Add 2 Followers To Start Posting!";
                                } else if (friendCount == 1) {
                                    addFollowerTitle = "Almost there! Add 1 More Follower To Posting!";
                                }

                                new AdaptableSignup(
                                        request,
                                        new AdaptableSignupCriteria()
                                                .setHumanId(requestedProfile)
                                                .setWidgetTitle(addFollowerTitle)
                                                .setAdaptableSignupCallback(new AdaptableSignupCallback() {
                                                    @Override
                                                    public String afterInvite(final HumanId invitee) {
                                                        return ai.ilikeplaces.logic.Listeners.widgets.UserProperty.HUMANS_IDENTITY_CACHE
                                                                .get(invitee.getHumanId(), invitee.getHumanId()).getHuman().getDisplayName() + " is now following you!";
                                                    }

                                                    @Override
                                                    public String jsToSend(HumanId invitee) {
                                                        return JSCodeToSend.refreshPageIn(5);
                                                    }
                                                }),
                                        $$(InfoIds.InfoAppend));
                            }
                        }
                    };

                } else {

                    String addFollowerTitle = "Add more followers!";

                    new AdaptableSignup(
                            request,
                            new AdaptableSignupCriteria()
                                    .setHumanId(requestedProfile)
                                    .setWidgetTitle(addFollowerTitle)
                                    .setAdaptableSignupCallback(new AdaptableSignupCallback() {
                                        @Override
                                        public String afterInvite(final HumanId invitee) {
                                            return ai.ilikeplaces.logic.Listeners.widgets.UserProperty.HUMANS_IDENTITY_CACHE
                                                    .get(invitee.getHumanId(), invitee.getHumanId()).getHuman().getDisplayName() + " is now following you!";
                                        }

                                        @Override
                                        public String jsToSend(HumanId invitee) {
                                            return JSCodeToSend.refreshPageIn(5);
                                        }
                                    }),
                            $$(WallWidgetIds.wallFollowers));
                }
            }
        }


        fetchToEmail();

        final Wall wall = DB.getHumanCrudWallLocal(true).readWall(requestedProfile, new Obj<HumanId>(currUserAsVisitor), REFRESH_SPEC).returnValueBadly();
        final List<Msg> wallEntries = DB.getHumanCrudWallLocal(true).readWallLastEntries(requestedProfile, new Obj<HumanId>(currUserAsVisitor), 25, REFRESH_SPEC_EMPTY).returnValueBadly();

        for (final Msg msg : wallEntries) {
            new UserProperty(request, $$(WallWidgetIds.wallContent), new HumanId(msg.getMsgMetadata())) {
                protected void init(final Object... initArgs) {
                    $$(Controller.Page.user_property_content).setTextContent(msg.getMsgContent());
                }
            };
        }

        DB.getHumanCRUDHumansUnseenLocal(false).removeEntry(currUserAsVisitor.getObjectAsValid(), wall.getWallId());

        $$displayWallAsMuted($$(WallWidgetIds.wallMute), wall.getWallMutes().contains(currUserAsVisitor));

        UCFiltering:
        {

            final List<HumansNetPeople> beFriends = (List<HumansNetPeople>) $$getHumanUserFromRequest(request)
                    .cache(requestedProfile.getHumanId(), DownTownFlow.FRIENDS);

            @_see(seeClasses = {
                    WallWidgetHumansWall.class,
                    PrivateEventDelete.class,
                    PrivateEventView.class,
                    Tribe.class
            })
            final String peopleFetchToEmail1 = new People(
                    request,
                    new PeopleCriteria()
                            .setPeople((List<HumanIdFace>) (List<?>) beFriends),
                    $(Controller.Page.Skeleton_left_column)).fetchToEmail;

            Loggers.debug("PEOPLE FETCH TO EMAIL CONTENT:" + peopleFetchToEmail1);

            fetchToEmailSetLeftSidebar(peopleFetchToEmail1);
            fetchToEmailSetRightSidebar("&nbsp;");
        }

    }


    /**
     * @param args
     */
    protected void fetchToEmail(final Object... args) {
        try {
            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + WALL_SUBIT_FROM_EMAIL);

            displayNone($$(ORGANIZE_SECTION, document));
            $$(ORGANIZE_SECTION, document).getParentNode().removeChild($$(ORGANIZE_SECTION, document));

            fetchToEmail = HTMLDocParser.convertNodeToHtml($$(WALL_SUBMIT_WIDGET, document));

        } catch (final TransformerException e) {
            Loggers.EXCEPTION.error("", e);
        } catch (final SAXException e) {
            Loggers.EXCEPTION.error("", e);
        } catch (final IOException e) {
            Loggers.EXCEPTION.error("", e);
        }
    }


    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(WallWidgetIds.wallSubmit), EventType.CLICK.toString(), new EventListener() {

            private HumanId myrequestedProfile = requestedProfile;
            private HumanId mycurrUserAsVisitor = currUserAsVisitor;

            @Override
            public void handleEvent(final Event evt_) {

                if (wallAppend.validate() == 0) {
                    if (!wallAppend.getObj().equals("")) {

                        @_fix("THIRD PARAMETER WRONG! SINCE 1 AND 3 PARAMETERS ARE SAME AND BACKEND USES 1, SO FAR NO ISSUE")
                        final Return<Wall> r = DB.getHumanCrudWallLocal(true).addEntryToWall(myrequestedProfile, mycurrUserAsVisitor, new Obj<HumanId>(mycurrUserAsVisitor), wallAppend.getObj());


                        if (r.returnStatus() == 0) {
                            $$(WallWidgetIds.wallAppend).setAttribute(MarkupTag.TEXTAREA.value(), "");
                            wallAppend.setObj("");
                            LAST_WALL_ENTRY.MAP.remove(new String(myrequestedProfile.getHumanId()));

                            clear($$(WallWidgetIds.wallContent));
                            final Wall wall = DB.getHumanCrudWallLocal(true).readWall(requestedProfile, new Obj<HumanId>(currUserAsVisitor), REFRESH_SPEC).returnValue();
                            final StringBuilder b = new StringBuilder("");
                            List<Msg> wallMsgs = wall.getWallMsgs();
                            for (int i = 0, wallMsgsSize = wallMsgs.size(); i < wallMsgsSize && i < 25; i++) {
                                Msg msg = wallMsgs.get(i);
                                b.append(new UserProperty(request,
                                        $$(WallWidgetIds.wallContent),
                                        ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(msg.getMsgContent()).get(),
                                        new HumanId(msg.getMsgMetadata())) {
                                }.fetchToEmail);
                            }

                            new Thread(new Runnable() {

                                private HumanId mymyrequestedProfile = myrequestedProfile;
                                private HumanId mycurrUserAsVisitor = currUserAsVisitor;
                                private String myfetchToEmail = WallWidgetHumansWall.this.fetchToEmail;
                                private StringBuilder myb = b;
                                private Wall mywall = wall;


                                @Override
                                public void run() {
                                    final HumansNetPeople hnps = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(mymyrequestedProfile);

                                    for (final HumansNetPeople hpe : hnps.getHumansNetPeoples()) {
                                        if (!wall.getWallMutes().contains(hpe) && !hpe.getHumanId().equals(mycurrUserAsVisitor.getObj())) {
                                            SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                                    hpe.getHumanId(),
                                                    hnps.getDisplayName() + ": What is exciting lately?",
                                                    fetchToEmailSetCenter(b.toString(), myfetchToEmail));
                                            DB.getHumanCRUDHumansUnseenLocal(false).addEntry(hpe.getHumanId(), mywall.getWallId());
                                        }
                                    }

                                }
                            }).start();


                        } else {
                            $$(WallWidgetIds.wallNotice).setTextContent(r.returnMsg());
                        }

                        $$sendJS(JSCodeToSend.UpdateDocument);
                    }
                }
            }
        }, false);

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(WallWidgetIds.wallMute), EventType.CLICK.toString(), new EventListener() {

            private HumanId myrequestedProfile = requestedProfile;
            private HumanId mycurrUserAsVisitor = currUserAsVisitor;

            @Override
            public void handleEvent(final Event evt_) {

                if (DB.getHumanCrudWallLocal(true).readWall(myrequestedProfile, new Obj<HumanId>(currUserAsVisitor), REFRESH_SPEC_EMPTY).returnValueBadly().getWallMutes().contains(mycurrUserAsVisitor)) {
                    if (DB.getHumanCrudWallLocal(true).unmuteWall(myrequestedProfile, mycurrUserAsVisitor, new Obj<HumanId>(currUserAsVisitor)).returnStatus() == 0) {
                        $$displayWallAsMuted(evt_, false);
                    }

                } else {
                    if (DB.getHumanCrudWallLocal(true).muteWall(myrequestedProfile, mycurrUserAsVisitor, new Obj<HumanId>(currUserAsVisitor)).returnStatus() == 0) {
                        $$displayWallAsMuted(evt_, true);
                    }
                }
            }
        }, false);
    }
}
