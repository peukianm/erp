/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.util;

import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author peukianm
 */
public class EJBUtil {
    
    public static PersistenceHelper lookupPersistenceHelperBean() {
        try {
            
            Context c = new InitialContext();
            //return (PersistenceHelper) c.lookup("java:global/HERP/PersistenceHelper!com.hosp.util.PersistenceHelper");
            return (PersistenceHelper) c.lookup("java:module/PersistenceHelper");
        } catch (NamingException ne) {
            ne.printStackTrace();
            //java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
