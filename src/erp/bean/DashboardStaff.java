/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.dao.StaffDAO;
import erp.entities.Department;
import erp.entities.Sector;
import erp.entities.Staff;
import erp.entities.Usr;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
@Named("dbStaff")
@ViewScoped
public class DashboardStaff implements Serializable {

    private static final Logger logger = LogManager.getLogger(DashboardStaff.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private StaffDAO staffDao;

    private List<Department> selectedDepartments;
    private List<Sector> selectedSectors;
    private Staff searchStaff;
    private List<Staff> availableStaff;
    private boolean active = true;

    private Staff staffForUpdate;
    private String loggerCode;
    private List<Staff> staff = new ArrayList<>(0);

    @PostConstruct
    public void init() {
        System.out.println("INITIALIZE DB STaff BEAN");
    }

    @PreDestroy
    public void reset() {
    }

    public void resetSearchStaffForm() {
        selectedDepartments = null;
        selectedSectors = null;
        staff = new ArrayList<>(0);

    }

    public void onSectorChange() {
        try {
            if (selectedSectors != null) {
                selectedDepartments = new ArrayList<>(0);
                searchStaff = null;
                selectedSectors.forEach((temp) -> {
                    selectedDepartments.addAll(staffDao.getSectorDepartments(sessionBean.getUsers().getCompany(), temp));
                });

            } else {
                selectedDepartments = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public void onDepartmentChange() {
        selectedSectors = new ArrayList<Sector>();
        searchStaff = null;
    }

    public void autocompleteSurnameSelectStaff(SelectEvent event) {
        try {
            selectedDepartments = new ArrayList<>(0);
            selectedSectors = new ArrayList<>(0);                      
            if (!staff.contains(searchStaff))
                staff.add(searchStaff);
            searchStaff = null;
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
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

    public String getLoggerCode() {
        return loggerCode;
    }

    public void setLoggerCode(String loggerCode) {
        this.loggerCode = loggerCode;
    }

    
    public Staff getStaffForUpdate() {
        return staffForUpdate;
    }

    public void setStaffForUpdate(Staff staffForUpdate) {
        this.staffForUpdate = staffForUpdate;
    }

    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public List<Department> getSelectedDepartments() {
        return selectedDepartments;
    }

    public void setSelectedDepartments(List<Department> selectedDepartments) {
        this.selectedDepartments = selectedDepartments;
    }

    public List<Sector> getSelectedSectors() {
        return selectedSectors;
    }

    public void setSelectedSectors(List<Sector> selectedSectors) {
        this.selectedSectors = selectedSectors;
    }

    public Staff getSearchStaff() {
        return searchStaff;
    }

    public void setSearchStaff(Staff searchStaff) {
        this.searchStaff = searchStaff;
    }

    public List<Staff> getAvailableStaff() {
        return availableStaff;
    }

    public void setAvailableStaff(List<Staff> availableStaff) {
        this.availableStaff = availableStaff;
    }

    public List<Staff> getStaff() {
        return staff;
    }

    public void setStaff(List<Staff> staff) {
        this.staff = staff;
    }
    
    

}
