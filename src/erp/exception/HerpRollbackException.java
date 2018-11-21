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
public class HerpRollbackException  extends RuntimeException{
     public HerpRollbackException(String message) {
        super(message);
    }
      public HerpRollbackException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
}
