package erp.bean;

import erp.dao.AttendanceDAO;
import erp.dao.StaffDAO;
import erp.entities.Attendance;
import erp.entities.Usr;
import erp.util.AccessControl;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
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
@Named("dashboard")
@ViewScoped
public class Dashboard implements Serializable {

    private static final Logger logger = LogManager.getLogger(Dashboard.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AttendanceDAO attendanceDAO;

    @Inject
    private StaffDAO staffDao;

    private Attendance dayAttendance;
    private String entryTime = "N/A";
    private String exitTime = "N/A";
    private String attendanceDate = "N/A";

    private String lastExecution;

    Usr user;

    public void preRenderView() {
        user = sessionBean.getUsers();
        if (!AccessControl.control(user, SystemParameters.getInstance().getProperty("PAGE_ERP_HOME"), null, 1)) {
            return;
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_ERP_HOME"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("workerDashboardPage"));

    }

    @PostConstruct
    public void init() {
        System.out.println("INITIALIZE Dashboard BEAN");
        user = sessionBean.getUsers();
        lastExecution = staffDao.getTaskLastExecutionTime(user.getCompany(), Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_READ_LOGGERS")));
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

    public String getLastExecution() {
        return lastExecution;
    }

    public void setLastExecution(String lastExecution) {
        this.lastExecution = lastExecution;
    }
}
