/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.exception;

import erp.action.AdministrationAction;
import erp.bean.ErrorBean;
import erp.bean.SessionBean;
import erp.entities.Usr;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
@Stateless
public class ERPCustomException extends Exception implements Serializable {

    private static final long serialVersionUID = 7718828512143293558L;
    private static final Logger logger = LogManager.getLogger(ERPCustomException.class);

    private Usr user;
    private String errorMsgKey;
 
     public ERPCustomException() {
    }

    public ERPCustomException(String message, Usr user, String errorMsgKey) {
        super(message);
        this.user = user;
        this.errorMsgKey = errorMsgKey;       
    }

    public ERPCustomException(String message, Throwable throwable, Usr user, String errorMsgKey) {
        super(message, throwable);
        this.user = user;
         this.errorMsgKey = errorMsgKey;
        manipulate(throwable);
    }

    private void manipulate(Throwable ex) {
        try {
            logger.error("-----------AN ERROR HAPPENED !!!! ------------------------------------------------------------------------ ");
            if (user != null) {
                logger.error("User=" + user.getUsername());
            }
            logger.error("Cause=" + ex.getCause());
            logger.error("Class=" + ex.getClass());
            logger.error("Message=" + ex.getLocalizedMessage());
            logger.error(ex, ex);
            logger.error("--------------------- END OF ERROR ------------------------------------------------------------------------- \n \n");

            ErrorBean errorBean = (ErrorBean) FacesUtils.getManagedBean("errorBean");
            errorBean.reset();
            errorBean.setErrorMSG(MessageBundleLoader.getMessage(errorMsgKey));
            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/common/error.jsf?faces-redirect=true");
        } catch (IOException ex1) {
            ex1.printStackTrace();
            java.util.logging.Logger.getLogger(ERPCustomException.class.getName()).log(Level.SEVERE, null, ex1);
        }

    }

}
