package erp.util;

import erp.entities.Action;
import erp.entities.Auditing;
import erp.entities.Usr;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class PersistenceUtil {

    @EJB
    private PersistenceHelper persistenceHelper;
    
    private static final Logger logger = LogManager.getLogger(PersistenceUtil.class);

    public void audit(Usr user, long actionID,  String comments) throws Exception {
        try {
            Action action = (Action) persistenceHelper.find(Action.class, actionID);
            Auditing auditing = new Auditing();
            auditing.setUsr(user);
            if (logger.isDebugEnabled()) {
                logger.debug("AUDITING");
            }            
            auditing.setActiondate(FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN));
            auditing.setActiontime(FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditing.setAction(action);
            if (user.getCompany()!=null)
                auditing.setCompany(user.getCompany());
            
            auditing.setComments(comments);                    
            persistenceHelper.create(auditing);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
  
   
}
