package erp.bean;

import erp.dao.StaffDAO;
import erp.entities.Department;
import erp.entities.Sector;
import erp.entities.Staff;
import erp.exception.ERPCustomException;
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

    public void preRenderView() {
        if (sessionBean.getUsers().getDepartment() != null && (sessionBean.getUsers().getDepartment().getDepartmentid() != Integer.parseInt(SystemParameters.getInstance().getProperty("hrID"))
                            && sessionBean.getUsers().getDepartment().getDepartmentid() != Integer.parseInt(SystemParameters.getInstance().getProperty("itID")))) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_STAFF_ADMIN"), null, 1)) {
                return;
            }
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_STAFF_ADMIN"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("staffPage"));
    }

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void reset() {
    }

    public void resetSearchStaffForm() {
        selectedDepartments = null;
        selectedSectors = null;
        staff = new ArrayList<>(0);
        loggerCode=null;

    }

    public void onSectorChange() {
        if (selectedSectors != null) {
            selectedDepartments = new ArrayList<>(0);
            searchStaff = null;
            selectedSectors.forEach((temp) -> {
                selectedDepartments.addAll(staffDao.getSectorDepartments(sessionBean.getUsers().getCompany(), temp));
            });

        } else {
            selectedDepartments = new ArrayList<>();
        }
    }

    public void onDepartmentChange() {
        selectedSectors = new ArrayList<Sector>();
        searchStaff = null;
    }

    public void autocompleteSurnameSelectStaff(SelectEvent event) {
        selectedDepartments = new ArrayList<>(0);
        selectedSectors = new ArrayList<>(0);
        if (!staff.contains(searchStaff)) {
            staff.add(searchStaff);
        }
        searchStaff = null;
    }

    public List<Staff> completeStaff(String surname) throws ERPCustomException {
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
            throw new ERPCustomException("Throw From Autocomplete STaff Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
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
