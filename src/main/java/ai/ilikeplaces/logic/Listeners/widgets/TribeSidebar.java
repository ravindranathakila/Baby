package ai.ilikeplaces.logic.Listeners.widgets;

import ai.doc.License;
import ai.doc._ok;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.cache.SmartCache3;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_ok
public class TribeSidebar extends AbstractWidgetListener<TribeSidebarCriteria> {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS STATIC --------------------------

    public static final SmartCache3<Long, Tribe, HumanId> TRIBE_BASIC_INFO_CACHE = new SmartCache3<Long, Tribe, HumanId>(
            new SmartCache3.RecoverWith<Long, Tribe, HumanId>() {
                @Override
                public Tribe getValue(final Long tribeId, HumanId humanId) {

                    return DB.getHumanCRUDTribeLocal(false).getTribe(humanId, new VLong(tribeId));
                }
            }
    );

    private static final String READ_MORE = "read.more";

    // --------------------------- CONSTRUCTORS ---------------------------
    public TribeSidebar(final ItsNatServletRequest request__, final TribeSidebarCriteria tribeSidebarCriteria, final Element appendToElement__) {

        super(request__, Page.TribeSidebar, tribeSidebarCriteria, appendToElement__);
    }

// ------------------------ OVERRIDING METHODS ------------------------

    /**
     * @param tribeSidebarCriteria
     */
    @Override
    protected void init(final TribeSidebarCriteria tribeSidebarCriteria) {

        final HumanId humanId = new HumanId(tribeSidebarCriteria.getHumanId());
        final long tribeId = tribeSidebarCriteria.getTribeId();


        final Tribe tribe = TRIBE_BASIC_INFO_CACHE.get(tribeId, humanId);

        final Msg lastWallEntry = WallWidgetTribe.LAST_WALL_ENTRY.get(tribe.getTribeWall().getWallId(), humanId.getHumanId());

        final String href = new Parameter(Tribes.getURL())
                .append(DocTribesMode, DocTribesModeView, true)
                .append(DocTribesWhich, tribe.getTribeId())
                .get();

        $$(Page.tribe_sidebar_name).setTextContent(tribe.getTribeName());

        //$$(Page.tribe_sidebar_profile_photo).setAttribute(MarkupTag.IMG.title(),"Tribe Photo Url");

        if (lastWallEntry != null) {
            new UserPropertySidebar(
                    request,
                    $$(Page.tribe_sidebar_content),
                    new HumanId(lastWallEntry.getMsgMetadata()),
                    lastWallEntry,
                    href) {
                private Msg mylastWallEntry;
                private String myhref;

                protected void init(final Object... initArgs) {

                    mylastWallEntry = (Msg) ((Object[]) initArgs[1])[0];
                    myhref = (String) ((Object[]) initArgs[1])[1];

                    $$displayBlock($$(UserPropertySidebarIds.user_property_sidebar_talk));
                    $$displayNone($$(UserPropertySidebarIds.user_property_sidebar_name_section));


                    String msgContent = lastWallEntry.getMsgContent();

                    TrimMessageContentForReadabilityOnSidebar:
                    {
                        final int length = msgContent.length();
                        if (40 < length) {
                            msgContent = msgContent.substring(0, 40) + RBGet.gui().getString(READ_MORE);
                        }
                    }

                    Element commentHref = ElementComposer.compose($$(MarkupTag.A)).$ElementSetText(msgContent).$ElementSetHref(myhref).get();
                    $$(UserPropertySidebarIds.user_property_sidebar_content).appendChild(commentHref);
                }

                @Override
                protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

                    itsNatHTMLDocument_.addEventListener((EventTarget) $$(UserPropertySidebarIds.user_property_sidebar_engage), EventType.CLICK.toString(), new EventListener() {
                        @Override
                        public void handleEvent(final Event evt_) {

                            $$sendJS(JSCodeToSend.redirectPageWithURL(myhref));
                        }
                    }, false);
                }
            };
        } else {
            new UserPropertySidebar(
                    request,
                    $$(Page.tribe_sidebar_content),
                    humanId,
                    "",
                    href) {
                private String myhref;

                protected void init(final Object... initArgs) {

                    myhref = (String) ((Object[]) initArgs[1])[1];

                    $$displayBlock($$(UserPropertySidebarIds.user_property_sidebar_talk));
                    $$displayNone($$(UserPropertySidebarIds.user_property_sidebar_name_section));

                    Element commentHref = ElementComposer.compose($$(MarkupTag.A)).$ElementSetText("").$ElementSetHref(myhref).get();
                    $$(UserPropertySidebarIds.user_property_sidebar_content).appendChild(commentHref);
                }

                @Override
                protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

                    itsNatHTMLDocument_.addEventListener((EventTarget) $$(UserPropertySidebarIds.user_property_sidebar_engage), EventType.CLICK.toString(), new EventListener() {
                        @Override
                        public void handleEvent(final Event evt_) {

                            $$sendJS(JSCodeToSend.redirectPageWithURL(myhref));
                        }
                    }, false);
                }
            };
        }
    }

    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

    }
}
