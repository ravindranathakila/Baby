package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
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
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public PrivateLocationView(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final String humanId__, final long privateLocationId__) {
        super(itsNatDocument__, Page.PrivateLocationView, appendToElement__, humanId__, privateLocationId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        final String humanId = (String) initArgs[0];
        final long privateLocationId = (Long) initArgs[1];

        final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).rPrivateLocation(humanId, privateLocationId);


        LoggerFactory.getLogger(PrivateLocationView.class.getName()).debug(r.toString());

        if (r.returnStatus() == 0) {
            LoggerFactory.getLogger(PrivateLocationView.class.getName()).debug("Setting values");
            $$(privateLocationViewName).setTextContent("Location Name: "+r.returnValue().getPrivateLocationName());
            $$(privateLocationViewInfo).setTextContent("Location Info: "+r.returnValue().getPrivateLocationInfo());
        } else {
            LoggerFactory.getLogger(PrivateLocationView.class.getName()).debug("Error");
            $$(privateLocationViewNotice).setTextContent("Alert: " +r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        //No events as this is just a view widget/ Edit button???
    }
}