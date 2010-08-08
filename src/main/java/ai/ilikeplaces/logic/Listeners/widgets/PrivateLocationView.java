package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateLocationView extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(PrivateLocationView.class.getName());

    /**
     * @param privateLocationId__
     * @param appendToElement__
     */
    public PrivateLocationView(final ItsNatServletRequest request__,  final Element appendToElement__, final String humanId__, final long privateLocationId__) {
        super(request__, Page.PrivateLocationView, appendToElement__, humanId__, privateLocationId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        final HumanId humanId = new HumanId((String) initArgs[0]).getSelfAsValid();
        final long privateLocationId = (Long) initArgs[1];

        final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationAsViewer(humanId, privateLocationId);


        LoggerFactory.getLogger(PrivateLocationView.class.getName()).debug(r.toString());

        if (r.returnStatus() == 0) {
            LoggerFactory.getLogger(PrivateLocationView.class.getName()).debug("Setting values");
            $$(privateLocationViewName).setTextContent(r.returnValue().getPrivateLocationName());
            $$(privateLocationViewInfo).setTextContent(r.returnValue().getPrivateLocationInfo());
            new Button(request, $$(privateLocationViewLink), "Link to " + r.returnValue().getPrivateLocationName(), false, r.returnValue()) {
                PrivateLocation privateLocation = null;

                @Override
                protected void init(final Object... initArgs) {
                    privateLocation = (PrivateLocation) (((Object[]) initArgs[2])[0]);
                    SetLocationLink:
                    {
                        setLink:
                        {
                            $$(GenericButtonLink).setAttribute(MarkupTag.A.href(),
                                    new Parameter(Organize.getURL())
                                            .append(DocOrganizeCategory, 2, true)
                                            .append(DocOrganizeLocation, privateLocation.getPrivateLocationId())
                                            .get());
                        }
                        setImage:
                        {
                            $$(GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.globalConfig.getString(RBGet.url_CDN_STATIC) + "arrow-right.gif");
                        }
                    }
                }
            };
            SetEventList:
            {
                for (final PrivateEvent pe : r.returnValue().getPrivateEvents())
                    if (DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventIsViewer(humanId, pe.getPrivateEventId()).returnValue()) {
                        $$(privateLocationViewEventList).appendChild(
                                ElementComposer.compose($$(MarkupTag.LI))
                                        .wrapThis(
                                                ElementComposer.compose($$(MarkupTag.A))
                                                        .$ElementSetText(pe.getPrivateEventName())
                                                        .$ElementSetHref(
                                                                new Parameter(Organize.getURL())
                                                                        .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                                                                        .append(DocOrganizeLocation, r.returnValue().getPrivateLocationId())
                                                                        .append(DocOrganizeEvent, pe.getPrivateEventId())
                                                                        .get()
                                                        )
                                                        .get()
                                        )
                                        .get());
                    }
            }
        } else {
            LoggerFactory.getLogger(PrivateLocationView.class.getName()).debug("Error");
            $$(privateLocationViewNotice).setTextContent("Alert: " + r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        //No events as this is just a view widget/ Edit button???
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}