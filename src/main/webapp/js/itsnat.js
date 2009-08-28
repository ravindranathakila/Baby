/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function pkg_itsnat(pkg,browserType,browserSubType)
{
    pkg.List = List;
    pkg.Browser = new Browser(browserType,browserSubType); // SINGLETON
    pkg.MouseEventUtil = MouseEventUtil;
    pkg.DOMPathResolver = DOMPathResolver;
    pkg.EventQueue = EventQueue;
    pkg.Event = Event;
    pkg.NormalEvent = NormalEvent;
    pkg.TransportUtil = new TransportUtil(); // SINGLETON
    pkg.DOMEvent = DOMEvent;
    pkg.DOMStdEvent = DOMStdEvent;
    pkg.UserEvent = UserEvent;
    pkg.RemCtrlTimerRefreshEvent = RemCtrlTimerRefreshEvent;
    pkg.RemCtrlCometTaskRefreshEvent = RemCtrlCometTaskRefreshEvent;
    pkg.RemCtrlUnloadEvent = RemCtrlUnloadEvent;
    pkg.DOMStdEventListener = DOMStdEventListener;
    pkg.TimerEventListener = TimerEventListener;
    pkg.UserEventListener = UserEventListener;
    pkg.ContinueEventListener = ContinueEventListener;
    pkg.AsyncTaskEventListener = AsyncTaskEventListener;
    pkg.CometTaskEventListener = CometTaskEventListener;
    pkg.Document = Document;
    pkg.HTMLDocument = HTMLDocument;

function List() // mimics java.util.List y LinkedList
{
    this.add = add;
    this.remove = remove;
    this.removeByIndex = removeByIndex;
    this.removeFirst = removeFirst;
    this.addLast = addLast;
    this.get = get;
    this.isEmpty = isEmpty;
    this.size = size;
    this.getArrayCopy = getArrayCopy;

    // attributes
    this.array = new Array();

    function add(obj) { this.array[this.array.length] = obj; }  // length aumenta automaticamente
    function get(index) { return this.array[index]; }
    function isEmpty() { return (this.array.length == 0); }
    function size() { return this.array.length; }
    function removeFirst() { return this.array.shift(); }
    function addLast(obj) { return this.array.push(obj); }
    function removeByIndex(index) { this.array.splice(index,1); }
    function getArrayCopy() { return this.array.slice(0); }

    function remove(obj)
    {
        var len = this.array.length;;
        for(var i = 0; i < len; i++)
        {
            if (this.array[i] != obj) continue;
            this.removeByIndex(i);
            return true;
        }
        return false;
    }
}

function Browser(browserType,browserSubType)
{
    this.isMSIE = isMSIE;
    this.isMSIEPocket = isMSIEPocket;
    this.isWebKit = isWebKit;
    this.isNetFront = isNetFront;
    this.isW3C = isW3C;
    this.isOpera = isOpera;
    this.isOperaMini = isOperaMini;
    this.isOperaMobile9 = isOperaMobile9;
    this.isAndroid = isAndroid;
    this.isBlackBerry = isBlackBerry;

    /* UNKNOWN=0,MSIE=1,GECKO=2,WEBKIT=3,OPERA=4,NETFRONT=5,BLACKBERRY=6 */

    this.browserType = browserType;
    this.browserSubType = browserSubType;

    function isMSIE() { return this.browserType == 1; }
    function isMSIEPocket() { return this.isMSIE() && (this.browserSubType == 2); }
    function isWebKit() { return this.browserType == 3; }
    function isNetFront() { return this.browserType == 5; }
    function isBlackBerry() { return this.browserType == 6; }
    function isW3C() { return !this.isMSIE(); }
    function isOpera() { return this.browserType == 4; }
    function isOperaMini() { return this.isOpera() && (this.browserSubType == 2); }
    function isOperaMobile9() { return this.isOpera() && (this.browserSubType == 4); }
    function isAndroid() { return this.isWebKit() && (this.browserSubType == 3); }
}


function DOMPathResolver(itsNatDoc)
{
    this.getNodeDeep = getNodeDeep;
    this.getNodePath = getNodePath;
    this.getSuffix = getSuffix;
    this.getStringPathFromNode = getStringPathFromNode;
    this.getAttrPathString = getAttrPathString;
    this.getNodeFromArrayPath = getNodeFromArrayPath;
    this.getNodeFromPath = getNodeFromPath;
    this.getStringPathFromArray = getStringPathFromArray;
    this.getArrayPathFromString = getArrayPathFromString;
    this.getChildNodeFromPos = getChildNodeFromPos;
    this.getChildNodeFromStrPos = getChildNodeFromStrPos;
    this.getNodeChildPosition = getNodeChildPosition;

    this.itsNatDoc = itsNatDoc;

    function getArrayPathFromString(pathStr)
    {
        if (pathStr == null) return null;
        return pathStr.split(",");
    }

    function getChildNodeFromPos(parent,pos,isTextNode)
    {
        var childNodes = parent.childNodes;
        var node = null, currPos = 0, len = childNodes.length;
        for(var i = 0; i < len; i++)
        {
            var currNode = childNodes[i];
            var type = currNode.nodeType;
            if (currPos == pos)
                if (isTextNode)
                {
                    node = currNode;
                    break;
                }
                else if (type != 3)
                {
                    node = currNode;
                    break;
                }
                else continue;
            if (type != 3) currPos++;
        }
        return node;
    }

    function getChildNodeFromStrPos(parent,posStr)
    {
        if (posStr == "de") return document.documentElement;

        var posBracket = posStr.indexOf('[');
        if (posBracket == -1)
        {
            var pos = parseInt(posStr);
            return this.getChildNodeFromPos(parent,pos,false);
        }
        else
        {
            var pos = parseInt(posStr.substring(0,posBracket));
            if (posStr.charAt(posBracket + 1) == '@') // Atributo
            {
                var attrName = posStr.substring(posBracket + 2,posStr.length - 1);
                child = this.getChildNodeFromPos(parent,pos,false);
                return child.getAttributeNode(attrName);
            }
            else return this.getChildNodeFromPos(parent,pos,true);
        }
    }

    function getNodeFromArrayPath(arrayPath,topParent)
    {
        var doc = document;
        var firstPos = arrayPath[0];
        if (firstPos == "window") return window;
        else if (firstPos == "document") return doc;
        else if (firstPos == "doctype") return doc.doctype;

        if (topParent == null) topParent = doc;

        var node = topParent;
        var len = arrayPath.length;
        for(var i = 0; i < len; i++)
        {
            var posStr = arrayPath[i];
            node = this.getChildNodeFromStrPos(node,posStr);
        }

        return node;
    }

    function getNodeFromPath(pathStr,topParent)
    {
        var path = this.getArrayPathFromString(pathStr);
        if (path == null) return null;

        return this.getNodeFromArrayPath(path,topParent);
    }

    function getNodeChildPosition(node)
    {
        if (node == document.documentElement) return "de";

        var parentNode = this.itsNatDoc.getParentNode(node);
        if (parentNode == null) throw "Unexpected error";

        var childNodes = parentNode.childNodes;
        var len = childNodes.length;
        var pos = 0;
        for(var i = 0; i < len; i++)
        {
            var nodeItem = childNodes[i];
            if (nodeItem == node) return pos;
            if (nodeItem.nodeType != 3) pos++; // 3 = Node.TEXT_NODE
        }
        return "-1";
    }

    function getStringPathFromArray(path)
    {
        var code = "";
        var len = path.length;
        for(var i = 0; i < len; i++)
        {
            if (i != 0) code += ",";
            code += path[i];
        }

        return code;
    }

    function getNodeDeep(node,topParent)
    {
        var i = 0;
        while(node != topParent)
        {
            i++;
            node = this.itsNatDoc.getParentNode(node);
            if (node == null) return -1; // el nodo no esta bajo topParent
        }
        return i;
    }

    function getNodePath(nodeLeaf,topParent)
    {
        if (nodeLeaf == null) return null;

        if (topParent == null) topParent = document;

        if (nodeLeaf == window) return new Array("window");
        else if (nodeLeaf == document) return new Array("document");
        else if (nodeLeaf == document.doctype) return new Array("doctype");

        var node = nodeLeaf;
        var type = node.nodeType;
        if (type == 2) node = node.ownerElement; // Node.ATTRIBUTE_NODE
        var len = this.getNodeDeep(node,topParent);
        if (len < 0) return null;
        var path = new Array(len);
        for(var i = len - 1; i >= 0; i--)
        {
            var pos = this.getNodeChildPosition(node);
            path[i] = pos;
            node = this.itsNatDoc.getParentNode(node);
        }
        path[len - 1] += this.getSuffix(nodeLeaf);
        return path;
    }

    function getSuffix(nodeLeaf)
    {
        var type = nodeLeaf.nodeType;
        if (type == 3) return "[t]"; // Node.TEXT_NODE
        else if (type == 2) return "[@" + nodeLeaf.name + "]"; // Node.ATTRIBUTE_NODE
        else return "";
    }

    function getAttrPathString(attr,ownerElem)
    {
        var path = this.getStringPathFromNode(ownerElem);
        path += "[@" + attr.name + "]";
        return path;
    }

    function getStringPathFromNode(node,topParent)
    {
        if (node == null) return null;

        var path = this.getNodePath(node,topParent);
        if (path == null) return null;
        return this.getStringPathFromArray(path);
    }
}


function TransportUtil()
{
    this.transpAllAttrs = transpAllAttrs;
    this.transpAttr = transpAttr;
    this.transpNodeInner = transpNodeInner;
    this.transpNodeComplete = transpNodeComplete;

    function transpAllAttrs(evt)
    {
        var msie = itsnat.Browser.isMSIE();
        var target = evt.getCurrentTarget();
        var itsNatDoc = evt.itsNatDoc;
        var attribs = target.attributes;
        var num = 0,len = attribs.length;
        for(var i = 0; i < len; i++)
        {
            var attr = attribs[i];
            if (msie && !attr.specified) continue; // Pues la col. attribs contiene todos los posibles

            evt.setExtraParam("itsnat_attr_" + num,attr.name);
            evt.setExtraParam(attr.name,itsNatDoc.getAttribute(target,attr.name));
            num++;
        }
        evt.setExtraParam("itsnat_attr_num",num);
    }

    function transpAttr(evt,name)
    {
        var value = evt.itsNatDoc.getAttribute(evt.getCurrentTarget(),name);
        if (value != null) evt.setExtraParam(name,value);
    }

    function transpNodeInner(evt,name)
    {
        var value = evt.getCurrentTarget().innerHTML;
        if (value != null) evt.setExtraParam(name,value);
    }

    function transpNodeComplete(evt,nameInner)
    {
        this.transpAllAttrs(evt);
        this.transpNodeInner(evt,nameInner);
    }
}

function EventQueue(itsNatDoc)
{
    this.returnedEvent = returnedEvent;
    this.processEvents = processEvents;
    this.sendEvent = sendEvent;
    this.sendEventEffective = sendEventEffective;

    this.itsNatDoc = itsNatDoc;
    this.queue = new List();
    this.holdEvt = null;

    function returnedEvent(evt)
    {
        if (this.holdEvt == evt) this.processEvents(false);
    }

    function processEvents(notHold)
    {
        this.holdEvt = null;
        while(!this.queue.isEmpty())
        {
            var evt = this.queue.removeFirst();
            this.sendEventEffective(evt);
            if (notHold) continue;
            if (this.holdEvt != null) break; // El evento enviado ordena bloquear
        }
        if (notHold) this.holdEvt = null;
    }

    function sendEvent(evt)
    {
        if (this.itsNatDoc.disabledEvents) return;

        if (evt.ignoreHold)
        {
            this.processEvents(true); // liberamos la cola, recuerdar que es monohilo
            this.sendEventEffective(evt);
        }
        else if (this.holdEvt != null)
        {
            if (evt.saveEvent) evt.saveEvent();
            this.queue.addLast(evt);
        }
        else this.sendEventEffective(evt);
    }

    function sendEventEffective(evt)
    {
        if (this.itsNatDoc.disabledEvents) return; // pudo ser definido desde el servidor en el anterior evento

        var globalListeners = this.itsNatDoc.globalEventListeners;
        if (!globalListeners.isEmpty())
        {
            var array = globalListeners.getArrayCopy(); // asi permitimos que se añadan mientras se procesan
            var len = array.length;
            for(var i = 0; i < len; i++)
            {
                var listener = array[i];
                var res = listener(evt);
                if (!res && (typeof res == "boolean")) return; // no enviar
            }
        }

        this.itsNatDoc.fireEventMonitors(true,false,evt);

        // evt es como un listener y debe tener el metodo: processXHRResult
        var ajax = new itsnat.ajax.AJAX();
        var method = this.itsNatDoc.usePost ? "POST" : "GET";
        var servletPath = this.itsNatDoc.getServletPath();
        var syncMode = evt.getSyncMode();
        var url = evt.generateURL();

        switch(syncMode)
        {
            case 2: // ASYNC_HOLD
                this.holdEvt = evt;
            case 1: // ASYNC
                ajax.requestAsyncText(method,servletPath,url,evt,evt.getTimeout());
                break;
            case 3: // SYNC
                ajax.requestSyncText(method,servletPath,url);
                evt.processXHRResult(ajax.xhr,false);
                break;
        }
    }
}


function Event(syncMode,timeout,itsNatDoc)
{
    this.setMustBeSent = setMustBeSent;
    this.sendEvent = sendEvent;
    this.generateURL = generateURL;
    this.processXHRResult = processXHRResult;
    this.processTimeout = processTimeout;
    this.processResponse = processResponse;
    this.getSyncMode = getSyncMode;
    this.getTimeout = getTimeout;

    // attribs
    this.syncMode = syncMode;
    this.timeout = timeout;
    this.itsNatDoc = itsNatDoc;
    this.ignoreHold = false;
    this.mustBeSent = true;

    function getSyncMode() { return this.syncMode; }

    function getTimeout() { return this.timeout; }

    function setMustBeSent(value) { this.mustBeSent = value; }

    function sendEvent() { if (this.mustBeSent) this.itsNatDoc.evtQueue.sendEvent(this); }

    function generateURL()
    {
        var url = "";
        url += "itsnat_client_id=" + this.itsNatDoc.getClientId();
        url += "&itsnat_session_token=" + this.itsNatDoc.getSessionToken();
        url += "&itsnat_session_id=" + this.itsNatDoc.getSessionId();
        return url;
    }

    function processXHRResult(xhr,async)
    {
        if (!this.itsNatDoc) return; // En proceso de destruccion evitamos procesar respuestas asincronas para evitar errores (FireFox)
        if (this.itsNatDoc.doc != document) return; // evento perdido en cambio de pagina (raro)

        if (xhr.readyState != 4) return; // only if req shows "complete"

        var status;
        try { status = xhr.status; }
        catch(e) { return; } // Caso de fallo de red o unload en FireFox (esta destruyendose) https://bugzilla.mozilla.org/show_bug.cgi?id=238559#c0

        // http://www.monsur.com/httpstatuscodes/test.html
        // El status 0 o undefined es o bien un error no reconocido por el navegador
        // o un falso error (WebKits antiguos 4xx), como no sabemos nada del mismo lo "filtramos" sin mostrar error

        if (typeof status != "number") return; // caso de S60WebKit por ejemplo al enviar el unload
        else if (status == 0) return; // Habitual en Iris
        else if (status == 200) // "OK"
        {
            this.itsNatDoc.fireEventMonitors(false,false,this);

            this.processResponse(xhr.responseText);
        }
        else
        {
            // Normalmente: status == 500 => Error interno del servidor, el servidor ha lanzado una excepcion
            // "responseText" contiene el texto de la excepcion del servidor (en Opera esta vacio)
            // xhr.statusText nos da apenas la frase "Error Interno del Servidor"
            this.itsNatDoc.fireEventMonitors(false,false,this);

            var errMsg = "status: " + status + "\n" + xhr.statusText + "\n\n" + xhr.responseText;
            var errorMode = this.itsNatDoc.getErrorMode();
            if (errorMode == 0) alert(ERROR); // 0 == ClientErrorMode.NOT_CATCH_ERRORS . Provocamos un error para que los debuggers se paren
            else this.itsNatDoc.showErrorMessage(true,null,errMsg);
        }

        if (async) this.itsNatDoc.evtQueue.returnedEvent(this);
    }

    function processTimeout(xhr) { this.itsNatDoc.fireEventMonitors(false,true,this); }

    function processResponse(response)
    {
        if (response.length == 0) return;

        var func = new Function("itsNatDoc",response);
        var errorMode = this.itsNatDoc.getErrorMode();
        if (errorMode == 0) func.call(window,this.itsNatDoc); // 0 = ClientErrorMode.NOT_CATCH_ERRORS. En MSIE activar el mostrar mensaje de error y con un depurador podremos depurar. Si hay error no continua y en modo ASYNC_HOLD la aplicacion se para (pues no se quita de la cola el evento)
        else
        {
            try { func.call(window,this.itsNatDoc); }
            catch(e) { this.itsNatDoc.showErrorMessage(false,e,response); }
        }
    }
}

function NormalEvent(listener)
{
    this.Event = Event;
    this.Event(listener.getSyncMode(),listener.getTimeout(),listener.itsNatDoc);
    this.Event_generateURL = this.generateURL;

    this.getListener = getListener;
    this.generateURL = generateURL;
    this.getExtraParam = getExtraParam;
    this.setExtraParam = setExtraParam;
    this.getCurrentTarget = getCurrentTarget;
    this.getUtil = getUtil;

    // attribs
    this.listener = listener;
    this.extraParams = null;

    function getListener() { return this.listener; }

    function getCurrentTarget() { return this.listener.getCurrentTarget(); }

    function getExtraParam(name)
    {
        if (this.extraParams == null) this.extraParams = new Object();
        return this.extraParams[name];
    }

    function setExtraParam(name,value)
    {
        if (this.extraParams == null) this.extraParams = new Object();
        this.extraParams[name] = value;
    }

    function generateURL()
    {
        var url = this.Event_generateURL();
        url += this.listener.generateURL(this);
        return url;
    }

    function getUtil() { return itsnat.TransportUtil; }
}

function DOMEvent(listener)
{
    this.NormalEvent = NormalEvent;
    this.NormalEvent(listener);
}

function DOMStdEvent(evt,listener)
{
    this.DOMEvent = DOMEvent;
    this.DOMEvent(listener);

    this.getNativeEvent = getNativeEvent;
    this.saveEvent = saveEvent;
    this.getTarget = null; // En derivadas

    // attribs
    this.evt = evt;
    this.ignoreHold = (evt.type == "unload")||(evt.type == "beforeunload");

    function getNativeEvent() { return this.evt; }

    function saveEvent()
    {
        var evtUtil = this.getListener().getEventUtil();
        if (evtUtil.needBackUpEvent())
        {
            var evtN = this.evt;
            this.evt = new Object();
            evtUtil.backupEvent(evtN,this.evt);
        }
    }
}

function UserEvent(evt,listener)
{
    this.DOMEvent = DOMEvent;
    this.DOMEvent(listener);

    if ((evt != null) && (evt.extraParams != null)) // evt es un UserEventPublic
        for(var name in evt.extraParams)
            this.setExtraParam(name,evt.extraParams[name]);
}

function UserEventPublic(name)
{
    this.getExtraParam = getExtraParam;
    this.setExtraParam = setExtraParam;
    this.getName = getName;

    this.extraParams = null;
    this.name = name;

    function getExtraParam(name)
    {
        if (this.extraParams == null) this.extraParams = new Object();
        return this.extraParams[name];
    }

    function setExtraParam(name,value)
    {
        if (this.extraParams == null) this.extraParams = new Object();
        this.extraParams[name] = value;
    }

    function getName() { return this.name; }
}

function RemCtrlEvent(syncMode,timeout,method,itsNatDoc)
{
    this.Event = Event;
    this.Event(syncMode,timeout,itsNatDoc);
    this.Event_generateURL = this.generateURL;

    this.generateURL = generateURL;
    this.method = method;

    function generateURL()
    {
        var url = this.Event_generateURL();
        url += "&itsnat_remctrl=" + this.method;
        return url;
    }
}

function RemCtrlUnloadEvent(syncMode,timeout,method,itsNatDoc)
{
    this.RemCtrlEvent = RemCtrlEvent;
    this.RemCtrlEvent(syncMode,timeout,method,itsNatDoc);
    this.RemCtrlEvent_generateURL = this.generateURL;

    this.generateURL = generateURL;

    this.ignoreHold = true;

    function generateURL()
    {
        var url = this.RemCtrlEvent_generateURL();
        url += "&itsnat_unload=true";
        return url;
    }
}

function RemCtrlTimerRefreshEvent(callback,interval,syncMode,timeout,itsNatDoc)
{
    this.RemCtrlEvent = RemCtrlEvent;
    this.RemCtrlEvent(syncMode,timeout,"timer",itsNatDoc);

    this.RemCtrlEvent_processResponse = this.processResponse;
    this.processResponse = processResponse;

    // attribs
    this.callback = callback;
    this.interval = interval;

    function processResponse(response,async)
    {
        this.RemCtrlEvent_processResponse(response,async);

        setTimeout(this.callback,this.interval); // Mejor que usar setInterval y se acumulen los eventos
    }
}


function RemCtrlCometTaskRefreshEvent(listenerId,syncMode,timeout,itsNatDoc)
{
    this.RemCtrlEvent = RemCtrlEvent;
    this.RemCtrlEvent(syncMode,timeout,"comet",itsNatDoc);
    this.RemCtrlEvent_generateURL = this.generateURL;

    this.generateURL = generateURL;

    this.listenerId = listenerId;

    function generateURL()
    {
        var url = this.RemCtrlEvent_generateURL();
        url += "&itsnat_listener_id=" + this.listenerId;
        return url;
    }
}

function MouseEventUtil()
{
    this.generateURL = generateURL;
    this.backupEvent = backupEvent;

    function generateURL(evt,itsNatDoc)
    {
        var url = "";
        url += "&itsnat_evt_screenX=" + evt.screenX;
        url += "&itsnat_evt_screenY=" + evt.screenY;
        url += "&itsnat_evt_clientX=" + evt.clientX;
        url += "&itsnat_evt_clientY=" + evt.clientY;
        url += "&itsnat_evt_ctrlKey=" + evt.ctrlKey;
        url += "&itsnat_evt_shiftKey=" + evt.shiftKey;
        url += "&itsnat_evt_altKey=" + evt.altKey;
        url += "&itsnat_evt_button=" + evt.button;
        return url;
    }

    function backupEvent(evtN,evtB)
    {
        evtB.screenX = evtN.screenX;
        evtB.screenY = evtN.screenY;
        evtB.clientX = evtN.clientX;
        evtB.clientY = evtN.clientY;
        evtB.ctrlKey = evtN.ctrlKey;
        evtB.shiftKey = evtN.shiftKey;
        evtB.altKey = evtN.altKey;
        evtB.button = evtN.button;
    }
}


function NormalEventListener(itsNatDoc,eventType,currentTarget,listener,id,syncMode,timeout)
{
    this.getEventType = getEventType;
    this.getCurrentTarget = getCurrentTarget;
    this.getListener = getListener;
    this.getId = getId;
    this.getSyncMode = getSyncMode;
    this.getTimeout = getTimeout;
    this.generateURL = generateURL;
    this.createNormalEvent = null; // redefinir
    this.dispatchEvent = dispatchEvent;

    // attribs
    this.itsNatDoc = itsNatDoc;
    this.eventType = eventType;
    this.currentTarget = currentTarget;
    this.listener = listener;
    this.id = id;
    this.syncMode = syncMode;
    this.timeout = timeout;

    function getEventType() { return this.eventType; }

    function getCurrentTarget() { return this.currentTarget; }

    function getListener() { return this.listener; }

    function getId() { return this.id; }

    function getSyncMode() { return this.syncMode; }

    function getTimeout() { return this.timeout; }

    function generateURL(evt)
    {
        var url = "";
        url += "&itsnat_eventType=" + this.getEventType();
        url += "&itsnat_listener_id=" + this.getId();
        var params = evt.extraParams;
        if (params != null)
            for(var name in params)
                url += "&" + name + "=" + encodeURIComponent(params[name]);
        return url;
    }

    function dispatchEvent(evt)
    {
        var evtWrapper = this.createNormalEvent(evt);
        var listener = this.getListener();
        if (listener != null) listener(evtWrapper);
        evtWrapper.sendEvent();
    }
}

function DOMStdEventListener(itsNatDoc,currentTarget,type,listener,id,useCapture,syncMode,timeout,typeCode)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"domstd",currentTarget,listener,id,syncMode,timeout);
    this.NormalEventListener_generateURL = this.generateURL;

    this.getType = getType;
    this.isUseCapture = isUseCapture;
    this.getTypeCode = getTypeCode;
    this.getEventUtil = getEventUtil;
    this.generateURL = generateURL;
    this.createNormalEvent = createNormalEvent;

    // attribs
    this.type = type;
    this.useCapture = useCapture;
    this.typeCode = typeCode;
    this.evtUtil = itsnat.getEventUtil(typeCode);

    function getType() { return this.type; }
    function isUseCapture() { return this.useCapture; }
    function getTypeCode() { return this.typeCode; }
    function getEventUtil() { return this.evtUtil; }

    function generateURL(evt)
    {
        var url = this.NormalEventListener_generateURL(evt);
        url += "&itsnat_evt_type=" + this.type;
        url += this.evtUtil.generateURL(evt.getNativeEvent(),this.itsNatDoc);
        return url;
    }

    function createNormalEvent(evt)
    {
        if (itsnat.Browser.isMSIE()) return new itsnat.MSIEDOMEvent(evt,this);
        else return new itsnat.W3CDOMEvent(evt,this);
    }
}

