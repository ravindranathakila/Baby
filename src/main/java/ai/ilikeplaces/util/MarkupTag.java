package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc._fix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * We cannot except all tags to implement all abstract methods and throw
 * unsupportedness so we throw it by default. Overriding is the tags
 * responsibility.
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_fix(issue = "Each tag can be exposed through an interface to ensure type " +
        "safety and also, proper method calls. Current approach is simpler to use. " +
        "Using an interface based approach is easy to make changes to")
public enum MarkupTag implements MarkupTagFace {

    BODY() {
        @Override
        public String toString() {
            return BODY_TAG;
        }

    },
    P() {
        @Override
        public String toString() {
            return P_TAG;
        }

    },
    INPUT() {
        @Override
        public String toString() {
            return INPUT_TAG;
        }


        @Override
        public String value() {
            return VALUE;
        }

        @Override
        public String type() {
            return TYPE;
        }

        @Override
        public String typeValueText() {
            return TEXT;
        }

        @Override
        public String typeValueSelect() {
            return SELECT;
        }

        @Override
        public String typeValueHidden() {
            return HIDDEN;
        }
    },

    TEXTAREA() {
        @Override
        public String toString() {
            return TEXTAREA_TAG;
        }


        @Override
        public String value() {
            return VALUE;
        }

    },

    A() {
        @Override
        public String toString() {
            return A_TAG;
        }

        @Override
        public String href() {
            return HREF;
        }

        @Override
        public String alt() {
            return ALT;
        }

        @Override
        public String src() {
            return SRC;
        }
    },
    IMG() {
        @Override
        public String toString() {
            return IMG_TAG;
        }

        @Override
        public String alt() {
            return ALT;
        }

        @Override
        public String src() {
            return SRC;
        }
    },

    UL() {
        @Override
        public String toString() {
            return UL_TAG;
        }
    },

    LI() {
        @Override
        public String toString() {
            return LI_TAG;
        }

        @Override
        public String ul() {
            return UL_TAG;
        }

        @Override
        public String ol() {
            return OL;
        }
    },

    DIV() {
        @Override
        public String toString() {
            return DIV_TAG;
        }

    },


    SPAN() {
        @Override
        public String toString() {
            return SPAN_TAG;
        }

    },

    BR() {
        @Override
        public String toString() {
            return BR_TAG;
        }
    },
    META() {
        @Override
        public String namee() {
            return NAME;
        }

        @Override
        public String content() {
            return CONTENT;
        }
    },

    GENERIC() {
        @Override
        public String value() {
            return VALUE;
        }

        @Override
        public String type() {
            return TYPE;
        }

        @Override
        public String typeValueText() {
            return TEXT;
        }

        @Override
        public String typeValueSelect() {
            return SELECT;
        }

        @Override
        public String typeValueHidden() {
            return HIDDEN;
        }

        @Override
        public String href() {
            return HREF;
        }

        @Override
        public String alt() {
            return ALT;
        }

        @Override
        public String src() {
            return SRC;
        }

        @Override
        public String ul() {
            return UL_TAG;
        }

        @Override
        public String ol() {
            return OL;
        }

        @Override
        public String classs() {
            return CLASS;
        }

        @Override
        public String onclick() {
            return ONCLICK;
        }
    };
    private static final String BODY_TAG = "BODY";
    private static final String ONCLICK = "onclick";
    private static final String SPAN_TAG = "SPAN";
    private static final String CONTENT = "content";
    private static final String NAME = "name";
    private static final String UL_TAG = "UL";

    private static final String BR_TAG = "BR";
    private static final String INPUT_TAG = "INPUT";
    private static final String P_TAG = "P";
    private static final String VALUE = "value";
    private static final String TYPE = "type";
    private static final String TEXT = "text";
    private static final String SELECT = "select";
    private static final String HIDDEN = "hidden";
    private static final String TEXTAREA_TAG = "TEXTAREA";
    private static final String DIV_TAG = "DIV";
    private static final String OL = "OL";
    private static final String LI_TAG = "LI";
    private static final String A_TAG = "A";
    private static final String IMG_TAG = "IMG";
    private static final String HREF = "href";
    private static final String SRC = "src";
    private static final String ALT = "alt";

    final static Logger logger = LoggerFactory.getLogger(MarkupTag.class.getName());
    final static String ExceptionMsg = "SORRY! I RECEIVED A CALL ON NON OVERRIDDEN ATTRIBUTE. PROBABLY THIS ATTRIBUTE IS NOT RELEVANT TO THIS ELEMENT TYPE, OR IT WAS NOT IMPLEMENTED.";
    private static final String STYLE = "style";
    private static final String ID = "id";
    private static final String CLASS = "class";
    private static final String TITLE_ATTR = "title";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String style() {
        return STYLE;
    }

    @Override
    public String value() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String type() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String typeValueText() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String typeValueSelect() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String typeValueHidden() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String href() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String alt() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String title() {
        return TITLE_ATTR;
    }

    @Override
    public String src() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String tag() {
        return toString();
    }

    @Override
    public String ul() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String ol() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String classs() {
        return CLASS;
    }

    @Override
    public String namee() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }

    @Override
    public String content() {
        throw new UnsupportedOperationException(ExceptionMsg);
    }


    @Override
    public String onclick() {
        return ONCLICK;
    }

}

