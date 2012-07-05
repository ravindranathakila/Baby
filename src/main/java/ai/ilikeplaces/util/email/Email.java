package ai.ilikeplaces.util.email;

import ai.ilikeplaces.util.cache.SmartCache2String;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/5/11
 * Time: 10:24 AM
 *
 * @see <a href="http://www.rgagnon.com/javadetails/java-0452.html">
 *      http://www.rgagnon.com/javadetails/java-0452.html
 *      </a>
 * @see <a href="http://www.ahfx.net/weblog/107">Yahoo Email Rejection 1</a>
 * @see <a href="http://help.yahoo.com/l/us/yahoo/mail/postmaster/basics/postmaster-15.html;_ylt=AueFB6cUubaoB3dWcNvf0RoIJHdG">Yahoo Email Rejection 2</a>
 * @see <a href="http://help.yahoo.com/l/us/yahoo/mail/postmaster/errors/postmaster-27.html;_ylt=AmZV.STJit58m4_SryYhoFYlJHdG">Yahoo Email Rejection With "554"</a>
 */
public class Email {
// ------------------------------ FIELDS ------------------------------

    final static Logger L = LoggerFactory.getLogger("EMAIL_VALIDATOR_LOGGER");


// ------------------------------ FIELDS STATIC --------------------------

    static Map<String, String> CONFIRMED_EMAILS = new HashMap<String, String>() {
        {
            put("gmail.com", "ravindranathakila@gmail.com");
            put("yahoo.com", "ravindranathakila@yahoo.com");
        }
    };

    static SmartCache2String<Boolean, String> VALID_DOMAINS_WITH_VALID_EMAILS = new SmartCache2String<Boolean, String>(
            new SmartCache2String.RecoverWith<Boolean, String>() {
                @Override
                public Boolean getValue(final String k, final String runtimeArgument) {

                    final String confirmedEmail = CONFIRMED_EMAILS.get(k);
                    if (confirmedEmail == null) {
                        return runtimeArgument.endsWith(k) && isAddressValid(runtimeArgument);
                    } else {
                        return runtimeArgument.endsWith(k) && isAddressValid(confirmedEmail);
                    }
                }
            }
    );
    private static final String JAVA_NAMING_FACTORY_INITIAL = "java.naming.factory.initial";
    private static final String COM_SUN_JNDI_DNS_DNS_CONTEXT_FACTORY = "com.sun.jndi.dns.DnsContextFactory";
    private static final String MX = "MX";
    private static final String NO_MATCH_FOR_NAME = "No match for name '";
    private static final String SINGLE_QUOTE = "'";
    private static final String INVALID_HEADER = "Invalid header";
    private static final String EHLO_WITH_DOMAIN = "EHLO ilikeplaces.com";
    private static final String NOT_ESMTP = "Not ESMTP";
    private static final String MAIL_FROM = "MAIL FROM: <notifications@ilikeplaces.com>";
    private static final String SENDER_REJECTED = "Sender rejected";
    private static final String RCPT_TO = "RCPT TO: <";
    private static final String STRING = ">";
    private static final String RSET = "RSET";
    private static final String QUIT = "QUIT";
    private static final String ADDRESS_IS_NOT_VALID = " is not valid!";
    private static final char AT = '@';
    private static final String NEWLINE = "\r\n";
    private static final String SPACE = " ";
    private static final String PERIOD = ".";

    // -------------------------- OTHER METHODS --------------------------
    public static int doLookup(final String hostName) throws NamingException {

        Hashtable env = new Hashtable();
        env.put(JAVA_NAMING_FACTORY_INITIAL,
                COM_SUN_JNDI_DNS_DNS_CONTEXT_FACTORY);
        DirContext ictx = new InitialDirContext(env);
        Attributes attrs =
                ictx.getAttributes(hostName, new String[]{MX});
        Attribute attr = attrs.get(MX);
        if (attr == null) {
            return (0);
        }
        return (attr.size());
    }

