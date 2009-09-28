<%-- 
    Document   : signup
    Created on : Sep 18, 2009, 9:07:40 PM
    Author     : Ravindranath Akila
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link rel="stylesheet" href="/ilikeplaces/css/blueprint/screen.css" type="text/css" media="screen, projection"/>
        <link rel="stylesheet" href="/ilikeplaces/css/blueprint/print.css" type="text/css" media="print"/>
        <!--[if lt IE 8]><link rel="stylesheet" href="../../blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
        <link rel="stylesheet" href="/ilikeplaces/css/buttons.css" type="text/css"/>
        <script src="/ilikeplaces/js/jquery-1.3.2.js" type="text/javascript"></script>
        <title>Let's Join ilikeplaces!</title>
    </head>
    <body>
        <form action="/ilikeplaces/signup">
            <div class ="container showgrid">
                <div class="span-24">
                    <div class="span-4">
                    </div>
                    <div class="span-8">
                        Username:<input type="text" id="Username" name="Username"/>
                    </div>
                    <div class="span-8">
                        Password<input type="password" id="Password" name="Password"/>
                    </div>
                    <div class="span-4 last">
                    </div>
                </div>
                <div class="span-24">
                    <div class="span-3">
                    </div>
                    <div class="span-6">
                        A Valid Email Address:<input type="text" id="Email" name="Email"/>
                    </div>
                    <div class="span-6">
                        Date of Birth<input type="text"  id="DateOfBirth" name="DateOfBirth"/><span class="sub">Required for content filtering</span>
                    </div>
                    <div>
                        Gender<select id="Gender" name="Gender">
                            <optgroup label="Gender">
                                <option>Male</option>
                                <option>Female</option>
                                <option>Neutral</option>
                            </optgroup>
                        </select>
                    </div>
                    <div class="span-3 last">
                        <input type="submit" value="Sign me up!"/>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
