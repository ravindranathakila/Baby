package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Nov 22, 2010
 * Time: 7:22:13 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Hail {

    public static String honour(){
        final StringBuilderHelper s = new StringBuilderHelper();

        s.appendln("*** HAIL I LIKE PLACES! HAIL DOWNTOWN! ****************************************************** ***");
        s.ln();
        s.appendln("*** ***************************************************************************************** ***");
        s.ln();
        s.appendln("*** On the 19th of November, 2010, I Like Places Registered its 1st Booking Ever! *********** ***");
        s.appendln("*** This was greatly appreciated and accepted with a courteous applause! ******************** ***");
        s.appendln("*** The booking was made by Maria Romanova, for $189.34 (USD), ****************************** ***");
        s.appendln("*** at Hampton Inn Nanuet, 260 W Route 59, Nanuet NY 10954, US. ***************************** ***");
        s.appendln("*** I Like Places earned  $9.47 (USD) and this was heartily received! *********************** ***");
        s.appendln("*** ***************************************************************************************** ***");
        s.ln();        
        s.appendln("*** ***************************************************************************************** ***");
        s.appendln("*** ***************************************************************************************** ***");
        s.appendln("*** ***************************************************************************************** ***");
        s.appendln("*** ***************************************************************************************** ***");
        s.appendln("*** ***************************************************************************************** ***");

        return s.toString();
    }
}
