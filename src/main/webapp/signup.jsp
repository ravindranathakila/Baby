<%-- 
    Document   : signup
    Created on : Sep 18, 2009, 9:07:40 PM
    Author     : Ravindranath Akila

--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link rel="stylesheet" href="/css/blueprint/screen.css" type="text/css" media="screen, projection"/>
        <link rel="stylesheet" href="/css/blueprint/print.css" type="text/css" media="print"/>
        <!--[if lt IE 8]><link rel="stylesheet" href="../../blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
        <link rel="stylesheet" href="/css/buttons.css" type="text/css"/>
        <%--script src="/js/jquery-1.3.2.js" type="text/javascript"></script--%>
        <title>Let's Join ilikeplaces!</title>
    </head>
    <script type="text/javascript">
      var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-13297277-1']);
      _gaq.push(['_trackPageview']);

      (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
      })();
    </script>
    <body>
        <form action="/signup" method="post">
            <br/>
            <br/>
            <br/>
            <div class ="container showgrid">
                <div class="span-24 last">
                    <div class="notice span-24 last">
                        <h2>
                            <b>WELCOME</b> TO <strong>I LIKE PLACES . COM !</strong>
                        </h2>
                        <h3>Please fill in the following details <i>carefully</i>.</h3>
                        <h4>Please remember your username and keep your password in a <b>safe</b> place accessible only to you.</h4>
                    </div>
                    <div class="notice span-24">
                        <div class="span-4">
                            &nbsp;
                        </div>
                        <div class="span-8">
                           <div class="span-7">
                                Email: e.g. abraham.lincoln@example.org<br/>
                                <input type="text" id="Username" name="Username" value='<sess:attribute name="Username"/>'/>
                            </div>
                            <div class="span-1 last"></div>
                            <sess:equalsAttribute name="UsernameError" match="true">
                                <div class="error span-7">
                                    Please enter a correct email<br/>
                                    Mistakes you made:<br/>
                                    <sess:attribute name="UsernameErrorMsg"/>
                                </div>
                                <div class="span-1 last"></div>

                            </sess:equalsAttribute>
                        </div>
                        <div class="span-8">
                            <div class="span-7">
                                Password: e.g. Honest_Abe<br/>
                                <input type="password" id="Password" name="Password"/>
                            </div>
                            <div class="span-1 last"></div>
                            <sess:equalsAttribute name="PasswordError" match="true" >
                                <div class="error span-7">
                                    Please re-enter password:<br/>
                                    Mistakes you made:<br/>
                                    <sess:attribute name="PasswordErrorMsg"/>
                                </div>
                                <div class="span-1 last"></div>

                            </sess:equalsAttribute>
                        </div>
                        <div class="span-4 last">
                            &nbsp;
                        </div>
                    </div>
                </div>
                <div class="notice span-24 last">

                    <div class="span-10">
                       &nbsp;
                    </div>

                    <div class="span-5">
                        <br/>
                        <input type="submit" value="Sign me up!"/>
                    </div>

                    <div class="span-10 last">
                       &nbsp;
                    </div>

                </div>
            </div>
        </form>
    </body>
</html>
