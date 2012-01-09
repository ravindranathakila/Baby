package ai.ilikeplaces.servlets;

import ai.ilikeplaces.doc.DOCUMENTATION;
import ai.ilikeplaces.doc.LOGIC;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.Parameter;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/7/12
 * Time: 10:52 AM
 */
@DOCUMENTATION(
        NOTE = @NOTE(
                "This class was specifically designed to be vendor independent." +
                        "Much effort was made to build it by looking at http://tools.ietf.org/html/draft-ietf-oauth-v2 . " +
                        "Please immediately report any bugs you find at https://www.pivotaltracker.com/projects/304913 . " +
                        "Thank you!"),
        LOGIC = @LOGIC(
                @NOTE("A servlet is a shared resource." +
                        "Hence this class can contain only shared details of a the application as a client. " +
                        "It shall under no circumstance, contain data related to a specific user." +
                        "Since it is abstract, this class however, can have data related to a specific OAuth facilitator such as Facebook, Twitter and Google " +
                        "as each new instance with be created for that specific vendor and shared within that pool (logical pool).  ")))
public abstract class AbstractOAuth extends HttpServlet {
// ------------------------------ FIELDS ------------------------------

    // ------------------------------ FIELDS STATIC --------------------------
    static final RuntimeException RedirectToOAuthEndpointFailed = new RuntimeException("Redirect to OAuth Endpoint Failed!");
    static final String code = "code";
    static final String redirect_uri = "redirect_uri";
    static final String client_secret = "client_secret";
    static final String client_id = "client_id";
    static final String response_type = "response_type";
    static final String scope = "scope";
    static final String state = "state";
    static final String access_token = "access_token";
    static final String expires_in = "expires_in";
    static final String refresh_token = "refresh_token";
    static final String parameters = "parameters";
    static final String token_type = "token_type";


    private static final String QUESTION_MARK = "?";
    private static final String GOT_ERROR_CODE = "Got error code:";
    private static final String DOT_JASON = ".json";
    private static final String EQUALS = "=";
    private static final String API_KEY = "key" + EQUALS;
    private static final String AMPERSAND = "&";
    private static final String OPEN_SQR_BRCKT = "[";
    private static final String CLOSE_SQR_BRCKT = "]";
    private static final String EMPTY = "";
    private static final String expires = "expires";
    private static final String COMMA = ",";


// ------------------------------ FIELDS (NON-STATIC)--------------------


    final OAuthAuthorizationRequest oAuthAuthorizationRequest;
    final String oAuthEndpoint;
    final String oAuthAuthorizationEndpoint;

    /**
     * @see <a href='http://hc.apache.org/httpclient-3.x/threading.html'>httpclient threading</a>
     */
    private final HttpClient threadSafeHttpClient;
    private String api_key;
    private String jsonEndpoint;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * 4.1.1.  Authorization Request
     * <p/>
     * <p/>
     * The client constructs the request URI by adding the following
     * parameters to the query component of the authorization oAuthEndpoint URI
     * using the "application/x-www-form-urlencoded" format as defined by
     * [W3C.REC-html401-19991224]:
     * <p/>
     * <p/>
     * <b>response_type</b>
     * <p/>
     * REQUIRED.  Value MUST be set to "code".
     * <p/>
     * <b>client_id</b>
     * <p/>
     * REQUIRED.  The client identifier as described in Section 2.2.
     * <p/>
     * <b>redirect_uri</b>
     * <p/>
     * OPTIONAL, as described in Section 3.1.2.
     * <p/>
     * <b>scope</b>
     * <p/>
     * OPTIONAL.  The scope of the access request as described by
     * Section 3.3.
     * <p/>
     * <b>state</b>
     * <p/>
     * RECOMMENDED.  An opaque value used by the client to maintain
     * state between the request and callback.  The authorization
     * server includes this value when redirecting the user-agent back
     * to the client.  The parameter SHOULD be used for preventing
     * cross-site request forgery as described in Section 10.12.
     * <p/>
     * <p/>
     * The client directs the resource owner to the constructed URI using an
     * HTTP redirection response, or by other means available to it via the
     * user-agent.
     * <p/>
     * For example, the client directs the user-agent to make the following
     * HTTP request using transport-layer security (extra line breaks are
     * for display purposes only):
     * <p/>
     * <p/>
     * GET /authorize?response_type=code&client_id=s6BhdRkqt3&state=xyz
     * &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb HTTP/1.1
     * <p/>
     * Host: server.example.com
     */
    @LOGIC(
            @NOTE("Here our main focus ist to initialize data related to " +
                    "<a href='http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-4.1.1'>http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-4.1.1</a> . "))
    public AbstractOAuth() {
        this.oAuthEndpoint = oAuthProvider().oAuthEndpoint;
        this.oAuthAuthorizationEndpoint = oAuthProvider().oAuthAuthorizationEndpoint;
        this.oAuthAuthorizationRequest = oAuthProvider().oAuthAuthorizationRequest;
        final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        threadSafeHttpClient = new HttpClient(connectionManager);
    }

