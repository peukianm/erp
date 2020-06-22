package erp.bean;

import erp.dao.SchedulerDAO;
import erp.dao.StaffDAO;
import erp.entities.Companytask;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
import erp.entities.Usr;
import erp.util.AccessControl;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
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

/**
 *
 * @author peukianm
 */
@Named("dbTasks")
@ViewScoped
public class DashboardTasks implements Serializable {

    private static final Logger logger = LogManager.getLogger(DashboardTasks.class);

    private String lastExecution;
    private String lastStaffExecution;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private StaffDAO staffDao;
    
     @Inject
    private SchedulerDAO schedulerDAO;

    Usr user;
    
    List<Scheduletaskdetail> staffDetails = new ArrayList<>(0);
    List<Scheduletaskdetail> loggerDetails = new ArrayList<>(0);
    
    public void preRenderView() {
        if (sessionBean.getUsers().getDepartment() != null && user.getDepartment().getDepartmentid() != Integer.parseInt(SystemParameters.getInstance().getProperty("itID"))) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_TASK_ADMIN"), null, 1)) {
                return;
            }
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_TASK_ADMIN"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("taskPage"));
    }

    @PostConstruct
    public void init() {
        System.out.println("INITIALIZE DB TASKS BEAN");
        user = sessionBean.getUsers();

        loggerDetails = schedulerDAO.getScheduletaskdetail(Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_READ_LOGGERS")), user.getCompany(), 10);
        staffDetails = schedulerDAO.getScheduletaskdetail(Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_UPDATE_STAFF")), user.getCompany(), 10);
        
        lastExecution = staffDao.getTaskLastExecutionTime(user.getCompany(), Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_READ_LOGGERS")));
        lastStaffExecution = staffDao.getTaskLastExecutionTime(user.getCompany(), Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_UPDATE_STAFF")));
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

    public String getLastStaffExecution() {
        return lastStaffExecution;
    }

    public void setLastStaffExecution(String lastStaffExecution) {
        this.lastStaffExecution = lastStaffExecution;
    }

    public List<Scheduletaskdetail> getStaffDetails() {
        return staffDetails;
    }

    public void setStaffDetails(List<Scheduletaskdetail> staffDetails) {
        this.staffDetails = staffDetails;
    }

    public List<Scheduletaskdetail> getLoggerDetails() {
        return loggerDetails;
    }

    public void setLoggerDetails(List<Scheduletaskdetail> loggerDetails) {
        this.loggerDetails = loggerDetails;
    }
    
}
