package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: May 29, 2010
 * Time: 3:12:56 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class HeaderFilter implements Filter {

    private FilterConfig filterConfig;

    private Map<String, String> headersMap;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

        String headerParam = filterConfig.getInitParameter("header");
        if (headerParam == null) {
            Loggers.DEBUG.error("No headers were found in the web.xml (init-param) for the HeaderFilter !");
            return;
        }

        // Init the header list :  
        headersMap = new LinkedHashMap<String, String>();
        
        if (headerParam.contains("|")) {
            String[] headers = headerParam.split("|");
            for (String header : headers) {
                parseHeader(header);
            }

        } else {
            parseHeader(headerParam);
        }

        // Log configured headers .  
        Loggers.DEBUG.info("The following headers were registered in the HeaderFilter :");
        Set<Map.Entry<String, String>> headers = headersMap.entrySet();
        for (Map.Entry<String, String> item : headers) {
            Loggers.DEBUG.info(item.getKey() + ':' + item.getValue());
        }
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (headersMap != null) {
            // Add the header to the response  
            Set<Map.Entry<String, String>> headers = headersMap.entrySet();
            for (Map.Entry<String, String> header : headers) {
                ((HttpServletResponse) response).setHeader(header.getKey(), header.getValue());
            }
        }
        // Continue  
        chain.doFilter(request, response);
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
}  