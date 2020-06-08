/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.action;

import erp.bean.DashboardStaff;
import erp.bean.ErrorBean;
import erp.bean.InsertStaff;
import erp.bean.SessionBean;
import erp.bean.UpdateStaff;
import erp.dao.AuditingDAO;
import erp.dao.StaffDAO;
import erp.entities.Action;
import erp.entities.Auditing;
import erp.entities.Staff;
import erp.entities.Usr;
import erp.util.ErpUtil;
import erp.util.FacesUtils;
import erp.util.FormatUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author peukianm
 */
public class StaffAction {

    private static final Logger logger = LogManager.getLogger(StaffAction.class);
    @Inject
    private SessionBean sessionBean;

    @EJB
    AuditingDAO auditingDAO;

    @EJB
    StaffDAO staffDAO;

    @Inject
    DashboardStaff dbStaff;

    @Inject
    UpdateStaff updateStaff;

    @Inject
    InsertStaff insertStaff;

    public void fetchStaff() {
        try {
            List<Staff> retValue = new ArrayList<>(0);
            if (!dbStaff.getSelectedSectors().isEmpty()) {
                dbStaff.getSelectedSectors().forEach((temp) -> {
                    List<Staff> staff = staffDAO.getStaff(sessionBean.getUsers().getCompany(), temp, null, dbStaff.isActive());
                    retValue.addAll(staff);
                });

            } else if (!dbStaff.getSelectedDepartments().isEmpty()) {
                dbStaff.getSelectedDepartments().forEach((temp) -> {
                    List<Staff> staff = staffDAO.getStaff(sessionBean.getUsers().getCompany(), null, temp, dbStaff.isActive());
                    retValue.addAll(staff);
                });

            } else {
                List<Staff> staff = staffDAO.getStaff(sessionBean.getUsers().getCompany(), null, null, dbStaff.isActive());
                retValue.addAll(staff);
            }
            dbStaff.setStaff(retValue);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public String goUpdateStaff(long staffID) {
        try {
            return "updateStaff?faces-redirect=true&staffID=" + staffID;
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void goResetLoggerCode() {
        try {
            FacesUtils.callRequestContext("PF('resetLoggerDataDialogWidget').show()");
            FacesUtils.updateHTMLComponnetWIthJS("resetLoggerCodePanelID");
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public String resetLoggerCode() {
        try {
            Staff staff = dbStaff.getStaffForUpdate();
            staffDAO.updateStaff(staff);
            auditingDAO.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATELOGGERCODE")), "Staff logger code " + staff.getSurname() + " " + staff.getName() + " updated");

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("loggerCodeUpdated"));
            FacesUtils.callRequestContext("PF('resetLoggerDataDialogWidget').hide()");
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String insertStaff() {
        try {
            List<Staff> staff = staffDAO.findByProperty("afm", insertStaff.getStaff().getAfm().trim());
            
            if (staff.size() >= 1) {
                System.out.println("afm already  exists");
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("afmAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }
            
            if (insertStaff.isActive()) {
                insertStaff.getStaff().setActive(BigDecimal.ONE);
            } else {
                insertStaff.getStaff().setActive(BigDecimal.ZERO);
            }

           staffDAO.save(insertStaff.getStaff());

            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_INSERTSTAFF")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "Staff " + insertStaff.getStaff().getSurname()+" "+insertStaff.getStaff().getName() + " inserted",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("newStaffInserted"));
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_STAFF_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("staffPage"));
            return "dashboardStaff";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String updateStaff() {
        try {
            List<Staff> staff = staffDAO.findByProperty("afm", updateStaff.getStaff().getAfm().trim());
            System.out.println("staff="+staff.toString());
            if (staff.size() >= 1 && !staff.equals(updateStaff.getStaff())) {
                System.out.println("Afm already  exists");
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("afmAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                PrimeFaces.current().ajax().update("alertPanel");
                return "";
            }

            if (updateStaff.isActive()) {
                updateStaff.getStaff().setActive(BigDecimal.ONE);
            } else {
                updateStaff.getStaff().setActive(BigDecimal.ZERO);
            }

            staffDAO.update(updateStaff.getStaff());

            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATESTAFF")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "Staff " + updateStaff.getStaff().getSurname() + " updated",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("staffUpdated"));
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_STAFF_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("staffPage"));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void goError(Exception ex) {
        try {
            logger.error("-----------AN ERROR HAPPENED !!!! -------------------- : " + ex.toString());
            if (sessionBean.getUsers() != null) {
                logger.error("User=" + sessionBean.getUsers().getUsername());
            }
            logger.error("Cause=" + ex.getCause());
            logger.error("Class=" + ex.getClass());
            logger.error("Message=" + ex.getLocalizedMessage());
            logger.error(ex, ex);
            logger.error("--------------------- END OF ERROR --------------------------------------------------------\n\n");

            ErrorBean errorBean = (ErrorBean) FacesUtils.getManagedBean("errorBean");
            errorBean.reset();
            errorBean.setErrorMSG(MessageBundleLoader.getMessage(sessionBean.getErrorMsgKey()));
            //FacesUtils.redirectAJAX("./templates/error.jsf?faces-redirect=true");
            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/error.jsf?faces-redirect=true");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
