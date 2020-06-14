/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.exception;

import javax.ejb.ApplicationException;

/**
 *
 * @author peukianm
 */

@ApplicationException(rollback = true)
public class ERPRollbackException  extends RuntimeException{
    private static final long serialVersionUID = 7712323412143293558L;
     public ERPRollbackException(String message) {
        super(message);
    }
      public ERPRollbackException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
}
