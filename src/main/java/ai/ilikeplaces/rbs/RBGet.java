package ai.ilikeplaces.rbs;

import ai.ilikeplaces.doc.CONVENTION;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.TODO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@NOTE(note = "THIS PROVIDES DEFAULT RB ACCESS THROUGH VARIABLES. EXCEPTION IS MADE FOR GUI WHERE" +
        "CALLER IS THE ONE WHO KNOWS THE DIALECT." +
        "IN THE CASE OF logMsgs THE DEVELOPER CAN SET THE APPLICATION SPECIFIC LOCALIZAION IN THE" +
        "STARTUP SERVLET. config WILL ALWAYS HAVE NO DIALECT.")
public class
        RBGet {

    final static public ResourceBundle config = ResourceBundle.getBundle("ai.ilikeplaces.rbs.Config");
    @CONVENTION(convention = "THIS SHOULD BE AN IDENTICAL COPY OF Config.properties. IDEALLY GlobalConfig SHOULD BE IN THE SERVER PATH SO THAT IT CAN BE EDITED DURING RUNTIME.")
    static public ResourceBundle globalConfig;
    final static public ResourceBundle logMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.LogMsgs");
    final static public ResourceBundle expMsgs = ResourceBundle.getBundle("ai.ilikeplaces.rbs.ExceptionMsgs");
    final static public ResourceBundle l33t = ResourceBundle.getBundle("ai.ilikeplaces.rbs.l33t");

    final static Logger logger = LoggerFactory.getLogger(RBGet.class);

    final static public String url_CDN_STATIC = "url.CDN_STATIC";

    static {
        try {
            globalConfig = ResourceBundle.getBundle("GlobalConfig");
        } catch (@NOTE(note = "WE DON'T NEED THE STACK TRACE")
        final MissingResourceException e_) {
            logger.error("PLEASE INCLUDE GLOBALCONFIG.PROPERTIES IN WEB SERVER CLASSPATH. FALLING BACK TO USING INTERNAL CONFIG.", e_);
            globalConfig = ResourceBundle.getBundle("ai.ilikeplaces.rbs.Config");
        }
    }

    private RBGet() {
        logger.debug(logMsgs.getString("common.Constructor.Init"), RBGet.class, this.hashCode());
    }


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
            config:
            {
                result_.append("\n");
                result_.append(config.getString("verify"));
                final Set<String> keySet = config.keySet();
                for (String key : keySet) {
                    result_.append("\n");
                    result_.append("{" + key + "," + config.getString(key) + "}");
                }
                result_.append("\n");
            }

            logMsgs:
            {
                result_.append("\n");
                result_.append(logMsgs.getString("verify"));
                final Set<String> keySet = logMsgs.keySet();
                for (String key : keySet) {
                    result_.append("\n");
                    result_.append("{" + key + "," + logMsgs.getString(key) + "}");
                }
                result_.append("\n");
            }

            expMsgs:
            {
                result_.append("\n");
                result_.append(expMsgs.getString("verify"));
                final Set<String> keySet = expMsgs.keySet();
                for (String key : keySet) {
                    result_.append("\n");
                    result_.append("{" + key + "," + expMsgs.getString(key) + "}");
                }
                result_.append("\n");
            }
            globalConfig:
            {
                result_.append("\n");
                result_.append(globalConfig.getString("verify"));
                final Set<String> keySet = globalConfig.keySet();
                for (String key : keySet) {
                    result_.append("\n");
                    result_.append("{" + key + "," + globalConfig.getString(key) + "}");
                }
                result_.append("\n");
            }
        } catch (final Exception e) {
            result_.append(e.getMessage());
        }
        return result_.toString();
    }

    static public ResourceBundle gui() {
        return ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");
    }

    static public ResourceBundle gui(final Locale locale) {
        return ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI", locale);
    }
}
