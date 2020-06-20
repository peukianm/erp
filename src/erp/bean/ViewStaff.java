package erp.bean;

import erp.dao.StaffDAO;
import erp.entities.Staff;
import erp.util.AccessControl;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
@Named("viewStaff")
@ViewScoped
public class ViewStaff implements Serializable {

    private static final Logger logger = LogManager.getLogger(ViewStaff.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private StaffDAO staffDao;

    private Staff staff;
    boolean active;
    String staffID;

    public void init() {
        System.out.println("INIT VIEW STAFF!!!!!!");
        
        if (sessionBean.getUsers().getDepartment() != null && sessionBean.getUsers().getDepartment().getDepartmentid() == Integer.parseInt(SystemParameters.getInstance().getProperty("hrID"))) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_VIEW_STAFF"), null, 1)) {
                return;
            }
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_VIEW_STAFF"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("viewStaff"));

        if (staffID == null || staffID.equals("")) {
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("noStaffSelected"));
            FacesUtils.redirectWithNavigationID("dashboardStaff");
        }
        staff = staffDao.getStaff(Long.parseLong(staffID));

        if (staff == null) {
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("invalidStaffSelected"));
            FacesUtils.redirectWithNavigationID("dashboardStaff");
        }

        if (staff.getActive().equals(BigDecimal.ONE)) {
            active = true;
        } else {
            active = false;
        }
    }

    @PostConstruct
    public void pc() {
    }

    @PreDestroy
    public void reset() {
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

}
