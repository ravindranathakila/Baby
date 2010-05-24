package ai.ilikeplaces.logic.mail;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.Return;

import javax.ejb.Local;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
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

    public Return<Boolean> sendAsHTML(final String recepientEmail,
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