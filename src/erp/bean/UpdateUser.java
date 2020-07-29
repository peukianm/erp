package erp.bean;

import erp.dao.StaffDAO;
import erp.dao.UsrDAO;
import erp.entities.Department;
import erp.entities.Role;
import erp.entities.Staff;
import erp.entities.Usr;
import erp.exception.ERPCustomException;
import erp.util.AccessControl;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DualListModel;

/**
 *
 * @author peukianm
 */
@Named("updateUser")
@ViewScoped
public class UpdateUser implements Serializable {

    private static final Logger logger = LogManager.getLogger(UpdateUser.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private StaffDAO staffDao;

    @Inject
    private UsrDAO userDao;

    private List<Role> selectedRoles;
    private Staff staff;

    private List<Staff> availableStaff;
    private Usr user;

    private boolean active;
    private String userID;

    private DualListModel<Department> depsPickList;

    public void init() {        
        if (sessionBean.getUsers().getDepartment() != null && sessionBean.getUsers().getDepartment().getDepartmentid() != Integer.parseInt(SystemParameters.getInstance().getProperty("itID"))) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_UPDATE_USER"), null, 1)) {
                return;
            }
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_UPDATE_USER"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("updateUser"));

        if (userID == null) {
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("noUserSelected"));
            FacesUtils.redirectWithNavigationID("dashboardUsers");
        }
        user = userDao.get(Long.parseLong(userID));
        if (user == null) {
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("invalidUserSelected"));
            FacesUtils.redirectWithNavigationID("dashboardUsers");
        }

        if (user.getActive().equals(BigDecimal.ONE)) {
            active = true;
        } else {
            active = false;
        }
        setUser(user);
        List<Department> depsSource = applicationBean.getDepartments();
        depsSource.removeAll(user.getDepartments());
        List<Department> depstarget = user.getDepartments();
        depsPickList = new DualListModel<Department>(depsSource, depstarget);

    }

    @PostConstruct
    public void pc() {        
    }

    @PreDestroy
    public void reset() {
    }

    public List<Staff> completeStaff(String surname) throws ERPCustomException {
        try {
            if (surname != null && !surname.trim().isEmpty() && surname.trim().length() >= 1) {
                surname = surname.trim();
                availableStaff = staffDao.fetchStaffAutoCompleteSurname(surname, null, null);
                return availableStaff;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From AUtocomplete Staff Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void onDepartmentChange() {
        List<Department> temp = depsPickList.getTarget();
        user.setDepartments(new ArrayList<Department>(0));
        user.setDepartments(temp);
    }

    public DualListModel<Department> getDepsPickList() {
        return depsPickList;
    }

    public void setDepsPickList(DualListModel<Department> depsPickList) {
        this.depsPickList = depsPickList;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Staff> getAvailableStaff() {
        return availableStaff;
    }

    public void setAvailableStaff(List<Staff> availableStaff) {
        this.availableStaff = availableStaff;
    }

    public Usr getUser() {
        return user;
    }

    public void setUser(Usr user) {
        this.user = user;
    }

    public List<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
