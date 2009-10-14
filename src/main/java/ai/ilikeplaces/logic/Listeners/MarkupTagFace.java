package ai.ilikeplaces.logic.Listeners;

/**
 *
 * @author Ravindranath Akila
 */
public interface MarkupTagFace {

    public String id();

    public String value();

    public String type();

    public String typeValueText();

    public String typeValueSelect();

    public String typeValueHidden();

    /**
     * Exact String Representation of Enum.
     * Case sensitive as XML markup could be added here in future
     * @return
     */
    @Override
    public String toString();
}
