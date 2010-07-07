package ai.ilikeplaces.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Copied off
 * http://www.aviransplace.com/2008/01/08/make-http-post-or-get-request-from-java/?blogthis=msn
 */
public class HTTPRequestPoster {

    public static void main(String args[]) {
        System.out.println(sendGetRequest("http://disqus.com/api/get_forum_api_key/?user_api_key=IcChJgsHPtgVQIXP44cuA2BwJIEtvmamJJnmrvrN9L8Nfjy2dMGRgtE3YK28n4BU&api_version=1.1&forum_id=375663", ""));
        System.out.println(sendGetRequest("http://disqus.com/api/get_thread_by_url/?forum_api_key=0bY9W6lCnbhufNyCFbO58FrlAD72ixWeF0GTiXSwXwPYQIWJ0xudVi9FHNZCOAS0&url=http://www.ilikeplaces.com/page/Antananarivo_of_Madagascar?WOEID=2346150", ""));
        System.out.println(sendGetRequest("http://disqus.com/api/get_thread_posts/?thread_id=109673804&forum_api_key=0bY9W6lCnbhufNyCFbO58FrlAD72ixWeF0GTiXSwXwPYQIWJ0xudVi9FHNZCOAS0&exclude='killed'", ""));

    }

    /**
     * Sends an HTTP GET request to a url
     *
     * @param endpoint          - The URL of the server. (Example: " http://www.yahoo.com/search")
     * @param requestParameters - all the request parameters (Example: "param1=val1&param2=val2"). Note: This method will add the question mark (?) to the request - DO NOT add it yourself
     * @return - The response from the end point
     */
    public static String sendGetRequest(String endpoint, String requestParameters) {
        String result = null;
        if (endpoint.startsWith("http://")) {
// Send a GET request to the servlet
            try {
// Construct data
                StringBuffer data = new StringBuffer();
// Send data
                String urlStr = endpoint;
                if (requestParameters != null && requestParameters.length() > 0) {
                    urlStr += "?" + requestParameters;
                }
                URL url = new URL(urlStr);
                URLConnection conn = url.openConnection();
// Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                result = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Reads data from the data reader and posts it to a server via POST request.
     * data - The data you want to send
     * endpoint - The server's address
     * output - writes the server's response to output
     *
     * @throws Exception
     */
    public static void postData(Reader data, URL endpoint, Writer output) throws Exception {
        HttpURLConnection urlc = null;
        try {
            urlc = (HttpURLConnection) endpoint.openConnection();
            try {
                urlc.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new Exception("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
            }
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/xml; charset=" + "UTF-8");
            OutputStream out = urlc.getOutputStream();
            try {
                Writer writer = new OutputStreamWriter(out, "UTF-8");
                pipe(data, writer);
                writer.close();
            } catch (IOException e) {
                throw new Exception("IOException while posting data", e);
            } finally {
                if (out != null)
                    out.close();
            }
            InputStream in = urlc.getInputStream();
            try {
                Reader reader = new InputStreamReader(in);
                pipe(reader, output);
                reader.close();
            } catch (IOException e) {
                throw new Exception("IOException while reading response", e);
            } finally {
                if (in != null)
                    in.close();
            }
        } catch (IOException e) {
            throw new Exception("Connection error (is server running at " + endpoint + " ?): " + e);
        } finally {
            if (urlc != null)
                urlc.disconnect();
        }
    }

    /**
     * Pipes everything from the reader to the writer via a buffer
     */
    private static void pipe(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[1024];
        int read = 0;
        while ((read = reader.read(buf)) >= 0) {
            writer.write(buf, 0, read);
        }
        writer.flush();
    }
}