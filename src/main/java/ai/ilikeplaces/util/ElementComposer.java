package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Mar 14, 2010
 * Time: 10:23:47 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ElementComposer {

    private final Element element;

    private ElementComposer(final Element element) {
        this.element = element;
    }

    /**
     * Element to be composed can already be initialized.
     * Be prudent about what attributes you set as the element should support it
     *
     * @param elementToBeComposed
     * @return
     */
    static public ElementComposer compose(final Element elementToBeComposed) {
        return new ElementComposer(elementToBeComposed);
    }

    final public Element get(){
        return element;
    }

    /**
     * @param elementToBeWrapped
     * @return ElementComposer
     */
    public ElementComposer wrapThis(final Element elementToBeWrapped) {
        element.appendChild(elementToBeWrapped);
        return this;
    }


    /**
     * @param elementToBeWrappedInto
     * @return ElementComposer
     */
    public ElementComposer wrapIntoThis(final Element elementToBeWrappedInto) {
        elementToBeWrappedInto.appendChild(element);
        return ElementComposer.compose(elementToBeWrappedInto);
    }

    /**
     * W3C implementation returns the added element when appending a child.
     * This returns the element appended to. I.E parent
     *
     * @param appendToElement
     * @param childToBeAppended
     * @return appendToElement
     */
    static public Element appendChild(final Element appendToElement, final Element childToBeAppended) {
        appendToElement.appendChild(childToBeAppended);
        return appendToElement;
    }

    static public Element $ElementSetText(final Element elementToBeSetTextOf, final String textToBeSet) {
        ((Text) elementToBeSetTextOf.getFirstChild()).setData(textToBeSet);
        return elementToBeSetTextOf;
    }

    public final ElementComposer $ElementSetText(final String textToBeSet) {
        element.setTextContent(textToBeSet);
        return this;
    }

    public final ElementComposer $ElementSetHref(final String href) {
        element.setAttribute(MarkupTag.GENERIC.href(), href);
        return this;
    }

    static public final Element $ElementSetHref(final Element element, final String href) {
        element.setAttribute(MarkupTag.GENERIC.href(), href);
        return element;
    }

    public final ElementComposer $ElementSetTitle(final String title) {
        element.setAttribute(MarkupTag.GENERIC.title(), title);
        return this;
    }

    static public final Element $ElementSetTitle(final Element element, final String title) {
        element.setAttribute(MarkupTag.GENERIC.title(), title);
        return element;
    }

    public final ElementComposer $ElementSetAlt(final String alt) {
        element.setAttribute(MarkupTag.GENERIC.alt(), alt);
        return this;
    }

    static public final Element $ElementSetAlt(final Element element, final String alt) {
        element.setAttribute(MarkupTag.GENERIC.alt(), alt);
        return element;
    }

    public final ElementComposer $ElementSetClasses(String classes) {
        element.setAttribute(MarkupTag.GENERIC.classs(), classes);
        return this;
    }

    static public final Element $ElementSetClasses(final Element element, final String classes) {
        element.setAttribute(MarkupTag.GENERIC.classs(), classes);
        return element;
    }

    public final ElementComposer $ElementSetAttribute(final String attributeName, final String attributeValue) {
        element.setAttribute(attributeName, attributeValue);
        return this;
    }

    static public final Element $ElementSetAttribute(final Element element, final String attributeName, final String attributeValue) {
        element.setAttribute(attributeName, attributeValue);
        return element;
    }

}
