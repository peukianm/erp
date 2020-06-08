/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.dao.StaffDAO;
import erp.entities.Staff;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
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
@Named("updateStaff")
@ViewScoped
public class UpdateStaff implements Serializable {

    private static final Logger logger = LogManager.getLogger(UpdateStaff.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private StaffDAO staffDao;

    private Staff staff;
    boolean active;
    String staffID;

    public void init() {
        System.out.println("INIT UPDATE STAFF!!!!!!");
      
        if (staffID == null) {
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