function UserEventListener(itsNatDoc,currentTarget,name,listener,id,syncMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"user",currentTarget,listener,id,syncMode,timeout);

    this.getName = getName;
    this.createNormalEvent = createNormalEvent;

    // attribs
    this.name = name;

    function getName() { return this.name; }
    function createNormalEvent(evt) { return new UserEvent(evt,this); }
}

function TimerEventListener(itsNatDoc,currentTarget,listener,id,syncMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"timer",currentTarget,listener,id,syncMode,timeout);

    this.getHandle = getHandle;
    this.setHandle = setHandle;
    this.createNormalEvent = createNormalEvent;

    // attribs
    this.handle = 0;

    function getHandle() { return this.handle; }
    function setHandle(handle) { this.handle = handle; }
    function createNormalEvent(evt) { return new DOMEvent(this); }
}

function ContinueEventListener(itsNatDoc,currentTarget,listener,id,syncMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"continue",currentTarget,listener,id,syncMode,timeout);

    this.createNormalEvent = createNormalEvent;

    function createNormalEvent(evt) { return new DOMEvent(this); }
}

function AsyncTaskEventListener(itsNatDoc,currentTarget,listener,id,syncMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"asyncret",currentTarget,listener,id,syncMode,timeout);

    this.createNormalEvent = createNormalEvent;

    function createNormalEvent(evt) { return new DOMEvent(this); }
}

