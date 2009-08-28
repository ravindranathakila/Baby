/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

/*
 Links:
 http://developer.mozilla.org/en/docs/AJAX:Getting_Started
*/

function pkg_itsnat_ajax(pkg)
{
    pkg.AJAX = AJAX;

function AJAX()
{
    // Crear un objeto por cada llamada, no reutilizar
    this.init = init;
    this.request = request;
    this.requestAsyncText = requestAsyncText;
    this.requestSyncText = requestSyncText;

    this.xhr = null;
    this.timerHnd = null;
    this.aborted = false;

    this.init();

    function init()
    {
        if (window.XMLHttpRequest) // Caso FireFox, Opera, MSIE 7 etc
        {
            this.xhr = new XMLHttpRequest();
            // this.xhr.overrideMimeType('text/xml'); // Si se devuelve XML (FireFox)
        }
        else if (window.ActiveXObject)  // MSIE 6 
        {
            try { this.xhr = new ActiveXObject("Msxml2.XMLHTTP"); } catch (ex)
            {
                try { this.xhr = new ActiveXObject("Microsoft.XMLHTTP"); }
                catch (ex2) { this.xhr = null; }
            }
        }
    }

    function request(method,url,async,content,requestListener,timeout)
    {
        var func = function() { defaultRequestListener(arguments.callee.thisObj,arguments.callee.requestListener) };
        func.thisObj = this;
        func.requestListener = requestListener;
        this.xhr.onreadystatechange = func; 
        if (method == "GET")
        {
            url += "?" + content;
            content = "";
        }

        this.xhr.open(method, url, async);
        if (method == "POST") this.xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

        this.xhr.send(content);

        if (async && (timeout >= 0)) // en modo sincrono no sirve
        {
            var func = function() { abort(arguments.callee.thisObj,arguments.callee.requestListener) };
            func.thisObj = this;
            func.requestListener = requestListener;
            this.timerHnd = setTimeout(func,timeout);
        }
    }

    function requestAsyncText(method,url,content,listener,timeout)
    {
        this.request(method,url,true,content,listener,timeout);
    }

    function requestSyncText(method,url,content)
    {
        this.request(method,url,false,content,null,-1);
        return this.xhr.responseText;
    }

    function defaultRequestListener(thisObj,listener)
    {
        var xhr = thisObj.xhr;

        if ((xhr.readyState == 4) && (thisObj.timerHnd != null)) // only if req shows "complete"
            clearTimeout(thisObj.timerHnd); // lo primero por si el proceso normal tarda mucho (o lanza un alert)

        // El XHR abort() de MSIE llama al listener de todas formas con readyState a 4
        // y status != 200, el proceso del abort no debe ir por aqui
        if ((listener != null) && listener.processXHRResult && !thisObj.aborted)
            listener.processXHRResult(xhr,true);
    }

    function abort(thisObj,listener)
    {
        var xhr = thisObj.xhr;
        if ((xhr.readyState >= 1) || (xhr.readyState <= 3)) // http://msdn2.microsoft.com/en-us/library/ms534361.aspx
        {
            thisObj.aborted = true;
            xhr.abort();
            if ((listener != null) && listener.processTimeout) listener.processTimeout(xhr);
        }
    }
}

} // pkg_itsnat_ajax


