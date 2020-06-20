package erp.bean;

import erp.dao.StaffDAO;
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

    Usr user;

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

}
