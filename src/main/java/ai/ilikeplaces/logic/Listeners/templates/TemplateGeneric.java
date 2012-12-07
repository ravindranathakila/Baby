package ai.ilikeplaces.logic.Listeners.templates;

import ai.doc.License;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.LogNull;
import ai.ilikeplaces.util.Loggers;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.tmpl.TemplateSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class TemplateGeneric implements TemplateSource {
// ------------------------------ FIELDS ------------------------------

    public static final String PAGE_NAME_PARAMETER_KEY = "name";
    public static final String HTML_DOT_EXTENSION = ".html";
    public static final String ROOT = "/";
    private static final String PREVENT_INTRUTION_TO_OTHER_TEMPLATES_FOLDER = "public" + ROOT;

    private final boolean mustReload;
    public static final String SORRY_NO_SUCH_RESOURCE = "SORRY! NO SUCH RESOURCE.";
    public static final String SORRY_GIVEN_RESOURCE_IS_NOT_VALID = "SORRY! GIVEN RESOURCE IS NOT VALID!";

// --------------------------- CONSTRUCTORS ---------------------------

    public TemplateGeneric(final boolean mustReload) {
        this.mustReload = mustReload;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface TemplateSource ---------------------

    @Override
    public boolean isMustReload(final ItsNatServletRequest itsNatServletRequest, final ItsNatServletResponse response) {
        return mustReload;
    }

    @Override
    public InputStream getInputStream(final ItsNatServletRequest itsNatServletRequest, final ItsNatServletResponse response) {

        final String realPath__ = itsNatServletRequest.getItsNatServlet().getItsNatServletContext().getServletContext().getRealPath(ROOT);

        /* Security measure to prevent access to ai.ilikeplaces. folders */
        final String pathPrefix__ = realPath__ + Controller.WEB_INF_PAGES + PREVENT_INTRUTION_TO_OTHER_TEMPLATES_FOLDER;

        final String pageName = itsNatServletRequest.getServletRequest().getParameter(PAGE_NAME_PARAMETER_KEY);

        InputStream requestedResource = null;

        try {
            requestedResource = new FileInputStream(pathPrefix__ + pageName + HTML_DOT_EXTENSION);
        } catch (FileNotFoundException e) {
            Loggers.EXCEPTION.error(SORRY_NO_SUCH_RESOURCE, e);
        }

        return (InputStream) LogNull.logThrow(requestedResource, SORRY_GIVEN_RESOURCE_IS_NOT_VALID);
    }

// ------------------------ CANONICAL METHODS ------------------------


// -------------------------- STATIC METHODS --------------------------
}
