package ai.ilikeplaces.logic.Listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class JSCodeToSend {

    private JSCodeToSend() throws IllegalAccessException {
        throw new IllegalAccessException("SORRY! THIS CLASS IS NOT INSTRANTIABLE.");
    }
    final static Logger logger = LoggerFactory.getLogger(JSCodeToSend.class);
    final static public String FnEventMonitor = "\ndocument.monitor = new EventMonitor();document.getItsNatDoc().addEventMonitor(document.monitor);";
    final static public String LocationId = "locationId";
    final static public String FnLocationId = "\nfunction getLocationId(){return document.getElementById('" + LocationId + "').value;}\n";
    final static public String LocationName = "locationName";
    final static public String FnLocationName = "\nfunction getLocationName(){return document.getElementById('" + LocationName + "').value;}\n";
    final static public String FnSetTitle = "\ndocument.title=getLocationName();\n";
}
