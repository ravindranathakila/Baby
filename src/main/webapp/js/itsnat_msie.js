/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function pkg_itsnat_msie(pkg)
{
    pkg.MSIEDOMEvent = MSIEDOMEvent;
    pkg.getEventUtil = getMSIEEventUtil;
    pkg.MSIEHTMLDocument = MSIEHTMLDocument;
    pkg.MSIEDOMListenerList = MSIEDOMListenerList;

function MSIEDOMEvent(evt,listener)
{
    this.DOMStdEvent = itsnat.DOMStdEvent;
    this.DOMStdEvent(evt,listener);

    this.getTarget = function () { return this.getNativeEvent().srcElement; };
}

function getMSIEEventUtil(typeCode)
{
    switch(typeCode)
    {
        case 0: // DOMStdEventTypeInfo.UNKNOWN_EVENT
            return MSIEEventUtil_SINGLETON;
        case 1: // UI_EVENT
            return MSIEUIEventUtil_SINGLETON;
        case 2: // MOUSE_EVENT
            return MSIEMouseEventUtil_SINGLETON;
        case 3: // HTML_EVENT
            return MSIEEventUtil_SINGLETON;
        case 4: // MUTATION_EVENT
            return null; // Not supported
        case 5: // KEY_EVENT
            return MSIEKeyEventUtil_SINGLETON;
    }
    return null; // ?
}

function MSIEEventUtil()
{
    this.generateURL = generateURL;
    this.needBackUpEvent = needBackUpEvent;
    this.backupEvent = backupEvent;

    function generateURL(evt,itsNatDoc)
    {
        var url = "";
        url += "&itsnat_evt_srcElement=" + itsNatDoc.getStringPathFromNode(evt.srcElement);
        url += "&itsnat_evt_cancelBubble=" + evt.cancelBubble;
        url += "&itsnat_evt_returnValue=" + evt.returnValue;
        return url;
    }

    function needBackUpEvent() { return true; }

    function backupEvent(evtN,evtB)
    {
        // Para evitar el problema de acceder en modo ASYNC_HOLD
        // al evento original tras haberse encolado y terminado el proceso del evento por el navegador (da error en MSIE)

        evtB.native_evt = evtN; // no se necesita por ahora
        evtB.type = evtN.type;
        evtB.srcElement = evtN.srcElement;
        evtB.cancelBubble = evtN.cancelBubble;
        evtB.returnValue = evtN.returnValue;
        evtB.keyCode = evtN.keyCode;
    }
}

MSIEEventUtil_SINGLETON = new MSIEEventUtil();

function MSIEUIEventUtil()
{
    this.MSIEEventUtil = MSIEEventUtil;
    this.MSIEEventUtil();
}

MSIEUIEventUtil_SINGLETON = new MSIEUIEventUtil();

function MSIEMouseEventUtil()
{
    this.MSIEUIEventUtil = MSIEUIEventUtil;
    this.MSIEUIEventUtil();
    this.MSIEUIEventUtil_generateURL = this.generateURL;
    this.MSIEUIEventUtil_backupEvent = this.backupEvent;

    this.MouseEventUtil = itsnat.MouseEventUtil;
    this.MouseEventUtil();
    this.MouseEventUtil_generateURL = this.generateURL;
    this.MouseEventUtil_backupEvent = this.backupEvent;

    this.generateURL = generateURL;
    this.backupEvent = backupEvent;

    function generateURL(evt,itsNatDoc)
    {
        var url = this.MSIEUIEventUtil_generateURL(evt,itsNatDoc);
        url += this.MouseEventUtil_generateURL(evt,itsNatDoc);
        url += "&itsnat_evt_fromElement=" + itsNatDoc.getStringPathFromNode(evt.fromElement);
        url += "&itsnat_evt_toElement=" + itsNatDoc.getStringPathFromNode(evt.toElement);
        return url;
    }

    function backupEvent(evtN,evtB)
    {
        this.MSIEUIEventUtil_backupEvent(evtN,evtB);
        this.MouseEventUtil_backupEvent(evtN,evtB);

        evtB.fromElement = evtN.fromElement;
        evtB.toElement = evtN.toElement;
    }
}

MSIEMouseEventUtil_SINGLETON = new MSIEMouseEventUtil();

function MSIEKeyEventUtil()
{
    this.MSIEUIEventUtil = MSIEUIEventUtil;
    this.MSIEUIEventUtil();

    this.MSIEUIEventUtil_generateURL = this.generateURL;
    this.generateURL = generateURL;

    function generateURL(evt,itsNatDoc)
    {
        var url = this.MSIEUIEventUtil_generateURL(evt,itsNatDoc);

        url += "&itsnat_evt_altKey=" + evt.altKey;
        url += "&itsnat_evt_keyCode=" + evt.keyCode;
        url += "&itsnat_evt_ctrlKey=" + evt.ctrlKey;
        url += "&itsnat_evt_shiftKey=" + evt.shiftKey;

        return url;
    }
}

MSIEKeyEventUtil_SINGLETON = new MSIEKeyEventUtil();

function MSIEDOMListenerList(type,currentTarget,itsNatDoc)
{
    this.List = itsnat.List;
    this.List();

    this.dispatchEvent = dispatchEvent;
    this.dispatchEventLocal = dispatchEventLocal;

    // Attrs
    this.type = type;
    this.currentTarget = currentTarget;
    this.itsNatDoc = itsNatDoc;


    function dispatchEventLocal(evt,capture)
    {
        // El handler inline ya lo ejecuta el navegador
        if (this.isEmpty()) return;
        var listeners = this.getArrayCopy(); // Asi permitimos cambios concurrentes
        for(var i = 0; i < listeners.length; i++) // Del primero al ultimo igual que en W3C
        {
            var listenerWrapper = listeners[i];
            if (capture != listenerWrapper.isUseCapture()) continue;
            listenerWrapper.dispatchEvent(evt);
        }
    }

    function dispatchEvent(evt)
    {
        // returnValue es la unica propiedad que se mantiene entre llamadas a listeners
        // Por defecto returnValue es undefined, cualquier otro valor suponemos "ya capturado"
        // El valor true es el valor por defecto deseado aunque hace que beforeunload (caso window) nos "pregunte" si queremos salir,
        // por eso no capturamos con target window y porque no tiene sentido
        if (evt.srcElement && (typeof evt.returnValue == "undefined")) // Si srcElement no definido el target original es window
        {
            evt.returnValue = true;
            this.itsNatDoc.dispatchEventCapture(evt);
            if (evt.itsnat_stopPropagation)
                return;
        }
        this.dispatchEventLocal(evt,false);
        // No retornar "return evt.returnValue" pues se superpone al return del posible handler inline (pues se ejecuta antes)
        // pues este metodo fue registrado via attachEvent y el returnValue cuenta pero con menor prioridad que si lo retornamos (return) a modo de inline handler
        // y queremos que mande el inline por ejemplo para hacer onclick="return false" en los falsos links AJAX "Google friendly"
        // Yo creo que esta mal: http://msdn.microsoft.com/en-us/library/ms534372(VS.85).aspx
    }
}

function MSIEHTMLDocument(sessionToken,sessionId,docId,servletPath,usePost,remView,errorMode,useCache,numScripts)
{
    this.HTMLDocument = itsnat.HTMLDocument;
    this.HTMLDocument(sessionToken,sessionId,docId,servletPath,usePost,remView,errorMode,useCache,numScripts,document.body);

    this.getEventListenerRegistry = getEventListenerRegistry;
    this.cleanEventListenerRegistry = cleanEventListenerRegistry;
    this.addDOMEventListener2 = addDOMEventListener2;
    this.removeDOMEventListener2 = removeDOMEventListener2;
    this.addRemCtrlUnloadListener = addRemCtrlUnloadListener;
    this.dispatchEvent = dispatchEvent;
    this.dispatchEventCapture = dispatchEventCapture;

    function getEventListenerRegistry(node,type,create)
    {
        if (node.itsNatListeners)
        {
            var func = node.itsNatListeners[type];
            if (func) return func;
        }
        if (!create) return null;

        if (!node.itsNatListeners) node.itsNatListeners = new Object();
        func = function (evt) { return arguments.callee.listeners.dispatchEvent(evt); };
        func.listeners = new MSIEDOMListenerList(type,node,this);
        node.itsNatListeners[type] = func;
        node.attachEvent("on" + type,func); // node tiene que ser un elemento en MSIE
        return func;
    }

    function cleanEventListenerRegistry(type,node,func)
    {
        delete node.itsNatListeners[type];
        node.detachEvent("on" + type,func);
    }

    function addDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        var func = this.getEventListenerRegistry(node,type,true);
        func.listeners.add(listenerWrapper);
    }

    function removeDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        var func = this.getEventListenerRegistry(node,type,true);
        func.listeners.remove(listenerWrapper);
        if (func.listeners.isEmpty())
            this.cleanEventListenerRegistry(type,node,func);
    }

    function addRemCtrlUnloadListener(listener)
    {
        this.addDOMEventListener([null,null,"window"],"unload","rem_ctrl_unload",listener,false,3,-1,3); // Aseguramos asi que se ejecuta el ultimo (si se registro el ultimo)
    }

    function dispatchEvent(node,type,evt) { return node.fireEvent("on" + type,evt); }

    function dispatchEventCapture(evt)
    {
        // Se espera que se despache primero el listener del elemento mas bajo
        var target = evt.srcElement; 

        var parentList = new itsnat.List();
        var parentNode = this.getParentNode(target);
        while (parentNode != null)
        {
            parentList.add(parentNode);
            parentNode = this.getParentNode(parentNode);
        }
        var size = parentList.size();
        for (var i = size - 1; i >= 0; i--) // capture
        {
            var current = parentList.get(i);
            var func = this.getEventListenerRegistry(current,evt.type,false);
            if (!func) continue;
            func.listeners.dispatchEventLocal(evt,true);
            if (evt.itsnat_stopPropagation) break;
        }
        return parentList; // util para IE Pocket
    }
}

} // pkg_itsnat_msie