    public static boolean isAddressValidTolerant(final String address) {

        // Find the separator for the domain name
        int pos = address.indexOf(AT);

        // If the address does not contain an '@', it's not valid
        if (pos == -1) {
            return false;
        }

        // Isolate the domain/machine name and get a list of mail exchangers
        final String domain = address.substring(++pos);

        ArrayList mxList = null;
        try {
            mxList = getMX(domain);
        } catch (NamingException ex) {
            return false;
        }

        // Just because we can send mail to the domain, doesn't mean that the
        // address is valid, but if we can't, it's a sure sign that it isn't
        if (mxList.size() == 0) {
            return false;
        }

        if (VALID_DOMAINS_WITH_VALID_EMAILS.get(domain, address)) {
            return isAddressValid(address);
        } else {
            debug("DEFAULTING:" + address);
            return true;
        }
    }

    private static ArrayList getMX(String hostName)
            throws NamingException {

        // Perform a DNS lookup for MX records in the domain
        Hashtable env = new Hashtable();
        env.put(JAVA_NAMING_FACTORY_INITIAL,
                COM_SUN_JNDI_DNS_DNS_CONTEXT_FACTORY);
        DirContext ictx = new InitialDirContext(env);
        Attributes attrs = ictx.getAttributes
                (hostName, new String[]{MX});
        Attribute attr = attrs.get(MX);

        // if we don't have an MX record, try the machine itself
        if ((attr == null) || (attr.size() == 0)) {
            attrs = ictx.getAttributes(hostName, new String[]{"A"});
            attr = attrs.get("A");
            if (attr == null)
                throw new NamingException
                        (NO_MATCH_FOR_NAME + hostName + SINGLE_QUOTE);
        }
        // Huzzah! we have machines to try. Return them as an array list
        // NOTE: We SHOULD take the preference into account to be absolutely
        //   correct. This is left as an exercise for anyone who cares.
        ArrayList res = new ArrayList();
        NamingEnumeration en = attr.getAll();

        while (en.hasMore()) {
            String mailhost;
            String x = (String) en.next();
            String f[] = x.split(SPACE);
            //  THE fix *************
            if (f.length == 1)
                mailhost = f[0];
            else if (f[1].endsWith(PERIOD))
                mailhost = f[1].substring(0, (f[1].length() - 1));
            else
                mailhost = f[1];
            //  THE fix *************
            res.add(mailhost);
        }
        return res;
    }

    // --------------------------- main() method ---------------------------
    public static void main(String args[]) {

        String testData[] = {
                "real@rgagnon.com",
                "you@acquisto.net",
                "fail.me@nowhere.spam", // Invalid domain name
                "arkham@bigmeanogre.net", // Invalid address
                "nosuchaddress@yahoo.com" // Failure of this method
        };

        for (int ctr = 0; ctr < testData.length; ctr++) {
            System.out.println(testData[ctr] + " is valid? " +
                    isAddressValid(testData[ctr]));
        }
        return;
    }

