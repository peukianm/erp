/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.action;

import erp.bean.DashboardTasks;
import erp.bean.ErrorBean;
import erp.bean.SessionBean;
import erp.dao.AuditingDAO;
import erp.entities.Action;
import erp.entities.Auditing;
import erp.exception.ERPCustomException;
import erp.scheduler.LoggerDataRetrieveTask;
import erp.scheduler.StaffUpdateTask;
import erp.util.FacesUtils;
import erp.util.FormatUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.IOException;
import java.util.Date;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
public class SchedulerAction {

    private static final Logger logger = LogManager.getLogger(SchedulerAction.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    LoggerDataRetrieveTask ldrTask;

    @Inject
    StaffUpdateTask staffTask;

    @Inject
    AuditingDAO auditingDAO;

    @Inject
    DashboardTasks dbTasks;

    public String updateFromLoggers() throws ERPCustomException {

        try {
            int stat = ldrTask.doSchedulerWork(true);
            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_EXECUTETASKLOGGERS")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "Update Data from Loggers executed manually",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);
            if (stat == 1) {
                FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("loggerDataUpdated"));
            } else if (stat == -1) {
                FacesUtils.addErrorMessage(MessageBundleLoader.getMessage("failureRunningLogggerDataTask"));
            } else {
                FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("taskInUse"));
            }
            dbTasks.resetLoggerPanel();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From UpdateFrom Loggers Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String updateFromStaff() throws ERPCustomException {

        try {
            int stat = staffTask.doSchedulerWork(true);
            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_EXECUTETASKSTAFF")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "Update Staff Data from OPSY from OPSY executed manually",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);
            if (stat == 1) {
                FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("staffDataUpdated"));
            } else if (stat == -1) {
                FacesUtils.addErrorMessage(MessageBundleLoader.getMessage("failureRunningStaffDataTask"));
            } else {
                FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("taskInUse"));
            }
            dbTasks.resetStaffPanel();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Update From Staff Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }
}