    abstract OAuthProvider oAuthProvider();

// ------------------------ CANONICAL METHODS ------------------------


// -------------------------- OTHER METHODS --------------------------

    /**
     * @param endpointEndValue
     * @param parameters
     * @return
     */
    public JSONObject getHttpContentAsJson(final String endpointEndValue, final Map<String, String> parameters) {
        final StringBuilder sb = new StringBuilder(EMPTY);
        for (final String key : parameters.keySet()) {
            sb.append(AMPERSAND).append(key).append(EQUALS).append(parameters.get(key));
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(getHttpContent(endpointEndValue, sb.toString()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }


    /**
     * @param endpointEndValue
     * @param parameters
     * @return
     */
    public JSONObject postHttpContentAsJson(final String endpointEndValue, final Map<String, String> parameters) {
        final StringBuilder sb = new StringBuilder(EMPTY);
        for (final String key : parameters.keySet()) {
            sb.append(AMPERSAND).append(key).append(EQUALS).append(parameters.get(key));
        }

        JSONObject jsonObject;
        try {
            final String response = postHttpContent(endpointEndValue, sb.toString());
            Loggers.info(response);
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

    /**
     * @param endpointEndValue
     * @param optionalAppend   All strings in array will be concatenated and appended
     * @return
     */
    String getHttpContent(final String endpointEndValue, final String... optionalAppend) {
        final String toBeCalled = endpointEndValue
                + QUESTION_MARK
                + ((optionalAppend != null && optionalAppend.length != 0) ? Arrays.toString(optionalAppend).replace(OPEN_SQR_BRCKT, EMPTY).replace(CLOSE_SQR_BRCKT, EMPTY) : EMPTY);

        final GetMethod getMethod = new GetMethod(toBeCalled);

        int statusCode = 0;
        try {
            statusCode = threadSafeHttpClient.executeMethod(getMethod);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
            throw new RuntimeException(GOT_ERROR_CODE + statusCode);
        }
        InputStream inputStream = null;

        try {
            inputStream = getMethod.getResponseBodyAsStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String accumulator = EMPTY;
        try {
            while ((line = br.readLine()) != null) {
                accumulator += line;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accumulator;
    }

    /**
     * @param endpointEndValue
     * @param optionalAppend   All strings in array will be concatenated and appended
     * @return
     */
    String postHttpContent(final String endpointEndValue, final String... optionalAppend) {
        final String toBeCalled = endpointEndValue
                + QUESTION_MARK
                + ((optionalAppend != null && optionalAppend.length != 0) ? Arrays.toString(optionalAppend).replace(OPEN_SQR_BRCKT, EMPTY).replace(CLOSE_SQR_BRCKT, EMPTY) : EMPTY);

        final PostMethod getMethod = new PostMethod(toBeCalled);

        int statusCode = 0;
        try {
            statusCode = threadSafeHttpClient.executeMethod(getMethod);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
            throw new RuntimeException(GOT_ERROR_CODE + statusCode);
        }
        InputStream inputStream = null;

        try {
            inputStream = getMethod.getResponseBodyAsStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String accumulator = EMPTY;
        try {
            while ((line = br.readLine()) != null) {
                accumulator += line;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accumulator;
    }

    /**
     * @param endpointEndValue
     * @param headerName
     * @param optionalAppend   All strings in array will be concatenated and appended
     * @return
     */
    Header getHttpHeader(final String endpointEndValue, final String headerName, final String... optionalAppend) {
        final String toBeCalled = jsonEndpoint + endpointEndValue
                + QUESTION_MARK
                + ((optionalAppend != null && optionalAppend.length != 0) ? Arrays.toString(optionalAppend).replace(OPEN_SQR_BRCKT, EMPTY).replace(CLOSE_SQR_BRCKT, EMPTY) : EMPTY);


        final GetMethod getMethod = new GetMethod(toBeCalled);

        int statusCode = 0;
        try {
            statusCode = threadSafeHttpClient.executeMethod(getMethod);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
            throw new RuntimeException(GOT_ERROR_CODE + statusCode);
        }

        return getMethod.getRequestHeader(headerName);
    }

    /**
     * Only supports format: application/x-www-form-urlencoded
     *
     * @param oAuthAuthorizationResponse
     * @param clientAuthentication
     * @return
     * @see <a href='http://tools.ietf.org/html/draft-ietf-oauth-v2-05#section-3.3.2'>http://tools.ietf.org/html/draft-ietf-oauth-v2-05#section-3.3.2</a>
     */
    OAuthAccessTokenResponse getOAuthAccessTokenResponse(final OAuthAuthorizationResponse oAuthAuthorizationResponse, final ClientAuthentication clientAuthentication) {
        //Last time we checked, this did now work. 
        /*final Header[] oAuthAccessTokenResponseHeaders = getHttpHeaders(
                oAuthAuthorizationEndpoint,
                new Parameter()
                        .append(code, oAuthAuthorizationResponse.code)
                        .append(client_id, clientAuthentication.client_id)
                        .append(client_secret, clientAuthentication.client_secret)
                        .append(redirect_uri, "http://www.ilikeplaces.com/oauth2")
                        .get()
        );  */

        final String access_token_string = getHttpContent(
                oAuthAuthorizationEndpoint,
                new Parameter()
                        .append(code, oAuthAuthorizationResponse.code)
                        .append(client_id, clientAuthentication.client_id)
                        .append(redirect_uri, clientAuthentication.redirect_uri)
                        .append(client_secret, clientAuthentication.client_secret)
                        .get()
        );

        String name;
        String value;

        String access_token_value = AbstractOAuth.EMPTY;

        String token_type_value = AbstractOAuth.EMPTY;

        String expires_in_value = AbstractOAuth.EMPTY;

        String refresh_token_value = AbstractOAuth.EMPTY;

        String parameters_value = AbstractOAuth.EMPTY;

        for (final String key_value : access_token_string.split("&")) {

            final String[] split = key_value.split("=");
            name = split[0];
            value = split[1];

            Loggers.debug(name + COMMA + value);

            if (name.equals(access_token)) {
                access_token_value = value;
                continue;
            }
            if (name.equals(token_type)) {
                token_type_value = value;
                continue;
            }
            if (name.equals(expires_in) || /*I know. Facebook!!!*/name.equals(expires)) {
                expires_in_value = value;
                continue;
            }
            if (name.equals(refresh_token)) {
                refresh_token_value = value;
                continue;
            }
            if (name.equals(parameters)) {
                parameters_value = value;
                continue;
            }
        }

        return new OAuthAccessTokenResponse(access_token_value, token_type_value, expires_in_value, refresh_token_value, parameters_value);
    }

    /**
     * Only supports format: application/x-www-form-urlencoded
     *
     * @param oAuthAuthorizationResponse
     * @param clientAuthentication
     * @return
     * @see <a href='http://tools.ietf.org/html/draft-ietf-oauth-v2-05#section-3.3.2'>http://tools.ietf.org/html/draft-ietf-oauth-v2-05#section-3.3.2</a>
     */

    OAuthAccessTokenResponse getOAuthAccessTokenResponseUsingJson(final OAuthAuthorizationResponse oAuthAuthorizationResponse, final ClientAuthentication clientAuthentication) {

        final JSONObject access_token_string = postHttpContentAsJson(oAuthAuthorizationEndpoint,
                new HashMap<String, String>() {
                    {
                        put(code, oAuthAuthorizationResponse.code);
                        put(client_id, clientAuthentication.client_id);
                        put(redirect_uri, clientAuthentication.redirect_uri);
                        put(client_secret, clientAuthentication.client_secret);
                        put("grant_type", "authorization_code");
                    }
                });


        String access_token_value = AbstractOAuth.EMPTY;

        String token_type_value = AbstractOAuth.EMPTY;

        String expires_in_value = AbstractOAuth.EMPTY;

        String refresh_token_value = AbstractOAuth.EMPTY;

        String parameters_value = AbstractOAuth.EMPTY;


        try {
            access_token_value = access_token_string.getString(access_token);
        } catch (final JSONException e) {
            //hell uf a world!
        }

        try {
            token_type_value = access_token_string.getString(token_type);
        } catch (final JSONException e) {
            //hell uf a world!
        }

        try {
            expires_in_value = access_token_string.getString(expires_in);
        } catch (final JSONException e) {
            //hell uf a world!
        }

        try {
            refresh_token_value = access_token_string.getString(refresh_token);
        } catch (final JSONException e) {
            //hell uf a world!
        }

        try {
            parameters_value = access_token_string.getString(parameters);
        } catch (final JSONException e) {
            //hell uf a world!
        }


        return new OAuthAccessTokenResponse(access_token_value, token_type_value, expires_in_value, refresh_token_value, parameters_value);
    }

    /**
     * @param endpointEndValue
     * @param optionalAppend   All strings in array will be concatenated and appended
     * @return
     */
    Header[] getHttpHeaders(final String endpointEndValue, final String... optionalAppend) {
        final String toBeCalled = endpointEndValue
                + QUESTION_MARK
                + ((optionalAppend != null && optionalAppend.length != 0) ? Arrays.toString(optionalAppend).replace(OPEN_SQR_BRCKT, EMPTY).replace(CLOSE_SQR_BRCKT, EMPTY) : EMPTY);


        final GetMethod getMethod = new GetMethod(toBeCalled);

        int statusCode = 0;
        try {
            statusCode = threadSafeHttpClient.executeMethod(getMethod);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
            throw new RuntimeException(GOT_ERROR_CODE + statusCode);
        }

        return getMethod.getRequestHeaders();
    }

    /**
     * @param request
     * @param response
     * @return OAuthAuthorizationResponse or redirects user to endpoint and returns null
     */
    OAuthAuthorizationResponse getOAuthAuthorizationResponse(final HttpServletRequest request, final HttpServletResponse response) {
        final String code = request.getParameter(AbstractOAuth.code);
        final String state = request.getParameter(AbstractOAuth.state);

        if (code == null || code.isEmpty()) {
            try {
                response.sendRedirect(
                        new Parameter(this.oAuthEndpoint)
                                .append(client_id, this.oAuthAuthorizationRequest.client_id, true)
                                .append(redirect_uri, this.oAuthAuthorizationRequest.redirect_uri)
                                .append(response_type, this.oAuthAuthorizationRequest.response_type)
                                .append(scope, this.oAuthAuthorizationRequest.scope)
                                .append(AbstractOAuth.state, this.oAuthAuthorizationRequest.state)
                                .get()

                );
            } catch (final IOException e) {
                //hmmm!
                throw RedirectToOAuthEndpointFailed;
            }
            return null;
        } else {
            return new OAuthAuthorizationResponse(code, state);
        }
    }

// -------------------------- INNER CLASSES --------------------------

    public final class OAuthProvider {
// ------------------------------ FIELDS STATIC --------------------------


// ------------------------------ FIELDS (NON-STATIC)--------------------


        final public OAuthAuthorizationRequest oAuthAuthorizationRequest;
        final public String oAuthEndpoint;
        final public String oAuthAuthorizationEndpoint;

        /**
         * @param oAuthEndpoint             Such as facebook - https://www.facebook.com/dialog/oauth
         * @param oAuthAuthorizationRequest Contains requred parameters for the endpoint
         */
        public OAuthProvider(
                final String oAuthEndpoint,
                final String oAuthAuthorizationEndpoint,
                final OAuthAuthorizationRequest oAuthAuthorizationRequest) {
            this.oAuthEndpoint = oAuthEndpoint;
            this.oAuthAuthorizationEndpoint = oAuthAuthorizationEndpoint;
            this.oAuthAuthorizationRequest = oAuthAuthorizationRequest;
        }

        @Override
        public String toString() {
            return "OAuthProvider{" +
                    "oAuthAuthorizationRequest=" + oAuthAuthorizationRequest +
                    ", oAuthEndpoint='" + oAuthEndpoint + '\'' +
                    ", oAuthAuthorizationEndpoint='" + oAuthAuthorizationEndpoint + '\'' +
                    '}';
        }
    }

    /**
     * * 4.1.1.  Authorization Request
     * <p/>
     * <p/>
     * The client constructs the request URI by adding the following
     * parameters to the query component of the authorization endpoint URI
     * using the "application/x-www-form-urlencoded" format as defined by
     * [W3C.REC-html401-19991224]:
     * <p/>
     * <p/>
     * <b>response_type</b>
     * <p/>
     * REQUIRED.  Value MUST be set to "code".
     * <p/>
     * <b>client_id</b>
     * <p/>
     * REQUIRED.  The client identifier as described in Section 2.2.
     * <p/>
     * <b>redirect_uri</b>
     * <p/>
     * OPTIONAL, as described in Section 3.1.2.
     * <p/>
     * <b>scope</b>
     * <p/>
     * OPTIONAL.  The scope of the access request as described by
     * Section 3.3.
     * <p/>
     * <b>state</b>
     * <p/>
     * RECOMMENDED.  An opaque value used by the client to maintain
     * state between the request and callback.  The authorization
     * server includes this value when redirecting the user-agent back
     * to the client.  The parameter SHOULD be used for preventing
     * cross-site request forgery as described in Section 10.12.
     * <p/>
     * <p/>
     * The client directs the resource owner to the constructed URI using an
     * HTTP redirection response, or by other means available to it via the
     * user-agent.
     * <p/>
     * For example, the client directs the user-agent to make the following
     * HTTP request using transport-layer security (extra line breaks are
     * for display purposes only):
     * <p/>
     * <p/>
     * GET /authorize?response_type=code&client_id=s6BhdRkqt3&state=xyz
     * &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb HTTP/1.1
     * <p/>
     * Host: server.example.com
     */
    @LOGIC(
            @NOTE("By nature, we don't want this object to be modified after construction."))
    public final class OAuthAuthorizationRequest {
// ------------------------------ FIELDS (NON-STATIC)--------------------


        /**
         * REQUIRED.  Value MUST be set to "code".
         */
        final String response_type;

        /**
         * REQUIRED.  The client identifier as described in Section 2.2.
         */
        final String client_id;

        /**
         * OPTIONAL, as described in Section 3.1.2.
         */
        final String redirect_uri;

        /**
         * OPTIONAL.  The scope of the access request as described by
         */
        final String scope;

        /**
         * RECOMMENDED.  An opaque value used by the client to maintain
         * state between the request and callback.  The authorization
         * server includes this value when redirecting the user-agent back
         * to the client.  The parameter SHOULD be used for preventing
         * cross-site request forgery as described in Section 10.12.
         */
        final String state;

        @LOGIC(
                @NOTE("By nature, we don't want this object to be modified after construction."))
        public OAuthAuthorizationRequest(final String response_type,
                                         final String client_id,
                                         final String redirect_uri,
                                         final String scope,
                                         final String state) {
            this.response_type = response_type != null ? response_type : AbstractOAuth.EMPTY;
            this.client_id = client_id != null ? client_id : AbstractOAuth.EMPTY;
            this.redirect_uri = redirect_uri != null ? redirect_uri : AbstractOAuth.EMPTY;
            this.scope = scope != null ? scope : AbstractOAuth.EMPTY;
            this.state = state != null ? state : AbstractOAuth.EMPTY;
        }

        @Override
        public String toString() {
            return "OAuthAuthorizationRequest{" +
                    "response_type='" + response_type + '\'' +
                    ", client_id='" + client_id + '\'' +
                    ", redirect_uri='" + redirect_uri + '\'' +
                    ", scope='" + scope + '\'' +
                    ", state='" + state + '\'' +
                    '}';
        }
    }

    /**
     * If the resource owner grants the access request, the authorization
     * server issues an authorization code and delivers it to the client by
     * adding the following parameters to the query component of the
     * redirection URI using the "application/x-www-form-urlencoded" format:
     * <p/>
     * code
     * REQUIRED.  The authorization code generated by the
     * authorization server.  The authorization code MUST expire
     * shortly after it is issued to mitigate the risk of leaks.  A
     * maximum authorization code lifetime of 10 minutes is
     * RECOMMENDED.  The client MUST NOT use the authorization code
     * more than once.  If an authorization code is used more than
     * once, the authorization server MUST deny the request and SHOULD
     * attempt to revoke all tokens previously issued based on that
     * authorization code.  The authorization code is bound to the
     * client identifier and redirection URI.
     * state
     * REQUIRED if the "state" parameter was present in the client
     * authorization request.  The exact value received from the
     * client.
     * <p/>
     * For example, the authorization server redirects the user-agent by
     * sending the following HTTP response:
     * <p/>
     * <p/>
     * HTTP/1.1 302 Found
     * Location: https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA
     * &state=xyz
     * <p/>
     * <p/>
     * The client SHOULD ignore unrecognized response parameters.  The
     * authorization code string size is left undefined by this
     * specification.  The client should avoid making assumptions about code
     * value sizes.  The authorization server should document the size of
     * any value it issues.
     */
    @LOGIC(
            @NOTE("By nature, we don't want this object to be modified after construction."))
    public final class OAuthAuthorizationResponse {
// ------------------------------ FIELDS (NON-STATIC)--------------------


        /**
         * REQUIRED.  The authorization code generated by the
         * authorization server.  The authorization code MUST expire
         * shortly after it is issued to mitigate the risk of leaks.  A
         * maximum authorization code lifetime of 10 minutes is
         * RECOMMENDED.  The client MUST NOT use the authorization code
         * more than once.  If an authorization code is used more than
         * once, the authorization server MUST deny the request and SHOULD
         * attempt to revoke all tokens previously issued based on that
         * authorization code.  The authorization code is bound to the
         * client identifier and redirection URI.
         */
        final String code;

        /**
         * REQUIRED if the "state" parameter was present in the client
         * authorization request.  The exact value received from the
         * client.
         */
        final String state;

        public OAuthAuthorizationResponse(final String code, final String state) {
            this.code = code != null ? code : AbstractOAuth.EMPTY;
            this.state = state != null ? state : AbstractOAuth.EMPTY;
        }

        @Override
        public String toString() {
            return "OAuthAuthorizationResponse{" +
                    "code='" + code + '\'' +
                    ", state='" + state + '\'' +
                    '}';
        }
    }

    /**
     * The client makes a request to the token endpoint by adding the
     * following parameters using the "application/x-www-form-urlencoded"
     * format in the HTTP request entity-body:
     * <p/>
     * grant_type
     * REQUIRED.  Value MUST be set to "authorization_code".
     * code
     * REQUIRED.  The authorization code received from the
     * authorization server.
     * redirect_uri
     * REQUIRED, if the "redirect_uri" parameter was included in the
     * authorization request as described in Section 4.1.1, and their
     * values MUST be identical.
     * <p/>
     * If the client type is confidential or the client was issued client
     * credentials (or assigned other authentication requirements), the
     * client MUST authenticate with the authorization server as described
     * in Section 3.2.1.
     * <p/>
     * For example, the client makes the following HTTP request using
     * transport-layer security (extra line breaks are for display purposes
     * only):
     * <p/>
     * <p/>
     * POST /token HTTP/1.1
     * Host: server.example.com
     * Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
     * Content-Type: application/x-www-form-urlencoded;charset=UTF-8
     * <p/>
     * grant_type=authorization_code&code=SplxlOBeZQQYbYS6WxSbIA
     * &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb
     */
    public final class OAuthAccessTokenRequest {
// ------------------------------ FIELDS (NON-STATIC)--------------------


        /**
         * REQUIRED.  Value MUST be set to "authorization_code".
         */
        final String grant_type;

        /**
         * REQUIRED.  The authorization code received from the
         * authorization server.
         */
        final String code;

        /**
         * REQUIRED, if the "redirect_uri" parameter was included in the
         * authorization request as described in Section 4.1.1, and their
         * values MUST be identical.
         */
        final String redirect_uri;

        @LOGIC(
                @NOTE("By nature, we don't want this object to be modified after construction."))
        public OAuthAccessTokenRequest(final String grant_type, final String code, final String redirect_uri) {
            this.grant_type = grant_type != null ? grant_type : AbstractOAuth.EMPTY;
            this.code = code != null ? code : AbstractOAuth.EMPTY;
            this.redirect_uri = redirect_uri != null ? redirect_uri : AbstractOAuth.EMPTY;
        }

        @Override
        public String toString() {
            return "OAuthAccessTokenRequest{" +
                    "grant_type='" + grant_type + '\'' +
                    ", code='" + code + '\'' +
                    ", redirect_uri='" + redirect_uri + '\'' +
                    '}';
        }
    }

    /**
     * If the access token request is valid and authorized, the
     * authorization server issues an access token and optional refresh
     * token as described in Section 5.1.  If the request client
     * authentication failed or is invalid, the authorization server returns
     * an error response as described in Section 5.2.
     * <p/>
     * An example successful response:
     * <p/>
     * <p/>
     * HTTP/1.1 200 OK
     * Content-Type: application/json;charset=UTF-8
     * Cache-Control: no-store
     * Pragma: no-cache
     * <p/>
     * {
     * "access_token":"2YotnFZFEjr1zCsicMWpAA",
     * "token_type":"example",
     * "expires_in":3600,
     * "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
     * "example_parameter":"example_value"
     * }
     *
     * @see <a href='http://tools.ietf.org/html/draft-ietf-oauth-v2-05#section-3.3.2'>http://tools.ietf.org/html/draft-ietf-oauth-v2-05#section-3.3.2</a>
     */
    public final class OAuthAccessTokenResponse {
// ------------------------------ FIELDS (NON-STATIC)--------------------


        /**
         * REQUIRED.  The access token issued by the authorization server.
         */
        final String access_token;

        /**
         * REQUIRED.  The type of the token issued as described in Section 7.1.  Value is case insensitive.
         */
        final String token_type;

        /**
         * OPTIONAL.  The lifetime in seconds of the access token.  For
         * example, the value "3600" denotes that the access token will
         * expire in one hour from the time the response was generated.
         */
        final String expires_in;
        /**
         * OPTIONAL.  The refresh token which can be used to obtain new
         * access tokens using the same authorization grant as described
         * in Section 6.
         */
        final String refresh_token;

        /**
         * OPTIONAL.  The scope of the access token as described by
         */
        final String parameters;

        @LOGIC(
                @NOTE("By nature, we don't want this object to be modified after construction."))
        public OAuthAccessTokenResponse(final String access_token,
                                        final String token_type,
                                        final String expires_in,
                                        final String refresh_token,
                                        final String parameters) {
            this.access_token = access_token != null ? access_token : AbstractOAuth.EMPTY;
            this.token_type = token_type != null ? token_type : AbstractOAuth.EMPTY;
            this.expires_in = expires_in != null ? expires_in : AbstractOAuth.EMPTY;
            this.refresh_token = refresh_token != null ? refresh_token : AbstractOAuth.EMPTY;
            this.parameters = parameters != null ? parameters : AbstractOAuth.EMPTY;
        }

        @Override
        public String toString() {
            return "OAuthAccessTokenResponse{" +
                    "access_token='" + access_token + '\'' +
                    ", token_type='" + token_type + '\'' +
                    ", expires_in='" + expires_in + '\'' +
                    ", refresh_token='" + refresh_token + '\'' +
                    ", parameters='" + parameters + '\'' +
                    '}';
        }
    }

    /**
     * Alternatively, the authorization server MAY allow including the
     * client credentials in the request body using the following
     * parameters:
     * <p/>
     * client_id
     * REQUIRED.  The client identifier issued to the client during
     * the registration process described by Section 2.2.
     * client_secret
     * REQUIRED.  The client secret.  The client MAY omit the
     * parameter if the client secret is an empty string.
     * <p/>
     * Including the client credentials in the request body using the two
     * parameters is NOT RECOMMENDED, and should be limited to clients
     * unable to directly utilize the HTTP Basic authentication scheme (or
     * other password-based HTTP authentication schemes).
     * <p/>
     * For example, requesting to refresh an access token (Section 6) using
     * the body parameters (extra line breaks are for display purposes
     * only):
     * <p/>
     * <p/>
     * POST /token HTTP/1.1
     * Host: server.example.com
     * Content-Type: application/x-www-form-urlencoded;charset=UTF-8
     * <p/>
     * grant_type=refresh_token&refresh_token=tGzv3JOkF0XG5Qx2TlKWIA
     * &client_id=s6BhdRkqt3&client_secret=7Fjfp0ZBr1KtDRbnfVdmIw
     * <p/>
     * <p/>
     * The authorization server MUST require the use of a transport-layer
     * security mechanism when sending requests to the token endpoint, as
     * requests using this authentication method result in the transmission
     * of clear-text credentials.
     * <p/>
     * Since this client authentication method involves a password, the
     * authorization server MUST protect any endpoint utilizing it against
     * brute force attacks.
     */
    public final class ClientAuthentication {
        /**
         * REQUIRED.  The client identifier issued to the client during
         * the registration process described by Section 2.2.
         */
        final public String client_id;
        /**
         * REQUIRED.  The client secret.  The client MAY omit the
         * parameter if the client secret is an empty string.
         */
        final public String client_secret;

        final public String redirect_uri;


        @LOGIC(
                @NOTE("By nature, we don't want this object to be modified after construction."))
        public ClientAuthentication(final String client_id, final String client_secret, final String redirect_uri) {
            this.client_id = client_id;
            this.client_secret = client_secret;
            this.redirect_uri = redirect_uri;
        }

        @Override
        public String toString() {
            return "ClientAuthentication{" +
                    "client_id='" + client_id + '\'' +
                    ", client_secret='" + client_secret + '\'' +
                    '}';
        }
    }

// ------------------------ OVERRIDING METHODS ------------------------

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException if a servlet-specific error occurs
     * @throws java.io.IOException            if an I/O error occurs
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * This generally is called with code and state, as a redirect from the OAuth vendor.
     * <p/>
     * <p/>
     * Below is a redirect from FB.
     * <p/>
     * <p/>
     * e.g. http://www.ilikeplaces.com/oauth2?code=AQC3Zblablablabla9uO8MN_miEUIlZvMWNNblabla6zOyblablafCwDFYMli5zdblabla0peBblablaINKIDw#_=_
     * <p/>
     * <p/>
     * Note that the REQUIRED "state" parameter is missing.
     * <p/>
     * <p/>
     * Generally, if "code" is missing, which will never be missing, we will send the user to the {@link #oAuthEndpoint OAuth Endpoint}
     * with the non-null parameters specified in {@link #oAuthAuthorizationRequest}.
     * <p/>
     * <p/>
     * <b>code</b>
     * <p/>
     * REQUIRED.  The authorization code generated by the
     * authorization server.  The authorization code MUST expire
     * shortly after it is issued to mitigate the risk of leaks.  A
     * maximum authorization code lifetime of 10 minutes is
     * RECOMMENDED.  The client MUST NOT use the authorization code
     * more than once.  If an authorization code is used more than
     * once, the authorization server MUST deny the request and SHOULD
     * attempt to revoke all tokens previously issued based on that
     * authorization code.  The authorization code is bound to the
     * client identifier and redirection URI.
     * <p/>
     * <b>state</b>
     * <p/>
     * REQUIRED if the "state" parameter was present in the client
     * authorization request.  The exact value received from the
     *
     * @param request
     * @param response
     */
    @LOGIC(
            @NOTE(
                    "As of now, this method is where we get a redirect from the OAuth server with the related data below: (<a href='http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-4.1.2'>http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-4.1.2</a> " +
                            "   code\n" +
                            "         REQUIRED.  The authorization code generated by the\n" +
                            "         authorization server.  The authorization code MUST expire\n" +
                            "         shortly after it is issued to mitigate the risk of leaks.  A\n" +
                            "         maximum authorization code lifetime of 10 minutes is\n" +
                            "         RECOMMENDED.  The client MUST NOT use the authorization code\n" +
                            "         more than once.  If an authorization code is used more than\n" +
                            "         once, the authorization server MUST deny the request and SHOULD\n" +
                            "         attempt to revoke all tokens previously issued based on that\n" +
                            "         authorization code.  The authorization code is bound to the\n" +
                            "         client identifier and redirection URI.\n" +
                            "   state\n" +
                            "         REQUIRED if the \"state\" parameter was present in the client\n" +
                            "         authorization request.  The exact value received from the"))
    abstract void processRequest(final HttpServletRequest request, final HttpServletResponse response);

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
