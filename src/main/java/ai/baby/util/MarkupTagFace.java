package ai.baby.util;

import ai.scribble.License;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public interface MarkupTagFace {

    public String id();

    public String style();

    public String value();

    public String type();

    public String typeValueText();

    public String typeValueSelect();

    public String typeValueHidden();

    /**
     * Exact String Representation of Enum.
     * Case sensitive as XML markup could be added here in future
     *
     * @return String representation of Tag
     */
    @Override
    public String toString();


    public String href();

    public String alt();

    public String title();

    public String src();

    public String tag();

    public String ul();

    public String ol();

    public String classs();

    public String namee();

    public String content();

    public String onclick();

}
