/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.action;

import erp.bean.InsertProadmission;
import erp.bean.SessionBean;
import erp.dao.AuditingDAO;
import erp.dao.ProadmissionDAO;
import erp.entities.Action;
import erp.entities.Auditing;
import erp.entities.Usr;
import erp.exception.ERPCustomException;
import erp.util.ErpUtil;
import erp.util.FacesUtils;
import erp.util.FormatUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author user
 */
public class AdmissionAction {

    private static final Logger logger = LogManager.getLogger(AdmissionAction.class);

    @Inject
    private SessionBean sessionBean;

    @EJB
    ProadmissionDAO proadmissionDAO;

    @EJB
    AuditingDAO auditingDAO;

    @Inject
    InsertProadmission insertProadmission;

    public String insertProadmission() throws ERPCustomException {
        try {

            proadmissionDAO.save(insertProadmission.getProadmission());

            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_INSERTPROADMISSION")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "Proadmission " + insertProadmission.getProadmission() + " inserted",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("newAdmissionInserted"));
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_ADMISSION_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("admissionPage"));
            return "dashboardAdmission";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From insert Proadmission Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

}
