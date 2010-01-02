package ai.ilikeplaces.util;

/**
 * DOM event types
 * 
 * User: Ravindranath Akila
 * Date: Dec 8, 2009
 * Time: 11:17:06 PM
 */
public enum EventType {
    click() {
        @Override
        public String toString() {
            return "click";
        }
    },
    blur() {
        @Override
        public String toString() {
            return "blur";
        }
    };

    public String toString(){
        throw new UnsupportedOperationException("SORRY! THIS METHOD IS SUPPOSED TO BE OVERRIDEN.");
    }
}
