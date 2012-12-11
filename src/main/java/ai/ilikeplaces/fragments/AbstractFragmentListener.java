package ai.ilikeplaces.fragments;

/**
 * @author Ravindranath Akila
 */
@Deprecated
public abstract class AbstractFragmentListener {

    /**
     *
     */
    /**
     *
     */
    protected final static String Click = "click";

    /**
     *
     */
    /**
     *
     */
    public AbstractFragmentListener() {
        registerEventListeners();
    }

    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the contructor
     */
    protected abstract void registerEventListeners();
}