    public static boolean isAddressValid(final String address) {

        // Find the separator for the domain name
        int pos = address.indexOf(AT);

        // If the address does not contain an '@', it's not valid
        if (pos == -1) {
            return false;
        }

        // Isolate the domain/machine name and get a list of mail exchangers
        final String domain = address.substring(++pos);


        ArrayList mxList = null;
        try {
            mxList = getMX(domain);
        } catch (NamingException ex) {
            return false;
        }

        // Just because we can send mail to the domain, doesn't mean that the
        // address is valid, but if we can't, it's a sure sign that it isn't
        if (mxList.size() == 0) {
            return false;
        }


//        if (domain.equals("yahoo.com") && !address.equals("ravindranathakila@yahoo.com")) {
//            if (isAddressValid("ravindranathakila@yahoo.com")) {
//                //This means yahoo checks work. So we allow the checks to proceed
//            } else {
//                //Yahoo checks do not work. We simply have to return true
//                System.out.println("DEFAULTED ON:" + address);
//                return true;
//            }
//        }
//
//        if (domain.equals("gmail.com") && !address.equals("ravindranathakila@gmail.com")) {
//            if (isAddressValid("ravindranathakila@gmail.com")) {
//                //This means yahoo checks work. So we allow the checks to proceed
//            } else {
//                //Yahoo checks do not work. We simply have to return true
//                System.out.println("DEFAULTED ON:" + address);
//                return true;
//            }
//        }

        // Now, do the SMTP validation, try each mail exchanger until we get
        // a positive acceptance. It *MAY* be possible for one MX to allow
        // a message [store and forwarder for example] and another [like
        // the actual mail server] to reject it. This is why we REALLY ought
        // to take the preference into account.
        for (Object aMxList : mxList) {
            System.out.println(aMxList);
            boolean valid = false;
            try {
                int res;
                //
                Socket skt = new Socket((String) aMxList, 25);
                BufferedReader rdr = new BufferedReader
                        (new InputStreamReader(skt.getInputStream()));
                BufferedWriter wtr = new BufferedWriter
                        (new OutputStreamWriter(skt.getOutputStream()));

                res = hear(rdr);
                if (res != 220) {//Check if "220 <domain> Service ready"
                    debug("554 Transaction failed  (Or, in the case of a connection-opening response, \"No SMTP service here\")");
                    continue;
                    //throw new Exception(INVALID_HEADER); //Got "554 Transaction failed  (Or, in the case of a connection-opening response, "No SMTP service here")"
                }
                say(wtr, EHLO_WITH_DOMAIN);

                res = hear(rdr);
                if (res != 250) {//Check if "Requested mail action okay, completed"
                    if (res == 504) {
                        debug("504 Command parameter not implemented");
                    }
                    if (res == 550) {
                        debug("550 Requested action not taken: mailbox unavailable(e.g., mailbox not found, no access, or command rejected for policy reasons)");
                    }
                    continue;
                    //throw new Exception(NOT_ESMTP);//Got "504 Command parameter not implemented" or "550 Requested action not taken: mailbox unavailable(e.g., mailbox not found, no access, or command rejected for policy reasons)"
                }

                // validate the sender address
                say(wtr, MAIL_FROM);
                res = hear(rdr);
                if (res != 250) {
                    debug(SENDER_REJECTED);
                    continue;
                    //throw new Exception(SENDER_REJECTED);
                }

                say(wtr, RCPT_TO + address + STRING);
                res = hear(rdr);
                if (res != 250) {
                    debug(address + ADDRESS_IS_NOT_VALID);
                    continue;
                    //throw new Exception(address + ADDRESS_IS_NOT_VALID);
                }

                // be polite
                say(wtr, RSET);
                hear(rdr);
                say(wtr, QUIT);
                hear(rdr);

                valid = true;
                rdr.close();
                wtr.close();
                skt.close();
            } catch (Exception ex) {
                // Do nothing but try next host
                ex.printStackTrace();
            } finally {
                if (valid) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void say(BufferedWriter wr, String text)
            throws IOException {

        wr.write(text + NEWLINE);
        wr.flush();
    }

    private static int hear(BufferedReader in) throws IOException {

        String line = null;
        int res = 0;

        while ((line = in.readLine()) != null) {
            String pfx = line.substring(0, 3);
            try {
                res = Integer.parseInt(pfx);
            } catch (Exception ex) {
                res = -1;
            }
            if (line.charAt(3) != '-') {
                break;
            }
            System.out.println(line);
        }

        System.out.println(res);
        return res;
    }


    static void debug(final Object object) {
        if (L.isDebugEnabled()) {
            L.debug(object.toString());
        }
    }
}

/*

      220 <domain> Service ready
      221 <domain> Service closing transmission channel
      421 <domain> Service not available, closing transmission channel
         (This may be a reply to any command if the service knows it
         must shut down)

      250 Requested mail action okay, completed
      251 User not local; will forward to <forward-path>
         (See section 3.4)
      252 Cannot VRFY user, but will accept message and attempt
          delivery
         (See section 3.5.3)
      450 Requested mail action not taken: mailbox unavailable
         (e.g., mailbox busy)
      550 Requested action not taken: mailbox unavailable
         (e.g., mailbox not found, no access, or command rejected
         for policy reasons)
      451 Requested action aborted: error in processing
      551 User not local; please try <forward-path>
         (See section 3.4)
      452 Requested action not taken: insufficient system storage
      552 Requested mail action aborted: exceeded storage allocation
      553 Requested action not taken: mailbox name not allowed
         (e.g., mailbox syntax incorrect)
      354 Start mail input; end with <CRLF>.<CRLF>
      554 Transaction failed (Or, in the case of a connection-opening
          response, "No SMTP service here")

          */
