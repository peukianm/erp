/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.action.AdministrationAction;
import erp.dao.StaffDAO;
import erp.entities.Attendance;
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
@Named("dbView")
@ViewScoped
public class DashboardView implements Serializable {

    private static final Logger logger = LogManager.getLogger(DashboardView.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private StaffDAO staffDao;

    private Attendance dayAttendance;
    private String entryTime = "N/A";
    private String exitTime = "N/A";
    private String attendanceDate = "N/A";
    

    Usr user;

    @PostConstruct
    public void init() {
        user = sessionBean.getUsers();
        dayAttendance = staffDao.getDayAttendance(user.getStaff(), false);
        if (dayAttendance != null) {
            attendanceDate = dayAttendance.getEntrance().toString().substring(0, 10);
            entryTime = dayAttendance.getEntrance().toString().substring(11, 16);
            if (dayAttendance.getExit() != null) {
                exitTime = dayAttendance.getExit().toString().substring(11, 16);
            }
        }
         
    }

    @PreDestroy
    public void reset() {

    }
       
    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public Attendance getDayAttendance() {
        return dayAttendance;
    }

    public void setDayAttendance(Attendance dayAttendance) {
        this.dayAttendance = dayAttendance;
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
