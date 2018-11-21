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
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author peukianm
 */
public class ErpUtil {

//    public static List<Productline> getProductLineFromCompany(Company company) {
//        ArrayList<Productline> retValue = new ArrayList<Productline>(0);
//        try {
//            List<Companycatalogue> catalogues = new ArrayList<Companycatalogue>(company.getCompanycatalogues());
//            for (int i = 0; i < catalogues.size(); i++) {
//                Catalogue catalogue = catalogues.get(i).getCatalogue();
//                if (catalogue.getActive().equals(BigDecimal.ONE)) {
//                    List<Catalogueproductline> lines = new ArrayList<Catalogueproductline>(catalogue.getCatalogueproductlines());
//                    for (int j = 0; j < lines.size(); j++) {
//                        Catalogueproductline catalogueproductline = lines.get(j);
//                        if (catalogueproductline.getProductline().getActive().equals(BigDecimal.ONE)) {
//                            retValue.add(catalogueproductline.getProductline());
//                        }
//                    }
//                }
//            }
//        } catch (RuntimeException ex) {
//            ex.printStackTrace();
//            throw ex;
//        }
//
//        Collections.sort(retValue, new Comparator<Productline>() {
//            public int compare(Productline one, Productline other) {
//                return one.getName().compareTo(other.getName());
//            }
//        });
//        return retValue;
//    }
    
    private static final int iterations = 10 * 1024;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    /**
     * Computes a salted PBKDF2 hash of given plaintext password suitable for storing in a database. Empty passwords are not supported.
     */
    public static String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        // store the salt with the password
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    /**
     * Checks whether given plaintext password corresponds to a stored salted hash of the password.
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
    
    
    
    public static void sendFromGMail(String ghost, String gport, String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = ghost;
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", gport);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
    
}
