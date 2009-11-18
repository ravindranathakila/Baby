package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.FIXME;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * We cannot except all tags to implement all abstract methods and throw
 * unsupportedness so we throw it by default. Overriding is the tags
 * responsibility.
 *
 * @author Ravindranath Akila
 */
@FIXME(issue = "Each tag can be exposed through an interface to ensure type " +
"safety and also, proper method calls. Current approach is simpler to use. " +
"Using an interface based approach is easy to make changes to")
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
    final static Logger logger = LoggerFactory.getLogger(MarkupTag.class.getName());
    final static String ExceptionMsg = "SORRY! I RECEIVED A CALL ON NON OVERRIDDEN ATTRIBUTE.";

    public String id() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    public String value() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    public String type() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    public String typeValueText() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    public String typeValueSelect() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    public String typeValueHidden() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }
}
