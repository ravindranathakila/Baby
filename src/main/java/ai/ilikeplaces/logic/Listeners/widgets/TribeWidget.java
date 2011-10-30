package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/20/11
 * Time: 8:50 PM
 */
public class TribeWidget extends AbstractWidgetListener<TribeWidgetCriteria> {

    public TribeWidget(final ItsNatServletRequest request__, final TribeWidgetCriteria tribeWidgetCriteria, final Element appendToElement__) {
        super(request__, Controller.Page.TribeHome, tribeWidgetCriteria, appendToElement__);
    }


    /**
     * Use this only in conjunction with {@link #AbstractWidgetListener(org.itsnat.core.ItsNatServletRequest, ai.ilikeplaces.servlets.Controller.Page, Object, org.w3c.dom.Element)}
     * GENERIC constructor.
     *
     * @param tribeWidgetCriteria
     */
    @Override
    protected void init(final TribeWidgetCriteria tribeWidgetCriteria) {
        new WallWidgetTribe(request, new WallWidgetTribeCriteria().setHumanId(criteria.getHumanId()).setTribeId(criteria.getTribeId().getObj()), $$(Controller.Page.tribeHomeWall));
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        AddRemoveVisitors:
        {
            final Tribe tribe = DB.getHumanCRUDTribeLocal(false).getTribe(criteria.getHumanId(), criteria.getTribeId(), true).returnValueBadly();

            final HumansNetPeople user = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(criteria.getHumanId());

            new MemberHandler<HumansFriend, List<HumansFriend>, Return<Tribe>>(
                    request, $$(Controller.Page.tribeHomeMembers),
                    user,
                    user.getHumansNetPeoples(),
                    tribe.getTribeMembers(),
                    new Save<Return<Tribe>>() {

                        final long mytribeId = tribe.getTribeId();
                        final String myeventLink = "#";

                        @Override
                        public Return<Tribe> save(final HumanId humanId, final HumansFriend humansFriend) {

                            final Return<Tribe> returnVal = DB.getHumanCRUDTribeLocal(false).addToTribe(humanId, new VLong(mytribeId));

                            DB.getHumanCRUDTribeLocal(false).addToTribe(humanId, new VLong(mytribeId));
                            if (returnVal.returnStatus() == 0) {
                                SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                        humanId.getObj(),
                                        ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                new HumanId(humanId.getHumanId()),
                                                humansFriend.getHumanId(),
                                                ElementComposer.compose($$(MarkupTag.A))
                                                        .$ElementSetHref(myeventLink)
                                                        .$ElementSetText("I added you as an member of tribe " + returnVal.returnValue().getTribeName())
                                                        .get()
                                        ));
                            }
                            return returnVal;

                        }
                    },
                    new Save<Return<Tribe>>() {

                        final long mytribeId = tribe.getTribeId();


                        @Override
                        public Return<Tribe> save(final HumanId humanId, final HumansFriend humansFriend) {
                            final Return<Tribe> returnVal = DB.getHumanCRUDTribeLocal(false).removeFromTribe(humanId, new VLong(mytribeId));
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
    }
}
