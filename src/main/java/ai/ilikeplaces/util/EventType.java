package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

/**
 * DOM event types
 * <p/>
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Dec 8, 2009
 * Time: 11:17:06 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public enum EventType {
    CLICK() {
        @Override
        public String toString() {
            return "click";
        }
    },
    BLUR() {
        @Override
        public String toString() {
            return "blur";
        }
    },
    UNLOAD() {
        @Override
        public String toString() {
            return "onunload";
        }
    },

    ONMOUSEOVER() {
        @Override
        public String toString() {
            return "mouseover";
        }
    };

    public String toString() {
        throw new UnsupportedOperationException("SORRY! THIS METHOD IS SUPPOSED TO BE OVERRIDEN.");
    }
}


/*    These are the events copied off from some internal ItsNat code

        "click",
        "dblclick",
        "mousedown",
        "mouseup",
        "mouseover",
        "mousemove",
        "mouseout",

        "keypress",
        "keydown",
        "keyup",

        "load",
        "unload",
        "abort",
        "error",
        "resize",
        "scroll",
        "select",
        "change",
        "submit",
        "reset",
        "focus",
        "blur",

        "DOMFocusIn"
        "DOMFocusOut"
        "DOMActivate",

        "DOMSubtreeModified"
        "DOMNodeInserted"
        "DOMNodeRemoved"
        "DOMAttrModified"
        "DOMCharacterDataModified"
        "DOMNodeInsertedIntoDocument"
        "DOMNodeRemovedFromDocument"

        "beforeunload"

        "DOMContentLoaded"


        */