package ai.ilikeplaces.logic.mail;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.crud.HumanCRUDPublicPhotoLocal;
import ai.ilikeplaces.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
@Interceptors({ParamValidator.class, MethodTimer.class, MethodParams.class})
public class SendMail extends AbstractSLBCallbacks implements SendMailLocal {

    final static private Properties P_ = new Properties();
    static private Context Context_ = null;
    static private boolean OK_ = false;
    final static private ResourceBundle config = ResourceBundle.getBundle("ai.ilikeplaces.rbs.Config");
    final static private String ICF = config.getString("oejb.LICF");
    final static Logger logger = LoggerFactory.getLogger(DB.class);

    static {
        try {
            SendMail.P_.put(Context.INITIAL_CONTEXT_FACTORY, SendMail.ICF);
            SendMail.Context_ = new InitialContext(P_);
            SendMail.OK_ = true;
        } catch (NamingException ex) {
            SendMail.OK_ = false;
            Loggers.EXCEPTION.error("{}", ex);
        }
    }

    static public void isOK() {
        if (!OK_) {
            throw new IllegalStateException("SORRY! MAIL SESSION BEAN INITIALIZATION FAILED!");
        }
    }

    public static SendMailLocal getSendMailLocal() {
        isOK();
        SendMailLocal h = null;
        try {
            h = (SendMailLocal) Context_.lookup(SendMailLocal.NAME);
        } catch (NamingException ex) {
            logger.error("{}", ex);
        }
        return h != null ? h : (SendMailLocal) LogNull.logThrow();
    }

    private static final String U_S_E_R_N_A_M_E = "notifications@ilikeplaces.com";
    private static final String P_A_S_S_W_O_R_D = "### MANUALLY REMOVED OFF CVS FOR SECURITY REASONS ###";

    Properties props;
    Session mailSession;
    Transport transport;
    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_HTML = "text/html; charset=ISO-8859-1";

    @PostConstruct
    public void postConstruct() {
        props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.emailsrvr.com");
        props.setProperty("mail.user", U_S_E_R_N_A_M_E);
        props.setProperty("mail.password", P_A_S_S_W_O_R_D);
        mailSession = Session.getDefaultInstance(props, null);
        try {
            transport = mailSession.getTransport();
            transport.connect(U_S_E_R_N_A_M_E, "### MANUALLY REMOVED OFF CVS FOR SECURITY REASONS ###");
        } catch (final MessagingException e) {
            Loggers.EXCEPTION.error("SORRY! MAIL SESSION BEAN INITIALIZATION FAILED!\n" +
                    "This could happen if Gerenimo javamail is taken instead of Sun.", e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            transport.close();
        } catch (MessagingException e) {
            Loggers.EXCEPTION.error("SORRY! SESSION BEAN TRANSPORT CLOSE FAILED WHILE BEAN DESTROY!", e);
        }

    }


    private boolean finalSend(final Message message) throws MessagingException, IOException {
        boolean returnVal = false;
        //String protocol = "smtp";
        //props.put("mail." + protocol + ".auth", "true");
        try {
            if (!transport.isConnected()) {
                transport.connect(U_S_E_R_N_A_M_E, P_A_S_S_W_O_R_D);
            }
            transport.sendMessage(message, message.getAllRecipients());
            returnVal = true;
        } catch (final Exception e) {
            Loggers.EXCEPTION.error("SORRY! MAIL SEND FAILED!", e);
            for (final Address address : message.getAllRecipients()) {
                Loggers.USER.error(address + " was supposed to get the following mail notification, but did not.[" +
                        "Subject:" + message.getSubject() +
                        "Body:" + message.getContent().toString() +
                        "]");
            }
        }
        return returnVal;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Return<Boolean> sendAsSimpleText(final String recepientEmail, final String simpleTextSubject, final String simpleTextBody) {
        Return<Boolean> r;
        try {

            final MimeMessage message = new MimeMessage(mailSession);
            message.setSender(new InternetAddress(U_S_E_R_N_A_M_E));

            message.setSubject(simpleTextSubject);
            message.setContent(simpleTextBody, TEXT_PLAIN);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepientEmail));

            r = new ReturnImpl<Boolean>(finalSend(message), "Mail sending to " + recepientEmail + " successful!");

        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "FAILED! Mail sending to " + recepientEmail + " FAILED!", true);
        }
        return r;
    }


    @Override
    public Return<Boolean> sendAsHTML(final String recepientEmail, final String simpleTextSubject, final String htmlBody) {
        Return<Boolean> r;
        try {

            final MimeMessage message = new MimeMessage(mailSession);
            message.setSender(new InternetAddress(U_S_E_R_N_A_M_E));

            message.setSubject(simpleTextSubject);
            message.setContent(htmlBody, TEXT_HTML);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepientEmail));

            r = new ReturnImpl<Boolean>(finalSend(message), "Mail sending to " + recepientEmail + " successful!");

        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "FAILED! Mail sending to " + recepientEmail + " FAILED!", true);
        }
        return r;
    }

    @Override
    public Return<Boolean> sendWithAttachmentAsSimpleText(final String recepientEmail, final String simpleTextSubject, final String simpleTextBody, final File file) {

        Return<Boolean> r;
        try {

            final MimeMessage message = new MimeMessage(mailSession);
            message.setSender(new InternetAddress(U_S_E_R_N_A_M_E));
            message.setSubject(simpleTextSubject);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepientEmail));


            final MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(simpleTextBody, TEXT_PLAIN);

            final FileDataSource fds = new FileDataSource(file);

            final MimeBodyPart attachFilePart = new MimeBodyPart();
            attachFilePart.setDataHandler(new DataHandler(fds));
            attachFilePart.setFileName(fds.getName());

            final Multipart mp = new MimeMultipart();
            mp.addBodyPart(textPart);
            mp.addBodyPart(attachFilePart);

            message.setContent(mp);

            r = new ReturnImpl<Boolean>(finalSend(message), "Mail sending to " + recepientEmail + " successful!");


        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "FAILED! Mail sending to " + recepientEmail + " FAILED!", true);
        }
        return r;

    }

    @Override
    public Return<Boolean> sendWithAttachmentAsHTML(final String recepientEmail, final String simpleTextSubject, final String htmlBody, final File file) {

        Return<Boolean> r;
        try {

            final MimeMessage message = new MimeMessage(mailSession);
            message.setSender(new InternetAddress(U_S_E_R_N_A_M_E));
            message.setSubject(simpleTextSubject);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepientEmail));


            final MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(htmlBody, TEXT_HTML);

            final FileDataSource fds = new FileDataSource(file);

            final MimeBodyPart attachFilePart = new MimeBodyPart();
            attachFilePart.setDataHandler(new DataHandler(fds));
            attachFilePart.setFileName(fds.getName());

            final Multipart mp = new MimeMultipart();
            mp.addBodyPart(textPart);
            mp.addBodyPart(attachFilePart);

            message.setContent(mp);

            r = new ReturnImpl<Boolean>(finalSend(message), "Mail sending to " + recepientEmail + " successful!");


        } catch (final Throwable t) {
            r = new ReturnImpl<Boolean>(t, "FAILED! Mail sending to " + recepientEmail + " FAILED!", true);
        }
        return r;
    }

}