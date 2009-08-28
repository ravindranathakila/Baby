/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function pkg_itsnat_w3c(pkg)
{
    pkg.W3CDOMEvent = W3CDOMEvent;
    pkg.getEventUtil = getW3CEventUtil;
    pkg.W3CSVGDocument = W3CSVGDocument;
    pkg.W3CHTMLDocument = W3CHTMLDocument;

function W3CDOMEvent(evt,listener)
{
    this.DOMStdEvent = itsnat.DOMStdEvent;
    this.DOMStdEvent(evt,listener);
    if (itsnat.Browser.isWebKit() && SafariRejectDOMNodeRemoved(evt))
        this.sendEvent = function () { }; // solo afecta a este evento (el objeto no se puede reutilizar)

    this.getTarget = function () { return this.getNativeEvent().target; };
}

function getW3CEventUtil(typeCode)
{
    switch(typeCode)
    {
        case 0: return W3CEventUtil_SINGLETON; // DOMStdEventTypeInfo.UNKNOWN_EVENT
        case 1: return W3CUIEventUtil_SINGLETON; // UI_EVENT
        case 2: return W3CMouseEventUtil_SINGLETON; // MOUSE_EVENT
        case 3: return W3CEventUtil_SINGLETON; // HTML_EVENT
        case 4: return W3CMutationEventUtil_SINGLETON; // MUTATION_EVENT
        case 5: return W3CKeyEventUtil_SINGLETON; // KEY_EVENT
    }
    return null; // ?
}

function W3CEventUtil()
{
    this.generateURL = generateURL;
    this.needBackUpEvent = needBackUpEvent;
    this.backupEvent = backupEvent;

    function generateURL(evt,itsNatDoc)
    {
        var url = "";
        url += "&itsnat_evt_target=" + itsNatDoc.getStringPathFromNode(evt.target);
        url += "&itsnat_evt_eventPhase=" + evt.eventPhase;
        url += "&itsnat_evt_timeStamp=" + evt.timeStamp;
        url += "&itsnat_evt_bubbles=" + evt.bubbles;
        url += "&itsnat_evt_cancelable=" + evt.cancelable;
        return url;
    }

    function needBackUpEvent() { return itsnat.Browser.isNetFront(); }

    function backupEvent(evtN,evtB)
    {
        evtB.native_evt = evtN; // no se necesita por ahora
        evtB.type = evtN.type;
        evtB.target = evtN.target;
        evtB.eventPhase = evtN.eventPhase;
        evtB.timeStamp = evtN.timeStamp;
        evtB.bubbles = evtN.bubbles;
        evtB.cancelable = evtN.cancelable;
    }
}

W3CEventUtil_SINGLETON = new W3CEventUtil();


function W3CUIEventUtil()
{
    this.W3CEventUtil = W3CEventUtil;
    this.W3CEventUtil();
    this.W3CEventUtil_generateURL = this.generateURL;
    this.W3CEventUtil_backupEvent = this.backupEvent;

    this.generateURL = generateURL;
    this.backupEvent = backupEvent;

    function generateURL(evt,itsNatDoc)
    {
        var url = this.W3CEventUtil_generateURL(evt,itsNatDoc);
        url += "&itsnat_evt_detail=" + evt.detail;
        return url;
    }

    function backupEvent(evtN,evtB)
    {
        this.W3CEventUtil_backupEvent(evtN,evtB);
        evtB.detail = evtN.detail;
    }
}

W3CUIEventUtil_SINGLETON = new W3CUIEventUtil();


function W3CMouseEventUtil()
{
    this.W3CUIEventUtil = W3CUIEventUtil;
    this.W3CUIEventUtil();
    this.W3CUIEventUtil_generateURL = this.generateURL;
    this.W3CUIEventUtil_backupEvent = this.backupEvent;

    this.MouseEventUtil = itsnat.MouseEventUtil;
    this.MouseEventUtil();
    this.MouseEventUtil_generateURL = this.generateURL;
    this.MouseEventUtil_backupEvent = this.backupEvent;

    this.generateURL = generateURL;
    this.backupEvent = backupEvent;

    function generateURL(evt,itsNatDoc)
    {
        var url = this.W3CUIEventUtil_generateURL(evt,itsNatDoc);
        url += this.MouseEventUtil_generateURL(evt,itsNatDoc);

        url += "&itsnat_evt_metaKey=" + evt.metaKey;
        url += "&itsnat_evt_relatedTarget=" + itsNatDoc.getStringPathFromNode(evt.relatedTarget);

        return url;
    }

    function backupEvent(evtN,evtB)
    {
        this.W3CUIEventUtil_backupEvent(evtN,evtB);
        this.MouseEventUtil_backupEvent(evtN,evtB);

        evtB.metaKey = evtN.metaKey;
        evtB.relatedTarget = evtN.relatedTarget;
    }
}

W3CMouseEventUtil_SINGLETON = new W3CMouseEventUtil();

function W3CMutationEventUtil()
{
    this.W3CEventUtil = W3CEventUtil;
    this.W3CEventUtil();

    this.W3CEventUtil_generateURL = this.generateURL;
    this.generateURL = generateURL;
    // backupEvent no por ahora, NetFront no tiene mutation events

    function generateURL(evt,itsNatDoc)
    {
        var url = this.W3CEventUtil_generateURL(evt,itsNatDoc);

        var relatedNodePath;
        var newValue = evt.newValue;
        var type = evt.type;
        if (type == "DOMAttrModified")
        {
            // relatedNode es el atributo (Attr) y target es el elemento propietario
            switch(evt.attrChange)
            {
                case 1: // MutationEvent.MODIFICATION
                    relatedNodePath = itsNatDoc.getStringPathFromNode(evt.relatedNode); // existe en el servidor
                    break;
                case 2: // MutationEvent.ADDITION
                    relatedNodePath = null; // como es nuevo no esta en el servidor
                    break;
                case 3: // MutationEvent.REMOVAL
                    // El atributo esta ya fuera del documento y del elemento (relatedNode.ownerElement es null) por lo que getStringPathFromNode no funcionaria
                    // queremos que el servidor obtenga el atributo que alli esta todavia en el documento
                    relatedNodePath = itsNatDoc.getAttrPathString(evt.relatedNode,evt.target);
                    break;
            }
        }
        else if (type == "DOMNodeInserted")
        {
            var newNode = evt.target;
            // Truco para tener algo parecido al outerHTML en FireFox http://www.webdeveloper.com/forum/showthread.php?t=67841
            var nodeTmp = document.createElement("div");
            nodeTmp.appendChild(newNode.cloneNode(true));
            newValue = nodeTmp.innerHTML; // Lo de dentro del <div>

            relatedNodePath = itsNatDoc.getStringPathFromNode(evt.relatedNode); // El nodo padre

            // El path del nodo de referencia en el servidor es el que ocupa el nodo recien insertado (el target)
            // El nodo de referencia puede ser un nodo texto y en el path del nuevo
            // nodo no esta el sufijo [t], se lo indicamos al servidor si es necesario
            var refChildPath;
            var refChild = evt.target.nextSibling;
            if (refChild != null)
            {
                refChildPath = itsNatDoc.getStringPathFromNode(evt.target);
                if (refChild.nodeType == 3) // Node.TEXT_NODE
                    refChildPath += "[t]";
            }
            else refChildPath = null; // ha sido un appendChild

            url += "&itsnat_evt_refChild=" + refChildPath;
        }
        else relatedNodePath = itsNatDoc.getStringPathFromNode(evt.relatedNode);

        // El evento "DOMNodeRemoved" se procesa antes de que se haya quitado
        // de forma efectiva el nodo, por lo que al servidor le llegara el path del nodo a quitar (el target)

        url += "&itsnat_evt_relatedNode=" + relatedNodePath;
        url += "&itsnat_evt_prevValue=" + encodeURIComponent(evt.prevValue);
        url += "&itsnat_evt_newValue=" + encodeURIComponent(newValue);
        url += "&itsnat_evt_attrName=" + evt.attrName;
        url += "&itsnat_evt_attrChange=" + evt.attrChange;

        return url;
    }
}

W3CMutationEventUtil_SINGLETON = new W3CMutationEventUtil();


function W3CKeyEventUtil()
{
    this.W3CUIEventUtil = W3CUIEventUtil;
    this.W3CUIEventUtil();

    this.W3CUIEventUtil_generateURL = this.generateURL;
    this.generateURL = generateURL;
    this.W3CUIEventUtil_backupEvent = this.backupEvent;
    this.backupEvent = backupEvent;

    function generateURL(evt,itsNatDoc)
    {
        var url = this.W3CUIEventUtil_generateURL(evt,itsNatDoc);

        var charCode = evt.charCode;
        if (typeof charCode == "undefined") charCode = 0; // Opera y BlackBerry no tienen

        if ((charCode == 0) && evt.itsnat_charCode) charCode = evt.itsnat_charCode; // WebKit y BlackBerry
        url += "&itsnat_evt_charCode=" + charCode;

        if (itsnat.Browser.isWebKit()||itsnat.Browser.isBlackBerry())
        {
            url += "&itsnat_evt_keyIdentifier=" + encodeURIComponent(evt.keyIdentifier);
            url += "&itsnat_evt_keyLocation=" + evt.keyLocation;
        }

        url += "&itsnat_evt_altKey=" + evt.altKey;
        url += "&itsnat_evt_ctrlKey=" + evt.ctrlKey;
        url += "&itsnat_evt_metaKey=" + evt.metaKey;
        url += "&itsnat_evt_shiftKey=" + evt.shiftKey;
        url += "&itsnat_evt_keyCode=" + evt.keyCode;

        return url;
    }

    function backupEvent(evtN,evtB)
    {
        this.W3CUIEventUtil_backupEvent(evtN,evtB);

        evtB.altKey = evtN.altKey;
        evtB.ctrlKey = evtN.ctrlKey;
        evtB.metaKey = evtN.metaKey;
        evtB.shiftKey = evtN.shiftKey;
        evtB.keyCode = evtN.keyCode;
        evtB.charCode = evtN.charCode;
        // Solo NetFront por ahora, no necesitamos memorizar otros atributos
    }
}

W3CKeyEventUtil_SINGLETON = new W3CKeyEventUtil();

function SafariRejectDOMNodeRemoved(evt)
{
    // Safari 3.0 y 3.1 emite dos eventos seguidos DOMNodeRemoved, evitamos el segundo
    // http://lists.webkit.org/pipermail/webkit-dev/2007-July/002067.html
    if (evt.type != "DOMNodeRemoved") return false;

    var removed = evt.target;
    if (!removed.itsnat_safari_removed)
    {
        removed.itsnat_safari_removed = true;
        return false; // primero
    }
    else
    {
        delete removed.itsnat_safari_removed;
        return true; // segundo, rechazado!
    }
}

function W3CDocument()
{
    this.addDOMEventListener2 = addDOMEventListener2;
    this.removeDOMEventListener2 = removeDOMEventListener2;
    this.addRemCtrlUnloadListener = addRemCtrlUnloadListener;
    this.dispatchEvent = dispatchEvent;
    this.dispatchEventNetFront2 = dispatchEventNetFront2;

    function addDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        if (itsnat.Browser.isOperaMini() || itsnat.Browser.isAndroid())
        {
            // Opera Mini: al menos 'change' en select y 'click' en input. Android: blur en <input type=text>.
            var elem = node; if (elem == window) elem = document.body;
            if (elem.hasAttribute && !elem.hasAttribute("on" + type))
                elem.setAttribute("on" + type,"");
        }
        var w3cHandler = function(evt)
        {
            arguments.callee.listenerWrapper.dispatchEvent(evt);
        };
        w3cHandler.listenerWrapper = listenerWrapper;
        listenerWrapper.w3cHander = w3cHandler;
        node.addEventListener(type,w3cHandler,useCapture);
    }

    function removeDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        node.removeEventListener(type,listenerWrapper.w3cHander,useCapture);
    }

    function addRemCtrlUnloadListener(listener)
    {
        window.addEventListener("unload",listener,false);
    }

    function dispatchEvent(node,type,evt)
    {
        return node.dispatchEvent(evt);
    }

    function dispatchEventNetFront2(idObj,evt,captureIds,atTargetIds,bubbleIds)
    {
        var node = this.getNodeFromPath(idObj);
        return this.dispatchEventNetFront(node,evt,captureIds,atTargetIds,bubbleIds); // Se carga dinamicamente antes de usar
    }
}

