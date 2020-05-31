/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.dao.StaffDAO;
import erp.dao.UsrDAO;
import erp.entities.Role;
import erp.entities.Staff;
import erp.entities.Usr;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
@Named("updateUser")
@ViewScoped
public class UpdateUser implements Serializable {

    private static final Logger logger = LogManager.getLogger(InsertUser.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private StaffDAO staffDao;

    @Inject
    private UsrDAO userDao;

    List<Role> selectedRoles;
    Staff staff;

    List<Staff> availableStaff;

    Usr user;

    boolean active;
    String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void init() {
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

    }

    @PostConstruct
    public void pc (){
    }
    
    @PreDestroy
    public void reset() {
    }

    public List<Staff> completeStaff(String surname) {
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
            goError(e);
            return null;
        }
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
