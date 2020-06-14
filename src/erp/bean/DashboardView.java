package erp.bean;

import erp.dao.AttendanceDAO;
import erp.entities.Attendance;
import erp.entities.Usr;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
import java.io.IOException;
import java.io.Serializable;
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
@Named("dbView")
@ViewScoped
public class DashboardView implements Serializable {

    private static final Logger logger = LogManager.getLogger(DashboardView.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AttendanceDAO attendanceDAO;

    private Attendance dayAttendance;
    private String entryTime = "N/A";
    private String exitTime = "N/A";
    private String attendanceDate = "N/A";
    

    Usr user;

    @PostConstruct
    public void init() {
        user = sessionBean.getUsers();
        dayAttendance = attendanceDAO.getDayAttendance(user.getStaff(), false);
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

}
