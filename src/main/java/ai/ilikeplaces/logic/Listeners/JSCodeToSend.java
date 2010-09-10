package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.ExceptionCache;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class JSCodeToSend {
    private static final String WINDOW_LOCATION_HREF_WINDOW_LOCATION_HREF = "window.location.href=window.location.href";

    private JSCodeToSend() throws IllegalAccessException {
        throw ExceptionCache.STATIC_USAGE_ONLY_EXCEPTION;
    }

    final static public String FnEventMonitor = "\ndocument.monitor = new EventMonitor();document.getItsNatDoc().addEventMonitor(document.monitor);";
    final static public String LocationId = "locationId";
    final static public String FnLocationId = "\ngetLocationId = function(){return document.getElementById('" + LocationId + "').value;}\n";
    final static public String LocationName = "locationName";
    final static public String FnLocationName = "\ngetLocationName = function(){return document.getElementById('" + LocationName + "').value;}\n";
    final static public String FnSetTitle = "\ndocument.title=\"Escape to \"+getLocationName()+\"!\";\n";
    final static public String RefreshPage = "\nwindow.location.href=window.location.href;\n";
    final static public String ClosePage = "\nwindow.close();\n";


    /**
     * Uses window.location.href = window.location.href + 'stringToBeAppended'
     * Might turn out to be buggy if there are outer quotes. Verify with firebug for safety.
     *
     * @param stringToBeAppended
     * @return
     */
    public static String refreshPageWith(final String stringToBeAppended) {
        return WINDOW_LOCATION_HREF_WINDOW_LOCATION_HREF + "+" + "'" + stringToBeAppended + "';\n";

    }
}
