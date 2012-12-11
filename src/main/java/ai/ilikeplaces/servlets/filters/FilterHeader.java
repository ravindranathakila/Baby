package ai.ilikeplaces.servlets.filters;

import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.Loggers;
import ai.scribble.License;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: May 29, 2010
 * Time: 3:12:56 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class FilterHeader implements Filter {

    private FilterConfig filterConfig;

    private Map<String, String> headersMap;

    static private String BUILD_NUMBER;
    static private String[] RECINC_ARRAY;
    static private int YEAR;
    static private int MONTH;
    static private int DATE;
    static private int HOUR;
    static private int MINUTE;
    static private int SECOND;
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String PUBLIC_MAX_AGE = "PUBLIC, max-age=";
    private static final String MUST_REVALIDATE = ", must-revalidate";
    private static final String LAST_MODIFIED = "Last-Modified";
    private static final String EXPIRES = "Expires";
    private static final String ETAG = "ETag";
    private static final String EEE_DD_MMM_YYYY_HH_MM_SS_Z = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String GMT = "GMT";
    private static final GregorianCalendar CAL = new GregorianCalendar();
    private static Date DEPLOYED;


    static {
        try {
            BUILD_NUMBER = RBGet.globalConfig.getString("bn");
            Loggers.DEBUG.debug(BUILD_NUMBER.split("-")[1]);
            RECINC_ARRAY = BUILD_NUMBER.split("-")[1].replace(".", "-").split("-");  //replaced to avoid regex behavior
            Loggers.DEBUG.debug(Arrays.toString(BUILD_NUMBER.split("-")[1].replace(".", "-").split("-")));
            YEAR = Integer.parseInt(RECINC_ARRAY[2]);
            MONTH = Integer.parseInt(RECINC_ARRAY[3]) - 1;//Did that take time to fix :D
            DATE = Integer.parseInt(RECINC_ARRAY[4]);
            HOUR = Integer.parseInt(RECINC_ARRAY[5]);
            MINUTE = Integer.parseInt(RECINC_ARRAY[6]);
            SECOND = Integer.parseInt(RECINC_ARRAY[7]);

            CAL.set(YEAR, MONTH, DATE, HOUR, MINUTE, SECOND);//Deployed time
            CAL.setTimeZone(TimeZone.getTimeZone(GMT));
            DEPLOYED = new Date(CAL.getTimeInMillis());

        } catch (final Throwable t) {
            Loggers.EXCEPTION.error("", t);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            this.filterConfig = filterConfig;

            // Init the header list :
            this.headersMap = new LinkedHashMap<String, String>();

            String headerParam = filterConfig.getInitParameter("header");
            if (headerParam == null) {
                Loggers.DEBUG.error("No headers were found in the web.xml (init-param) for the HeaderFilter !");
                //return;
            } else {
                if (headerParam.contains("|")) {
                    String[] headers = headerParam.split("|");
                    for (String header : headers) {
                        parseHeader(header);
                    }

                } else {
                    parseHeader(headerParam);
                }
            }

            // Log configured headers .
            Loggers.DEBUG.debug("The following headers were registered in the HeaderFilter :");
            Set<Map.Entry<String, String>> headers = headersMap.entrySet();
            for (Map.Entry<String, String> item : headers) {
                Loggers.DEBUG.debug(item.getKey() + ':' + item.getValue());
            }
            Loggers.INFO.info("BUILD NUMBER:" + BUILD_NUMBER);
            Loggers.DEBUG.debug("REINC ARRAY:" + Arrays.toString(RECINC_ARRAY));
            Loggers.INFO.info("BUILD DATE:" + YEAR + " " + (MONTH + 1) + " " + DATE + " " + HOUR + " " + MINUTE + " " + SECOND + " ");
        } catch (final Throwable t) {
            Loggers.EXCEPTION.error("", t);
        }
    }


    /**
     * We expect the client to detect the last modified date. Apart from that, two weaks of caching would be ok for now.
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse servletResponse = (HttpServletResponse) response;
        try {
            if (headersMap != null) {
                // Add the header to the response
                Set<Map.Entry<String, String>> headers = headersMap.entrySet();
                for (Map.Entry<String, String> header : headers) {
                    servletResponse.setHeader(header.getKey(), header.getValue());
                }
            }
            setCacheExpireDate(servletResponse, 2 * 24 * 60 * 60);
            setEtag(servletResponse);
        } catch (final Throwable t) {
            Loggers.EXCEPTION.error("", t);
        } finally {
            chain.doFilter(request, servletResponse);
        }
    }

    public void destroy() {
        this.filterConfig = null;
        this.headersMap = null;
    }

    private void parseHeader(final String header) {
        String headerName = header.substring(0, header.indexOf(":"));
        if (!headersMap.containsKey(headerName)) {
            headersMap.put(headerName, header.substring(header.indexOf(":") + 1));
        }
    }

    public static void setCacheExpireDate(final HttpServletResponse response, final int seconds) {
        if (response != null) {

            response.setHeader(CACHE_CONTROL, PUBLIC_MAX_AGE + (seconds) + MUST_REVALIDATE);

            response.setHeader(LAST_MODIFIED, htmlExpiresDateFormat().format(DEPLOYED));

            final Date expires = new Date(System.currentTimeMillis() + seconds * 1000);
            response.setHeader(EXPIRES, htmlExpiresDateFormat().format(expires.getTime()));

        }
    }

    public static void setEtag(HttpServletResponse response) {
        if (response != null) {
            response.setHeader(ETAG, BUILD_NUMBER);
        }
    }

    public static DateFormat htmlExpiresDateFormat() {
        DateFormat httpDateFormat = new SimpleDateFormat(EEE_DD_MMM_YYYY_HH_MM_SS_Z, Locale.US);
        httpDateFormat.setTimeZone(TimeZone.getTimeZone(GMT));
        return httpDateFormat;
    }

}  