function CometTaskEventListener(itsNatDoc,id,syncMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"cometret",null,null,id,syncMode,timeout);

    this.createNormalEvent = createNormalEvent;

    function createNormalEvent(evt) { return new DOMEvent(this); }
}

function Document(sessionToken,sessionId,clientId,servletPath,usePost,remView,errorMode,useCache,numScripts,scriptParent)
{
    this.getParentNode = getParentNode;
    this.getAttribute = getAttribute;
    this.showErrorMessage = showErrorMessage;
    this.getErrorMode = getErrorMode;
    this.getSessionToken = getSessionToken;
    this.getSessionId = getSessionId;
    this.getClientId = getClientId;
    this.getServletPath = getServletPath;
    this.addNodeCache = addNodeCache;
    this.addNodeCache2 = addNodeCache2;
    this.getNodeFromPath = getNodeFromPath;
    this.getNodeFromPath2 = getNodeFromPath2;
    this.getNodeCached = getNodeCached;
    this.removeNodeCache = removeNodeCache;
    this.getNodeCacheId = getNodeCacheId;
    this.getStringPathFromNode = getStringPathFromNode;
    this.getAttrPathString = getAttrPathString;
    this.addDOMEventListener = addDOMEventListener;
    this.removeDOMEventListener = removeDOMEventListener;
    this.addTimerEventListener = addTimerEventListener;
    this.removeTimerEventListener = removeTimerEventListener;
    this.updateTimerEventListener = updateTimerEventListener;
    this.sendAsyncTaskEvent = sendAsyncTaskEvent;
    this.sendCometTaskEvent = sendCometTaskEvent;
    this.sendContinueEvent = sendContinueEvent;
    this.addUserEventListener = addUserEventListener;
    this.removeUserEventListener = removeUserEventListener;
    this.createUserEvent = createUserEvent;
    this.dispatchUserEvent = dispatchUserEvent;
    this.dispatchUserEvent2 = dispatchUserEvent2;
    this.fireUserEvent = fireUserEvent;
    this.sendRemCtrlTimerRefresh = sendRemCtrlTimerRefresh;
    this.stopRemCtrlTimerRefresh = stopRemCtrlTimerRefresh;
    this.sendRemCtrlCometTaskRefresh = sendRemCtrlCometTaskRefresh;
    this.sendRemCtrlTimerUnload = sendRemCtrlTimerUnload;
    this.sendRemCtrlCometUnload = sendRemCtrlCometUnload;
    this.setCharacterData = setCharacterData;
    this.setTextNode = setTextNode;
    this.setAttribute = setAttribute;
    this.setAttribute2 = setAttribute2;
    this.setAttributeNS = setAttributeNS;
    this.setAttributeNS2 = setAttributeNS2;
    this.removeAttribute = removeAttribute;
    this.removeAttribute2 = removeAttribute2;
    this.removeAttributeNS = removeAttributeNS;
    this.removeAttributeNS2 = removeAttributeNS2;
    this.setCSSStyle = setCSSStyle;
    this.appendChild = appendChild;
    this.appendChild2 = appendChild2;
    this.insertBefore = insertBefore;
    this.insertBefore2 = insertBefore2;
    this.removeChild = removeChild;
    this.removeChild2 = removeChild2;
    this.removeChild3 = removeChild3;
    this.setInnerHTML2 = setInnerHTML2;
    this.setInnerXML = setInnerXML;
    this.setInnerXML2 = setInnerXML2;
    this.addEventMonitor = addEventMonitor;
    this.removeEventMonitor = removeEventMonitor;
    this.fireEventMonitors = fireEventMonitors;
    this.setEnableEventMonitors = setEnableEventMonitors;
    this.dispatchEvent2 = dispatchEvent2;
    this.addGlobalEventListener = addGlobalEventListener;
    this.removeGlobalEventListener = removeGlobalEventListener;

    // attribs
    this.errorMode = errorMode;
    this.sessionToken = sessionToken;
    this.sessionId = sessionId;
    this.clientId = clientId;
    this.servletPath = servletPath;
    this.usePost = usePost;
    this.remView = remView;
    this.stopCtrlTimer = false;
    this.disabledEvents = false;

    this.doc = document;
    this.pathResolver = new DOMPathResolver(this);
    this.domListeners = new Object();
    this.timerListeners = new Object();
    this.userListenersById = new Object();
    this.userListenersByName = new Object(); // listeners sin nodo asociado
    this.evtQueue = new EventQueue(this);
    this.nodeCacheById = useCache ? new Object() : null;
    this.evtMonitors = new List();
    this.enableEvtMonitors = true;
    this.globalEventListeners = new List();

    if (numScripts > 0)
    {
        var current = scriptParent.lastChild;
        do
        {
            if ((current.nodeType == 1) && //  1 = Node.ELEMENT_NODE
                (current.getAttribute("id") == ("itsnat_load_script_" + numScripts)) )
            {
                var prev = current.previousSibling;
                scriptParent.removeChild(current);
                current = prev;
                numScripts--;
            }
            else current = current.previousSibling;
        }
        while((numScripts > 0)&&(current != null));
    }

    function getParentNode(node) { return node.parentNode; }

    function getAttribute(elem,name)
    {
        // Utilidad interna
        var msie = itsnat.Browser.isMSIE();
        if (msie && (name == "style")) return elem.style.cssText; // Necesariamente X/HTML
        else
        {
            // Distinguimos entre atributo con posible valor "" y atributo que no existe (null)
            var hasAttr;
            if (elem.hasAttribute) hasAttr = elem.hasAttribute(name); // MSIE no tiene hasAttribute
            else hasAttr = (elem.getAttributeNode(name) != null);
            if (!hasAttr) return null;
            return elem.getAttribute(name);
        }
    }

    function showErrorMessage(serverErr,e,msg)
    {
        var errorMode = this.getErrorMode();
        if (errorMode == 1) return; // ClientErrorMode.NOT_SHOW_ERRORS

        if (serverErr) // Pagina HTML con la excepcion del servidor
        {
            if ((errorMode == 2) || (errorMode == 4)) // 2 = ClientErrorMode.SHOW_SERVER_ERRORS, 4 = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS
                    alert("SERVER ERROR: " + msg);
        }
        else
        {
             if ((errorMode == 3) || (errorMode == 4)) // 3 = ClientErrorMode.SHOW_CLIENT_ERRORS, 4 = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS
             {
                // Ha sido un error JavaScript
                if (e != null) alert(e + "\n" + msg); // En FireFox la info de la excepcion es util (en MSIE no)
                else alert(msg);
             }
        }
    }

    function getErrorMode() { return this.errorMode; }

    function getSessionToken() { return this.sessionToken; }

    function getSessionId() { return this.sessionId; }

    function getClientId() { return this.clientId; }

    function getServletPath() { return this.servletPath; }

    function getNodeCached(id)
    {
        if (id == null) return null;
        var node = this.nodeCacheById[id];
        if (!node) return null; // para hacer que "undefined" sea null
        return node;
    }

    function addNodeCache2(id,node)
    {
        if (id == null) return; // si id es null cache desactivado
        this.nodeCacheById[id] = node;
        node.itsNatCacheId = id;
    }

    function removeNodeCache(idList)
    {
        var len = idList.length;
        for(var i = 0; i < len; i++)
        {
            var id = idList[i];
            var node = this.getNodeCached(id);
            delete this.nodeCacheById[id];
            node.itsNatCacheId = null; // No vale delete en MSIE en propiedades de objetos nativos
        }
    }

    function getNodeCacheId(node)
    {
        if (node == null) return null;
        if (!node.itsNatCacheId) return null; // si no esta definido es "undefined"
        return node.itsNatCacheId;
    }

    function addNodeCache(idObj) { return this.getNodeFromPath(idObj); }

    function getNodeFromPath(idObj)
    {
        if (idObj == null) return null;
        var cachedParentId = null,id = null,path = null,newCachedParentIds = null;
        if (typeof idObj == "string") id = idObj;
        else // array
        {
            var len = idObj.length;
            if (len == 2)
            {
              id = idObj[0];
              newCachedParentIds = idObj[1];
            }
            else if (len >= 3)
            {
              cachedParentId = idObj[0];
              id = idObj[1];
              path = idObj[2];
              if (len == 4) newCachedParentIds = idObj[3];
            }
        }

        var cachedParent = null;
        if (cachedParentId != null)
        {
            cachedParent = this.getNodeCached(cachedParentId);
            if (cachedParent == null) throw "Unexpected error";
        }
        var node = this.getNodeFromPath2(cachedParent,[id,path]);
        if (newCachedParentIds != null)
        {
            var parentNode = this.getParentNode(node);
            var len = newCachedParentIds.length;
            for(var i = 0; i < len; i++)
            {
                this.addNodeCache2(newCachedParentIds[i],parentNode);
                parentNode = this.getParentNode(parentNode);
            }
        }

        return node;
    }

    function getNodeFromPath2(parent,idObj)
    {
        if (idObj == null) return null;
        var id = null;
        var path = null;
        if (typeof idObj == "string") id = idObj;
        else // array
        {
            id = idObj[0];
            if (idObj.length == 2) path = idObj[1];
        }

        if ((id == null) && (path == null)) return null; // raro

        if (path == null) return this.getNodeCached(id); // Debe estar en la cache
        else
        {
            // si parent es null caso de path absoluto, si no, path relativo
            var node = this.pathResolver.getNodeFromPath(path,parent);
            if (id != null) this.addNodeCache2(id,node); // Si id es null=> cache desactiva o no cacheable (text node)
            return node;
        }
    }

    function getStringPathFromNode(node)
    {
        if (node == null) return null;

        var nodeId = this.getNodeCacheId(node);
        if (nodeId != null) return "id:" + nodeId; // es undefined si no esta cacheado (o null si se quito)
        else if (this.nodeCacheById != null) // cache activada
        {
            var parentId = null;
            var parentNode = node;
            do
            {
                parentNode = this.getParentNode(parentNode);
                parentId = this.getNodeCacheId(parentNode); // si parentNode es null devuelve null
            }
            while((parentId == null)&&(parentNode != null));

            var path = this.pathResolver.getStringPathFromNode(node,parentNode); // Si parentNode es null (parentId es null) devuelve un path absoluto
            if (parentNode != null) return "pid:" + parentId + ":" + path;
            else return path; // absoluto
        }
        else return this.pathResolver.getStringPathFromNode(node,null); // absoluto
    }

    function getAttrPathString(attr,ownerElem)
    {
        return this.pathResolver.getAttrPathString(attr,ownerElem);
    }

    function addDOMEventListener(idObj,type,listenerId,listener,useCapture,syncMode,timeout,typeCode)
    {
        var node = this.getNodeFromPath(idObj);
        var listenerWrapper = new DOMStdEventListener(this,node,type,listener,listenerId,useCapture,syncMode,timeout,typeCode);
        this.domListeners[listenerId] = listenerWrapper;
        this.addDOMEventListener2(listenerWrapper,node,type,useCapture);
    }

    function removeDOMEventListener(listenerId)
    {
        var listenerWrapper = this.domListeners[listenerId];
        delete this.domListeners[listenerId];

        var node = listenerWrapper.getCurrentTarget();
        var type = listenerWrapper.getType();
        var useCapture = listenerWrapper.isUseCapture();
        this.removeDOMEventListener2(listenerWrapper,node,type,useCapture);
    }

    function addTimerEventListener(idObj,listenerId,listener,syncMode,timeout,delay)
    {
        var node = this.getNodeFromPath(idObj); // puede ser nulo
        var listenerWrapper = new TimerEventListener(this,node,listener,listenerId,syncMode,timeout);
        var timerFunction = function()
        {
            arguments.callee.listenerWrapper.dispatchEvent(null);
        };
        timerFunction.listenerWrapper = listenerWrapper;
        listenerWrapper.timerFunction = timerFunction;
        this.timerListeners[listenerId] = listenerWrapper;
        listenerWrapper.setHandle(window.setTimeout(timerFunction,delay));
    }

    function removeTimerEventListener(listenerId)
    {
        var listenerWrapper = this.timerListeners[listenerId];
        if (listenerWrapper)
        {
            window.clearTimeout(listenerWrapper.getHandle());
            delete this.timerListeners[listenerId];
        }
    }

    function updateTimerEventListener(listenerId,delay)
    {
        var listenerWrapper = this.timerListeners[listenerId];
        if (listenerWrapper)
            listenerWrapper.setHandle(window.setTimeout(listenerWrapper.timerFunction,delay));
    }

    function sendAsyncTaskEvent(idObj,listenerId,listener,syncMode,timeout)
    {
        var currTarget = this.getNodeFromPath(idObj);
        var listenerWrapper = new AsyncTaskEventListener(this,currTarget,listener,listenerId,syncMode,timeout);
        listenerWrapper.dispatchEvent(null);
    }

    function sendCometTaskEvent(listenerId,syncMode,timeout)
    {
        var listenerWrapper = new CometTaskEventListener(this,listenerId,syncMode,timeout);
        listenerWrapper.dispatchEvent(null);
    }

    function sendContinueEvent(idObj,listenerId,listener,syncMode,timeout)
    {
        var currTarget = this.getNodeFromPath(idObj);
        var listenerWrapper = new ContinueEventListener(this,currTarget,listener,listenerId,syncMode,timeout);
        listenerWrapper.dispatchEvent(null);
    }

    function addUserEventListener(idObj,name,listenerId,listener,syncMode,timeout)
    {
        var currTarget = this.getNodeFromPath(idObj);

        var listenerWrapper = new UserEventListener(this,currTarget,name,listener,listenerId,syncMode,timeout);
        this.userListenersById[listenerId] = listenerWrapper;

        var userListenersByName;
        if (currTarget == null) userListenersByName = this.userListenersByName;
        else
        {
            userListenersByName = currTarget.itsNatUserListenersByName;
            if (!userListenersByName)
            {
                userListenersByName = new Object();
                currTarget.itsNatUserListenersByName = userListenersByName;
            }
        }

        var listeners = userListenersByName[name];
        if (!listeners)
        {
            listeners = new Object();
            userListenersByName[name] = listeners;
        }
        listeners[listenerId] = listenerWrapper;
    }

    function removeUserEventListener(listenerId)
    {
        var listenerWrapper = this.userListenersById[listenerId];
        if (!listenerWrapper) return;

        delete this.userListenersById[listenerId];

        var userListenersByName;
        var currTarget = listenerWrapper.getCurrentTarget();
        if (currTarget == null) userListenersByName = this.userListenersByName;
        else userListenersByName = currTarget.itsNatUserListenersByName;

        var name = listenerWrapper.getName();
        var listeners = userListenersByName[name];
        delete listeners[listenerId];
        // No podemos saber cuando el Array queda vacio (length no funciona)
    }

    function createUserEvent(name) { return new UserEventPublic(name); }

    function dispatchUserEvent(currTarget,evt)
    {
        var userListenersByName;
        if (currTarget == null) userListenersByName = this.userListenersByName;
        else userListenersByName = currTarget.itsNatUserListenersByName;
        if (!userListenersByName) return true;

        var listeners = userListenersByName[evt.getName()];
        if (!listeners) return true;
        for(var id in listeners)
        {
            var listenerWrapper = listeners[id];
            listenerWrapper.dispatchEvent(evt);
        }
        return true; // no cancelable
    }

    function dispatchUserEvent2(idObj,name,evt)
    {
        var elem = this.getNodeFromPath(idObj);
        return this.dispatchUserEvent(elem,name,evt);
    }

    function fireUserEvent(currTarget,name)
    {
        var evt = this.createUserEvent(name);
        this.dispatchUserEvent(currTarget,evt);
    }

    function sendRemCtrlTimerRefresh(callback,interval,syncMode,timeout)
    {
        if (!callback) return; // Se esta destruyendo el documento (FireFox)
        if (this.stopCtrlTimer) return;
        var evt = new RemCtrlTimerRefreshEvent(callback,interval,syncMode,timeout,this);
        evt.sendEvent();
    }

    function stopRemCtrlTimerRefresh() { this.stopCtrlTimer = true; }

    function sendRemCtrlCometTaskRefresh(listenerId,syncMode,timeout)
    {
        var evt = new RemCtrlCometTaskRefreshEvent(listenerId,syncMode,timeout,this);
        evt.sendEvent();
    }

    function sendRemCtrlCometUnload(syncMode,timeout)
    {
        var evt = new RemCtrlUnloadEvent(syncMode,timeout,"comet",this);
        evt.sendEvent();
    }

    function sendRemCtrlTimerUnload(syncMode,timeout)
    {
        var evt = new RemCtrlUnloadEvent(syncMode,timeout,"timer",this);
        evt.sendEvent();
    }

    function setCharacterData(idObj,value)
    {
        var charNode = this.getNodeFromPath(idObj);
        charNode.data = value;
    }

    function setTextNode(parentIdObj,nextSibIdObj,value)
    {
        var parentNode = this.getNodeFromPath(parentIdObj);
        var nextSibling = this.getNodeFromPath2(parentNode,nextSibIdObj);
        var textNode = null;
        if (nextSibling != null) textNode = nextSibling.previousSibling;
        else textNode = parentNode.lastChild;
        if (textNode != null)
        {
            if (textNode.nodeType != 3) textNode = null;  // 3 = Node.TEXT_NODE No es de texto, fue filtrado
            else if (itsnat.Browser.isOperaMini())
            {
                // Text.data es solo lectura => reinsertamos con el nuevo valor
                parentNode.removeChild(textNode);
                textNode = null;
            }
        }

        if (textNode == null)
        {
            // Hay nodo de texto en el servidor pero en el browser se filtro por tener solo espacios o similares
            textNode = document.createTextNode(value);
            if (nextSibling != null) parentNode.insertBefore(textNode,nextSibling);
            else parentNode.appendChild(textNode);
        }
        else textNode.data = value;
    }

    function setAttribute(elem,name,value)
    {
        elem.setAttribute(name,value);
    }

    function setAttribute2(idObj,name,value)
    {
        var elem = this.getNodeFromPath(idObj);
        this.setAttribute(elem,name,value);
    }

    function setAttributeNS(elem,namespaceURI,name,value) { elem.setAttributeNS(namespaceURI,name,value); }

    function setAttributeNS2(idObj,namespaceURI,name,value)
    {
        var elem = this.getNodeFromPath(idObj);
        this.setAttributeNS(elem,namespaceURI,name,value);
    }

    function removeAttribute(elem,name)
    {
        elem.removeAttribute(name);
    }

    function removeAttribute2(idObj,name)
    {
        var elem = this.getNodeFromPath(idObj);
        this.removeAttribute(elem,name);
    }

    function removeAttributeNS(elem,namespaceURI,name) { elem.removeAttributeNS(namespaceURI,name); }

    function removeAttributeNS2(idObj,namespaceURI,name)
    {
        var elem = this.getNodeFromPath(idObj);
        this.removeAttributeNS(elem,namespaceURI,name);
    }

    function setCSSStyle(idObj,value)
    {
        var elem = this.getNodeFromPath(idObj);
        elem.style.cssText = value;
    }

    function appendChild(parentNode,newChild) { parentNode.appendChild(newChild); }

    function appendChild2(idObj,newChild)
    {
        var parentNode = this.getNodeFromPath(idObj);
        this.appendChild(parentNode,newChild);
    }

    function insertBefore(parentNode,newChild,childRef)
    {
        if (childRef == null) this.appendChild(parentNode,newChild);
        parentNode.insertBefore(newChild,childRef);
    }

    function insertBefore2(parentIdObj,newChild,childRefIdObj)
    {
        var parentNode = this.getNodeFromPath(parentIdObj);
        var childRef = this.getNodeFromPath2(parentNode,childRefIdObj);
        this.insertBefore(parentNode,newChild,childRef);
    }

    function removeChild(child)
    {
        if (child == null) return; // Si es un nodo de texto que fue filtrado
        this.getParentNode(child).removeChild(child);
    }

    function removeChild2(id,isText)
    {
        var child = this.getNodeFromPath(id);
        if (isText && (child != null) && (child.nodeType != 3)) return; // 3 = Node.TEXT_NODE, no encontrado, parece que fue filtrado
        this.removeChild(child);
    }

    function removeChild3(parentIdObj,childRelPath,isText)
    {
        var parentNode = this.getNodeFromPath(parentIdObj);
        var child = this.getNodeFromPath2(parentNode,[null,childRelPath]);
        if (isText && (child != null) && (child.nodeType != 3)) return; // 3 = Node.TEXT_NODE, no encontrado, parece que fue filtrado
        this.removeChild(child);
    }

    function setInnerHTML2(idObj,value)
    {
        var parentNode = this.getNodeFromPath(idObj);
        parentNode.innerHTML = value;
    }

    function setInnerXML(elem,value)
    {
        // No es usado por MSIE. La alternativa en MSIE: loadXML
        var parser = new DOMParser();
        var doc = parser.parseFromString(value,"text/xml");
        // No usamos DocumentFragment porque importNode no funciona en Safari con DocumentFragment
        var child = doc.documentElement.firstChild;
        while (child != null)
        {
            var newChild = document.importNode(child,true);
            elem.appendChild(newChild);
            child = child.nextSibling;
        }
    }

    function setInnerXML2(idObj,value)
    {
        var parentNode = this.getNodeFromPath(idObj);
        this.setInnerXML(parentNode,value);
    }

    function addEventMonitor(monitor) { this.evtMonitors.addLast(monitor); }

    function removeEventMonitor(monitor)
    {
        var index = -1;
        for(var i = 0; i < this.evtMonitors.size(); i++)
        {
            var curr = this.evtMonitors.get(i);
            if (curr == monitor)
                index = i;
        }
        if (index == -1) return;
        this.evtMonitors.array.splice(index,1);
    }

    function fireEventMonitors(before,timeout,evt)
    {
        if (!this.enableEvtMonitors) return;

        for(var i = 0; i < this.evtMonitors.size(); i++)
        {
            var curr = this.evtMonitors.get(i);
            if (before) curr.before(evt);
            else curr.after(evt,timeout);
        }
    }

    function setEnableEventMonitors(value) { this.enableEvtMonitors = value; }

    function dispatchEvent2(idObj,type,evt)
    {
        var node = this.getNodeFromPath(idObj);
        return this.dispatchEvent(node,type,evt);
    }

    function addGlobalEventListener(listener) { this.globalEventListeners.add(listener); }

    function removeGlobalEventListener(listener) { this.globalEventListeners.remove(listener); }

} // Document

