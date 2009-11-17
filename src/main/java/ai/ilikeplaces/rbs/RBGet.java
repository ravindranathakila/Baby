package ai.ilikeplaces.rbs;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@NOTE(note = "THIS PROVIDES DEFAULT RB ACCESS THROUGH VARIABLES. EXCEPTION IS MADE FOR GUI WHERE" +
        "CALLER IS THE ONE WHO KNOWS THE DIALECT." +
        "IN THE CASE OF logMsgs THE DEVELOPER CAN SET THE APPLICATION SPECIFIC LOCALIZAION IN THE" +
        "STARTUP SERVLET. config WILL ALWAYS HAVE NO DIALECT.")
public class RBGet {

    final static public ResourceBundle config = ResourceBundle.getBundle("ai.ilikeplaces.rbs.Config");
    final static public ResourceBundle logMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.LogMsgs");

    private RBGet() {
        logger.debug(logMsgs.getString("common.Constructor.Init"), RBGet.class, this.hashCode());
    }

    final static Logger logger = LoggerFactory.getLogger(RBGet.class);

}
