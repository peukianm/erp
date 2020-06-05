/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import erp.action.AdministrationAction;
import erp.dao.StaffDAO;
import erp.dao.UsrDAO;
import erp.entities.Attendance;
import erp.entities.Company;
import erp.entities.Department;
import erp.entities.Role;
import erp.entities.Sector;
import erp.entities.Usr;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author peukianm
 */
@Named("dbUsers")
@ViewScoped
public class DashboardUsers implements Serializable {

    private static final Logger logger = LogManager.getLogger(DashboardUsers.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private UsrDAO userDao;

    Usr user;

    private Usr searchUser;
    private Department selectedDepartment;
    private Sector selectedSector;
    private Role selectedRole;
    private Company selectedCompany;
    private String surname;
    private boolean active = true;

    List<Usr> availableUsers;

    List<Usr> searchUsers = new ArrayList<>(0);

    String showUsers = "";
    String showNewUser = "hidden='true'";

    @PostConstruct
    public void init() {
        System.out.println("INITIALIZE DB USERS BEAN");
        user = sessionBean.getUsers();
       
    }

    @PreDestroy
    public void reset() {

    }

    public void resetSearchUsersForm() {
        searchUser = null;
        selectedDepartment = null;
        selectedSector = null;
        selectedRole = null;
        selectedCompany = null;
        surname = null;
        active = true;
    }

    public List<Usr> completeUser(String username) {
        try {
            user = sessionBean.getUsers();
            if (username != null && !username.trim().isEmpty() && username.trim().length() >= 1) {
                username = username.trim();
                availableUsers = userDao.fetchUserAutoCompleteUsername(username);
                return availableUsers;
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

    public void autocompleteUsernameSelectUser(SelectEvent event) {
        try {
            searchUsers.add(searchUser);
            searchUser = null;
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public void goAddNewUser() {
        showUsers = "hidden='true'";
        showNewUser = "";
    }

    public String getShowUsers() {
        return showUsers;
    }

    public void setShowUsers(String showUsers) {
        this.showUsers = showUsers;
    }

    public String getShowNewUser() {
        return showNewUser;
    }

    public void setShowNewUser(String showNewUser) {
        this.showNewUser = showNewUser;
    }

    public List<Usr> getAvailableUsers() {
        return availableUsers;
    }

    public void setAvailableUsers(List<Usr> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Company getSelectedCompany() {
        return selectedCompany;
    }

    public void setSelectedCompany(Company selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    public List<Usr> getSearchUsers() {
        return searchUsers;
    }

    public void setSearchUsers(List<Usr> searchUsers) {
        this.searchUsers = searchUsers;
    }

    public Usr getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(Usr searchUser) {
        this.searchUser = searchUser;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Department selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public Sector getSelectedSector() {
        return selectedSector;
    }

    public void setSelectedSector(Sector selectedSector) {
        this.selectedSector = selectedSector;
    }

    public Role getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole;
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
