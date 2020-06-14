/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

import java.util.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.smtp.SMTPTransport;

/**
 *
 * @author peukianm
 */
public class ErpUtil {

    private static final int iterations = 10 * 1024;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    /**
     * Computes a salted PBKDF2 hash of given plaintext password suitable for
     * storing in a database. Empty passwords are not supported.
     */
    public static String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        // store the salt with the password
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    /**
     * Checks whether given plaintext password corresponds to a stored salted
     * hash of the password.
     */
    public static boolean check(String password, String stored) throws Exception {
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2) {
            return false;
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }

    private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Empty passwords are not supported.");
        }
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, iterations, desiredKeyLen));
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static boolean sendFromGMail(String ghost, String gport, String from, String pass, String to, String subject, String body) {
        System.out.println(" ghost=" + ghost + " gport=" + gport + " from=" + from + " pass=" + pass + " to=" + to + " subject=" + subject + " body=" + body);

        Properties props = System.getProperties();
        String host = ghost;
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        //props.put("mail.smtp.user", from);
        //props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", gport);
        props.put("mail.smtp.auth", "true");
        
        Session session = Session.getInstance(props);
        MimeMessage msg = new MimeMessage(session);

        try {
           // msg.setHeader("Content-Type", "text/plain; charset=UTF-8");
            // from
            msg.setFrom(new InternetAddress(from));

            // to
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            
            // subject
            msg.setSubject(subject,"UTF-8");

            // content
            msg.setText(body,"UTF-8","html");

            msg.setSentDate(new Date());

            // Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

            // connect
            t.connect(ghost, from, pass);

            // send
            t.sendMessage(msg, msg.getAllRecipients());          
            t.close();
            return true;
        } catch (MessagingException me) {
            me.printStackTrace();
            return false;
        }
    }

}
