package ai.baby.util;

import ai.scribble.License;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jul 10, 2010
 * Time: 1:21:24 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class HTMLDocParser {

    private HTMLDocParser() {
        throw ExceptionCache.STATIC_USAGE_ONLY_EXCEPTION;
    }

    /**
     * Please note that file paths should  contain only backslashes. Slashes can be mistaken to protocol definition by
     * the Parser. so use c:\ and not c:/
     *
     * @param filePath
     * @return
     * @throws IOException
     * @throws SAXException
     */
    public static Document getDocument(final String filePath) throws IOException, SAXException {
        final DOMParser domParser = new DOMParser();
        domParser.parse(filePath);
        return domParser.getDocument();
    }

    public static String getDocumentAsString(final String filePath) throws IOException, SAXException, TransformerException {
        final DOMParser domParser = new DOMParser();
        domParser.parse(filePath);
        return convertNodeToHtml(domParser.getDocument());
    }

    public static String convertNodeToHtml(final Node node) throws TransformerException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter sw = new StringWriter();
        t.transform(new DOMSource(node), new StreamResult(sw));
        return sw.toString();
    }

    public static void print(final Node node, final String indent) {
        System.out.println(indent + node.getClass().getName());
        Node child = node.getFirstChild();
        while (child != null) {
            print(child, indent + " ");
            child = child.getNextSibling();
        }
    }


    /**
     * Fetches the element disregarding widget behavior. i.e. ignores dynamic nature of ID's
     *
     * @param key__
     * @param document__
     * @return
     */
    public static Element $(final String key__, final Document document__) {
        return document__.getElementById(key__);
    }

}
