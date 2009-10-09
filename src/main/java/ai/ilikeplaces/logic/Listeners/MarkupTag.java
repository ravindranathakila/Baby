package ai.ilikeplaces.logic.Listeners;

import java.util.logging.Logger;

/**
 *
 * @author Ravindranath Akila
 */
public enum MarkupTag implements MarkupTagFace {

    P() {

        @Override
        public String toString() {
            return "P";
        }

        @Override
        public String id() {
            return "id";
        }
    },
    INPUT() {

        @Override
        public String toString() {
            return "INPUT";
        }

        @Override
        public String id() {
            return "id";
        }

        @Override
        public String value() {
            return "value";
        }

        @Override
        public String type() {
            return "type";
        }

        @Override
        public String typeValueText() {
            return "text";
        }

        @Override
        public String typeValueSelect() {
            return "select";
        }

        @Override
        public String typeValueHidden() {
            return "hidden";
        }
    };
    final static  Logger logger = Logger.getLogger(MarkupTag.class.getName());

    public String id() {
        warnNonOverriddenAttributeCall();
        return null;
    }

    public String value() {
        warnNonOverriddenAttributeCall();
        return null;
    }

    public String type() {
        warnNonOverriddenAttributeCall();
        return null;
    }

    public String typeValueText() {
        warnNonOverriddenAttributeCall();
        return null;
    }

    public String typeValueSelect() {
        warnNonOverriddenAttributeCall();
        return null;
    }

    public String typeValueHidden() {
        warnNonOverriddenAttributeCall();
        return null;
    }

    final static private void warnNonOverriddenAttributeCall() {
        logger.severe("OOPS! I RECIEVE CALL ON NON OVERRIDDEN ATTRIBUTE. YOU SHOULD EXPECT NPE SOON.");
    }
}