function W3CSVGDocument(sessionToken,sessionId,docId,servletPath,usePost,remView,errorMode,useCache,numScripts)
{
    this.Document = itsnat.Document;
    this.Document(sessionToken,sessionId,docId,servletPath,usePost,remView,errorMode,useCache,numScripts,document.documentElement);

    this.W3CDocument = W3CDocument;
    this.W3CDocument();
}

function W3CHTMLDocument(sessionToken,sessionId,docId,servletPath,usePost,remView,errorMode,useCache,numScripts)
{
    this.HTMLDocument = itsnat.HTMLDocument;
    this.HTMLDocument(sessionToken,sessionId,docId,servletPath,usePost,remView,errorMode,useCache,numScripts,getBody());

    this.W3CDocument = W3CDocument;
    this.W3CDocument();

    this.HTMLDocument_setAttribute = this.setAttribute;
    this.setAttribute = setAttribute;

    this.HTMLDocument_removeAttribute = this.removeAttribute;
    this.removeAttribute = removeAttribute;

    function getBody()
    {
        var body;
        if (!document.body) // En Safari y XHTML, document NO es HTMLDocument (los elementos contenidos si son HTML DOM)
        {
            body = document.documentElement.lastChild;
            while( body.nodeType != 1) body = body.previousSibling; // Node.ELEMENT_NODE == 1
        }
        else body = document.body;
        return body;
    }

    function setAttribute(elem,name,value)
    {
        if (itsnat.Browser.isBlackBerry()) // Para evitar que las dos primeras veces se ignoren visualmente (curioso)
        {
          elem.setAttribute(name,value);
          var style = elem.style;
          var oldDisp = style.display;
          style.display = "none"; // "Refresca" el elemento.
          style.display = oldDisp;
        }
        else this.HTMLDocument_setAttribute(elem,name,value);
    }

    function removeAttribute(elem,name)
    {
        if (itsnat.Browser.isBlackBerry()) // Ver setAttribute
        {
          if (name == "style") elem.style.cssText = "";
          else
          {
              elem.removeAttribute(name);
              var style = elem.style;
              var oldDisp = style.display;
              style.display = "none";
              style.display = oldDisp;
          }
        }
        else this.HTMLDocument_removeAttribute(elem,name);
    }
}


} // pkg_itsnat_w3c

