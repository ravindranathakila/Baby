package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.jpa.RefreshSpec;
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

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jun 17, 2010
 * Time: 12:17:36 AM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class WallWidgetHumansWall extends WallWidget {

    private static final String WALL_SUBIT_FROM_EMAIL = "ai/ilikeplaces/widgets/WallSubmitFromEmail.xhtml";
    private static final RefreshSpec REFRESH_SPEC = new RefreshSpec("wallMsgs","wallMutes");
    private static final RefreshSpec REFRESH_SPEC_EMPTY = new RefreshSpec();


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

        fetchToEmail();

        final Wall wall = DB.getHumanCrudWallLocal(true).readWall(requestedProfile, new Obj<HumanId>(currUserAsVisitor), REFRESH_SPEC).returnValueBadly();

        for (final Msg msg : wall.getWallMsgs()) {
            new UserProperty(request, $$(Controller.Page.wallContent), new HumanId(msg.getMsgMetadata())) {
                protected void init(final Object... initArgs) {
                    $$(Controller.Page.user_property_content).setTextContent(msg.getMsgContent());
                }
            };
        }

        if (wall.getWallMutes().contains(currUserAsVisitor)) {
            $$(Controller.Page.wallMute).setTextContent(WallWidget.LISTEN);
        } else {
            $$(Controller.Page.wallMute).setTextContent(WallWidget.MUTE);
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


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.wallSubmit), EventType.CLICK.toString(), new EventListener() {

            private HumanId myrequestedProfile = requestedProfile;
            private HumanId mycurrUserAsVisitor = currUserAsVisitor;

            @Override
            public void handleEvent(final Event evt_) {

                if (wallAppend.validate() == 0) {
                    if (!wallAppend.getObj().equals("")) {

                        final Return<Wall> r = DB.getHumanCrudWallLocal(true).addEntryToWall(myrequestedProfile, currUserAsVisitor, new Obj<HumanId>(mycurrUserAsVisitor), wallAppend.getObj());


                        if (r.returnStatus() == 0) {
                            $$(Controller.Page.wallAppend).setAttribute(MarkupTag.TEXTAREA.value(), "");
                            wallAppend.setObj("");

                            clear($$(Controller.Page.wallContent));
                            final Wall wall = DB.getHumanCrudWallLocal(true).readWall(requestedProfile, new Obj<HumanId>(currUserAsVisitor), REFRESH_SPEC).returnValue();
                            final StringBuilder b = new StringBuilder("");
                            for (final Msg msg : wall.getWallMsgs()) {
                                b.append(new UserProperty(request,
                                        $$(Controller.Page.wallContent),
                                        ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(msg.getMsgContent()).get(),
                                        new HumanId(msg.getMsgMetadata())) {
                                }.fetchToEmail);
                            }

                            final HumansNetPeople hnps = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(myrequestedProfile);

                            for (final HumansNetPeople hpe : hnps.getHumansNetPeoples()) {
                                if (!wall.getWallMutes().contains(hpe)) {
                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(hpe.getHumanId(), hnps.getDisplayName(), fetchToEmail + b.toString());
                                }
                            }
                        } else {
                            $$(Controller.Page.wallNotice).setTextContent(r.returnMsg());
                        }
                    }
                }
            }
        }, false);

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.wallMute), EventType.CLICK.toString(), new EventListener() {

            private HumanId myrequestedProfile = requestedProfile;
            private HumanId mycurrUserAsVisitor = currUserAsVisitor;

            @Override
            public void handleEvent(final Event evt_) {

                if (DB.getHumanCrudWallLocal(true).readWall(myrequestedProfile, new Obj<HumanId>(currUserAsVisitor), REFRESH_SPEC_EMPTY).returnValueBadly().getWallMutes().contains(mycurrUserAsVisitor)) {
                    if (DB.getHumanCrudWallLocal(true).unmuteWall(myrequestedProfile, mycurrUserAsVisitor, new Obj<HumanId>(currUserAsVisitor)).returnStatus() == 0) {
                        $$(evt_).setTextContent(WallWidget.MUTE);
                    }

                } else {
                    if (DB.getHumanCrudWallLocal(true).muteWall(myrequestedProfile, mycurrUserAsVisitor, new Obj<HumanId>(currUserAsVisitor)).returnStatus() == 0) {
                        $$(evt_).setTextContent(WallWidget.LISTEN);
                    }
                }
            }
        }, false);
    }
}