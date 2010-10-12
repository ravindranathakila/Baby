package ai.ilikeplaces.logic.mail;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.io.File;

/**
 * Just one thing. When sending mails, DON'T WASTE TIME OF PEOPLE.
 * <p/>
 * Right now I am deciding even whether to talk off the disclaimer at the end.
 * <p/>
 * The snappier the mails and responses are, the closer we are to McDonaldization anyway, which is also good.
 * <p/>
 * <p/>
 * So, make the subject bear what the mail is about which makes it easier for the user to decide whether to open the
 * mail even at work or not.
 * <p/>
 * Make the mail bodies in point form unless it is a really creative content or an HTML promotional mail.
 * <p/>
 * <p/>
 * Happy Mailing!
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 2:41:45 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface SendMailLocal {

    public static final String NAME = SendMailLocal.class.getSimpleName();

    public Return<Boolean> sendAsSimpleText(final String recepientEmail,
                                            final String simpleTextSubject,
                                            final String simpleTextBody);

    public Return<Boolean> sendAsSimpleTextAsynchronously(final String recepientEmail,
                                                          final String simpleTextSubject,
                                                          final String simpleTextBody);

    public Return<Boolean> sendAsHTML(final String recepientEmail,
                                      final String simpleTextSubject,
                                      final String htmlBody);

    public Return<Boolean> sendAsHTMLAsynchronously(
            final String recepientEmail,
            final String simpleTextSubject,
            final String htmlBody);


    public Return<Boolean> sendWithAttachmentAsSimpleText(final String recepientEmail,
                                                          final String simpleTextSubject,
                                                          final String simpleTextBody,
                                                          final File file);

    public Return<Boolean> sendWithAttachmentAsHTML(final String recepientEmail,
                                                    final String simpleTextSubject,
                                                    final String htmlBody,
                                                    final File file);

}