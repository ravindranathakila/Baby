package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.ExceptionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class JSCodeToSend {

    private JSCodeToSend() throws IllegalAccessException {
        throw ExceptionCache.STATIC_USAGE_ONLY_EXCEPTION;
    }

    final static Logger logger = LoggerFactory.getLogger(JSCodeToSend.class);
    final static public String FnEventMonitor = "\ndocument.monitor = new EventMonitor();document.getItsNatDoc().addEventMonitor(document.monitor);";
    final static public String LocationId = "locationId";
    final static public String FnLocationId = "\nfunction getLocationId(){return document.getElementById('" + LocationId + "').value;}\n";
    final static public String LocationName = "locationName";
    final static public String FnLocationName = "\nfunction getLocationName(){return document.getElementById('" + LocationName + "').value;}\n";
    final static public String FnSetTitle = "\ndocument.title=\"Escape to \"+getLocationName()+\"!\";\n";
    final static public String RefreshPage = "\nwindow.location.href=window.location.href;\n";
}
