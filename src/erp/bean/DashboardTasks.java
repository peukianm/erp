package erp.bean;

import erp.dao.CompanyDAO;
import erp.dao.SchedulerDAO;
import erp.dao.StaffDAO;
import erp.entities.Companytask;
import erp.entities.Department;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
import erp.entities.Sector;
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

    @Inject
    private CompanyDAO companyDAO;

    Usr user;

    private Department departmentForUpdate;
    private Sector sectorForUpdate;
    private List<Department> departmentsList;
    private List<Department> activeDepartments;
    private List<Department> sectorDepartments = new ArrayList<>(0);
    private List<Sector> sectorsList;
    private Sector selectedSector;

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

        loggerDetails = schedulerDAO.getScheduletaskdetail(Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_READ_LOGGERS")), user.getCompany(), 5);
        staffDetails = schedulerDAO.getScheduletaskdetail(Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_UPDATE_STAFF")), user.getCompany(), 5);

        lastExecution = staffDao.getTaskLastExecutionTime(user.getCompany(), Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_READ_LOGGERS")));
        lastStaffExecution = staffDao.getTaskLastExecutionTime(user.getCompany(), Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_UPDATE_STAFF")));

        departmentsList = companyDAO.getAllDepartment(false);
        activeDepartments = companyDAO.getAllDepartment(true);
        sectorsList = companyDAO.getAllSector();
    }

    
    public void reset() {        
        departmentsList = companyDAO.getAllDepartment(false);
        activeDepartments = companyDAO.getAllDepartment(true);
        sectorsList = companyDAO.getAllSector();
    }

    public List<Department> getSectorDepartments() {
        return sectorDepartments;
    }

    public void setSectorDepartments(List<Department> sectorDepartments) {
        this.sectorDepartments = sectorDepartments;
    }

    public void onSectorChange() {
        if (selectedSector != null) {
            sectorDepartments.clear();
           sectorDepartments.addAll(staffDao.getSectorDepartments(sessionBean.getUsers().getCompany(), selectedSector));
        }
    }

    public List<Department> getActiveDepartments() {
        return activeDepartments;
    }

    public void setActiveDepartments(List<Department> activeDepartments) {
        this.activeDepartments = activeDepartments;
    }

    public Sector getSelectedSector() {
        return selectedSector;
    }

    public void setSelectedSector(Sector selectedSector) {
        this.selectedSector = selectedSector;
    }

    public List<Sector> getSectorsList() {
        return sectorsList;
    }

    public void setSectorsList(List<Sector> sectorsList) {
        this.sectorsList = sectorsList;
    }

    public List<Department> getDepartmentsList() {
        return departmentsList;
    }

    public void setDepartmentsList(List<Department> departmentsList) {
        this.departmentsList = departmentsList;
    }

    public Department getDepartmentForUpdate() {
        return departmentForUpdate;
    }

    public void setDepartmentForUpdate(Department departmentForUpdate) {
        this.departmentForUpdate = departmentForUpdate;
    }

    public Sector getSectorForUpdate() {
        return sectorForUpdate;
    }

    public void setSectorForUpdate(Sector sectorForUpdate) {
        this.sectorForUpdate = sectorForUpdate;
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
