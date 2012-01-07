package ai.ilikeplaces.servlets;

import ai.ilikeplaces.doc.DOCUMENTATION;
import ai.ilikeplaces.doc.LOGIC;
import ai.ilikeplaces.doc.NOTE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    private OAuthAuthorizationRequest OAuthAuthorizationRequest;

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
        /**
         * REQUIRED.  Value MUST be set to "code".
         */
        final private String response_type;

        /**
         * REQUIRED.  The client identifier as described in Section 2.2.
         */
        final private String client_id;

        /**
         * OPTIONAL, as described in Section 3.1.2.
         */
        final private String redirect_uri;

        /**
         * OPTIONAL.  The scope of the access request as described by
         */
        final private String scope;

        /**
         * RECOMMENDED.  An opaque value used by the client to maintain
         * state between the request and callback.  The authorization
         * server includes this value when redirecting the user-agent back
         * to the client.  The parameter SHOULD be used for preventing
         * cross-site request forgery as described in Section 10.12.
         */
        final private String state;

        @LOGIC(
                @NOTE("By nature, we don't want this object to be modified after construction."))
        public OAuthAuthorizationRequest(final String response_type,
                                         final String client_id,
                                         final String redirect_uri,
                                         final String scope,
                                         final String state) {
            this.response_type = response_type;
            this.client_id = client_id;
            this.redirect_uri = redirect_uri;
            this.scope = scope;
            this.state = state;
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
        final private String code;

        /**
         * REQUIRED if the "state" parameter was present in the client
         * authorization request.  The exact value received from the
         * client.
         */
        final private String state;

        public OAuthAuthorizationResponse(final String code, final String state) {
            this.code = code;
            this.state = state;
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

        /**
         * REQUIRED.  Value MUST be set to "authorization_code".
         */
        private final String grant_type;

        /**
         * REQUIRED.  The authorization code received from the
         * authorization server.
         */
        private final String code;

        /**
         * REQUIRED, if the "redirect_uri" parameter was included in the
         * authorization request as described in Section 4.1.1, and their
         * values MUST be identical.
         */
        private final String redirect_uri;

        @LOGIC(
                @NOTE("By nature, we don't want this object to be modified after construction."))
        public OAuthAccessTokenRequest(final String grant_type, final String code, final String redirect_uri) {
            this.grant_type = grant_type;
            this.code = code;
            this.redirect_uri = redirect_uri;
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
     */
    public final class OAuthAccessTokenResponse {

        /**
         * REQUIRED.  The access token issued by the authorization server.
         */
        final private String access_token;

        /**
         * REQUIRED.  The type of the token issued as described in Section 7.1.  Value is case insensitive.
         */
        final private String token_type;

        /**
         * OPTIONAL.  The lifetime in seconds of the access token.  For
         * example, the value "3600" denotes that the access token will
         * expire in one hour from the time the response was generated.
         */
        final private String expires_in;
        /**
         * OPTIONAL.  The refresh token which can be used to obtain new
         * access tokens using the same authorization grant as described
         * in Section 6.
         */
        final private String refresh_token;

        /**
         * OPTIONAL.  The scope of the access token as described by
         */
        final private String example_parameter;

        @LOGIC(
                @NOTE("By nature, we don't want this object to be modified after construction."))
        public OAuthAccessTokenResponse(final String access_token,
                                        final String token_type,
                                        final String expires_in,
                                        final String refresh_token,
                                        final String example_parameter) {
            this.access_token = access_token;
            this.token_type = token_type;
            this.expires_in = expires_in;
            this.refresh_token = refresh_token;
            this.example_parameter = example_parameter;
        }
    }

    /**
     * 4.1.1.  Authorization Request
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
     *
     * @param OAuthAuthorizationRequest OAuth Authorization Request
     */
    @LOGIC(
            @NOTE("Here our main focus ist to initialize data related to " +
                    "<a href='http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-4.1.1'>http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-4.1.1</a> . "))
    protected AbstractOAuth(final OAuthAuthorizationRequest OAuthAuthorizationRequest) {
        this.OAuthAuthorizationRequest = OAuthAuthorizationRequest;
    }

    /**
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
    private void processRequest(final HttpServletRequest request, final HttpServletResponse response) {

    }

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
