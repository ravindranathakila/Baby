package ai.ilikeplaces.logic.validators.unit;

import ai.ilikeplaces.util.RefObj;
import ai.scribble.License;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 22, 2010
 * Time: 2:16:43 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class Email extends RefObj<String> {

    public Email() {
    }

    public Email(final String humanId) {
        obj = humanId;
    }

    @IsInvariant
    @NotNull
    @net.sf.oval.constraint.Email
    @Override
    final public String getObj() {
        return obj;
    }

    public static List<Email> emails(final String rawEmailsAsString) {
        final List<Email> emails = new ArrayList<Email>();
        //Pattern p = EmailCheck.EMAIL_PATTERN;
        Pattern p = Pattern.compile("(['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,})))");
        Matcher m = p.matcher(rawEmailsAsString);

        while (m.find()) {
            final Email e = new Email(m.group());
            if (e.validate() == 0) {
                emails.add(e);
            }
        }
        return emails;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RefObj)) return false;

        final RefObj refObj = (RefObj) o;

        return obj.equals(refObj.getObj());
    }
}


/*


<!-- TWO STEPS TO INSTALL EXTRACT EMAIL ADDRESSES:

1.  Copy the coding into the HEAD of your HTML document
2.  Add the last code into the BODY of your HTML document  -->

<!-- STEP ONE: Paste this code into the HEAD of your HTML document  -->

<HEAD>

<SCRIPT LANGUAGE="JavaScript">
<!-- Original:  Ronnie T. Moore, Editor -->

<!-- This script and many more are available free online at -->
<!-- The JavaScript Source!! http://javascript.internet.com -->

<!-- Begin
function findEmailAddresses(StrObj) {
var separateEmailsBy = ", ";
var email = "<none>"; // if no match, use this
var emailsArray = StrObj.match(/([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\.[a-zA-Z0-9._-]+)/gi);
if (emailsArray) {
email = "";
for (var i = 0; i < emailsArray.length; i++) {
if (i != 0) email += separateEmailsBy;
email += emailsArray[i];
}
}
return email;
}
//  End -->
</script>
</HEAD>

<!-- STEP TWO: Copy this code into the BODY of your HTML document  -->

<BODY>

<center>
<form>
<textarea name=comments rows=10 cols=50 onBlur="this.form.email.value=findEmailAddresses(this.value);"></textarea>
<br>
Email:  <input type=text name=email>
</form>
</center>

<p><center>
<font face="arial, helvetica" size="-2">Free JavaScripts provided<br>
by <a href="http://javascriptsource.com">The JavaScript Source</a></font>
</center><p>

<!-- Script Size:  1.19 KB -->*/

/*   //WORKING METHOD FOR EMAIL EXTRACTION
 public static String emails(final String StrObj) {

        //Pattern p = EmailCheck.EMAIL_PATTERN;
        Pattern p = Pattern.compile("(['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,})))");
        //Pattern p = Pattern.compile("/([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9._-]+)/gi");
        //Pattern p = Pattern.compile("cat");
        //Pattern p = Pattern.compile("@");
        Matcher m = p.matcher(" akila@fmid.com sdfd wer sdfs@dfdf.com df2342rf &*((#,dfsd@der.li " +
                "\"quotecheck\"@doma.com " +
                "\"space check\"@doma.com " +
                "valid@doma.com " +
                "nothere@\"quotecheck\".com " +
                "nothere@\"space heck\".com " +
                "nothere@valid.com " +
                "");
        //Matcher m = p.matcher("one cat two cats in the yard");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            sb.append(m.group()+"\n");
        }
        //m.appendTail(sb);
        return sb.toString();

//        String separateEmailsBy = ", ";
//        String email = ""; // if no match, use this
//        String[] emailsArray = StrObj.split(" ");
//        String[] emailsArray = StrObj.split("/([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9._-]+)/gi");
//        email = "";
//        for (int i = 0; i < emailsArray.length; i++) {
//            if (i != 0) email += separateEmailsBy;
//            email += emailsArray[i];
//        }
//        return email;
    }*/