function HTMLDocument(sessionToken,sessionId,docId,servletPath,usePost,remView,errorMode,useCache,numScripts,scriptParent)
{
    this.Document = Document;
    this.Document(sessionToken,sessionId,docId,servletPath,usePost,remView,errorMode,useCache,numScripts,scriptParent);

    this.getSelectedHTMLSelect = getSelectedHTMLSelect;

    function getSelectedHTMLSelect(elem)
    {
        // Es llamada por codigo generado desde el servidor
        var res = ""; var nselec = 0;
        var options = elem.options; var len = options.length;
        for(var i = 0; i < len; i++)
        {
            if (!options[i].selected) continue;
            if (nselec > 0) res += ",";
            res += i;
            nselec++;
        }
        return res;
    }
}

} // pkg_itsnat

function itsnat_init(doc,browserType,browserSubType)
{
    itsnat = new Object();
    pkg_itsnat(itsnat,browserType,browserSubType);

    itsnat.ajax = new Object();
    pkg_itsnat_ajax(itsnat.ajax);

    if (itsnat.Browser.isMSIE())
    {
        pkg_itsnat_msie(itsnat);
        if (itsnat.Browser.isMSIEPocket())
            pkg_itsnat_msie_pocket(itsnat); // ademas
    }

    if (itsnat.Browser.isW3C()) pkg_itsnat_w3c(itsnat);

    doc.getItsNatDoc = function ()
      {
          if (itsnat.Browser.isMSIEPocket())
              return document.itsNatDoc; // IE Pocket bug
          else
              return this.itsNatDoc;
      }
}

