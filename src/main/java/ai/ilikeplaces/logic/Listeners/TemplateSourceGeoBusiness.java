package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.util.ExceptionCache;
import ai.ilikeplaces.util.Loggers;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.tmpl.TemplateSource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class TemplateSourceGeoBusiness implements TemplateSource {
// ------------------------------ FIELDS ------------------------------

    public static final String DOMAIN = "domain";
    public static final String USER_AGENT = "User-Agent";
    public static final String HTML = ".html";
    public static final String URISYNTAX_EXCEPTION = "URISyntaxException";
    public static final String MALFORMED_URLEXCEPTION = "MalformedURLException";
    public static final String IOEXCEPTION = "IOException";
    public static final String ILIKEPLACES_COM = "ilikeplaces.com";
    public static final String LOCALHOST_8080 = "localhost:8080";
    public static final String WWW_ILIKEPLACES_COM = "www.ilikeplaces.com";
    public static final String SLASH = "/";

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface TemplateSource ---------------------

    @Override
    public boolean isMustReload(final ItsNatServletRequest itsNatServletRequest, final ItsNatServletResponse response) {
        return true;
    }

    @Override
    public InputStream getInputStream(final ItsNatServletRequest itsNatServletRequest, final ItsNatServletResponse response) {
        final String domain;
        final InputStream returnVal;

        try {
            domain = new URI(((HttpServletRequest) itsNatServletRequest.getServletRequest()).getRequestURL().toString()).getHost();
            if (domain.endsWith(ILIKEPLACES_COM) || domain.contains(LOCALHOST_8080)) {//Normal request on our website. Just forward.
                throw ExceptionCache.ILLEGAL_ACCESS_ERROR;
            } else {
                final URL url;

                TemplateSourceGeoBusinessType type = TemplateSourceGeoBusinessType.FullyImplementedHTML;

                switch (type) {
                    case FullyImplementedHTML:
                        url = new URL(type.toString().replace(WWW_ILIKEPLACES_COM, WWW_ILIKEPLACES_COM) + domain + SLASH + domain + HTML);
                        URLConnection conn = url.openConnection();
                        HttpServletRequest httpRequest = (HttpServletRequest) itsNatServletRequest.getServletRequest();
                        String userAgent = httpRequest.getHeader(USER_AGENT);
                        conn.setRequestProperty(USER_AGENT, userAgent);
                        returnVal = conn.getInputStream();
                        break;
                    case PiratesAttackingUsPirates:
                        throw ExceptionCache.ILLEGAL_ACCESS_ERROR;//IMPLEMENT to string and redirect to pirate caught page instead
                    default:
                        throw ExceptionCache.UNSUPPORTED_SWITCH;
                }
            }
        } catch (final URISyntaxException e) {
            Loggers.EXCEPTION.error(URISYNTAX_EXCEPTION, e);
            throw ExceptionCache.ILLEGAL_ACCESS_ERROR;
        } catch (final MalformedURLException e) {
            Loggers.EXCEPTION.error(MALFORMED_URLEXCEPTION, e);
            throw ExceptionCache.ILLEGAL_ACCESS_ERROR;
        } catch (final IOException e) {
            Loggers.EXCEPTION.error(IOEXCEPTION, e);
            throw ExceptionCache.ILLEGAL_ACCESS_ERROR;
        }

        return returnVal;
    }

// -------------------------- ENUMERATIONS --------------------------

    public static enum TemplateSourceGeoBusinessType {
        FullyImplementedHTML() {
            /**
             * @return the url BASE, starting with http: if web based, or file: if local. use base/www.exampledomain.co.uk.html format
             */
            @Override
            public String toString() {
                return "http:" + SLASH + "/www.ilikeplaces.com/cdn/FullyImplementedHTML/";
            }},

        PiratesAttackingUsPirates() {
            /**
             * @return the url of this template, starting with http: if web based, or file: if local.
             */
            @Override
            public String toString() {
                throw ExceptionCache.UNSUPPORTED_OPERATION_EXCEPTION;//This should be the url of the pirate caught page
            }};

        /**
         * @return the url of this template, starting with http: if web based, or file: if local.
         */
        @Override
        public String toString() {
            return super.toString();
        }
    }
}