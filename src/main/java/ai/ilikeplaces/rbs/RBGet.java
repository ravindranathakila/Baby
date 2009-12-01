package ai.ilikeplaces.rbs;

import ai.ilikeplaces.doc.CONVENTION;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.MissingResourceException;
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
    @CONVENTION(convention = "THIS SHOULD BE AN IDENTICAL COPY OF Config.properties. IDEALLY GlobalConfig SHOULD BE IN THE SERVER PATH SO THAT IT CAN BE EDITED DURING RUNTIME.")
    static private ResourceBundle globalConfig;
    final static public ResourceBundle logMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.LogMsgs");
    final static public ResourceBundle expMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.ExceptionMsgs");

    static {
        try {
            globalConfig = ResourceBundle.getBundle("GlobalConfig");
        } catch (final MissingResourceException e_) {
            e_.printStackTrace(System.out);
        }
    }

    private RBGet() {
        logger.debug(logMsgs.getString("common.Constructor.Init"), RBGet.class, this.hashCode());
    }

    final static Logger logger = LoggerFactory.getLogger(RBGet.class);


    /**
     * @param key__
     * @return Value of Null if value could not be fetched
     */
    final static public String getGlobalConfigKey(final String key__) {
        String returnVal = null;
        try {
            returnVal = globalConfig.getString(key__);
        } catch (final Exception e__) {
            /*We return null*/
        }
        return returnVal;
    }

    @TODO(task = "IF A KEY STARTS WITH ai.ilikeplaces, LOAD PACKAGE AND SEE IF THE KEY IS VALID.")
    final static public String verify() {
        final StringBuilder result_ = new StringBuilder();
        try {
            result_.append("\n");
            result_.append("\n");
            result_.append(config.getString("verify"));
            result_.append(Arrays.toString(config.keySet().toArray()));
            result_.append("\n");
            result_.append(logMsgs.getString("verify"));
            result_.append(Arrays.toString(logMsgs.keySet().toArray()));
            result_.append("\n");
            result_.append(expMsgs.getString("verify"));
            result_.append(Arrays.toString(expMsgs.keySet().toArray()));
            result_.append("\n");
            result_.append(globalConfig.getString("verify"));
            result_.append(Arrays.toString(globalConfig.keySet().toArray()));
        } catch (final Exception e) {
            result_.append(e.getMessage());
        }
        return result_.toString();
    }
}
