
package ai.ilikeplaces.depricated;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.inc.ItsNatFreeInclude;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.html.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;


/**
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Deprecated
class Login implements EventListener {

    private ItsNatHTMLDocument itsNatHTMLDocument;
    private ItsNatFreeInclude itsNatFreeInclude;
    private ItsNatDocument itsNatDocument;
    private EventTarget et;
    public Login(ItsNatDocument itsNatDocument) {
        this.itsNatDocument = itsNatDocument;
        ItsNatComponentManager incm = itsNatDocument.getItsNatComponentManager();
        Null(incm);
        ItsNatFreeInclude newWay = incm.createItsNatFreeInclude(itsNatDocument.getDocument().getElementById("tempId"), null);
        //        feaShowDoc.getItsNatFreeInclude().includeFragment(panelName, false);

        ItsNatComponent inc = incm.createItsNatComponentById("tempId", "freeInclude", null);
        Null(inc);
        ItsNatFreeInclude infi = (ItsNatFreeInclude) inc;
        Null(infi);
        //newWay.includeFragment("include", true);
        //infi.includeFragment("include2", true);
        //infi.includeFragment("include3", true);

        ItsNatServlet inservlet = itsNatDocument.getItsNatDocumentTemplate().getItsNatServlet();
        ItsNatHTMLDocFragmentTemplate inhdft = (ItsNatHTMLDocFragmentTemplate) inservlet.getItsNatDocFragmentTemplate("include2");
        DocumentFragment df = inhdft.loadDocumentFragmentBody(itsNatDocument);
        Document doc = itsNatDocument.getDocument();
        Element ele = doc.getElementById("tempId");
        ele.appendChild(df);
        Element ele2 = doc.getElementById("temp");
        ele2.appendChild(doc.createTextNode("Yay!"));
        itsNatDocument.addEventListener((EventTarget) ele2, "click", this, true);
        this.et = (EventTarget) ele2;

//        ItsNatModalLayer inml = incm.createItsNatModalLayer(ele2, true, new Float(1), null, null);
//        int modalint = inml.getZIndex();
//
//        ele2.setAttribute("style", "z-index:"+modalint);
        


        ItsNatServlet ins = itsNatDocument.getItsNatDocumentTemplate().getItsNatServlet();
        ItsNatHTMLDocFragmentTemplate indft = (ItsNatHTMLDocFragmentTemplate) ins.getItsNatDocFragmentTemplate("include3");
        
    }
    @Override
    public void handleEvent(Event e){
        if(e.getTarget() == et ){
            HTMLDocument hd = ((ItsNatHTMLDocument)itsNatDocument).getHTMLDocument();
            hd.getBody().appendChild(hd.getBody().appendChild(hd.createTextNode("Clicked:"+System.currentTimeMillis())));
            ItsNatServlet inservlet = itsNatDocument.getItsNatDocumentTemplate().getItsNatServlet();
            ItsNatHTMLDocFragmentTemplate inhdft = (ItsNatHTMLDocFragmentTemplate) inservlet.getItsNatDocFragmentTemplate("include2");
            DocumentFragment df = inhdft.loadDocumentFragmentBody(itsNatDocument);
            Document doc = itsNatDocument.getDocument();
            Element ele = doc.getElementById("tempId");
            ele.appendChild(df);
            Element ele2 = doc.getElementById("temp");
            ele2.appendChild(doc.createTextNode("Yay!"));
        }
    }
    private void Null(Object obj){
        if(obj == null ){
            throw new java.lang.NullPointerException("NULL OBJECT RECIEVED");
        }
    }
}
