<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Upload Test!</title>
    </head>
    <body>
        <form method="POST" enctype="multipart/form-data" action="/ilikeplaces/fileuploads">

            File to upload: <input type=file name=upfile><br/>
            Notes about the file: <input type=text name=note><br/>
            <br/>
            <input type=submit value=Press> to upload the file!
        </form>
    </body>
</html>
