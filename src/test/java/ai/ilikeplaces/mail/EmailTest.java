package ai.ilikeplaces.mail;

import ai.ilikeplaces.util.spam.mail.Email;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/5/11
 * Time: 10:29 AM
 */
public class EmailTest {

    public static void main(final String args[]) {
        final String[] domains = new String[3];
        domains[0] = "google.com";
        domains[1] = "yahoo.com";
        domains[2] = "ilikeplaces.com";

        for (String domain : domains) {
            try {
                System.out.println(domain + " has " +
                        Email.doLookup(domain) + " mail servers");
            } catch (Exception e) {
                System.out.println(domain + " : " + e.getMessage());
            }
        }

        String testData[] = {
                //"tim@orbaker.com", // Valid address
                //"fail.me@nowhere.spam", // Invalid domain name
                //"arkham@bigmeanogre.net", // Invalid address
                "nosuchaddress@yahoo.com", // Failure of this method
                //"catchallbutme@ilikeplaces.com", // Invalid but is catch-all on ilikeplaces.com
                //"catchallbutme@lindfaith.com", // Invalid but is NOT catch-all on ilikeplaces.com
                //"catchallbutme@google.com", // Invalid but is NOT catch-all on ilikeplaces.com
                "akila@adimpression.mobi", // Invalid
                "nosuchemail@adimpression.mobi", //
                //"ravindranathakila@gmail.com", //
                "billgatesisnotongmailsfasdklk@gmail.com", //
                "ravindranathakila@yahoo.com" ,// Valid
                "201111081@ilikeplaces.com" ,// Valid
                "notifications@ilikeplaces.com" ,// Valid
                "example@example.com" ,// Valid
                "example3@example.com" ,// Valid
        };

        for (String aTestData : testData) {
            System.out.println(aTestData + " is valid? " +
                    Email.isAddressValidTolerant(aTestData));
        }
    }
}
