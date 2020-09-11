package erp.action;

import erp.bean.DashboardStaff;
import erp.bean.InsertStaff;
import erp.bean.SessionBean;
import erp.bean.UpdateStaff;
import erp.dao.AuditingDAO;
import erp.dao.StaffDAO;
import erp.entities.Action;
import erp.entities.Auditing;
import erp.entities.Staff;
import erp.exception.ERPCustomException;
import erp.util.FacesUtils;
import erp.util.FormatUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
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

    public void fetchStaff() throws ERPCustomException {
        try {
            List<Staff> retValue = new ArrayList<>(0);
//            if (!dbStaff.getSelectedSectors().isEmpty()) {
//                dbStaff.getSelectedSectors().forEach((temp) -> {
//                    List<Staff> staff = staffDAO.getStaff(sessionBean.getUsers().getCompany(), temp, null, dbStaff.isActive(), dbStaff.getLoggerCode());
//                    retValue.addAll(staff);
//                });
//
//            } else 
                
                if (!dbStaff.getSelectedDepartments().isEmpty()) {
                dbStaff.getSelectedDepartments().forEach((temp) -> {
                    List<Staff> staff = staffDAO.getStaff(sessionBean.getUsers().getCompany(), null, temp, dbStaff.isActive(), dbStaff.getAfm());
                    retValue.addAll(staff);
                });

            } else {
                List<Staff> staff = staffDAO.getStaff(sessionBean.getUsers().getCompany(), null, null, dbStaff.isActive(), dbStaff.getAfm());
                retValue.addAll(staff);
            }
            dbStaff.setStaff(retValue);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Fetch Staff Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String goUpdateStaff(long staffID) {
        return "updateStaff?faces-redirect=true&staffID=" + staffID;
    }

    public String goViewStaff(long staffID) {
        return "viewStaff?faces-redirect=true&staffID=" + staffID;
    }

    public String deactivateStaff(long staffID) throws ERPCustomException {
        try {
            Staff staff = dbStaff.getStaffForUpdate();
            staff.setActive(BigDecimal.ZERO);
            staffDAO.updateStaff(staff);
            auditingDAO.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATESTAFF")), "Staff " + staff.getSurname() + " " + staff.getName() + " deactivated");
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("staffUpdated"));
            fetchStaff();
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From deactivate staff ", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }
    
    public String activateStaff(long staffID) throws ERPCustomException {
        try {
            Staff staff = dbStaff.getStaffForUpdate();
            staff.setActive(BigDecimal.ONE);
            staffDAO.updateStaff(staff);
            auditingDAO.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATESTAFF")), "Staff " + staff.getSurname() + " " + staff.getName() + " activated");
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("staffUpdated"));
            fetchStaff();
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From deactivate staff ", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void goResetLoggerCode() {
        FacesUtils.callRequestContext("PF('resetLoggerDataDialogWidget').show()");
        FacesUtils.updateHTMLComponnetWIthJS("resetLoggerCodePanelID");
    }

    public String resetLoggerCode() throws ERPCustomException {
        try {
            Staff staff = dbStaff.getStaffForUpdate();

            List<Staff> staffs = staffDAO.findByProperty("loggercode", staff.getLoggercode().trim());
            if (staffs.size() >= 1 && !staffs.get(0).equals(staff)) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("loggerCodeAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                PrimeFaces.current().ajax().update("alertPanel");
                return "";
            }

            staffDAO.updateStaff(staff);
            auditingDAO.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATELOGGERCODE")), "Staff logger code " + staff.getSurname() + " " + staff.getName() + " updated");

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("loggerCodeUpdated"));
            FacesUtils.callRequestContext("PF('resetLoggerDataDialogWidget').hide()");
          
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Reset Logger Code Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }
    
    public String resetStaffDepartment() throws ERPCustomException {
        try {
            Staff staff = dbStaff.getStaffForUpdate();

            staffDAO.updateStaff(staff);
            auditingDAO.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATESTAFF")), "Staff Department " + staff.getSurname() + " " + staff.getName() + " updated");

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("departmentUpdated"));
            FacesUtils.callRequestContext("PF('resetDepartmentDialogWidget').hide()");
          
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Reset department Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String insertStaff() throws ERPCustomException {
        try {
            List<Staff> staff = staffDAO.findByProperty("afm", insertStaff.getStaff().getAfm().trim());
            if (staff.size() >= 1) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("afmAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }

            staff = staffDAO.findByProperty("amy", insertStaff.getStaff().getAmy().trim());
            if (staff.size() >= 1) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("amyAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }

            staff = staffDAO.findByProperty("loggercode", insertStaff.getStaff().getLoggercode().trim());
            if (staff.size() >= 1) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("loggerCodeAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }

            staff = staffDAO.findByProperty("amka", insertStaff.getStaff().getAmka().trim());
            if (staff.size() >= 1) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("amkaCodeAlreadyUsed"));
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
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "Staff " + insertStaff.getStaff().getSurname() + " " + insertStaff.getStaff().getName() + " inserted",
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
            throw new ERPCustomException("Throw From Insert Staff Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String updateStaff() throws ERPCustomException {
        try {
            List<Staff> staff = staffDAO.findByProperty("afm", updateStaff.getStaff().getAfm().trim());

            if (staff.size() >= 1 && !staff.get(0).equals(updateStaff.getStaff())) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("afmAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                PrimeFaces.current().ajax().update("alertPanel");
                return "";
            }

            staff = staffDAO.findByProperty("amy", updateStaff.getStaff().getAmy().trim());
            if (staff.size() >= 1 && !staff.get(0).equals(updateStaff.getStaff())) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("amyAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                PrimeFaces.current().ajax().update("alertPanel");
                return "";
            }

            staff = staffDAO.findByProperty("amka", updateStaff.getStaff().getAmka().trim());
            if (staff.size() >= 1 && !staff.get(0).equals(updateStaff.getStaff())) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("amkaAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                PrimeFaces.current().ajax().update("alertPanel");
                return "";
            }

            staff = staffDAO.findByProperty("loggercode", updateStaff.getStaff().getLoggercode().trim());
            if (staff.size() >= 1 && !staff.get(0).equals(updateStaff.getStaff())) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("loggerCodeAlreadyUsed"));
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
            throw new ERPCustomException("Throw From Update Staff Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }
}
